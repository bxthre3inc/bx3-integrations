package com.bxthre3.design.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Surface(
    modifier: Modifier = Modifier,
    elevation: BX3Elevation = BX3Elevation.NONE,
    intent: BX3SurfaceIntent = BX3SurfaceIntent.DEFAULT,
    border: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    
    Box(
        modifier = modifier
            .clip(shape)
            .background(intent.backgroundColor)
            .then(
                when (elevation) {
                    BX3Elevation.NONE -> Modifier
                    BX3Elevation.LOW -> Modifier.shadow(2.dp, shape)
                    BX3Elevation.MEDIUM -> Modifier.shadow(4.dp, shape)
                    BX3Elevation.HIGH -> Modifier.shadow(8.dp, shape)
                    BX3Elevation.ULTRA -> Modifier.shadow(16.dp, shape)
                    BX3Elevation.DYNAMIC -> Modifier.shadow(8.dp, shape) // Patent: ambient-aware
                }
            )
            .then(
                if (border) Modifier.border(1.dp, intent.borderColor, shape) else Modifier
            ),
        content = content
    )
}

enum class BX3Elevation {
    NONE, LOW, MEDIUM, HIGH, ULTRA, DYNAMIC
}

enum class BX3SurfaceIntent(val backgroundColor: Color, val borderColor: Color) {
    DEFAULT(BX3Colors.surface, BX3Colors.border),
    PRIMARY(BX3Colors.primarySurface, BX3Colors.primary),
    RAISED(BX3Colors.surfaceRaised, BX3Colors.border),
    SUNKEN(BX3Colors.surfaceSunken, BX3Colors.borderSubtle),
    AI(BX3Colors.aiSurface, BX3Colors.aiAccent) // Patent: AI-specific surface
}
