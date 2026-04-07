package com.bxthre3.design.molecules.pagination

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Surface
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Pagination(
    totalPages: Int,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        repeat(totalPages) { index ->
            val isSelected = index == currentPage
            val pageNum = index + 1
            
            BX3Surface(
                color = if (isSelected) BX3Colors.primary else BX3Colors.surface,
                onClick = { onPageChange(index) },
                modifier = Modifier.width(40.dp)
            ) {
                BX3Text(
                    text = pageNum.toString(),
                    color = if (isSelected) BX3Colors.onPrimary else BX3Colors.onSurface
                )
            }
            if (index < totalPages - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}
