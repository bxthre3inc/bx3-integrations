package com.bxthre3.design.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.bxthre3.design.atoms.BX3Surface
import com.bxthre3.design.theme.BX3Colors

data class BX3MenuItem(val label: String, val action: () -> Unit)

@Composable
fun BX3Menu(
    visible: Boolean,
    onDismiss: () -> Unit,
    items: List<BX3MenuItem>,
    modifier: Modifier = Modifier
) {
    if (visible) {
        Popup(onDismissRequest = onDismiss) {
            BX3Surface(
                modifier = modifier,
                color = BX3Colors.surfaceContainer,
                shape = RoundedCornerShape(4.dp)
            ) {
                Column {
                    items.forEach { item ->
                        BX3MenuItemRow(item, onDismiss)
                    }
                }
            }
        }
    }
}

@Composable
private fun BX3MenuItemRow(item: BX3MenuItem, onDismiss: () -> Unit) {
    BX3Text(
        text = item.label,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                item.action()
                onDismiss()
            }
            .padding(16.dp)
    )
}
