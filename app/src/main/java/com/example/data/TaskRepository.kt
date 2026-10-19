package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.ui.theme.AuraThemePreset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskRepository(
    private val taskDao: TaskDao,
    context: Context
) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _themePreset = MutableStateFlow(
        AuraThemePreset.fromId(
            prefs.getString(KEY_THEME_PRESET, AuraThemePreset.ROYAL_INDIGO.id)
                ?: AuraThemePreset.ROYAL_INDIGO.id
        )
    )
    val themePreset: StateFlow<AuraThemePreset> = _themePreset.asStateFlow()

    private val _isDarkMode = MutableStateFlow(
        prefs.getBoolean(KEY_DARK_MODE, false)
    )
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    val allTasks: Flow<List<TaskEntity>> = taskDao.observeAllTasks()

    suspend fun addTask(title: String): Long {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return -1L
        val now = System.currentTimeMillis()
        return taskDao.insertTask(
            TaskEntity(
                title = trimmed,
                isCompleted = false,
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun restoreTask(task: TaskEntity): Long {
        return taskDao.insertTask(task)
    }

    suspend fun toggleTaskCompletion(task: TaskEntity) {
        taskDao.updateTask(
            task.copy(
                isCompleted = !task.isCompleted,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateTaskTitle(task: TaskEntity, newTitle: String) {
        val trimmed = newTitle.trim()
        if (trimmed.isEmpty()) return
        taskDao.updateTask(
            task.copy(
                title = trimmed,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun clearCompletedTasks() {
        taskDao.deleteCompletedTasks()
    }

    fun setThemePreset(preset: AuraThemePreset) {
        _themePreset.value = preset
        prefs.edit().putString(KEY_THEME_PRESET, preset.id).apply()
    }

    fun toggleDarkMode() {
        val next = !_isDarkMode.value
        _isDarkMode.value = next
        prefs.edit().putBoolean(KEY_DARK_MODE, next).apply()
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    companion object {
        private const val PREFS_NAME = "aura_tasks_preferences"
        private const val KEY_THEME_PRESET = "key_theme_preset"
        private const val KEY_DARK_MODE = "key_dark_mode"
    }
}
