package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.firebase.FirebaseManager
import com.example.data.model.AmenityExperience
import com.example.data.model.Booking
import com.example.data.model.RawFirebaseDoc
import com.example.data.model.ResortAnnouncement
import com.example.data.model.ResortRoom
import com.example.data.model.ResortStats
import com.example.data.model.ServiceRequest
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MainTab(val title: String, val index: Int) {
    DASHBOARD("Dashboard", 0),
    PROFILE("Profile", 1),
    SETTINGS("Settings", 2)
}

data class ResortUiState(
    val isAuthenticated: Boolean = false,
    val isAuthLoading: Boolean = false,
    val authError: String? = null,
    val currentTab: MainTab = MainTab.DASHBOARD,
    val userProfile: UserProfile = UserProfile(),
    val bookings: List<Booking> = emptyList(),
    val rooms: List<ResortRoom> = emptyList(),
    val amenities: List<AmenityExperience> = emptyList(),
    val services: List<ServiceRequest> = emptyList(),
    val announcements: List<ResortAnnouncement> = emptyList(),
    val stats: ResortStats = ResortStats(),
    val rawDocs: List<RawFirebaseDoc> = emptyList(),
    val isRefreshing: Boolean = false,
    val firebaseStatus: String = "Connected: wingsresort-1063b",
    val isFirebaseLive: Boolean = true,
    val searchQuery: String = "",
    val activeFilterCategory: String = "All",
    val showBookingDialog: Boolean = false,
    val showServiceDialog: Boolean = false,
    val showRawInspector: Boolean = false,
    val actionSuccessMessage: String? = null
)

class ResortViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ResortUiState())
    val uiState: StateFlow<ResortUiState> = _uiState.asStateFlow()

    init {
        FirebaseManager.initialize(application.applicationContext)
        loadInitialData()
    }

    private fun loadInitialData() {
        _uiState.update {
            it.copy(
                rooms = FirebaseManager.getInitialRooms(),
                bookings = FirebaseManager.getInitialBookings(),
                amenities = FirebaseManager.getInitialAmenities(),
                services = FirebaseManager.getInitialServices(),
                announcements = FirebaseManager.getInitialAnnouncements(),
                stats = ResortStats(
                    occupancyRate = 94,
                    activeGuests = 186,
                    openServices = 8,
                    satisfactionScore = 4.98
                )
            )
        }
        syncWithFirebaseDB()
    }

    fun login(emailInput: String, passwordInput: String) {
        if (emailInput.isBlank() || passwordInput.isBlank()) {
            _uiState.update { it.copy(authError = "Please enter username/email and password") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, authError = null) }
            val result = FirebaseManager.signIn(emailInput, passwordInput)
            result.onSuccess { profile ->
                _uiState.update {
                    it.copy(
                        isAuthenticated = true,
                        isAuthLoading = false,
                        userProfile = profile,
                        authError = null
                    )
                }
                syncWithFirebaseDB()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isAuthLoading = false,
                        authError = error.message ?: "Authentication failed. Please try demo login."
                    )
                }
            }
        }
    }

    fun loginDemo(role: String = "Guest") {
        viewModelScope.launch {
            _uiState.update { it.copy(isAuthLoading = true, authError = null) }
            val profile = if (role == "Manager") {
                UserProfile(
                    uid = "mgr_alex_wings",
                    email = "manager@wingsresort.com",
                    displayName = "Executive Manager",
                    role = "Manager",
                    roomNumber = "Executive HQ",
                    memberTier = "Resort Director",
                    points = 99999
                )
            } else {
                UserProfile(
                    uid = "vip_guest_108",
                    email = "krazywingsresort@gmail.com",
                    displayName = "Krazy Wings VIP",
                    role = "Guest",
                    roomNumber = "Villa 108",
                    memberTier = "Diamond Wings VIP",
                    points = 15850
                )
            }

            _uiState.update {
                it.copy(
                    isAuthenticated = true,
                    isAuthLoading = false,
                    userProfile = profile,
                    authError = null
                )
            }
            syncWithFirebaseDB()
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                isAuthenticated = false,
                currentTab = MainTab.DASHBOARD,
                authError = null
            )
        }
    }

    fun setTab(tab: MainTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setFilterCategory(category: String) {
        _uiState.update { it.copy(activeFilterCategory = category) }
    }

    fun syncWithFirebaseDB() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val docs = FirebaseManager.fetchRawDocs()
            _uiState.update {
                it.copy(
                    rawDocs = docs,
                    isRefreshing = false,
                    isFirebaseLive = true,
                    firebaseStatus = "Live Sync: wingsresort-1063b (${docs.size} docs)"
                )
            }
        }
    }

    fun createBooking(
        guestName: String,
        roomType: String,
        roomNum: String,
        checkIn: String,
        checkOut: String,
        guests: Int,
        price: Double
    ) {
        val newBooking = Booking(
            id = "WNG-" + System.currentTimeMillis().toString().takeLast(6),
            guestName = guestName.ifBlank { _uiState.value.userProfile.displayName },
            guestEmail = _uiState.value.userProfile.email,
            suiteTitle = "$roomType $roomNum",
            suiteType = roomType,
            roomNumber = roomNum,
            checkInDate = checkIn,
            checkOutDate = checkOut,
            guestsCount = guests,
            totalPrice = price,
            status = "Confirmed",
            isPaid = true
        )

        viewModelScope.launch {
            FirebaseManager.saveBookingToFirebase(newBooking)
            _uiState.update { state ->
                state.copy(
                    bookings = listOf(newBooking) + state.bookings,
                    showBookingDialog = false,
                    actionSuccessMessage = "Reservation $roomType successfully confirmed!"
                )
            }
        }
    }

    fun requestService(serviceType: String, details: String, priority: String) {
        val newService = ServiceRequest(
            id = "srv_" + System.currentTimeMillis().toString().takeLast(5),
            guestName = _uiState.value.userProfile.displayName,
            roomNumber = _uiState.value.userProfile.roomNumber,
            serviceType = serviceType,
            details = details,
            timeFormatted = "Just now",
            status = "In Progress",
            priority = priority
        )

        viewModelScope.launch {
            FirebaseManager.saveServiceRequest(newService)
            _uiState.update { state ->
                state.copy(
                    services = listOf(newService) + state.services,
                    showServiceDialog = false,
                    actionSuccessMessage = "Concierge notified for $serviceType!"
                )
            }
        }
    }

    fun updateServiceStatus(serviceId: String, newStatus: String) {
        _uiState.update { state ->
            val updated = state.services.map {
                if (it.id == serviceId) it.copy(status = newStatus) else it
            }
            state.copy(services = updated)
        }
    }

    fun updateProfile(name: String, phone: String, room: String) {
        val updated = _uiState.value.userProfile.copy(
            displayName = name,
            phone = phone,
            roomNumber = room
        )
        viewModelScope.launch {
            FirebaseManager.saveProfile(updated)
            _uiState.update {
                it.copy(
                    userProfile = updated,
                    actionSuccessMessage = "Profile updated & synced with Firebase!"
                )
            }
        }
    }

    fun setShowBookingDialog(show: Boolean) {
        _uiState.update { it.copy(showBookingDialog = show) }
    }

    fun setShowServiceDialog(show: Boolean) {
        _uiState.update { it.copy(showServiceDialog = show) }
    }

    fun setShowRawInspector(show: Boolean) {
        _uiState.update { it.copy(showRawInspector = show) }
    }

    fun clearActionMessage() {
        _uiState.update { it.copy(actionSuccessMessage = null) }
    }
}
