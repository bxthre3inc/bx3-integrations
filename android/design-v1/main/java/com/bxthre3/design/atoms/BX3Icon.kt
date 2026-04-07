package com.bxthre3.design.atoms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Icon(
    name: String,
    size: Int = 24,
    tint: Color = BX3Colors.onSurfaceVariant,
    modifier: Modifier = Modifier
) {
    val icon: ImageVector = when (name) {
        "add" -> Icons.Default.Add
        "close" -> Icons.Default.Close
        "done" -> Icons.Default.Done
        "search" -> Icons.Default.Search
        "settings" -> Icons.Default.Settings
        "warning" -> Icons.Default.Warning
        else -> Icons.Default.Add
    }
    
    Icon(
        imageVector = icon,
        contentDescription = name,
        modifier = modifier.size(size.dp),
        tint = tint
    )
}
