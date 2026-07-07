package com.example.ui.desktop.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.WindowModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TopBar(
    isStartMenuOpen: Boolean,
    onStartClick: () -> Unit,
    windows: List<WindowModel>,
    activeWindowId: String?,
    onWindowClick: (String) -> Unit
) {
    var currentTime by remember { mutableStateOf(SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        color = Color(0xCC0F172A), // Frosted dark slate
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Start Button & Bravia Badge & Running Window Pills
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Start Button
                Surface(
                    onClick = onStartClick,
                    shape = RoundedCornerShape(8.dp),
                    color = if (isStartMenuOpen) MaterialTheme.colorScheme.primary else Color(0xFF1E293B),
                    modifier = Modifier.height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageIconsStart(),
                            contentDescription = "Start",
                            tint = if (isStartMenuOpen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Bravia OS",
                            color = if (isStartMenuOpen) MaterialTheme.colorScheme.onPrimary else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 4.dp),
                    color = Color.White.copy(alpha = 0.2f)
                )

                // Window Taskbar Pills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    windows.forEach { win ->
                        val isActive = win.id == activeWindowId && !win.isMinimized
                        Surface(
                            onClick = { onWindowClick(win.id) },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isActive) Color(0xFF334155) else Color(0xFF1E293B).copy(alpha = 0.7f),
                            border = if (isActive) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)) else null,
                            modifier = Modifier.height(32.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (isActive) MaterialTheme.colorScheme.primary else Color.Gray)
                                )
                                Text(
                                    text = win.title,
                                    color = if (isActive) Color.White else Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Right: System Tray & Clock
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Peripherals Status (Mouse & Keyboard)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .background(Color(0xFF1E293B), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mouse,
                        contentDescription = "Mouse Connected",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(14.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Keyboard,
                        contentDescription = "Keyboard Connected",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Sony Bravia 2 TV",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "WiFi",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )

                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Volume",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = currentTime,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun imageIconsStart() = Icons.Default.GridView
