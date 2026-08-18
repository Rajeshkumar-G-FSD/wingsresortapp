package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.firebase.FirebaseManager
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
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.ResortUiState

@Composable
fun SettingsScreen(
    state: ResortUiState,
    onRefresh: () -> Unit,
    onOpenRawInspector: () -> Unit,
    onLogout: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var butlerAlertsEnabled by remember { mutableStateOf(true) }
    var highPrecisionSync by remember { mutableStateOf(true) }
    var isPinging by remember { mutableStateOf(false) }
    var latencyMs by remember { mutableStateOf("48 ms") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ResortTealDark)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 1. Settings Header
        item {
            Column(modifier = Modifier.padding(20.dp)) {
                AnimatedLoadText(
                    text = "System & Database Settings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Manage Firebase backend connections, alerts & app preferences",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = ResortGoldLight.copy(alpha = 0.8f)
                    )
                )
            }
        }

        // 2. Firebase Database Configuration Card
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Firebase Project Details",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                LuxuryCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = ResortGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Firebase Firestore & Auth",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                            LiveStatusPulse(isLive = state.isFirebaseLive, text = "Connected")
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        ConfigRowItem(label = "Project ID", value = FirebaseManager.PROJECT_ID)
                        ConfigRowItem(label = "Auth Domain", value = FirebaseManager.AUTH_DOMAIN)
                        ConfigRowItem(label = "Storage Bucket", value = FirebaseManager.STORAGE_BUCKET)
                        ConfigRowItem(label = "App ID", value = FirebaseManager.APP_ID.take(24) + "...")
                        ConfigRowItem(label = "Latency", value = latencyMs)

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    isPinging = true
                                    onRefresh()
                                    isPinging = false
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ResortTealDeep,
                                    contentColor = ResortGoldLight
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Ping Latency", fontSize = 12.sp)
                            }

                            Button(
                                onClick = onOpenRawInspector,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ResortGold,
                                    contentColor = ResortTealDark
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .testTag("open_raw_inspector_button")
                            ) {
                                Icon(imageVector = Icons.Default.Dataset, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("View DB JSON", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 3. Application Preferences
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Guest Notifications & Sync",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                LuxuryCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Push Notifications", color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Receive daily resort bulletins & activity invites", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ResortGold,
                                    checkedTrackColor = ResortTealDeep
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Live Butler & Service Updates", color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Instant alerts when luggage or dining is dispatched", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                            Switch(
                                checked = butlerAlertsEnabled,
                                onCheckedChange = { butlerAlertsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ResortGold,
                                    checkedTrackColor = ResortTealDeep
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Realtime Background Sync", color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Continuous Firestore snapshot synchronization", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                            Switch(
                                checked = highPrecisionSync,
                                onCheckedChange = { highPrecisionSync = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ResortGold,
                                    checkedTrackColor = ResortTealDeep
                                )
                            )
                        }
                    }
                }
            }
        }

        // 4. About App & Version
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                LuxuryCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Wings Resort Suite App", color = Color.White, fontWeight = FontWeight.Bold)
                            LuxuryBadge(text = "v1.0 Release", contentColor = ResortGoldLight)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Powered by Google AI Studio, Jetpack Compose, and Firebase DB Platform.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray)
                        )
                    }
                }
            }
        }

        // 5. Sign Out Button
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("logout_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3E1F1F),
                        contentColor = Color(0xFFFF8A80)
                    )
                ) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out & Return to Login", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ConfigRowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall.copy(
                color = ResortGoldLight,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
