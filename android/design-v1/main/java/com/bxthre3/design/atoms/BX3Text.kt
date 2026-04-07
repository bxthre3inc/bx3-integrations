package com.bxthre3.design.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.bxthre3.design.theme.BX3Colors

@Composable
fun BX3Text(
    text: String,
    modifier: Modifier = Modifier,
    size: BX3TextSize = BX3TextSize.MEDIUM,
    weight: BX3TextWeight = BX3TextWeight.NORMAL,
    intent: BX3TextIntent = BX3TextIntent.PRIMARY
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = size.value,
        fontWeight = weight.fontWeight,
        color = intent.color
    )
}

enum class BX3TextSize(val value: TextUnit) {
    TINY(10.sp),
    SMALL(12.sp),
    MEDIUM(14.sp),
    LARGE(16.sp),
    XL(20.sp),
    XXL(24.sp),
    HERO(32.sp)
}

enum class BX3TextWeight(val fontWeight: FontWeight) {
    LIGHT(FontWeight.Light),
    NORMAL(FontWeight.Normal),
    MEDIUM(FontWeight.Medium),
    SEMIBOLD(FontWeight.SemiBold),
    BOLD(FontWeight.Bold),
    EXTRABOLD(FontWeight.ExtraBold)
}

enum class BX3TextIntent(val color: Color) {
    PRIMARY(BX3Colors.primary),
    SECONDARY(BX3Colors.textSecondary),
    SUBTLE(BX3Colors.textSubtle),
    CONTRAST(BX3Colors.onPrimary),
    ERROR(BX3Colors.error),
    SUCCESS(BX3Colors.success),
    WARNING(BX3Colors.warning),
    INFO(BX3Colors.info)
}
