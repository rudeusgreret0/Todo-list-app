package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * State-based controller that manages the active premium color palette,
 * dark/light appearance mode, and interactive palette switcher visibility.
 */
@Stable
class AuraThemeStateController(
    initialPreset: AuraThemePreset = AuraThemePreset.ROYAL_INDIGO,
    initialDarkMode: Boolean = false,
    initialStripExpanded: Boolean = false,
    private val onPresetChanged: (AuraThemePreset) -> Unit = {},
    private val onDarkModeChanged: (Boolean) -> Unit = {},
    private val onStripExpandedChanged: (Boolean) -> Unit = {}
) {
    var currentPreset by mutableStateOf(initialPreset)
        private set

    var isDarkMode by mutableStateOf(initialDarkMode)
        private set

    var isThemeStripExpanded by mutableStateOf(initialStripExpanded)
        private set

    val availablePresets: List<AuraThemePreset> = AuraThemePreset.entries

    fun selectPreset(preset: AuraThemePreset) {
        if (currentPreset != preset) {
            currentPreset = preset
            onPresetChanged(preset)
        }
    }

    fun cycleNextPreset() {
        val nextPreset = currentPreset.next()
        currentPreset = nextPreset
        onPresetChanged(nextPreset)
    }

    fun toggleDarkMode() {
        val nextDark = !isDarkMode
        isDarkMode = nextDark
        onDarkModeChanged(nextDark)
    }

    fun setDarkAppearance(enabled: Boolean) {
        if (isDarkMode != enabled) {
            isDarkMode = enabled
            onDarkModeChanged(enabled)
        }
    }

    fun toggleThemeStrip() {
        val nextExpanded = !isThemeStripExpanded
        isThemeStripExpanded = nextExpanded
        onStripExpandedChanged(nextExpanded)
    }

    internal fun syncExternalState(
        preset: AuraThemePreset,
        darkMode: Boolean,
        stripExpanded: Boolean
    ) {
        currentPreset = preset
        isDarkMode = darkMode
        isThemeStripExpanded = stripExpanded
    }
}

val LocalAuraThemeController = staticCompositionLocalOf {
    AuraThemeStateController()
}

/**
 * Central convenience accessor for the current Aura Theme tokens and state controller.
 */
object AuraTheme {
    val colors: AuraColorTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraColors.current

    val controller: AuraThemeStateController
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraThemeController.current
}
