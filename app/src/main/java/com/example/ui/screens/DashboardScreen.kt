package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AmenityExperience
import com.example.data.model.Booking
import com.example.data.model.ResortAnnouncement
import com.example.data.model.ResortRoom
import com.example.data.model.ServiceRequest
import com.example.ui.components.AnimatedLoadText
import com.example.ui.components.LiveStatusPulse
import com.example.ui.components.LuxuryBadge
import com.example.ui.components.LuxuryCard
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ResortCardDark
import com.example.ui.theme.ResortGold
import com.example.ui.theme.ResortGoldDark
import com.example.ui.theme.ResortGoldLight
import com.example.ui.theme.ResortTealAccent
import com.example.ui.theme.ResortTealDark
import com.example.ui.theme.ResortTealDeep
import com.example.ui.theme.ResortTealPrimary
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.ResortUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: ResortUiState,
    onRefresh: () -> Unit,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onOpenBookingDialog: () -> Unit,
    onOpenServiceDialog: () -> Unit,
    onOpenRawInspector: () -> Unit,
    onUpdateServiceStatus: (id: String, status: String) -> Unit
) {
    val categories = listOf("All", "Suites", "Dining & Spa", "Bookings", "Services", "Announcements")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ResortTealDark)
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 1. Top Bar & Guest Greeting
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        AnimatedLoadText(
                            text = "WINGS RESORT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold,
                                color = ResortGold
                            )
                        )
                        AnimatedLoadText(
                            text = "Welcome, ${state.userProfile.displayName}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            delayMs = 100,
                            animateTypewriter = false
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onRefresh,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ResortCardDark)
                                .border(1.dp, CardBorder, CircleShape)
                                .testTag("refresh_button")
                        ) {
                            if (state.isRefreshing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = ResortGold,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh Firebase DB",
                                    tint = ResortGold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Live Firebase Status & Weather Pill Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LiveStatusPulse(
                        isLive = state.isFirebaseLive,
                        text = if (state.rawDocs.isNotEmpty()) "Firebase: ${state.rawDocs.size} Live Records" else "Firebase Realtime DB"
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(ResortCardDark)
                            .border(1.dp, CardBorder, RoundedCornerShape(50.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = ResortGoldLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${state.stats.weatherTemp} • Tropical",
                            style = MaterialTheme.typography.labelSmall,
                            color = ResortGoldLight
                        )
                    }
                }
            }
        }

        // 2. Hero Showcase Banner with Luxury Metrics
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, ResortGold.copy(alpha = 0.35f), RoundedCornerShape(24.dp))
            ) {
                AsyncImage(
                    model = R.drawable.img_resort_hero,
                    contentDescription = "Wings Resort Ocean View",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(185.dp),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(185.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    ResortTealDark.copy(alpha = 0.5f),
                                    ResortTealDark.copy(alpha = 0.95f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    LuxuryBadge(
                        text = "VIP RESORT EXPERIENCE",
                        backgroundColor = ResortTealDeep.copy(alpha = 0.85f),
                        contentColor = ResortGoldLight
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    AnimatedLoadText(
                        text = "Private Luxury Ocean Villas & Haven",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        delayMs = 150
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ResortMetricItem(label = "Occupancy", value = "${state.stats.occupancyRate}%")
                        ResortMetricItem(label = "Active Guests", value = "${state.stats.activeGuests}")
                        ResortMetricItem(label = "Rating", value = "${state.stats.satisfactionScore} ★")
                        ResortMetricItem(label = "Your Villa", value = state.userProfile.roomNumber)
                    }
                }
            }
        }

        // 3. Search and Quick Action Buttons
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search suites, dining, spa, services...", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = ResortGold
                        )
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search",
                                    tint = ResortGoldLight
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ResortGold,
                        unfocusedBorderColor = CardBorder,
                        focusedContainerColor = ResortCardDark,
                        unfocusedContainerColor = ResortCardDark,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_field")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onOpenBookingDialog,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ResortGold,
                            contentColor = ResortTealDark
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("action_new_booking")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reserve Villa", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = onOpenServiceDialog,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ResortTealDeep,
                            contentColor = ResortGoldLight
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                            .testTag("action_concierge")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RoomService,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = ResortGold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Concierge", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }

                    IconButton(
                        onClick = onOpenRawInspector,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ResortCardDark)
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                            .testTag("action_raw_db")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dataset,
                            contentDescription = "Raw Firebase DB",
                            tint = ResortGoldLight
                        )
                    }
                }
            }
        }

        // 4. Filter Categories Horizontal Scroll
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = state.activeFilterCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelect(category) },
                        label = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ResortGold,
                            selectedLabelColor = ResortTealDark,
                            containerColor = ResortCardDark,
                            labelColor = ResortGoldLight
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = CardBorder,
                            selectedBorderColor = ResortGold
                        ),
                        shape = RoundedCornerShape(100.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Live Firebase DB Realtime Sync Card Banner
        item {
            LuxuryCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                backgroundColor = ResortTealDeep
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Dataset,
                                contentDescription = null,
                                tint = ResortGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Firebase DB Live Data Feed",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        LiveStatusPulse(
                            isLive = state.isFirebaseLive,
                            text = if (state.rawDocs.isNotEmpty()) "${state.rawDocs.size} Live Docs" else "Realtime Synced"
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Realtime stream for project wingsresort-1063b • Logged in as ${state.userProfile.displayName} (${state.userProfile.email})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ResortGoldLight.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ResortTealDark)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Reservations", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 10.sp)
                            Text("${state.bookings.size} Active", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Available Suites", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 10.sp)
                            Text("${state.rooms.size} Villas", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Dispatched Services", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 10.sp)
                            Text("${state.services.size} Requests", style = MaterialTheme.typography.bodyMedium, color = ResortGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 5. Active Bookings from Firebase DB
        if (state.activeFilterCategory in listOf("All", "Bookings") && state.bookings.isNotEmpty()) {
            item {
                SectionHeader(title = "Your Active Reservations", subtitle = "Synced with Firebase Realtime Database")
            }

            items(state.bookings.filter {
                state.searchQuery.isEmpty() ||
                        it.suiteTitle.contains(state.searchQuery, ignoreCase = true) ||
                        it.guestName.contains(state.searchQuery, ignoreCase = true)
            }) { booking ->
                BookingItemCard(booking = booking)
            }
        }

        // 6. Featured Resort Suites / Accommodations
        if (state.activeFilterCategory in listOf("All", "Suites")) {
            item {
                SectionHeader(title = "Luxury Accommodations", subtitle = "Overwater villas & beachfront suites")
            }

            items(state.rooms.filter {
                state.searchQuery.isEmpty() ||
                        it.name.contains(state.searchQuery, ignoreCase = true) ||
                        it.type.contains(state.searchQuery, ignoreCase = true)
            }) { room ->
                RoomItemCard(room = room, onBookNow = onOpenBookingDialog)
            }
        }

        // 7. Resort Dining & Spa Experiences
        if (state.activeFilterCategory in listOf("All", "Dining & Spa")) {
            item {
                SectionHeader(title = "Curated Resort Experiences", subtitle = "Michelin dining, serene spa & catamaran tours")
            }

            items(state.amenities.filter {
                state.searchQuery.isEmpty() ||
                        it.title.contains(state.searchQuery, ignoreCase = true) ||
                        it.category.contains(state.searchQuery, ignoreCase = true)
            }) { amenity ->
                AmenityItemCard(amenity = amenity)
            }
        }

        // 8. Service Requests & Concierge Tracker
        if (state.activeFilterCategory in listOf("All", "Services") && state.services.isNotEmpty()) {
            item {
                SectionHeader(title = "Active Concierge & Room Requests", subtitle = "Live status updates in Firebase")
            }

            items(state.services) { service ->
                ServiceRequestCard(
                    service = service,
                    onUpdateStatus = { newStatus -> onUpdateServiceStatus(service.id, newStatus) }
                )
            }
        }

        // 9. Resort Bulletins & Announcements
        if (state.activeFilterCategory in listOf("All", "Announcements") && state.announcements.isNotEmpty()) {
            item {
                SectionHeader(title = "Resort Notices & Daily Events", subtitle = "Live guest bulletins from Wings Management")
            }

            items(state.announcements) { notice ->
                AnnouncementCard(notice = notice)
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        AnimatedLoadText(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(
                color = ResortGoldLight.copy(alpha = 0.7f)
            )
        )
    }
}

@Composable
private fun ResortMetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = ResortGold
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.LightGray,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
fun BookingItemCard(booking: Booking) {
    LuxuryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.suiteTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Booking ID: ${booking.id} • ${booking.roomNumber}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ResortGoldLight.copy(alpha = 0.8f)
                        )
                    )
                }

                LuxuryBadge(
                    text = booking.status,
                    backgroundColor = if (booking.status == "Checked In") SuccessGreen.copy(alpha = 0.2f) else ResortGold.copy(alpha = 0.2f),
                    contentColor = if (booking.status == "Checked In") Color(0xFF81C784) else ResortGoldLight
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(ResortTealDeep.copy(alpha = 0.5f))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Check-In", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(booking.checkInDate, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text("Check-Out", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text(booking.checkOutDate, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total (Paid)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("$${String.format("%.0f", booking.totalPrice)}", style = MaterialTheme.typography.bodyMedium, color = ResortGold, fontWeight = FontWeight.Bold)
                }
            }

            if (booking.specialRequests.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Requests: ${booking.specialRequests}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Composable
fun RoomItemCard(room: ResortRoom, onBookNow: () -> Unit) {
    LuxuryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Column {
            Box {
                val imageRes = when {
                    room.name.contains("Overwater") -> R.drawable.img_resort_hero
                    room.name.contains("Presidential") -> R.drawable.img_villa_suite
                    else -> R.drawable.img_resort_spa
                }

                AsyncImage(
                    model = imageRes,
                    contentDescription = room.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, ResortTealDark.copy(alpha = 0.8f))
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.0f", room.pricePerNight)} / night",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ResortGold
                        )
                    )

                    LuxuryBadge(
                        text = if (room.isAvailable) "Available" else "Reserved",
                        backgroundColor = if (room.isAvailable) SuccessGreen.copy(alpha = 0.3f) else Color.Red.copy(alpha = 0.3f),
                        contentColor = if (room.isAvailable) Color(0xFFB9F6CA) else Color(0xFFFFCDD2)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = room.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.LightGray
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${room.capacity} Guests • ${room.sizeSqFt} sq ft • ${room.view}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ResortGoldLight.copy(alpha = 0.8f)
                        )
                    )

                    Button(
                        onClick = onBookNow,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ResortGold,
                            contentColor = ResortTealDark
                        ),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Reserve", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

@Composable
fun AmenityItemCard(amenity: AmenityExperience) {
    LuxuryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageRes = when (amenity.category) {
                "Dining" -> R.drawable.img_dining_experience
                "Wellness" -> R.drawable.img_resort_spa
                else -> R.drawable.img_resort_hero
            }

            AsyncImage(
                model = imageRes,
                contentDescription = amenity.title,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = amenity.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${amenity.rating} ★",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ResortGold,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Text(
                    text = amenity.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${amenity.timing} • ${amenity.location}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ResortGoldLight.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = amenity.price,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ResortGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceRequestCard(
    service: ServiceRequest,
    onUpdateStatus: (String) -> Unit
) {
    LuxuryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RoomService,
                        contentDescription = null,
                        tint = ResortGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = service.serviceType,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                LuxuryBadge(
                    text = service.status,
                    backgroundColor = when (service.status) {
                        "Completed" -> SuccessGreen.copy(alpha = 0.2f)
                        "In Progress" -> OceanBlue.copy(alpha = 0.2f)
                        else -> ResortGold.copy(alpha = 0.2f)
                    },
                    contentColor = when (service.status) {
                        "Completed" -> Color(0xFF81C784)
                        "In Progress" -> Color(0xFF81D4FA)
                        else -> ResortGoldLight
                    }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = service.details,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${service.roomNumber} • ${service.timeFormatted} • ${service.priority} Priority",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ResortGoldLight.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                )

                // Quick toggle status button
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (service.status != "Completed") {
                        Text(
                            text = "Mark Done",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF81C784),
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SuccessGreen.copy(alpha = 0.2f))
                                .clickable { onUpdateStatus("Completed") }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnnouncementCard(notice: ResortAnnouncement) {
    LuxuryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = ResortGold,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = notice.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = notice.timestamp,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ResortGoldLight.copy(alpha = 0.7f),
                            fontSize = 10.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notice.content,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray)
                )
            }
        }
    }
}
