package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.R

enum class AuraThemePreset(
    val id: String,
    val labelResId: Int,
    val subtitle: String,
    val previewPrimary: Color,
    val previewSecondary: Color,
    val previewSurfaceLight: Color,
    val previewSurfaceDark: Color
) {
    ROYAL_INDIGO(
        id = "royal_indigo",
        labelResId = R.string.theme_royal_indigo,
        subtitle = "Electric Indigo & Mint",
        previewPrimary = Color(0xFF3D36D3),
        previewSecondary = Color(0xFF0BB87B),
        previewSurfaceLight = Color(0xFFF5F6FF),
        previewSurfaceDark = Color(0xFF131524)
    ),
    OBSIDIAN_GOLD(
        id = "obsidian_gold",
        labelResId = R.string.theme_obsidian_gold,
        subtitle = "Champagne Gold & Teal",
        previewPrimary = Color(0xFFC59B27),
        previewSecondary = Color(0xFF14B8A6),
        previewSurfaceLight = Color(0xFFF9F4EA),
        previewSurfaceDark = Color(0xFF171512)
    ),
    EMERALD_SILK(
        id = "emerald_silk",
        labelResId = R.string.theme_emerald_silk,
        subtitle = "Botanical Jade & Sage",
        previewPrimary = Color(0xFF059669),
        previewSecondary = Color(0xFF34D399),
        previewSurfaceLight = Color(0xFFF0FAF5),
        previewSurfaceDark = Color(0xFF101F19)
    ),
    ROSE_QUARTZ(
        id = "rose_quartz",
        labelResId = R.string.theme_rose_quartz,
        subtitle = "Velvet Rose & Seafoam",
        previewPrimary = Color(0xFFDB2777),
        previewSecondary = Color(0xFF10B981),
        previewSurfaceLight = Color(0xFFFFF5F9),
        previewSurfaceDark = Color(0xFF1C1118)
    ),
    NORDIC_FROST(
        id = "nordic_frost",
        labelResId = R.string.theme_nordic_frost,
        subtitle = "Arctic Sapphire & Glacier",
        previewPrimary = Color(0xFF0284C7),
        previewSecondary = Color(0xFF0BB87B),
        previewSurfaceLight = Color(0xFFF1F7FE),
        previewSurfaceDark = Color(0xFF111B27)
    ),
    AMETHYST_VELVET(
        id = "amethyst_velvet",
        labelResId = R.string.theme_amethyst_velvet,
        subtitle = "Imperial Violet & Emerald",
        previewPrimary = Color(0xFF7C3AED),
        previewSecondary = Color(0xFF10B981),
        previewSurfaceLight = Color(0xFFF6F2FF),
        previewSurfaceDark = Color(0xFF161124)
    );

    fun next(): AuraThemePreset {
        val all = entries
        val nextIndex = (ordinal + 1) % all.size
        return all[nextIndex]
    }

    companion object {
        fun fromId(id: String): AuraThemePreset =
            entries.firstOrNull { it.id == id } ?: ROYAL_INDIGO
    }
}

@Immutable
data class AuraColorTokens(
    val background: Color,
    val backgroundAccentGlow: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val accentPrimary: Color,
    val accentBorder: Color,
    val accentHalo: Color,
    val onAccentPrimary: Color,
    val completionGreen: Color,
    val onCompletionGreen: Color,
    val dangerRed: Color,
    val divider: Color,
    val checkboxUnchecked: Color
) {
    fun toMaterialColorScheme(isDark: Boolean): ColorScheme {
        return if (isDark) {
            darkColorScheme(
                primary = accentPrimary,
                onPrimary = onAccentPrimary,
                primaryContainer = accentHalo,
                onPrimaryContainer = textPrimary,
                secondary = completionGreen,
                onSecondary = onCompletionGreen,
                secondaryContainer = completionGreen.copy(alpha = 0.2f),
                onSecondaryContainer = textPrimary,
                tertiary = accentBorder,
                onTertiary = onAccentPrimary,
                background = background,
                onBackground = textPrimary,
                surface = surface,
                onSurface = textPrimary,
                surfaceVariant = surfaceElevated,
                onSurfaceVariant = textSecondary,
                error = dangerRed,
                onError = Color.White,
                outline = divider,
                outlineVariant = checkboxUnchecked
            )
        } else {
            lightColorScheme(
                primary = accentPrimary,
                onPrimary = onAccentPrimary,
                primaryContainer = accentHalo,
                onPrimaryContainer = textPrimary,
                secondary = completionGreen,
                onSecondary = onCompletionGreen,
                secondaryContainer = completionGreen.copy(alpha = 0.16f),
                onSecondaryContainer = textPrimary,
                tertiary = accentBorder,
                onTertiary = onAccentPrimary,
                background = background,
                onBackground = textPrimary,
                surface = surface,
                onSurface = textPrimary,
                surfaceVariant = surfaceElevated,
                onSurfaceVariant = textSecondary,
                error = dangerRed,
                onError = Color.White,
                outline = divider,
                outlineVariant = checkboxUnchecked
            )
        }
    }
}

fun getAuraColorTokens(preset: AuraThemePreset, isDark: Boolean): AuraColorTokens {
    return when (preset) {
        AuraThemePreset.ROYAL_INDIGO -> if (!isDark) {
            AuraColorTokens(
                background = Color(0xFFFCFCFF),
                backgroundAccentGlow = Color(0xFFEEF0FF),
                surface = Color(0xFFFFFFFF),
                surfaceElevated = Color(0xFFF5F6FF),
                textPrimary = Color(0xFF1E222B),
                textSecondary = Color(0xFF646A7A),
                textMuted = Color(0xFF8E95A5),
                accentPrimary = Color(0xFF3D36D3),
                accentBorder = Color(0xFF4F46E5),
                accentHalo = Color(0xFFE5E6FA),
                onAccentPrimary = Color(0xFFFFFFFF),
                completionGreen = Color(0xFF0BB87B),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFEF4444),
                divider = Color(0xFFF1F2F6),
                checkboxUnchecked = Color(0xFF6E7685)
            )
        } else {
            AuraColorTokens(
                background = Color(0xFF0B0C14),
                backgroundAccentGlow = Color(0xFF191B34),
                surface = Color(0xFF131524),
                surfaceElevated = Color(0xFF1B1E32),
                textPrimary = Color(0xFFF4F5FA),
                textSecondary = Color(0xFFA0A6BE),
                textMuted = Color(0xFF636982),
                accentPrimary = Color(0xFF635BFF),
                accentBorder = Color(0xFF7C75FF),
                accentHalo = Color(0xFF232252),
                onAccentPrimary = Color(0xFFFFFFFF),
                completionGreen = Color(0xFF10B981),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFF87171),
                divider = Color(0xFF1E2136),
                checkboxUnchecked = Color(0xFF7B829E)
            )
        }

        AuraThemePreset.OBSIDIAN_GOLD -> if (!isDark) {
            AuraColorTokens(
                background = Color(0xFFFDFBF7),
                backgroundAccentGlow = Color(0xFFF7F0E2),
                surface = Color(0xFFFFFFFF),
                surfaceElevated = Color(0xFFF9F4EA),
                textPrimary = Color(0xFF1F1C18),
                textSecondary = Color(0xFF6E6559),
                textMuted = Color(0xFF9C9284),
                accentPrimary = Color(0xFFB38728),
                accentBorder = Color(0xFFC99E3A),
                accentHalo = Color(0xFFF3E8CE),
                onAccentPrimary = Color(0xFFFFFFFF),
                completionGreen = Color(0xFF0D9488),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFE11D48),
                divider = Color(0xFFF1ECE1),
                checkboxUnchecked = Color(0xFF7D7365)
            )
        } else {
            AuraColorTokens(
                background = Color(0xFF0E0D0B),
                backgroundAccentGlow = Color(0xFF241E14),
                surface = Color(0xFF171512),
                surfaceElevated = Color(0xFF221F1A),
                textPrimary = Color(0xFFF7F3EB),
                textSecondary = Color(0xFFB5AA99),
                textMuted = Color(0xFF736B5E),
                accentPrimary = Color(0xFFD4AF37),
                accentBorder = Color(0xFFE5C158),
                accentHalo = Color(0xFF332B17),
                onAccentPrimary = Color(0xFF14110B),
                completionGreen = Color(0xFF14B8A6),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFFB7185),
                divider = Color(0xFF26221C),
                checkboxUnchecked = Color(0xFF8C8170)
            )
        }

        AuraThemePreset.EMERALD_SILK -> if (!isDark) {
            AuraColorTokens(
                background = Color(0xFFF9FDFB),
                backgroundAccentGlow = Color(0xFFE6F7EF),
                surface = Color(0xFFFFFFFF),
                surfaceElevated = Color(0xFFF0FAF5),
                textPrimary = Color(0xFF13261F),
                textSecondary = Color(0xFF526E63),
                textMuted = Color(0xFF84A095),
                accentPrimary = Color(0xFF059669),
                accentBorder = Color(0xFF10B981),
                accentHalo = Color(0xFFD9F5E9),
                onAccentPrimary = Color(0xFFFFFFFF),
                completionGreen = Color(0xFF0BB87B),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFEF4444),
                divider = Color(0xFFEAF3EE),
                checkboxUnchecked = Color(0xFF648276)
            )
        } else {
            AuraColorTokens(
                background = Color(0xFF08120E),
                backgroundAccentGlow = Color(0xFF0F2920),
                surface = Color(0xFF101F19),
                surfaceElevated = Color(0xFF172B23),
                textPrimary = Color(0xFFECFDF5),
                textSecondary = Color(0xFF94B8A9),
                textMuted = Color(0xFF59786B),
                accentPrimary = Color(0xFF10B981),
                accentBorder = Color(0xFF34D399),
                accentHalo = Color(0xFF133A2C),
                onAccentPrimary = Color(0xFF051B12),
                completionGreen = Color(0xFF34D399),
                onCompletionGreen = Color(0xFF062117),
                dangerRed = Color(0xFFF87171),
                divider = Color(0xFF1B3028),
                checkboxUnchecked = Color(0xFF6F9485)
            )
        }

        AuraThemePreset.ROSE_QUARTZ -> if (!isDark) {
            AuraColorTokens(
                background = Color(0xFFFFFBFD),
                backgroundAccentGlow = Color(0xFFFCEEF5),
                surface = Color(0xFFFFFFFF),
                surfaceElevated = Color(0xFFFFF5F9),
                textPrimary = Color(0xFF281822),
                textSecondary = Color(0xFF755B6A),
                textMuted = Color(0xFFA38998),
                accentPrimary = Color(0xFFBE185D),
                accentBorder = Color(0xFFDB2777),
                accentHalo = Color(0xFFFCE3EE),
                onAccentPrimary = Color(0xFFFFFFFF),
                completionGreen = Color(0xFF0BB87B),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFE11D48),
                divider = Color(0xFFF6EBF0),
                checkboxUnchecked = Color(0xFF876C7C)
            )
        } else {
            AuraColorTokens(
                background = Color(0xFF130B10),
                backgroundAccentGlow = Color(0xFF2D1524),
                surface = Color(0xFF1C1118),
                surfaceElevated = Color(0xFF281822),
                textPrimary = Color(0xFFFDF2F8),
                textSecondary = Color(0xFFC4A5B7),
                textMuted = Color(0xFF7D6272),
                accentPrimary = Color(0xFFEC4899),
                accentBorder = Color(0xFFF472B6),
                accentHalo = Color(0xFF3D192D),
                onAccentPrimary = Color(0xFFFFFFFF),
                completionGreen = Color(0xFF10B981),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFFB7185),
                divider = Color(0xFF2D1C26),
                checkboxUnchecked = Color(0xFF96788A)
            )
        }

        AuraThemePreset.NORDIC_FROST -> if (!isDark) {
            AuraColorTokens(
                background = Color(0xFFF8FBFF),
                backgroundAccentGlow = Color(0xFFE6F2FE),
                surface = Color(0xFFFFFFFF),
                surfaceElevated = Color(0xFFF1F7FE),
                textPrimary = Color(0xFF112030),
                textSecondary = Color(0xFF55697E),
                textMuted = Color(0xFF8AA0B6),
                accentPrimary = Color(0xFF0284C7),
                accentBorder = Color(0xFF0EA5E9),
                accentHalo = Color(0xFFDFF0FC),
                onAccentPrimary = Color(0xFFFFFFFF),
                completionGreen = Color(0xFF0BB87B),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFEF4444),
                divider = Color(0xFFECF2F8),
                checkboxUnchecked = Color(0xFF687E94)
            )
        } else {
            AuraColorTokens(
                background = Color(0xFF091019),
                backgroundAccentGlow = Color(0xFF11243B),
                surface = Color(0xFF111B27),
                surfaceElevated = Color(0xFF172536),
                textPrimary = Color(0xFFF0F7FF),
                textSecondary = Color(0xFF94ACC6),
                textMuted = Color(0xFF5B718A),
                accentPrimary = Color(0xFF38BDF8),
                accentBorder = Color(0xFF7DD3FC),
                accentHalo = Color(0xFF14324F),
                onAccentPrimary = Color(0xFF061929),
                completionGreen = Color(0xFF10B981),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFF87171),
                divider = Color(0xFF1B2B3D),
                checkboxUnchecked = Color(0xFF6F88A3)
            )
        }

        AuraThemePreset.AMETHYST_VELVET -> if (!isDark) {
            AuraColorTokens(
                background = Color(0xFFFCFAFF),
                backgroundAccentGlow = Color(0xFFF2EBFF),
                surface = Color(0xFFFFFFFF),
                surfaceElevated = Color(0xFFF6F2FF),
                textPrimary = Color(0xFF1F1630),
                textSecondary = Color(0xFF66587D),
                textMuted = Color(0xFF9688AD),
                accentPrimary = Color(0xFF7C3AED),
                accentBorder = Color(0xFF8B5CF6),
                accentHalo = Color(0xFFEDE5FF),
                onAccentPrimary = Color(0xFFFFFFFF),
                completionGreen = Color(0xFF0BB87B),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFE11D48),
                divider = Color(0xFFF0EAFA),
                checkboxUnchecked = Color(0xFF786A91)
            )
        } else {
            AuraColorTokens(
                background = Color(0xFF0D0A14),
                backgroundAccentGlow = Color(0xFF211638),
                surface = Color(0xFF161124),
                surfaceElevated = Color(0xFF201933),
                textPrimary = Color(0xFFF6F2FF),
                textSecondary = Color(0xFFB3A5CC),
                textMuted = Color(0xFF6E6087),
                accentPrimary = Color(0xFFA78BFA),
                accentBorder = Color(0xFFC4B5FD),
                accentHalo = Color(0xFF2E214D),
                onAccentPrimary = Color(0xFF160E29),
                completionGreen = Color(0xFF10B981),
                onCompletionGreen = Color(0xFFFFFFFF),
                dangerRed = Color(0xFFFB7185),
                divider = Color(0xFF261E3B),
                checkboxUnchecked = Color(0xFF8576A1)
            )
        }
    }
}
