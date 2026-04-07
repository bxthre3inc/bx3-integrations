package com.bxthre3.design.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bxthre3.design.theme.BX3Colors
import kotlin.math.roundToInt

@Composable
fun BX3Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f
) {
    val thumbPosition = remember(value) { mutableFloatStateOf(value) }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    val newValue = (thumbPosition.floatValue + dragAmount / 1000).coerceIn(valueRange)
                    thumbPosition.floatValue = newValue
                    onValueChange(newValue)
                }
            }
    ) {
        // Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(BX3Colors.subtle)
                .align(androidx.compose.ui.Alignment.Center)
        )
        
        // Fill
        Box(
            modifier = Modifier
                .fillMaxWidth(thumbPosition.floatValue)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(BX3Colors.primary)
                .align(androidx.compose.ui.Alignment.CenterStart)
        )
        
        // Thumb
        Box(
            modifier = Modifier
                .size(20.dp)
                .offset { IntOffset((thumbPosition.floatValue * 1000).roundToInt(), 0) }
                .clip(CircleShape)
                .background(BX3Colors.primary)
                .align(androidx.compose.ui.Alignment.CenterStart)
        )
    }
}
