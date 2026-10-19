package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AuraDatabase
import com.example.data.TaskEntity
import com.example.data.TaskRepository
import com.example.ui.theme.AuraThemePreset
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val themePreset: StateFlow<AuraThemePreset> = repository.themePreset
    val isDarkMode: StateFlow<Boolean> = repository.isDarkMode

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isThemeStripExpanded = MutableStateFlow(false)
    val isThemeStripExpanded: StateFlow<Boolean> = _isThemeStripExpanded.asStateFlow()

    private val _recentlyDeletedTask = MutableStateFlow<TaskEntity?>(null)
    val recentlyDeletedTask: StateFlow<TaskEntity?> = _recentlyDeletedTask.asStateFlow()

    private val _editingTask = MutableStateFlow<TaskEntity?>(null)
    val editingTask: StateFlow<TaskEntity?> = _editingTask.asStateFlow()

    private var undoDismissJob: Job? = null

    fun onInputTextChanged(text: String) {
        _inputText.value = text
    }

    fun addTask(customTitle: String? = null) {
        val titleToInsert = (customTitle ?: _inputText.value).trim()
        if (titleToInsert.isEmpty()) return
        if (customTitle == null) {
            _inputText.value = ""
        }
        viewModelScope.launch {
            repository.addTask(titleToInsert)
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
            _recentlyDeletedTask.value = task
            undoDismissJob?.cancel()
            undoDismissJob = viewModelScope.launch {
                delay(4500L)
                _recentlyDeletedTask.value = null
            }
        }
    }

    fun undoDelete() {
        val deleted = _recentlyDeletedTask.value ?: return
        undoDismissJob?.cancel()
        _recentlyDeletedTask.value = null
        viewModelScope.launch {
            repository.restoreTask(deleted)
        }
    }

    fun dismissUndoBanner() {
        undoDismissJob?.cancel()
        _recentlyDeletedTask.value = null
    }

    fun startEditingTask(task: TaskEntity) {
        _editingTask.value = task
    }

    fun dismissEditingTask() {
        _editingTask.value = null
    }

    fun saveEditedTask(newTitle: String) {
        val task = _editingTask.value ?: return
        _editingTask.value = null
        val trimmed = newTitle.trim()
        if (trimmed.isNotEmpty() && trimmed != task.title) {
            viewModelScope.launch {
                repository.updateTaskTitle(task, trimmed)
            }
        }
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            repository.clearCompletedTasks()
        }
    }

    fun toggleThemeStrip() {
        _isThemeStripExpanded.value = !_isThemeStripExpanded.value
    }

    fun setThemeStripExpanded(expanded: Boolean) {
        _isThemeStripExpanded.value = expanded
    }

    fun selectThemePreset(preset: AuraThemePreset) {
        repository.setThemePreset(preset)
    }

    fun cycleThemePreset() {
        val next = themePreset.value.next()
        repository.setThemePreset(next)
    }

    fun toggleDarkMode() {
        repository.toggleDarkMode()
    }

    fun setDarkMode(enabled: Boolean) {
        repository.setDarkMode(enabled)
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = AuraDatabase.getInstance(context)
                    val repository = TaskRepository(database.taskDao(), context)
                    return TaskViewModel(repository) as T
                }
            }
    }
}
