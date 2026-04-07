package com.bxthre3.design.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Divider(
    modifier: Modifier = Modifier,
    intent: BX3DividerIntent = BX3DividerIntent.DEFAULT
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(intent.color)
            .padding(vertical = 8.dp)
    )
}

enum class BX3DividerIntent(val color: androidx.compose.ui.graphics.Color) {
    DEFAULT(BX3Colors.border),
    SUBTLE(BX3Colors.borderSubtle),
    STRONG(BX3Colors.borderStrong)
}
