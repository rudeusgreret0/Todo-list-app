package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Swipe
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.TaskEntity
import com.example.ui.components.AuraCapsuleInputBar
import com.example.ui.components.AuraHeaderSection
import com.example.ui.components.GestureTaskRow
import com.example.ui.theme.LocalAuraColors

@Composable
fun AuraTasksScreen(
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()
    val activeTheme by viewModel.themePreset.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val isThemeStripExpanded by viewModel.isThemeStripExpanded.collectAsStateWithLifecycle()
    val recentlyDeletedTask by viewModel.recentlyDeletedTask.collectAsStateWithLifecycle()
    val editingTask by viewModel.editingTask.collectAsStateWithLifecycle()

    val auraColors = LocalAuraColors.current
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(auraColors.background)
            .drawBehind {
                // Subtle atmospheric luxury top glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            auraColors.backgroundAccentGlow,
                            auraColors.background.copy(alpha = 0f)
                        ),
                        center = Offset(size.width * 0.8f, size.height * 0.06f),
                        radius = size.width * 0.9f
                    ),
                    center = Offset(size.width * 0.8f, size.height * 0.06f),
                    radius = size.width * 0.9f
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .widthIn(max = 600.dp)
                .align(Alignment.TopCenter)
                .padding(horizontal = 24.dp)
        ) {
            AuraHeaderSection()

            Spacer(modifier = Modifier.height(8.dp))

            AuraCapsuleInputBar(
                value = inputText,
                onValueChange = viewModel::onInputTextChanged,
                onSubmit = { viewModel.addTask() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtle gesture hint & clear completed row when tasks exist
            AnimatedVisibility(
                visible = tasks.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val completedCount = tasks.count { it.isCompleted }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Swipe,
                            contentDescription = null,
                            tint = auraColors.textMuted,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = stringResource(R.string.gesture_hint_text),
                            style = MaterialTheme.typography.labelMedium,
                            color = auraColors.textMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (completedCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Clear ($completedCount)",
                            style = MaterialTheme.typography.labelMedium,
                            color = auraColors.accentPrimary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.clearCompletedTasks()
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("clear_completed_button")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (tasks.isEmpty()) {
                AuraEmptyTasksState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("tasks_lazy_column"),
                    contentPadding = PaddingValues(bottom = 104.dp)
                ) {
                    items(
                        items = tasks,
                        key = { it.id }
                    ) { task ->
                        GestureTaskRow(
                            task = task,
                            onToggleCompletion = viewModel::toggleTaskCompletion,
                            onDeleteTask = viewModel::deleteTask,
                            onEditTask = viewModel::startEditingTask,
                            modifier = Modifier.animateItem(
                                fadeInSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                placementSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                ),
                                fadeOutSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            )
                        )
                    }
                }
            }
        }

        // Floating Undo Delete Pill
        AnimatedVisibility(
            visible = recentlyDeletedTask != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            val deleted = recentlyDeletedTask
            if (deleted != null) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = auraColors.textPrimary,
                    tonalElevation = 8.dp,
                    shadowElevation = 10.dp,
                    modifier = Modifier
                        .widthIn(max = 460.dp)
                        .fillMaxWidth()
                        .testTag("undo_delete_snackbar")
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${stringResource(R.string.snackbar_task_deleted)}: \"${deleted.title}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = auraColors.background,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        TextButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.undoDelete()
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("undo_delete_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Undo,
                                contentDescription = null,
                                tint = auraColors.completionGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.snackbar_undo),
                                style = MaterialTheme.typography.labelLarge,
                                color = auraColors.completionGreen
                            )
                        }
                    }
                }
            }
        }

        // Quick Edit Task Dialog (triggered via long-press gesture)
        val taskToEdit = editingTask
        if (taskToEdit != null) {
            EditTaskDialog(
                task = taskToEdit,
                onDismiss = viewModel::dismissEditingTask,
                onConfirm = viewModel::saveEditedTask
            )
        }
    }
}

@Composable
private fun AuraEmptyTasksState(
    modifier: Modifier = Modifier
) {
    val auraColors = LocalAuraColors.current

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 48.dp)
            .testTag("empty_state_container"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Custom sculptural minimalist checkmark illustration
        Box(
            modifier = Modifier
                .size(92.dp)
                .clip(CircleShape)
                .background(auraColors.accentHalo.copy(alpha = 0.65f))
                .border(1.5.dp, auraColors.accentBorder.copy(alpha = 0.28f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(44.dp)) {
                val strokeWidth = 4.dp.toPx()
                drawCircle(
                    color = auraColors.accentPrimary.copy(alpha = 0.25f),
                    radius = size.minDimension / 2f,
                    style = Stroke(width = strokeWidth)
                )
                drawLine(
                    color = auraColors.completionGreen,
                    start = Offset(size.width * 0.28f, size.height * 0.52f),
                    end = Offset(size.width * 0.45f, size.height * 0.69f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = auraColors.completionGreen,
                    start = Offset(size.width * 0.45f, size.height * 0.69f),
                    end = Offset(size.width * 0.74f, size.height * 0.34f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = stringResource(R.string.empty_state_title),
            style = MaterialTheme.typography.titleLarge,
            color = auraColors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.empty_state_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = auraColors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EditTaskDialog(
    task: TaskEntity,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val auraColors = LocalAuraColors.current
    var text by remember(task.id) { mutableStateOf(task.title) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = auraColors.surface,
        titleContentColor = auraColors.textPrimary,
        textContentColor = auraColors.textSecondary,
        title = {
            Text(
                text = stringResource(R.string.edit_task_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = auraColors.accentBorder,
                    unfocusedBorderColor = auraColors.divider,
                    focusedTextColor = auraColors.textPrimary,
                    unfocusedTextColor = auraColors.textPrimary,
                    cursorColor = auraColors.accentPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("edit_task_input_field")
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(text) },
                enabled = text.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = auraColors.accentPrimary,
                    contentColor = auraColors.onAccentPrimary
                ),
                modifier = Modifier.testTag("save_edited_task_button")
            ) {
                Text(stringResource(R.string.action_save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_edit_task_button")
            ) {
                Text(
                    text = stringResource(R.string.action_cancel),
                    color = auraColors.textSecondary
                )
            }
        }
    )
}
