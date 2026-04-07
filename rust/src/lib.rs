//! BX3 Design System - High-performance Rust implementation
//! 
//! Zero-allocation, GPU-accelerated UI components with
//! runtime theme switching and accessibility detection.

#![warn(missing_docs)]
#![deny(unsafe_code)]

use std::sync::Arc;
use once_cell::sync::Lazy;
use parking_lot::RwLock;

pub mod theme;
pub mod color;
pub mod typography;
pub mod accessibility;
pub mod components;
pub mod rendering;

pub use theme::{Theme, ThemeVariant};
pub use color::Color;
pub use accessibility::{AccessibilityPreferences, AccessibilityManager};

/// Global theme singleton (thread-safe)
pub static THEME: Lazy<Arc<RwLock<Theme>>> = Lazy::new(|| {
    Arc::new(RwLock::new(Theme::default()))
});

/// Access the current theme
pub fn theme() -> Arc<RwLock<Theme>> {
    THEME.clone()
}

/// Switch theme variant at runtime
pub fn set_theme(variant: ThemeVariant) {
    let mut theme = THEME.write();
    theme.set_variant(variant);
}

/// Get current theme variant
pub fn current_variant() -> ThemeVariant {
    THEME.read().variant()
}

/// Check if system prefers reduced motion
pub fn prefers_reduced_motion() -> bool {
    AccessibilityManager::global().prefers_reduced_motion()
}

/// Check if system uses high contrast
pub fn prefers_high_contrast() -> bool {
    AccessibilityManager::global().prefers_high_contrast()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_theme_switching() {
        set_theme(ThemeVariant::AgentOS);
        assert_eq!(current_variant(), ThemeVariant::AgentOS);
        
        set_theme(ThemeVariant::ZoSpace);
        assert_eq!(current_variant(), ThemeVariant::ZoSpace);
    }

    #[test]
    fn test_color_parsing() {
        let color = Color::from_hex("#7B1FA2").unwrap();
        assert_eq!(color.r, 123);
        assert_eq!(color.g, 31);
        assert_eq!(color.b, 162);
    }
}