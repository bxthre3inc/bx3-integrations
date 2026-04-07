package com.bxthre3.design.molecules.tabgroup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors

data class TabItem(val id: String, val label: String)

@Composable
fun BX3TabGroup(
    tabs: List<TabItem>,
    selectedTab: String,
    onTabSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val isSelected = tab.id == selectedTab
            val bgColor = if (isSelected) BX3Colors.primary else BX3Colors.surface
            val textColor = if (isSelected) BX3Colors.onPrimary else BX3Colors.onSurface
            
            BX3Text(
                text = tab.label,
                style = BX3TextStyle.LabelLarge,
                color = textColor,
                modifier = Modifier
                    .clickable { onTabSelect(tab.id) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
