package com.bxthre3.design.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Button
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3EmptyState(
    icon: @Composable () -> Unit = { Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(48.dp), tint = BX3Colors.onSurfaceVariant) },
    title: String,
    description: String,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        Spacer(modifier = Modifier.height(16.dp))
        BX3Text(
            text = title,
            style = BX3TextStyle.HeadlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        BX3Text(
            text = description,
            style = BX3TextStyle.BodyMedium,
            color = BX3Colors.onSurfaceVariant
        )
        actionLabel?.let {
            Spacer(modifier = Modifier.height(24.dp))
            BX3Button(
                text = it,
                onClick = onAction
            )
        }
    }
}
