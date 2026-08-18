package com.example.data.model

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "VIP Guest",
    val role: String = "Guest", // "Guest", "Manager", "Concierge"
    val roomNumber: String = "Villa 108",
    val memberTier: String = "Diamond Wings VIP",
    val points: Int = 14250,
    val phone: String = "+1 (555) 389-2041",
    val preferences: List<String> = listOf("King Bed", "Ocean Front", "Late Checkout", "Gluten-Free"),
    val avatarUrl: String = ""
)

data class Booking(
    val id: String = "",
    val guestName: String = "",
    val guestEmail: String = "",
    val suiteTitle: String = "",
    val suiteType: String = "Overwater Villa",
    val roomNumber: String = "Villa 108",
    val checkInDate: String = "Aug 20, 2026",
    val checkOutDate: String = "Aug 27, 2026",
    val guestsCount: Int = 2,
    val totalPrice: Double = 3450.0,
    val status: String = "Confirmed", // "Confirmed", "Checked In", "Completed", "Cancelled"
    val isPaid: Boolean = true,
    val specialRequests: String = "Sunset view requested, Welcome champagne"
)

data class ResortRoom(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val pricePerNight: Double = 0.0,
    val capacity: Int = 2,
    val sizeSqFt: Int = 1200,
    val view: String = "Ocean Panorama",
    val rating: Double = 4.9,
    val description: String = "",
    val isAvailable: Boolean = true,
    val features: List<String> = emptyList()
)

data class AmenityExperience(
    val id: String = "",
    val title: String = "",
    val category: String = "Wellness", // "Wellness", "Dining", "Adventure", "Leisure"
    val timing: String = "08:00 AM - 10:00 PM",
    val location: String = "South Pier",
    val price: String = "Complimentary",
    val rating: Double = 4.9,
    val description: String = "",
    val isOpen: Boolean = true
)

data class ServiceRequest(
    val id: String = "",
    val guestName: String = "",
    val roomNumber: String = "",
    val serviceType: String = "", // "Luggage", "Room Dining", "Housekeeping", "Spa Booking", "Cabana Setup"
    val details: String = "",
    val timeFormatted: String = "",
    val status: String = "In Progress", // "Pending", "In Progress", "Completed"
    val priority: String = "High" // "Normal", "High", "VIP"
)

data class ResortAnnouncement(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val category: String = "Events",
    val timestamp: String = "Today, 10:00 AM",
    val isImportant: Boolean = false
)

data class ResortStats(
    val occupancyRate: Int = 94,
    val activeGuests: Int = 186,
    val openServices: Int = 8,
    val satisfactionScore: Double = 4.98,
    val weatherTemp: String = "28°C",
    val weatherCondition: String = "Sunny Tropical Breeze"
)

data class RawFirebaseDoc(
    val collection: String,
    val documentId: String,
    val data: Map<String, Any?>
)
