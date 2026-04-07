package com.bxthre3.design.accessibility

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.os.Build
import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

object BX3AccessibilityManager {
    
    fun isScreenReaderEnabled(context: Context): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        return am.isEnabled && am.getInstalledAccessibilityServiceList().any { 
            it.eventTypes and android.view.accessibility.AccessibilityEvent.TYPE_VIEW_HOVER_ENTER != 0 
        }
    }
    
    fun isReduceMotionEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
            am.isReduceMotionEnabled
        } else {
            // Check system settings manually on older devices
            false
        }
    }
    
    fun isHighContrastEnabled(context: Context): Boolean {
        // Check for high contrast text setting
        val highContrast = android.provider.Settings.Secure.getInt(
            context.contentResolver,
            "high_text_contrast_enabled",
            0
        )
        return highContrast == 1
    }
}

@Composable
fun BX3AccessibilityProvider(
    content: @Composable (accessibilityState: AccessibilityState) -> Unit
) {
    val context = LocalContext.current
    val state = remember { mutableStateOf(AccessibilityState()) }
    
    LaunchedEffect(Unit) {
        while (true) {
            state.value = AccessibilityState(
                screenReaderEnabled = BX3AccessibilityManager.isScreenReaderEnabled(context),
                reduceMotionEnabled = BX3AccessibilityManager.isReduceMotionEnabled(context),
                highContrastEnabled = BX3AccessibilityManager.isHighContrastEnabled(context)
            )
            delay(5000) // Check every 5 seconds
        }
    }
    
    content(state.value)
}

data class AccessibilityState(
    val screenReaderEnabled: Boolean = false,
    val reduceMotionEnabled: Boolean = false,
    val highContrastEnabled: Boolean = false
)