package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.LuxuryBadge
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ResortCardDark
import com.example.ui.theme.ResortGold
import com.example.ui.theme.ResortGoldLight
import com.example.ui.theme.ResortTealAccent
import com.example.ui.theme.ResortTealDark
import com.example.ui.theme.ResortTealDeep
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.ResortUiState
import com.example.ui.viewmodel.ResortViewModel
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    viewModel: ResortViewModel,
    state: ResortUiState
) {
    // Clear transient success notification after 3 seconds
    LaunchedEffect(state.actionSuccessMessage) {
        if (state.actionSuccessMessage != null) {
            delay(3000)
            viewModel.clearActionMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = ResortCardDark,
                contentColor = ResortGoldLight,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .border(width = 1.dp, color = CardBorder)
                    .navigationBarsPadding()
                    .testTag("bottom_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = state.currentTab == MainTab.DASHBOARD,
                    onClick = { viewModel.setTab(MainTab.DASHBOARD) },
                    icon = {
                        Icon(
                            imageVector = if (state.currentTab == MainTab.DASHBOARD) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                            contentDescription = "Dashboard",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Dashboard", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ResortTealDark,
                        selectedTextColor = ResortGold,
                        indicatorColor = ResortGold,
                        unselectedIconColor = ResortGoldLight.copy(alpha = 0.6f),
                        unselectedTextColor = ResortGoldLight.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("tab_dashboard")
                )

                NavigationBarItem(
                    selected = state.currentTab == MainTab.PROFILE,
                    onClick = { viewModel.setTab(MainTab.PROFILE) },
                    icon = {
                        Icon(
                            imageVector = if (state.currentTab == MainTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Profile", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ResortTealDark,
                        selectedTextColor = ResortGold,
                        indicatorColor = ResortGold,
                        unselectedIconColor = ResortGoldLight.copy(alpha = 0.6f),
                        unselectedTextColor = ResortGoldLight.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("tab_profile")
                )

                NavigationBarItem(
                    selected = state.currentTab == MainTab.SETTINGS,
                    onClick = { viewModel.setTab(MainTab.SETTINGS) },
                    icon = {
                        Icon(
                            imageVector = if (state.currentTab == MainTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text("Settings", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ResortTealDark,
                        selectedTextColor = ResortGold,
                        indicatorColor = ResortGold,
                        unselectedIconColor = ResortGoldLight.copy(alpha = 0.6f),
                        unselectedTextColor = ResortGoldLight.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("tab_settings")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ResortTealDark)
        ) {
            // Tab contents
            when (state.currentTab) {
                MainTab.DASHBOARD -> DashboardScreen(
                    state = state,
                    onRefresh = { viewModel.syncWithFirebaseDB() },
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    onCategorySelect = { viewModel.setFilterCategory(it) },
                    onOpenBookingDialog = { viewModel.setShowBookingDialog(true) },
                    onOpenServiceDialog = { viewModel.setShowServiceDialog(true) },
                    onOpenRawInspector = { viewModel.setShowRawInspector(true) },
                    onUpdateServiceStatus = { id, status -> viewModel.updateServiceStatus(id, status) }
                )

                MainTab.PROFILE -> ProfileScreen(
                    state = state,
                    onUpdateProfile = { name, phone, room -> viewModel.updateProfile(name, phone, room) },
                    onOpenServiceDialog = { viewModel.setShowServiceDialog(true) }
                )

                MainTab.SETTINGS -> SettingsScreen(
                    state = state,
                    onRefresh = { viewModel.syncWithFirebaseDB() },
                    onOpenRawInspector = { viewModel.setShowRawInspector(true) },
                    onLogout = { viewModel.logout() }
                )
            }

            // Top action feedback banner
            AnimatedVisibility(
                visible = state.actionSuccessMessage != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SuccessGreen,
                    contentColor = Color.White,
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.actionSuccessMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }

    // New Booking Reservation Dialog
    if (state.showBookingDialog) {
        BookingModalDialog(
            guestNameDefault = state.userProfile.displayName,
            onDismiss = { viewModel.setShowBookingDialog(false) },
            onConfirm = { guest, roomType, roomNum, checkIn, checkOut, guests, price ->
                viewModel.createBooking(guest, roomType, roomNum, checkIn, checkOut, guests, price)
            }
        )
    }

    // Concierge Request Dialog
    if (state.showServiceDialog) {
        ServiceRequestModalDialog(
            onDismiss = { viewModel.setShowServiceDialog(false) },
            onConfirm = { type, details, priority ->
                viewModel.requestService(type, details, priority)
            }
        )
    }

    // Raw Firebase DB Inspector Modal
    if (state.showRawInspector) {
        RawDbInspectorDialog(
            state = state,
            onDismiss = { viewModel.setShowRawInspector(false) },
            onRefresh = { viewModel.syncWithFirebaseDB() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingModalDialog(
    guestNameDefault: String,
    onDismiss: () -> Unit,
    onConfirm: (guest: String, type: String, num: String, inDate: String, outDate: String, guests: Int, price: Double) -> Unit
) {
    var guestName by remember { mutableStateOf(guestNameDefault) }
    var selectedType by remember { mutableStateOf("Overwater Ocean Villa") }
    var roomNumber by remember { mutableStateOf("Villa 112") }
    var checkInDate by remember { mutableStateOf("Aug 24, 2026") }
    var checkOutDate by remember { mutableStateOf("Aug 30, 2026") }
    var guestCount by remember { mutableIntStateOf(2) }
    var pricePerNight by remember { mutableDoubleStateOf(1250.0) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = ResortCardDark
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Reserve Luxury Villa",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Save directly to Firebase Realtime Database",
                    style = MaterialTheme.typography.labelSmall.copy(color = ResortGoldLight)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = guestName,
                    onValueChange = { guestName = it },
                    label = { Text("Guest Full Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ResortGold,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = selectedType,
                    onValueChange = { selectedType = it },
                    label = { Text("Villa Type") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ResortGold,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = checkInDate,
                        onValueChange = { checkInDate = it },
                        label = { Text("Check-In") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ResortGold,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = checkOutDate,
                        onValueChange = { checkOutDate = it },
                        label = { Text("Check-Out") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ResortGold,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancel", color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onConfirm(guestName, selectedType, roomNumber, checkInDate, checkOutDate, guestCount, pricePerNight * 6)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ResortGold,
                            contentColor = ResortTealDark
                        )
                    ) {
                        Text("Confirm & Sync DB", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceRequestModalDialog(
    onDismiss: () -> Unit,
    onConfirm: (serviceType: String, details: String, priority: String) -> Unit
) {
    var selectedService by remember { mutableStateOf("Champagne & Fruit Service") }
    var details by remember { mutableStateOf("Chilled Dom Pérignon & fresh mangoes to private sun deck") }
    var priority by remember { mutableStateOf("VIP") }

    val serviceOptions = listOf(
        "Champagne & Fruit Service",
        "Luggage Valet & Transfer",
        "Aromatherapy & Turndown",
        "Private Chef In-Villa Dining",
        "Spa Hydrotherapy Booking",
        "Sunset Buggy Charter"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = ResortCardDark
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Request Concierge Service",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("Select Service Type:", style = MaterialTheme.typography.labelSmall, color = ResortGoldLight)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = selectedService,
                    onValueChange = { selectedService = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ResortGold,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Special Instructions") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ResortGold,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancel", color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onConfirm(selectedService, details, priority) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ResortGold,
                            contentColor = ResortTealDark
                        )
                    ) {
                        Text("Dispatch to Concierge", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun RawDbInspectorDialog(
    state: ResortUiState,
    onDismiss: () -> Unit,
    onRefresh: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(520.dp)
                .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = ResortCardDark
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Firebase Database Inspector",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Project: wingsresort-1063b",
                            style = MaterialTheme.typography.labelSmall.copy(color = ResortGold)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.LightGray)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ResortTealDark)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    item {
                        Text(
                            text = "/// REALTIME SYNCED COLLECTIONS ///",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ResortGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        Text(
                            text = "{ \n  \"project_id\": \"wingsresort-1063b\",\n  \"status\": \"CONNECTED\",\n  \"active_bookings_count\": ${state.bookings.size},\n  \"available_rooms_count\": ${state.rooms.size},\n  \"services_dispatched\": ${state.services.size}\n}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFB9F6CA),
                                fontSize = 12.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(state.bookings) { b ->
                        Text(
                            text = "• Booking[${b.id}]: ${b.guestName} -> ${b.suiteTitle} ($${b.totalPrice}) [${b.status}]",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = ResortGoldLight,
                                fontSize = 11.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            ),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    items(state.services) { s ->
                        Text(
                            text = "• Service[${s.id}]: ${s.serviceType} @ ${s.roomNumber} [${s.status}]",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White,
                                fontSize = 11.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            ),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ResortGold,
                            contentColor = ResortTealDark
                        )
                    ) {
                        Text("Done", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
