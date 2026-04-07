package com.bxthre3.design.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextField
import com.bxthre3.design.theme.BX3Colors
import kotlinx.coroutines.delay

data class BX3Command(val id: String, val label: String, val shortcut: String?, val action: () -> Unit)

@Composable
fun BX3CommandPalette(
    visible: Boolean,
    onDismiss: () -> Unit,
    commands: List<BX3Command>
) {
    val searchQuery = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    
    val filtered = commands.filter { 
        it.label.contains(searchQuery.value, ignoreCase = true) 
    }
    
    LaunchedEffect(visible) {
        if (visible) {
            delay(100)
            focusRequester.requestFocus()
        }
    }
    
    if (visible) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(BX3Colors.surface, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                BX3TextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    hint = "Search commands...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
                
                filtered.take(7).forEach { cmd ->
                    BX3Text(
                        text = "${cmd.label} ${cmd.shortcut ?: ""}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                cmd.action()
                                onDismiss()
                            }
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}
