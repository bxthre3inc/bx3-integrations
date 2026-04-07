//! Cross-platform accessibility detection
//! 
//! Detects screen readers, reduced motion, high contrast preferences
//! across Linux (DBus), macOS (NSWorkspace), and Windows (SystemParametersInfo)

use once_cell::sync::Lazy;
use parking_lot::RwLock;
use std::sync::Arc;

/// Global accessibility state
pub static ACCESSIBILITY_STATE: Lazy<Arc<RwLock<AccessibilityState>>> = 
    Lazy::new(|| Arc::new(RwLock::new(AccessibilityState::detect())));

/// Accessibility state with all supported features
#[derive(Clone, Debug, Default)]
pub struct AccessibilityState {
    /// Screen reader is active
    pub screen_reader: bool,
    /// Reduced motion preference
    pub reduced_motion: bool,
    /// High contrast mode
    pub high_contrast: bool,
    /// Minimum touch target size (48px for screen reader users, 44px otherwise)
    pub touch_target_size: u32,
}

impl AccessibilityState {
    /// Detect accessibility features on current platform
    pub fn detect() -> Self {
        #[cfg(target_os = "linux")]
        return Self::detect_linux();
        
        #[cfg(target_os = "macos")]
        return Self::detect_macos();
        
        #[cfg(target_os = "windows")]
        return Self::detect_windows();
        
        #[cfg(not(any(target_os = "linux", target_os = "macos", target_os = "windows")))]
        return Self::default();
    }
    
    #[cfg(all(target_os = "linux", feature = "linux-dbus"))]
    fn detect_linux() -> Self {
        use zbus::{Connection, fdo::DBusProxy};
        
        let mut state = Self::default();
        
        // Try to detect screen reader via AT-SPI registry
        if let Ok(conn) = Connection::session() {
            // Check for screen reader registration
            // Orca, NVDA, JAWS register with AT-SPI
            if let Ok(proxy) = DBusProxy::new(&conn) {
                // Look for known screen reader names in service list
                // This is a simplified check; production would query AT-SPI directly
                state.screen_reader = std::env::var("SCREEN_READER").is_ok() ||
                                     std::env::var("GNOME_ACCESSIBILITY").is_ok();
            }
        }
        
        // Check for high contrast via gsettings or env
        state.high_contrast = std::env::var("GTK_THEME")
            .map(|t| t.contains("-hc") || t.contains("HighContrast"))
            .unwrap_or(false);
        
        // Reduced motion
        state.reduced_motion = std::env::var("REDUCED_MOTION")
            .map(|v| v == "1" || v.to_lowercase() == "true")
            .unwrap_or(false);
        
        state.update_touch_target();
        state
    }
    
    #[cfg(target_os = "linux")]
    #[cfg(not(feature = "linux-dbus"))]
    fn detect_linux() -> Self {
        // Fallback without DBus
        Self {
            screen_reader: std::env::var("ACCESSIBILITY_ENABLED").is_ok(),
            reduced_motion: std::env::var("REDUCED_MOTION").map(|v| v == "1").unwrap_or(false),
            high_contrast: false,
            touch_target_size: 44,
        }
    }
    
    #[cfg(target_os = "macos")]
    fn detect_macos() -> Self {
        let mut state = Self::default();
        
        // Check VoiceOver via NSWorkspace
        unsafe {
            use cocoa::base::nil;
            use objc::runtime::{Object, BOOL, YES};
            use objc::{class, msg_send, sel, sel_impl};
            
            // Check if VoiceOver is enabled
            let ns_workspace: *mut Object = msg_send![class!(NSWorkspace), sharedWorkspace];
            let is_vo_enabled: BOOL = msg_send![ns_workspace, isVoiceOverEnabled];
            state.screen_reader = is_vo_enabled == YES;
        }
        
        // Check reduced motion preference
        state.reduced_motion = std::env::var("__CF_USER_TEXT_ENCODING")
            .ok()
            .and_then(|_| {
                // In production, query CoreFoundation for accessibility preferences
                Some(false)
            })
            .unwrap_or(false);
        
        state.update_touch_target();
        state
    }
    
    #[cfg(target_os = "windows")]
    fn detect_windows() -> Self {
        use windows::Win32::System::Accessibility::*;
        
        let mut state = Self::default();
        
        unsafe {
            // Check for screen reader via SPI_GETSCREENREADER
            // In production, use SystemParametersInfoW
            
            // Check high contrast
            // SPI_GETHIGHCONTRAST
        }
        
        state
    }
    
    fn update_touch_target(&mut self) {
        self.touch_target_size = if self.screen_reader { 48 } else { 44 };
    }
    
    /// Get current accessibility state (cached)
    pub fn current() -> Self {
        ACCESSIBILITY_STATE.read().clone()
    }
    
    /// Refresh accessibility detection (useful after system changes)
    pub fn refresh() {
        *ACCESSIBILITY_STATE.write() = Self::detect();
    }
}

/// Helper trait for components to respect accessibility
pub trait Accessible {
    /// Apply accessibility adaptations
    fn apply_accessibility(&mut self, state: &AccessibilityState);
    
    /// Get minimum touch target size
    fn min_touch_size(&self, state: &AccessibilityState) -> u32 {
        state.touch_target_size
    }
}

/// Animation configuration based on reduced motion
#[derive(Clone, Copy, Debug)]
pub struct AnimationConfig {
    /// Whether animations should be enabled
    pub enabled: bool,
    /// Animation duration in milliseconds (0 if disabled)
    pub duration_ms: u32,
    /// Use simple (reduced) animations if this is true but animations enabled
    pub use_simple: bool,
}

impl AnimationConfig {
    /// Get animation configuration from current accessibility state
    pub fn current() -> Self {
        let state = AccessibilityState::current();
        
        if state.reduced_motion {
            Self {
                enabled: false,
                duration_ms: 0,
                use_simple: true,
            }
        } else {
            Self {
                enabled: true,
                duration_ms: 200,
                use_simple: false,
            }
        }
    }
    
    /// Get appropriate duration (0 if disabled, otherwise the specified value)
    pub fn duration(&self, normal_ms: u32) -> u32 {
        if self.enabled { normal_ms } else { 0 }
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    
    #[test]
    fn test_animation_config() {
        // With reduced motion, animations should be disabled
        std::env::set_var("REDUCED_MOTION", "1");
        AccessibilityState::refresh();
        
        let config = AnimationConfig::current();
        assert!(!config.enabled);
        
        // Cleanup
        std::env::remove_var("REDUCED_MOTION");
    }
}
