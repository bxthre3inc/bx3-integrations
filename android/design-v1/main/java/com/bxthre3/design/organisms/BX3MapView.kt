package com.bxthre3.design.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Icon
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors
import kotlin.math.roundToInt

data class MapMarker(val id: String, val lat: Float, val lng: Float, val label: String)

@Composable
fun BX3MapView(
    markers: List<MapMarker>,
    initialZoom: Float = 1f,
    modifier: Modifier = Modifier
) {
    val zoom = remember { mutableStateOf(initialZoom) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .background(BX3Colors.surfaceContainer)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offset.value = Offset(
                        offset.value.x + dragAmount.x,
                        offset.value.y + dragAmount.y
                    )
                }
            }
    ) {
        markers.forEach { marker ->
            val x = (marker.lng * zoom.value * 10) + offset.value.x + size.width / 2
            val y = (marker.lat * zoom.value * 10) + offset.value.y + size.height / 2
            
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BX3Icon(
                    name = "location",
                    size = 24,
                    tint = BX3Colors.error
                )
                BX3Text(
                    text = marker.label,
                    style = BX3TextStyle.LabelSmall
                )
            }
        }
    }
}
