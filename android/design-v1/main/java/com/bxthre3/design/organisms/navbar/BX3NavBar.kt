package com.bxthre3.design.organisms.navbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.theme.BX3Colors

data class NavItem(val id: String, val label: String, val icon: @Composable () -> Unit)

@Composable
fun BX3NavBar(
    items: List<NavItem>,
    selectedItem: String,
    onItemSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = item.id == selectedItem
            val color = if (isSelected) BX3Colors.primary else BX3Colors.onSurfaceVariant
            
            androidx.compose.animation.AnimatedContent(
                targetState = isSelected,
                label = "nav_item"
            ) { selected ->
                androidx.compose.foundation.layout.Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onItemSelect(item.id) }
                        .padding(8.dp)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Home,
                        contentDescription = item.label,
                        tint = if (selected) BX3Colors.primary else BX3Colors.onSurfaceVariant
                    )
                    androidx.compose.material3.Text(
                        text = item.label,
                        color = if (selected) BX3Colors.primary else BX3Colors.onSurfaceVariant
                    )
                }
            }
        }
        content()
    }
}
