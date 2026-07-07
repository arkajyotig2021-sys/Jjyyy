package com.example.ui.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.viewmodel.AppType

data class DockItem(
    val title: String,
    val icon: ImageVector,
    val appType: AppType,
    val color: Color
)

@Composable
fun Dock(
    onAppClick: (AppType, String) -> Unit
) {
    val dockApps = listOf(
        DockItem("Notepad", Icons.Default.EditNote, AppType.NOTEPAD, Color(0xFF38BDF8)),
        DockItem("Files", Icons.Default.Folder, AppType.FILES, Color(0xFFFACC15)),
        DockItem("Browser", Icons.Default.Public, AppType.BROWSER, Color(0xFF34D399)),
        DockItem("Calculator", Icons.Default.Calculate, AppType.CALCULATOR, Color(0xFFFB7185)),
        DockItem("Settings", Icons.Default.Tv, AppType.SETTINGS, Color(0xFF818CF8)),
        DockItem("AI", Icons.Default.SmartToy, AppType.AI_ASSISTANT, Color(0xFFC084FC)),
        DockItem("Terminal", Icons.Default.Terminal, AppType.TERMINAL, Color(0xFF22D3EE))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xCC0F172A),
            tonalElevation = 8.dp,
            shadowElevation = 12.dp,
            modifier = Modifier.height(64.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                dockApps.forEach { item ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(item.color.copy(alpha = 0.2f))
                            .clickable { onAppClick(item.appType, item.title) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = item.color,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}
