package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.TaskEntity
import com.example.ui.theme.LocalAuraColors
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GestureTaskRow(
    task: TaskEntity,
    onToggleCompletion: (TaskEntity) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit,
    onEditTask: (TaskEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val auraColors = LocalAuraColors.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val swipeThresholdPx = with(density) { 104.dp.toPx() }
    val maxSwipePx = with(density) { 180.dp.toPx() }
    val offsetX = remember(task.id) { Animatable(0f) }
    var hasCrossedThreshold by remember(task.id) { mutableStateOf(false) }

    val completeActionLabel = stringResource(
        if (task.isCompleted) R.string.swipe_action_undo_complete else R.string.swipe_action_complete
    )
    val deleteActionLabel = stringResource(R.string.swipe_action_delete)
    val toggleStatusCd = stringResource(R.string.cd_toggle_task_status)
    val deleteTaskCd = stringResource(R.string.cd_delete_task)

    val textColor by animateColorAsState(
        targetValue = if (task.isCompleted) auraColors.textMuted else auraColors.textPrimary,
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
        label = "TaskTextColor"
    )

    val checkCircleBackground by animateColorAsState(
        targetValue = if (task.isCompleted) auraColors.completionGreen else Color.Transparent,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
        label = "CheckCircleBg"
    )

    val checkCircleBorder by animateColorAsState(
        targetValue = if (task.isCompleted) auraColors.completionGreen else auraColors.checkboxUnchecked,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
        label = "CheckCircleBorder"
    )

    val checkScale by animateFloatAsState(
        targetValue = if (task.isCompleted) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "CheckIconScale"
    )

    val progress = (abs(offsetX.value) / swipeThresholdPx).coerceIn(0f, 1.25f)
    val isSwipingRight = offsetX.value > 4f
    val isSwipingLeft = offsetX.value < -4f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("task_item_card_${task.id}")
            .semantics {
                stateDescription = if (task.isCompleted) "Completed" else "Active"
                customActions = listOf(
                    CustomAccessibilityAction(label = completeActionLabel) {
                        onToggleCompletion(task)
                        true
                    },
                    CustomAccessibilityAction(label = deleteActionLabel) {
                        onDeleteTask(task)
                        true
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
        ) {
            // Gesture reveal background layer
            if (isSwipingRight || isSwipingLeft) {
                val bgColor = if (isSwipingRight) {
                    auraColors.completionGreen.copy(alpha = (0.16f + 0.78f * progress).coerceAtMost(0.95f))
                } else {
                    auraColors.dangerRed.copy(alpha = (0.16f + 0.78f * progress).coerceAtMost(0.95f))
                }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(bgColor, RoundedCornerShape(18.dp))
                        .padding(horizontal = 22.dp),
                    contentAlignment = if (isSwipingRight) Alignment.CenterStart else Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.scale((0.8f + 0.25f * progress).coerceAtMost(1.12f))
                    ) {
                        if (isSwipingRight) {
                            Icon(
                                imageVector = if (task.isCompleted) Icons.Filled.Replay else Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = completeActionLabel,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = deleteActionLabel,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Outlined.DeleteOutline,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            // Foreground interactive task surface
            Row(
                modifier = Modifier
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .fillMaxWidth()
                    .background(auraColors.background, RoundedCornerShape(18.dp))
                    .pointerInput(task.id, task.isCompleted) {
                        detectHorizontalDragGestures(
                            onDragStart = {
                                hasCrossedThreshold = false
                            },
                            onDragCancel = {
                                scope.launch {
                                    offsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMediumLow
                                        )
                                    )
                                }
                            },
                            onDragEnd = {
                                val currentOffset = offsetX.value
                                when {
                                    currentOffset >= swipeThresholdPx -> {
                                        // Swipe right -> toggle completion and spring back smoothly
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onToggleCompletion(task)
                                        scope.launch {
                                            offsetX.animateTo(
                                                targetValue = 0f,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessMedium
                                                )
                                            )
                                        }
                                    }

                                    currentOffset <= -swipeThresholdPx -> {
                                        // Swipe left -> delete task with smooth slide-out
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        scope.launch {
                                            offsetX.animateTo(
                                                targetValue = -maxSwipePx * 1.4f,
                                                animationSpec = tween(durationMillis = 180)
                                            )
                                            onDeleteTask(task)
                                            offsetX.snapTo(0f)
                                        }
                                    }

                                    else -> {
                                        scope.launch {
                                            offsetX.animateTo(
                                                targetValue = 0f,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessMediumLow
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                val dampedDrag = dragAmount * 0.88f
                                val nextOffset = (offsetX.value + dampedDrag).coerceIn(-maxSwipePx, maxSwipePx)
                                scope.launch {
                                    offsetX.snapTo(nextOffset)
                                }
                                val crossedNow = abs(nextOffset) >= swipeThresholdPx
                                if (crossedNow && !hasCrossedThreshold) {
                                    hasCrossedThreshold = true
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                } else if (!crossedNow && hasCrossedThreshold) {
                                    hasCrossedThreshold = false
                                }
                            }
                        )
                    }
                    .combinedClickable(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onToggleCompletion(task)
                        },
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onEditTask(task)
                        }
                    )
                    .padding(vertical = 14.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular checkbox button with 48.dp touch target matching the screenshot
                Box(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .size(48.dp)
                        .clip(CircleShape)
                        .combinedClickable(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onToggleCompletion(task)
                            }
                        )
                        .testTag("task_checkbox_${task.id}")
                        .semantics { contentDescription = toggleStatusCd },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(checkCircleBackground, CircleShape)
                            .border(
                                width = 2.dp,
                                color = checkCircleBorder,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (checkScale > 0.01f) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = auraColors.onCompletionGreen,
                                modifier = Modifier
                                    .size(18.dp)
                                    .scale(checkScale)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Task title matching the clean screenshot typography
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.isCompleted) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    ),
                    color = textColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .testTag("task_title_${task.id}")
                )

                // Right trash icon button matching the screenshot
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDeleteTask(task)
                    },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .size(48.dp)
                        .testTag("delete_task_button_${task.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = deleteTaskCd,
                        tint = auraColors.checkboxUnchecked,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }

        // Subtle hairline divider between tasks just like in the reference image
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 4.dp),
            thickness = 1.dp,
            color = auraColors.divider
        )
    }
}
