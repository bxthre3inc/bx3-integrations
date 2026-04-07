package com.bxthre3.design.organisms

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors

data class ChartPoint(val label: String, val value: Float)

@Composable
fun BX3Chart(
    data: List<ChartPoint>,
    title: String,
    modifier: Modifier = Modifier,
    chartHeight: Float = 200f
) {
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        BX3Text(
            text = title,
            style = BX3TextStyle.HeadlineSmall
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight.dp)
                .padding(top = 16.dp)
        ) {
            val maxValue = data.maxOfOrNull { it.value } ?: 1f
            
            Canvas(modifier = Modifier.fillMaxWidth()) {
                val barWidth = size.width / data.size * 0.7f
                val gap = size.width / data.size * 0.3f
                
                data.forEachIndexed { index, point ->
                    val barHeight = (point.value / maxValue) * size.height
                    val x = index * (barWidth + gap) + gap / 2
                    val y = size.height - barHeight
                    
                    drawRect(
                        color = BX3Colors.primary,
                        topLeft = Offset(x, y),
                        size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                    )
                }
            }
        }
    }
}
