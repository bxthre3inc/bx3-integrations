package com.bxthre3.design.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bxthre3.design.atoms.BX3Surface
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Dialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (visible) {
        Dialog(onDismissRequest = onDismiss) {
            BX3Surface(
                modifier = modifier.widthIn(max = 560.dp),
                color = BX3Colors.surface,
                shape = RoundedCornerShape(28.dp),
                content = content
            )
        }
    }
}
