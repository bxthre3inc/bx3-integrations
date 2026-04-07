package com.bxthre3.design.atoms

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Badge(
    text: String,
    modifier: Modifier = Modifier,
    intent: BX3BadgeIntent = BX3BadgeIntent.NEUTRAL,
    pulse: Boolean = false
) {
    val bgColor by animateColorAsState(intent.backgroundColor)
    
    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = intent.textColor)
    }
}

enum class BX3BadgeIntent(val backgroundColor: androidx.compose.ui.graphics.Color, val textColor: androidx.compose.ui.graphics.Color) {
    NEUTRAL(BX3Colors.subtle, BX3Colors.textSecondary),
    URGENCY_LOW(BX3Colors.success, BX3Colors.onSuccess),
    URGENCY_MEDIUM(BX3Colors.warning, BX3Colors.onWarning),
    URGENCY_HIGH(BX3Colors.error, BX3Colors.onError),
    URGENCY_CRITICAL(BX3Colors.critical, BX3Colors.onCritical),
    AI(BX3Colors.aiSurface, BX3Colors.aiAccent)
}
