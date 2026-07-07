package com.example.ui.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.AppType

data class StartMenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val appType: AppType,
    val tint: Color
)

@Composable
fun StartMenu(
    onAppSelected: (AppType, String) -> Unit,
    onDismiss: () -> Unit
) {
    val apps = listOf(
        StartMenuItem("Notepad", "Create & edit notes", Icons.Default.EditNote, AppType.NOTEPAD, Color(0xFF38BDF8)),
        StartMenuItem("Files", "Explore storage", Icons.Default.Folder, AppType.FILES, Color(0xFFFACC15)),
        StartMenuItem("Browser", "Web search & surf", Icons.Default.Public, AppType.BROWSER, Color(0xFF34D399)),
        StartMenuItem("Calculator", "Math & science", Icons.Default.Calculate, AppType.CALCULATOR, Color(0xFFFB7185)),
        StartMenuItem("Bravia Settings", "TV & Mouse/Keyboard", Icons.Default.Tv, AppType.SETTINGS, Color(0xFF818CF8)),
        StartMenuItem("AI Assistant", "Gemini AI Workspace", Icons.Default.SmartToy, AppType.AI_ASSISTANT, Color(0xFFC084FC)),
        StartMenuItem("Terminal", "System diagnostics", Icons.Default.Terminal, AppType.TERMINAL, Color(0xFF22D3EE))
    )

    Surface(
        modifier = Modifier
            .width(420.dp)
            .height(440.dp)
            .padding(start = 12.dp, top = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xF20F172A), // Translucent dark slate
        tonalElevation = 12.dp,
        shadowElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header / Profile
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tv,
                            contentDescription = "Sony Bravia",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Column {
                        Text(
                            text = "Sony Bravia 2",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Desktop Workstation Mode",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Start",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pinned Workstation Applications",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Apps Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(apps) { item ->
                    Surface(
                        onClick = { onAppSelected(item.appType, item.title) },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E293B),
                        tonalElevation = 2.dp,
                        modifier = Modifier.height(72.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(item.tint.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = item.tint,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = item.subtitle,
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
