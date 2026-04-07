package com.bxthre3.design.atoms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Progress(
    progress: Float,
    modifier: Modifier = Modifier,
    size: BX3ProgressSize = BX3ProgressSize.MEDIUM
) {
    val animatedProgress by animateFloatAsState(progress.coerceIn(0f, 1f))
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(size.height.dp)
            .clip(RoundedCornerShape(size.height.dp / 2))
            .background(BX3Colors.subtle)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(size.height.dp)
                .clip(RoundedCornerShape(size.height.dp / 2))
                .background(BX3Colors.primary)
        )
    }
}

enum class BX3ProgressSize(val height: Int) {
    SMALL(4), MEDIUM(8), LARGE(12)
}
