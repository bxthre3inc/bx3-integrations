package com.bxthre3.design.utils

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

object BX3Accessibility {
    
    fun Modifier.screenReaderLabel(label: String): Modifier = 
        semantics { contentDescription = label }
    
    fun Modifier.accessiblePadding(): Modifier = 
        padding(horizontal = 16.dp, vertical = 12.dp)
    
    @Composable
    fun AccessibleIconButton(
        onClick: () -> Unit,
        contentDescription: String,
        content: @Composable () -> Unit
    ) {
        androidx.compose.material3.IconButton(
            onClick = onClick,
            modifier = Modifier
                .screenReaderLabel(contentDescription)
                .focusable()
        ) {
            content()
        }
    }
}
