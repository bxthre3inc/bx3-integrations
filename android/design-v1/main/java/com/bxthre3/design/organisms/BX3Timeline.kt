package com.bxthre3.design.organisms

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.theme.BX3Colors

/**
 * BX3Timeline — Temporal event visualization
 * Patent: Adaptive temporal density (US 20XXXXXX)
 */

@Composable
fun BX3Timeline(
    events: List<TimelineEvent>,
    modifier: Modifier = Modifier,
    density: BX3TemporalDensity = BX3TemporalDensity.AUTO
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(events) { event ->
            TimelineItem(event = event)
        }
    }
}

@Composable
private fun TimelineItem(event: TimelineEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Timeline line + dot
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(8.dp)
                    .background(BX3Colors.subtle)
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        when (event.type) {
                            TimelineEventType.MILESTONE -> BX3Colors.primary
                            TimelineEventType.PROGRESS -> BX3Colors.info
                            TimelineEventType.WARNING -> BX3Colors.warning
                            TimelineEventType.ERROR -> BX3Colors.error
                            TimelineEventType.SUCCESS -> BX3Colors.success
                        }
                    )
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(BX3Colors.subtle)
            )
        }
        
        // Content
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxWidth()
        ) {
            BX3Text(
                text = event.title,
                size = BX3TextSize.MEDIUM,
                weight = BX3TextWeight.SEMIBOLD
            )
            BX3Text(
                text = event.timestamp,
                size = BX3TextSize.SMALL,
                intent = BX3TextIntent.SUBTLE
            )
            if (event.description != null) {
                BX3Text(
                    text = event.description,
                    size = BX3TextSize.SMALL,
                    intent = BX3TextIntent.SECONDARY,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

data class TimelineEvent(
    val title: String,
    val timestamp: String,
    val description: String? = null,
    val type: TimelineEventType = TimelineEventType.PROGRESS
)

enum class TimelineEventType {
    MILESTONE, PROGRESS, WARNING, ERROR, SUCCESS
}

enum class BX3TemporalDensity {
    COMPACT, NORMAL, EXPANDED, AUTO
}
