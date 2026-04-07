package com.bxthre3.design.atoms.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bxthre3.design.atoms.BX3Text
import com.bxthre3.design.atoms.BX3TextStyle
import com.bxthre3.design.theme.BX3Colors

sealed class AvatarContent {
    data class Image(val url: String, val fallback: Int? = null) : AvatarContent()
    data class Initials(val text: String) : AvatarContent()
    data class Icon(val icon: Int, val contentDescription: String) : AvatarContent()
}

@Composable
fun BX3Avatar(
    content: AvatarContent,
    size: AvatarSize = AvatarSize.MEDIUM,
    modifier: Modifier = Modifier,
    borderColor: Color? = null,
    onlineIndicator: Boolean = false
) {
    val dimensions = size.dimensions
    val bgColor = BX3Colors.primary
    val textColor = BX3Colors.onPrimary
    
    Box(
        modifier = modifier
            .size(dimensions.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        when (content) {
            is AvatarContent.Image -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(content.url)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                    placeholder = content.fallback?.let { painterResource(it) },
                    error = content.fallback?.let { painterResource(it) }
                )
            }
            is AvatarContent.Initials -> {
                val initials = content.text
                    .split(" ")
                    .take(2)
                    .map { it.first().uppercase() }
                    .joinToString("")
                
                BX3Text(
                    text = initials,
                    style = when (size) {
                        AvatarSize.XSMALL -> BX3TextStyle.BodyLarge
                        AvatarSize.SMALL -> BX3TextStyle.TitleSmall
                        AvatarSize.MEDIUM -> BX3TextStyle.TitleMedium
                        AvatarSize.LARGE -> BX3TextStyle.TitleLarge
                        AvatarSize.XLARGE -> BX3TextStyle.DisplaySmall
                    },
                    color = textColor
                )
            }
            is AvatarContent.Icon -> {
                Icon(
                    painter = painterResource(content.icon),
                    contentDescription = content.contentDescription,
                    tint = textColor,
                    modifier = Modifier.size(dimensions.dp * 0.5f)
                )
            }
        }
        
        // Online indicator
        if (onlineIndicator) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(dimensions.dp * 0.25f)
                    .background(BX3Colors.success, CircleShape)
            )
        }
        
        // Border
        borderColor?.let {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent, CircleShape)
            )
        }
    }
}

enum class AvatarSize(val dimensions: Int) {
    XSMALL(24),
    SMALL(32),
    MEDIUM(40),
    LARGE(56),
    XLARGE(72)
}