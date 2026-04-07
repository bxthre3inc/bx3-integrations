package com.bxthre3.design.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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

enum class BX3ButtonVariant {
    PRIMARY, SECONDARY, GHOST, DANGER
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

@Composable
fun BX3Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: BX3ButtonVariant = BX3ButtonVariant.PRIMARY,
    size: BX3ButtonSize = BX3ButtonSize.MEDIUM,
    enabled: Boolean = true,
    fullWidth: Boolean = false
) {
    val haptics = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")
    val bgColor by animateColorAsState(
        targetValue = if (!enabled) BX3Colors.disabled else when (variant) {
            BX3ButtonVariant.PRIMARY -> BX3Colors.primary
            BX3ButtonVariant.SECONDARY -> BX3Colors.surfaceVariant
            BX3ButtonVariant.GHOST -> Color.Transparent
            BX3ButtonVariant.DANGER -> BX3Colors.error
        },
        label = "background"
    )
    
    val textColor = when (variant) {
        BX3ButtonVariant.PRIMARY -> BX3Colors.onPrimary
        BX3ButtonVariant.SECONDARY -> BX3Colors.onSurface
        BX3ButtonVariant.GHOST -> BX3Colors.primary
        BX3ButtonVariant.DANGER -> BX3Colors.onPrimary
    }
    
    Box(
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .scale(scale)
            .background(bgColor, RoundedCornerShape(size.cornerRadius.dp))
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null
            ) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .padding(horizontal = size.horizontalPadding.dp, vertical = size.verticalPadding.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor
        )
    }
}
