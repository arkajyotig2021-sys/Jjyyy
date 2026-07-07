package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.BraviaDatabase
import com.example.data.NoteEntity
import com.example.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppType {
    NOTEPAD,
    BROWSER,
    FILES,
    CALCULATOR,
    SETTINGS,
    AI_ASSISTANT,
    TERMINAL
}

data class WindowModel(
    val id: String,
    val title: String,
    val appType: AppType,
    val isOpen: Boolean = true,
    val isMinimized: Boolean = false,
    val isMaximized: Boolean = false,
    val zIndex: Float = 1f,
    val width: Float = 640f,
    val height: Float = 480f,
    val posX: Float = 100f,
    val posY: Float = 80f
)

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class FileItem(
    val name: String,
    val size: String,
    val type: String,
    val isFolder: Boolean
)

class DesktopViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository

    val notes: StateFlow<List<NoteEntity>>

    private val _windows = MutableStateFlow<List<WindowModel>>(emptyList())
    val windows: StateFlow<List<WindowModel>> = _windows.asStateFlow()

    private val _activeWindowId = MutableStateFlow<String?>(null)
    val activeWindowId: StateFlow<String?> = _activeWindowId.asStateFlow()

    private val _isStartMenuOpen = MutableStateFlow(false)
    val isStartMenuOpen: StateFlow<Boolean> = _isStartMenuOpen.asStateFlow()

    // Chat messages for AI Assistant
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("Hello! I am your Sony Bravia 2 Desktop AI Assistant. How can I help you configure your workstation today with your mouse and keyboard?", false)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // File explorer state
    private val _currentPath = MutableStateFlow("/storage/emulated/0/BraviaStorage")
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    private val _files = MutableStateFlow<List<FileItem>>(
        listOf(
            FileItem("Documents", "4 items", "Folder", true),
            FileItem("Downloads", "12 items", "Folder", true),
            FileItem("Photos", "248 MB", "Folder", true),
            FileItem("Workstation_Setup.txt", "2.4 KB", "Text", false),
            FileItem("Bravia_Shortcuts.pdf", "1.1 MB", "PDF", false)
        )
    )
    val files: StateFlow<List<FileItem>> = _files.asStateFlow()

    // Settings state
    private val _wallpaperIndex = MutableStateFlow(0)
    val wallpaperIndex: StateFlow<Int> = _wallpaperIndex.asStateFlow()

    private val _mouseSensitivity = MutableStateFlow(1.0f)
    val mouseSensitivity: StateFlow<Float> = _mouseSensitivity.asStateFlow()

    private var maxZIndex = 1f

    init {
        val noteDao = BraviaDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        notes = repository.allNotes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun toggleStartMenu() {
        _isStartMenuOpen.value = !_isStartMenuOpen.value
    }

    fun closeStartMenu() {
        _isStartMenuOpen.value = false
    }

    fun openApp(appType: AppType, title: String) {
        closeStartMenu()
        val existing = _windows.value.find { it.appType == appType }
        if (existing != null) {
            if (existing.isMinimized) {
                restoreWindow(existing.id)
            } else {
                focusWindow(existing.id)
            }
            return
        }

        maxZIndex += 1f
        val newWindow = WindowModel(
            id = "${appType}_${System.currentTimeMillis()}",
            title = title,
            appType = appType,
            zIndex = maxZIndex,
            posX = 120f + (_windows.value.size * 30f),
            posY = 80f + (_windows.value.size * 30f)
        )
        _windows.update { it + newWindow }
        _activeWindowId.value = newWindow.id
    }

    fun closeWindow(id: String) {
        _windows.update { list -> list.filter { it.id != id } }
        if (_activeWindowId.value == id) {
            _activeWindowId.value = _windows.value.maxByOrNull { it.zIndex }?.id
        }
    }

    fun minimizeWindow(id: String) {
        _windows.update { list ->
            list.map { if (it.id == id) it.copy(isMinimized = true) else it }
        }
        if (_activeWindowId.value == id) {
            _activeWindowId.value = _windows.value.filter { !it.isMinimized }.maxByOrNull { it.zIndex }?.id
        }
    }

    fun restoreWindow(id: String) {
        maxZIndex += 1f
        _windows.update { list ->
            list.map { if (it.id == id) it.copy(isMinimized = false, zIndex = maxZIndex) else it }
        }
        _activeWindowId.value = id
    }

    fun maximizeWindow(id: String) {
        _windows.update { list ->
            list.map { if (it.id == id) it.copy(isMaximized = !it.isMaximized) else it }
        }
        focusWindow(id)
    }

    fun focusWindow(id: String) {
        maxZIndex += 1f
        _windows.update { list ->
            list.map { if (it.id == id) it.copy(zIndex = maxZIndex) else it }
        }
        _activeWindowId.value = id
    }

    // Notes actions
    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insert(NoteEntity(title = title, content = content))
        }
    }

    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    // AI Assistant sendMessage
    fun sendAiMessage(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = ChatMessage(prompt, true)
        _chatMessages.update { it + userMsg }
        _isAiLoading.value = true

        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(800)
                val reply = when {
                    prompt.contains("mouse", true) || prompt.contains("keyboard", true) ->
                        "For your Sony Bravia 2, connect your USB or Bluetooth mouse/keyboard directly into the TV's USB ports or pair via Android TV Bluetooth Settings. Android TV will instantly recognize mouse cursor and keyboard shortcuts!"
                    prompt.contains("shortcut", true) ->
                        "Desktop Keyboard Shortcuts:\n• Ctrl + N: New Note\n• Ctrl + Space: Open Start Menu\n• Alt + Tab: Switch Windows\n• Esc: Close Window / Dialog"
                    prompt.contains("resolution", true) ->
                        "Your Sony Bravia 2 is running in 4K UHD Desktop Mode. You can adjust UI scaling in Desktop Settings."
                    else ->
                        "I'm your Sony Bravia Desktop assistant! I can help you manage your files, take notes, browse the web, or optimize your external mouse and keyboard workstation experience."
                }
                _chatMessages.update { it + ChatMessage(reply, false) }
            } catch (e: Exception) {
                _chatMessages.update { it + ChatMessage("Error: ${e.localizedMessage}", false) }
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun setWallpaper(index: Int) {
        _wallpaperIndex.value = index
    }

    fun setMouseSensitivity(sens: Float) {
        _mouseSensitivity.value = sens
    }
}
