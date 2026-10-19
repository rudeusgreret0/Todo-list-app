package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.AuraTheme

@Composable
fun AuraHeaderSection(
    modifier: Modifier = Modifier
) {
    val auraColors = AuraTheme.colors
    val themeController = AuraTheme.controller
    val activeTheme = themeController.currentPreset
    val isDarkMode = themeController.isDarkMode
    val haptic = LocalHapticFeedback.current
    val cycleThemeCd = stringResource(R.string.cd_cycle_theme)

    val moonRotation by animateFloatAsState(
        targetValue = if (isDarkMode) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "MoonSunRotation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.header_title),
            style = MaterialTheme.typography.displaySmall,
            color = auraColors.textPrimary,
            modifier = Modifier.testTag("header_title_text")
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // One-tap Active Palette Pill — tap the theme name directly to cycle palettes
            Surface(
                shape = RoundedCornerShape(50),
                color = auraColors.surfaceElevated,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clip(RoundedCornerShape(50))
                    .border(
                        width = 1.dp,
                        color = auraColors.accentBorder.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(50)
                    )
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        themeController.cycleNextPreset()
                    }
                    .semantics { contentDescription = cycleThemeCd }
                    .testTag("cycle_theme_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(activeTheme.previewPrimary, activeTheme.previewSecondary)
                                )
                            )
                    )
                    Text(
                        text = stringResource(activeTheme.labelResId),
                        style = MaterialTheme.typography.labelMedium,
                        color = auraColors.textPrimary,
                        modifier = Modifier.testTag("active_theme_label")
                    )
                }
            }

            // Crescent Moon / Sun Toggle matching the screenshot
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    themeController.toggleDarkMode()
                },
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .size(48.dp)
                    .clip(CircleShape)
                    .testTag("dark_mode_toggle_button")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                    contentDescription = stringResource(R.string.cd_toggle_dark_mode),
                    tint = auraColors.textPrimary,
                    modifier = Modifier
                        .size(27.dp)
                        .rotate(moonRotation)
                )
            }
        }
    }
}

@Composable
fun AuraCapsuleInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val auraColors = AuraTheme.colors
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    // Outer luminous halo container matching the reference screenshot
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(auraColors.accentHalo, RoundedCornerShape(50))
            .padding(4.dp)
            .testTag("task_input_container")
    ) {
        // Inner crisp pill with border and + Add button inside
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .background(auraColors.surface, RoundedCornerShape(50))
                .border(
                    width = if (isFocused) 2.2.dp else 1.8.dp,
                    color = auraColors.accentBorder,
                    shape = RoundedCornerShape(50)
                )
                .padding(start = 22.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.input_placeholder),
                        style = MaterialTheme.typography.bodyLarge,
                        color = auraColors.textMuted
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = auraColors.textPrimary
                    ),
                    cursorBrush = SolidColor(auraColors.accentPrimary),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (value.isNotBlank()) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSubmit()
                                focusManager.clearFocus()
                            }
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocused = it.isFocused }
                        .testTag("task_input_field")
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Vibrant pill-shaped "+ Add" button tucked inside the right side
            Button(
                onClick = {
                    if (value.isNotBlank()) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSubmit()
                        focusManager.clearFocus()
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = auraColors.accentPrimary,
                    contentColor = auraColors.onAccentPrimary,
                    disabledContainerColor = auraColors.accentPrimary.copy(alpha = 0.75f),
                    disabledContentColor = auraColors.onAccentPrimary.copy(alpha = 0.9f)
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 22.dp,
                    vertical = 13.dp
                ),
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .height(48.dp)
                    .testTag("add_task_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.button_add),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
