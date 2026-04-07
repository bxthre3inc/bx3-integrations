package com.bxthre3.design.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Icon
import com.bxthre3.design.atoms.BX3Surface
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.theme.BX3Colors

data class DropdownItem(val label: String, val value: Any)

@Composable
fun BX3Dropdown(
    items: List<DropdownItem>,
    selected: DropdownItem?,
    onSelect: (DropdownItem) -> Unit,
    placeholder: String = "Select...",
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    
    Box(modifier = modifier) {
        BX3Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded.value = !expanded.value },
            content = {
                Box(modifier = Modifier.padding(16.dp)) {
                    BX3Text(
                        text = selected?.label ?: placeholder,
                        color = if (selected == null) BX3Colors.onSurfaceVariant else BX3Colors.onSurface
                    )
                }
            }
        )
        
        AnimatedVisibility(
            visible = expanded.value,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                items.forEach { item ->
                    BX3Text(
                        text = item.label,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(item)
                                expanded.value = false
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
