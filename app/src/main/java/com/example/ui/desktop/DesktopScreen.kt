package com.example.ui.desktop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.desktop.apps.*
import com.example.ui.desktop.components.*
import com.example.viewmodel.AppType
import com.example.viewmodel.DesktopViewModel

@Composable
fun DesktopScreen(viewModel: DesktopViewModel) {
    val windows by viewModel.windows.collectAsState()
    val activeWindowId by viewModel.activeWindowId.collectAsState()
    val isStartMenuOpen by viewModel.isStartMenuOpen.collectAsState()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when {
                        event.isCtrlPressed && event.key == Key.N -> {
                            viewModel.openApp(AppType.NOTEPAD, "Notepad")
                            true
                        }
                        event.isCtrlPressed && event.key == Key.Spacebar -> {
                            viewModel.toggleStartMenu()
                            true
                        }
                        event.key == Key.Escape -> {
                            if (isStartMenuOpen) {
                                viewModel.closeStartMenu()
                            } else if (activeWindowId != null) {
                                viewModel.closeWindow(activeWindowId!!)
                            }
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
            .clickable(enabled = isStartMenuOpen) {
                viewModel.closeStartMenu()
            }
    ) {
        // Desktop Wallpaper Background
        Image(
            painter = painterResource(id = R.drawable.img_desktop_wallpaper),
            contentDescription = "Desktop Wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Atmospheric Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x660F172A))
        )

        // Desktop Icons Grid (Left)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, bottom = 80.dp)
        ) {
            DesktopIcons(
                onAppOpen = { type, title -> viewModel.openApp(type, title) }
            )
        }

        // Open Floating Windows
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, bottom = 72.dp)
        ) {
            windows.forEach { win ->
                WindowFrame(
                    window = win,
                    isActive = win.id == activeWindowId,
                    onClose = { viewModel.closeWindow(win.id) },
                    onMinimize = { viewModel.minimizeWindow(win.id) },
                    onMaximize = { viewModel.maximizeWindow(win.id) },
                    onFocus = { viewModel.focusWindow(win.id) }
                ) {
                    when (win.appType) {
                        AppType.NOTEPAD -> NotepadApp(viewModel)
                        AppType.BROWSER -> BrowserApp()
                        AppType.FILES -> FileManagerApp(viewModel)
                        AppType.CALCULATOR -> CalculatorApp()
                        AppType.SETTINGS -> SettingsApp(viewModel)
                        AppType.AI_ASSISTANT -> AiAssistantApp(viewModel)
                        AppType.TERMINAL -> TerminalApp()
                    }
                }
            }
        }

        // Top Status Bar
        Column(modifier = Modifier.fillMaxWidth()) {
            TopBar(
                isStartMenuOpen = isStartMenuOpen,
                onStartClick = { viewModel.toggleStartMenu() },
                windows = windows,
                activeWindowId = activeWindowId,
                onWindowClick = { id ->
                    val win = windows.find { it.id == id }
                    if (win != null) {
                        if (win.isMinimized) viewModel.restoreWindow(id)
                        else viewModel.focusWindow(id)
                    }
                }
            )

            // Start Menu Popup
            if (isStartMenuOpen) {
                StartMenu(
                    onAppSelected = { type, title -> viewModel.openApp(type, title) },
                    onDismiss = { viewModel.closeStartMenu() }
                )
            }
        }

        // Bottom Dock Taskbar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.BottomCenter
        ) {
            Dock(
                onAppClick = { type, title -> viewModel.openApp(type, title) }
            )
        }
    }
}
