package com.example.ui.desktop.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.AppType

data class DesktopIconItem(
    val title: String,
    val icon: ImageVector,
    val appType: AppType,
    val color: Color
)

@Composable
fun DesktopIcons(
    onAppOpen: (AppType, String) -> Unit
) {
    val icons = listOf(
        DesktopIconItem("Notepad", Icons.Default.EditNote, AppType.NOTEPAD, Color(0xFF38BDF8)),
        DesktopIconItem("File Manager", Icons.Default.Folder, AppType.FILES, Color(0xFFFACC15)),
        DesktopIconItem("Web Browser", Icons.Default.Public, AppType.BROWSER, Color(0xFF34D399)),
        DesktopIconItem("Calculator", Icons.Default.Calculate, AppType.CALCULATOR, Color(0xFFFB7185)),
        DesktopIconItem("Bravia Settings", Icons.Default.Tv, AppType.SETTINGS, Color(0xFF818CF8)),
        DesktopIconItem("AI Assistant", Icons.Default.SmartToy, AppType.AI_ASSISTANT, Color(0xFFC084FC)),
        DesktopIconItem("Terminal", Icons.Default.Terminal, AppType.TERMINAL, Color(0xFF22D3EE))
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(24.dp)
            .width(100.dp)
            .fillMaxHeight()
    ) {
        items(icons) { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onAppOpen(item.appType, item.title) }
                    .padding(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = item.color.copy(alpha = 0.25f),
                    modifier = Modifier.size(54.dp),
                    tonalElevation = 4.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = item.color,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}
