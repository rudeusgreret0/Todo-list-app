package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AuraTasksScreen
import com.example.ui.TaskViewModel
import com.example.ui.theme.AuraThemeProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val taskViewModel: TaskViewModel = viewModel(
                factory = TaskViewModel.provideFactory(context)
            )
            val activePreset by taskViewModel.themePreset.collectAsStateWithLifecycle()
            val isDarkMode by taskViewModel.isDarkMode.collectAsStateWithLifecycle()
            val isThemeStripExpanded by taskViewModel.isThemeStripExpanded.collectAsStateWithLifecycle()

            LaunchedEffect(isDarkMode) {
                enableEdgeToEdge(
                    statusBarStyle = if (isDarkMode) {
                        SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT
                        )
                    },
                    navigationBarStyle = if (isDarkMode) {
                        SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(
                            android.graphics.Color.TRANSPARENT,
                            android.graphics.Color.TRANSPARENT
                        )
                    }
                )
            }

            AuraThemeProvider(
                preset = activePreset,
                darkTheme = isDarkMode,
                isThemeStripExpanded = isThemeStripExpanded,
                onSelectPreset = taskViewModel::selectThemePreset,
                onToggleDarkMode = taskViewModel::setDarkMode,
                onToggleThemeStrip = taskViewModel::setThemeStripExpanded
            ) {
                AuraTasksScreen(viewModel = taskViewModel)
            }
        }
    }
}
