package com.bxthre3.design.molecules.breadcrumb

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors

data class BreadcrumbItem(val label: String, val route: String)

@Composable
fun BX3Breadcrumb(
    items: List<BreadcrumbItem>,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val isLast = index == items.lastIndex
            
            BX3Text(
                text = item.label,
                style = if (isLast) BX3TextStyle.BodyMedium else BX3TextStyle.LabelLarge,
                color = if (isLast) BX3Colors.onSurface else BX3Colors.primary,
                modifier = if (!isLast) Modifier.clickable { onNavigate(item.route) } else Modifier
            )
            
            if (!isLast) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = BX3Colors.onSurfaceVariant)
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}
