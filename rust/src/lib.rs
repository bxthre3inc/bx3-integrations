//! BX3 Design System - High-performance Rust implementation
//! 
//! Zero-allocation color system with Oklab perceptual uniformity,
//! cross-platform accessibility detection, and GPU-accelerated rendering.

#![cfg_attr(not(feature = "std"), no_std)]

pub mod color;
pub mod accessibility;
pub mod theme;
pub mod render;

// Re-export commonly used items
pub use color::{BX3Color, brand};
pub use accessibility::{AccessibilityState, AnimationConfig, Accessible};
pub use theme::BX3Theme;

/// Check contrast ratio between two hex colors (CLI helper)
pub fn check_contrast_hex(hex1: &str, hex2: &str) -> Result<f32, color::ColorError> {
    let c1 = BX3Color::from_hex(hex1)?;
    let c2 = BX3Color::from_hex(hex2)?;
    Ok(c1.contrast_with(&c2))
}

/// Check WCAG compliance (CLI helper)
pub fn check_wcag_aa(hex1: &str, hex2: &str, is_large: bool) -> Result<bool, color::ColorError> {
    let c1 = BX3Color::from_hex(hex1)?;
    let c2 = BX3Color::from_hex(hex2)?;
    Ok(c1.meets_aa(&c2, is_large))
}

/// Check WCAG AAA compliance (CLI helper)
pub fn check_wcag_aaa(hex1: &str, hex2: &str, is_large: bool) -> Result<bool, color::ColorError> {
    let c1 = BX3Color::from_hex(hex1)?;
    let c2 = BX3Color::from_hex(hex2)?;
    Ok(c1.meets_aaa(&c2, is_large))
}

#[cfg(test)]
mod tests {
    use super::*;
    
    #[test]
    fn test_cli_helpers() {
        // White on black
        let ratio = check_contrast_hex("#FFFFFF", "#000000").unwrap();
        assert!(ratio > 20.0);
        
        assert!(check_wcag_aa("#FFFFFF", "#000000", false).unwrap());
        assert!(check_wcag_aaa("#FFFFFF", "#000000", false).unwrap());
    }
}
