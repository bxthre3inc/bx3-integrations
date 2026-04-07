package com.bxthre3.design.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Surface
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextIntent
import com.bxthre3.design.atoms.BX3TextSize
import com.bxthre3.design.theme.BX3Colors
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * BX3Calendar — Temporal selection interface
 * Patent: Temporal data visualization with urgency encoding
 */

@Composable
fun BX3Calendar(
    selectedDate: LocalDate? = null,
    onDateSelect: (LocalDate) -> Unit = {},
    events: Map<LocalDate, CalendarEventType> = emptyMap(),
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selected by remember { mutableStateOf(selectedDate) }
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ChevronLeft, null, tint = BX3Colors.primary)
            }
            
            BX3Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                size = BX3TextSize.LARGE,
                weight = BX3TextWeight.SEMIBOLD
            )
            
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ChevronRight, null, tint = BX3Colors.primary)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Day headers
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    BX3Text(
                        text = day,
                        size = BX3TextSize.SMALL,
                        intent = BX3TextIntent.SUBTLE
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Days grid
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
        val days = (1..daysInMonth).toList()
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(280.dp)
        ) {
            // Empty slots before first day
            items(firstDayOfMonth) {
                Box(modifier = Modifier.size(40.dp))
            }
            
            // Days
            items(days) { day ->
                val date = currentMonth.atDay(day)
                val isSelected = selected == date
                val eventType = events[date]
                
                CalendarDay(
                    day = day,
                    isSelected = isSelected,
                    eventType = eventType,
                    onClick = {
                        selected = date
                        onDateSelect(date)
                    }
                )
            }
        }
    }
}

@Composable
private fun CalendarDay(
    day: Int,
    isSelected: Boolean,
    eventType: CalendarEventType?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> BX3Colors.primary
                    else -> BX3Colors.transparent
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BX3Text(
                text = day.toString(),
                size = BX3TextSize.SMALL,
                intent = when {
                    isSelected -> BX3TextIntent.CONTRAST
                    else -> BX3TextIntent.PRIMARY
                }
            )
            if (eventType != null) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(
                            when (eventType) {
                                CalendarEventType.BUSY -> BX3Colors.error
                                CalendarEventType.FREE -> BX3Colors.success
                                CalendarEventType.TENTATIVE -> BX3Colors.warning
                            }
                        )
                )
            }
        }
    }
}

enum class CalendarEventType {
    BUSY, FREE, TENTATIVE
}
