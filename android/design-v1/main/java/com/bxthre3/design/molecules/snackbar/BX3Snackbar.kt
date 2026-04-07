package com.bxthre3.design.molecules.snackbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Snackbar(
    message: String,
    visible: Boolean,
    onDismiss: () -> Unit,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(BX3Colors.inverseSurface, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BX3Text(
                text = message,
                style = BX3TextStyle.BodyMedium,
                color = BX3Colors.inverseOnSurface,
                modifier = Modifier.weight(1f)
            )
            actionLabel?.let {
                Spacer(modifier = Modifier.width(8.dp))
                BX3Text(
                    text = it,
                    style = BX3TextStyle.LabelLarge,
                    color = BX3Colors.primary,
                    modifier = Modifier.clickable(onClick = onAction)
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = BX3Colors.inverseOnSurface)
            }
        }
    }
}
