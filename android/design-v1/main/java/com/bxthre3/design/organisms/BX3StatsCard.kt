package com.bxthre3.design.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.molecules.BX3Card
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3StatsCard(
    title: String,
    value: String,
    trend: String? = null,
    trendUp: Boolean = true,
    modifier: Modifier = Modifier
) {
    BX3Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            BX3Text(
                text = title,
                style = BX3TextStyle.LabelMedium,
                color = BX3Colors.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            BX3Text(
                text = value,
                style = BX3TextStyle.HeadlineLarge
            )
            trend?.let {
                Spacer(modifier = Modifier.height(4.dp))
                val color = if (trendUp) BX3Colors.trendUp else BX3Colors.trendDown
                BX3Text(
                    text = it,
                    style = BX3TextStyle.LabelMedium,
                    color = color
                )
            }
        }
    }
}
