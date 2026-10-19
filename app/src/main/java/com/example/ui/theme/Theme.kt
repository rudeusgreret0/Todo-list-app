package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAuraColors = staticCompositionLocalOf {
    getAuraColorTokens(AuraThemePreset.ROYAL_INDIGO, isDark = false)
}

@Composable
private fun animateAuraColor(target: Color): Color {
    val animated by animateColorAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing),
        label = "AuraColorTransition"
    )
    return animated
}

/**
 * Stateful Theme Provider composable that supplies animated [AuraColorTokens],
 * Material 3 [ColorScheme], and a state-based [AuraThemeStateController] down the tree.
 */
@Composable
fun AuraThemeProvider(
    preset: AuraThemePreset = AuraThemePreset.ROYAL_INDIGO,
    darkTheme: Boolean = false,
    isThemeStripExpanded: Boolean = false,
    onSelectPreset: (AuraThemePreset) -> Unit = {},
    onToggleDarkMode: (Boolean) -> Unit = {},
    onToggleThemeStrip: (Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    val controller = remember {
        AuraThemeStateController(
            initialPreset = preset,
            initialDarkMode = darkTheme,
            initialStripExpanded = isThemeStripExpanded,
            onPresetChanged = onSelectPreset,
            onDarkModeChanged = onToggleDarkMode,
            onStripExpandedChanged = onToggleThemeStrip
        )
    }

    SideEffect {
        controller.syncExternalState(
            preset = preset,
            darkMode = darkTheme,
            stripExpanded = isThemeStripExpanded
        )
    }

    val rawTokens = getAuraColorTokens(
        preset = controller.currentPreset,
        isDark = controller.isDarkMode
    )

    val animatedTokens = AuraColorTokens(
        background = animateAuraColor(rawTokens.background),
        backgroundAccentGlow = animateAuraColor(rawTokens.backgroundAccentGlow),
        surface = animateAuraColor(rawTokens.surface),
        surfaceElevated = animateAuraColor(rawTokens.surfaceElevated),
        textPrimary = animateAuraColor(rawTokens.textPrimary),
        textSecondary = animateAuraColor(rawTokens.textSecondary),
        textMuted = animateAuraColor(rawTokens.textMuted),
        accentPrimary = animateAuraColor(rawTokens.accentPrimary),
        accentBorder = animateAuraColor(rawTokens.accentBorder),
        accentHalo = animateAuraColor(rawTokens.accentHalo),
        onAccentPrimary = animateAuraColor(rawTokens.onAccentPrimary),
        completionGreen = animateAuraColor(rawTokens.completionGreen),
        onCompletionGreen = animateAuraColor(rawTokens.onCompletionGreen),
        dangerRed = animateAuraColor(rawTokens.dangerRed),
        divider = animateAuraColor(rawTokens.divider),
        checkboxUnchecked = animateAuraColor(rawTokens.checkboxUnchecked)
    )

    val colorScheme = animatedTokens.toMaterialColorScheme(isDark = controller.isDarkMode)

    CompositionLocalProvider(
        LocalAuraThemeController provides controller,
        LocalAuraColors provides animatedTokens
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Composable
fun AuraTasksTheme(
    preset: AuraThemePreset = AuraThemePreset.ROYAL_INDIGO,
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    AuraThemeProvider(
        preset = preset,
        darkTheme = darkTheme,
        content = content
    )
}
