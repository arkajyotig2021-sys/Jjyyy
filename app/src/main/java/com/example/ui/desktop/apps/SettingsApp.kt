package com.example.ui.desktop.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.DesktopViewModel

@Composable
fun SettingsApp(viewModel: DesktopViewModel) {
    val mouseSens by viewModel.mouseSensitivity.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = "Sony Bravia 2",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Column {
                Text(
                    text = "Sony Bravia 2 Desktop Settings",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "External Mouse & Keyboard Workstation Configuration",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

        // Device Info Card
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF1E293B),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Device Information", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Model: Sony Bravia 2 Android TV", color = Color.White, fontSize = 13.sp)
                Text("Operating System: Android TV OS (Desktop Mode Active)", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                Text("Display Resolution: 4K UHD (3840 x 2160 @ 60Hz)", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
            }
        }

        // Peripherals Guide Card
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF1E293B),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Mouse & Keyboard Setup Guide", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("• USB Peripherals: Plug your USB mouse and keyboard into the TV USB ports. Android TV detects them instantly with cursor support.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Text("• Bluetooth Peripherals: Go to Sony Bravia Settings > Remotes & Accessories > Pair Accessory to connect Bluetooth mice and keyboards.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Text("• Keyboard Shortcuts: Use Ctrl+N for notes, Esc to close windows, and Alt+Tab to switch apps.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }

        // Mouse Sensitivity
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF1E293B),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Mouse Cursor Sensitivity", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Slider(
                    value = mouseSens,
                    onValueChange = { viewModel.setMouseSensitivity(it) },
                    valueRange = 0.5f..2.0f
                )
                Text("Current Sensitivity: %.1fx".format(mouseSens), color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}
