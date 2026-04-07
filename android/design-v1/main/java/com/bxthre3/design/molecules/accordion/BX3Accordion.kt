package com.bxthre3.design.molecules.accordion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle

data class AccordionItem(val title: String, val content: @Composable () -> Unit)

@Composable
fun BX3Accordion(
    items: List<AccordionItem>,
    modifier: Modifier = Modifier,
    allowMultiple: Boolean = false
) {
    val expandedStates = remember { mutableStateOf(List(items.size) { false }) }
    
    Column(modifier = modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            val isExpanded = expandedStates.value[index]
            val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expandedStates.value = expandedStates.value.mapIndexed { i, v ->
                            if (i == index) !v else if (!allowMultiple) false else v
                        }
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BX3Text(text = item.title, style = BX3TextStyle.TitleMedium, modifier = Modifier.weight(1f))
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.rotate(rotation))
            }
            AnimatedVisibility(visible = isExpanded) {
                item.content()
            }
        }
    }
}
