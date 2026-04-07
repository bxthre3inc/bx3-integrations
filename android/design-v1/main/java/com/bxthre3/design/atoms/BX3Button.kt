package com.bxthre3.design.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: BX3ButtonSize = BX3ButtonSize.MEDIUM,
    intent: BX3ButtonIntent = BX3ButtonIntent.PRIMARY,
    enabled: Boolean = true
) {
    val haptics = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f)
    val bgColor by animateColorAsState(
        if (!enabled) BX3Colors.disabled else when (intent) {
            BX3ButtonIntent.PRIMARY -> BX3Colors.primary
            BX3ButtonIntent.SECONDARY -> BX3Colors.secondary
            BX3ButtonIntent.GHOST -> BX3Colors.transparent
            BX3ButtonIntent.DESTRUCTIVE -> BX3Colors.error
            BX3ButtonIntent.AI -> BX3Colors.aiAccent // Patent: AI-specific button
        }
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .background(bgColor, RoundedCornerShape(size.cornerRadius))
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null
            ) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .padding(horizontal = size.horizontalPadding, vertical = size.verticalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = when (intent) {
                BX3ButtonIntent.GHOST -> BX3Colors.primary
                else -> BX3Colors.onPrimary
            }
        )
    }
}

enum class BX3ButtonSize(
    val horizontalPadding: Int,
    val verticalPadding: Int,
    val cornerRadius: Int
) {
    SMALL(12, 6, 4),
    MEDIUM(16, 8, 6),
    LARGE(24, 12, 8)
}

enum class BX3ButtonIntent {
    PRIMARY, SECONDARY, GHOST, DESTRUCTIVE, AI
}
