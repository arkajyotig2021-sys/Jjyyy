package com.example.ui.desktop.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TerminalApp() {
    var commandInput by remember { mutableStateOf("") }
    val logs = remember {
        mutableStateListOf(
            "Sony Bravia OS [Version 2026.4.2]",
            "(c) Sony Corporation & Android TV Workstation. All rights reserved.",
            "",
            "Initializing USB & Bluetooth Input Manager...",
            "Mouse Device Detected: Logitech Wireless Mouse (Port 1)",
            "Keyboard Device Detected: Mechanical Keyboard (Port 2)",
            "Display Output: 3840x2160 @ 60Hz HDMI 2.1",
            "Type 'help' for available workstation commands.",
            ""
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(logs) { log ->
                Text(
                    text = log,
                    color = Color(0xFF38BDF8),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "bravia@desktop:~$",
                color = Color(0xFF34D399),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            OutlinedTextField(
                value = commandInput,
                onValueChange = { commandInput = it },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF34D399),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
            Button(
                onClick = {
                    if (commandInput.isNotBlank()) {
                        logs.add("bravia@desktop:~$ $commandInput")
                        when (commandInput.trim().lowercase()) {
                            "help" -> {
                                logs.add("Available commands: clear, lscpu, lsusb, status, reboot")
                            }
                            "clear" -> {
                                logs.clear()
                            }
                            "lsusb" -> {
                                logs.add("Bus 001 Device 002: ID 046d:c534 Logitech Receiver (Mouse & Keyboard)")
                                logs.add("Bus 001 Device 001: ID 1d6b:0002 Linux Foundation 2.0 root hub")
                            }
                            "status" -> {
                                logs.add("Sony Bravia 2 TV Workstation: OK")
                                logs.add("Memory: 3.2 GB / 8.0 GB Used")
                                logs.add("CPU Temperature: 38.4°C")
                            }
                            else -> {
                                logs.add("Command not found: $commandInput. Type 'help' for commands.")
                            }
                        }
                        commandInput = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
            ) {
                Text("Run", color = Color.White)
            }
        }
    }
}
