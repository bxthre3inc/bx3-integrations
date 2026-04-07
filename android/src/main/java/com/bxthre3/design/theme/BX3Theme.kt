package com.bxthre3.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BX3Colors.primary,
    onPrimary = BX3Colors.onPrimary,
    primaryContainer = BX3Colors.primaryContainer,
    onPrimaryContainer = BX3Colors.onPrimaryContainer,
    secondary = BX3Colors.secondary,
    onSecondary = BX3Colors.onSecondary,
    background = BX3Colors.background,
    surface = BX3Colors.surface,
    surfaceVariant = BX3Colors.surfaceVariant,
    onSurface = BX3Colors.onSurface,
    onSurfaceVariant = BX3Colors.onSurfaceVariant,
    error = BX3Colors.error
)

// Theme variants
enum class ThemeVariant {
    ZoSpace,    // Purple
    AgentOS,    // Green
    VPC,        // Teal
    Irrig8      // Blue
}

val LocalThemeVariant = staticCompositionLocalOf { ThemeVariant.ZoSpace }

@Composable
fun BX3Theme(
    initialTheme: ThemeVariant = ThemeVariant.ZoSpace,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = androidx.compose.material3.Typography(),
        content = {
            CompositionLocalProvider(LocalThemeVariant provides initialTheme) {
                content()
            }
        }
    )
}
