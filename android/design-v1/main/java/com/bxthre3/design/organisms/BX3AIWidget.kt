package com.bxthre3.design.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextField
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors

data class SuggestedPrompt(val label: String, val prompt: String)

@Composable
fun BX3AIWidget(
    contextSnapshot: String,
    onPrompt: (String) -> Unit,
    suggestedPrompts: List<SuggestedPrompt>,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    val promptText = remember { mutableStateOf("") }
    
    val rotation by animateFloatAsState(targetValue = if (expanded.value) 180f else 0f)
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(BX3Colors.aiThinking.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { expanded.value = !expanded.value }
        ) {
            // AI Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BX3Colors.aiThinking),
                contentAlignment = Alignment.Center
            ) {
                BX3Text("AI", color = BX3Colors.onPrimary)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            BX3Text(
                text = "Ask AI about this",
                style = BX3TextStyle.TitleMedium,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                modifier = Modifier.rotate(rotation)
            )
        }
        
        AnimatedVisibility(visible = expanded.value) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                BX3TextField(
                    value = promptText.value,
                    onValueChange = { promptText.value = it },
                    hint = "What would you like to know?",
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                suggestedPrompts.take(3).forEach { suggestion ->
                    BX3Text(
                        text = "• ${suggestion.label}",
                        style = BX3TextStyle.BodyMedium,
                        color = BX3Colors.aiThinking,
                        modifier = Modifier
                            .clickable { onPrompt(suggestion.prompt) }
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
