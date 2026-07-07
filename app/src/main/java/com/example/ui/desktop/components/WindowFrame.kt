package com.example.ui.desktop.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.viewmodel.WindowModel

@Composable
fun WindowFrame(
    window: WindowModel,
    isActive: Boolean,
    onClose: () -> Unit,
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onFocus: () -> Unit,
    content: @Composable () -> Unit
) {
    if (window.isMinimized) return

    var offsetX by remember { mutableStateOf(window.posX) }
    var offsetY by remember { mutableStateOf(window.posY) }

    val modifier = if (window.isMaximized) {
        Modifier
            .fillMaxSize()
            .padding(top = 48.dp, bottom = 76.dp, start = 16.dp, end = 16.dp)
    } else {
        Modifier
            .offset(offsetX.dp, offsetY.dp)
            .width(window.width.dp)
            .height(window.height.dp)
    }

    Surface(
        modifier = modifier
            .shadow(if (isActive) 16.dp else 4.dp, RoundedCornerShape(12.dp))
            .zIndex(window.zIndex),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1E293B),
        tonalElevation = 8.dp,
        border = if (isActive) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    onFocus()
                }
        ) {
            // Window Header (Draggable)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            if (!window.isMaximized) {
                                offsetX += dragAmount.x / 2
                                offsetY += dragAmount.y / 2
                            }
                        }
                    },
                color = if (isActive) Color(0xFF334155) else Color(0xFF1E293B)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = window.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Window Controls
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onMinimize,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Minimize,
                                contentDescription = "Minimize",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        IconButton(
                            onClick = onMaximize,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (window.isMaximized) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                                contentDescription = "Maximize",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Window Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A))
            ) {
                content()
            }
        }
    }
}
