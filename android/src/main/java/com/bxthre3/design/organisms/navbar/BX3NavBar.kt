package com.bxthre3.design.organisms.navbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bxthre3.design.theme.BX3Colors

data class NavItem(
    val id: String,
    val label: String,
    val icon: @Composable () -> Unit = { Icon(Icons.Default.Home, contentDescription = null) }
)

@Composable
fun BX3NavBar(
    items: List<NavItem>,
    selectedItem: String,
    onItemSelect: (String) -> Unit,
    modifier: Modifier = Modifier
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
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onItemSelect(item.id) }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = when (item.id) {
                        "home", "dashboard" -> Icons.Default.Home
                        "settings" -> Icons.Default.Settings
                        "profile" -> Icons.Default.Person
                        else -> Icons.Default.Star
                    },
                    contentDescription = item.label,
                    tint = color
                )
                Text(
                    text = item.label,
                    color = color,
                    fontSize = 12.sp
                )
            }
        }
    }
}
