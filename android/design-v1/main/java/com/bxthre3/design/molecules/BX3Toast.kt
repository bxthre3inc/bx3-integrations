package com.bxthre3.design.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.theme.BX3Colors
import kotlinx.coroutines.delay

@Composable
fun BX3Toast(
    message: String,
    visible: Boolean,
    onDismiss: () -> Unit,
    durationMs: Long = 3000,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(visible) {
        if (visible) {
            delay(durationMs)
            onDismiss()
        }
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Box(
            modifier = modifier
                .background(BX3Colors.inverseSurface, RoundedCornerShape(8.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            BX3Text(
                text = message,
                color = BX3Colors.inverseOnSurface,
                style = BX3TextStyle.BodyMedium
            )
        }
    }
}
