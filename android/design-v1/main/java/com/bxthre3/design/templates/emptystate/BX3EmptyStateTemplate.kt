package com.bxthre3.design.templates.emptystate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.bxthre3.design.atoms.BX3Button
import com.bxthre3.design.atoms.BX3ButtonVariant
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3EmptyStateTemplate(
    icon: ImageVector,
    title: String,
    description: String,
    actionText: String? = null,
    onAction: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.4f))
        
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = BX3Colors.onSurfaceVariant
        )
        
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
        
        actionText?.let {
            Spacer(modifier = Modifier.height(24.dp))
            BX3Button(
                text = it,
                onClick = onAction
            )
        }
        
        Spacer(modifier = Modifier.weight(0.6f))
    }
}
