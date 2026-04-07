package com.bxthre3.design.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale

object BX3Animations {
    @Composable
    fun shimmerModifier(): Modifier = composed {
        val transition = rememberInfiniteTransition(label = "shimmer")
        val alpha = transition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha"
        )
        alpha(alpha.value)
    }
    
    @Composable
    fun pressableModifier(onClick: () -> Unit): Modifier = composed {
        val interactionSource = remember { MutableInteractionSource() }
        clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    }
}
