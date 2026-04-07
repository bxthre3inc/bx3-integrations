//! Zero-allocation color system with WCAG contrast calculation
use palette::{Hsl, Srgb, Oklab, IntoColor};
use std::fmt;

/// BX3 color representation with Oklab perceptual uniformity
#[derive(Clone, Copy, Debug, PartialEq)]
pub struct BX3Color {
    /// OKLAB color space (perceptually uniform)
    pub oklab: Oklab<f32>,
    /// sRGB representation for output
    pub srgb: Srgb<f32>,
}

impl BX3Color {
    /// Create from hex string (e.g., "#7B1FA2")
    pub fn from_hex(hex: &str) -> Result<Self, ColorError> {
        let hex = hex.trim_start_matches('#');
        if hex.len() != 6 {
            return Err(ColorError::InvalidHex);
        }
        
        let r = u8::from_str_radix(&hex[0..2], 16).map_err(|_| ColorError::InvalidHex)?;
        let g = u8::from_str_radix(&hex[2..4], 16).map_err(|_| ColorError::InvalidHex)?;
        let b = u8::from_str_radix(&hex[4..6], 16).map_err(|_| ColorError::InvalidHex)?;
        
        Ok(Self::from_srgb8(r, g, b))
    }
    
    /// Create from sRGB 8-bit values
    pub fn from_srgb8(r: u8, g: u8, b: u8) -> Self {
        let srgb = Srgb::new(
            r as f32 / 255.0,
            g as f32 / 255.0,
            b as f32 / 255.0,
        );
        let oklab: Oklab<f32> = srgb.into_color();
        Self { srgb, oklab }
    }
    
    /// Calculate luminance for WCAG contrast
    pub fn luminance(&self) -> f32 {
        // Relative luminance per WCAG 2.1
        let [r, g, b] = self.srgb.into_linear().into_components();
        0.2126 * r + 0.7152 * g + 0.0722 * b
    }
    
    /// Calculate contrast ratio with another color (WCAG 2.1)
    pub fn contrast_with(&self, other: &BX3Color) -> f32 {
        let l1 = self.luminance().max(0.0001); // Avoid div by zero
        let l2 = other.luminance().max(0.0001);
        
        let lighter = l1.max(l2);
        let darker = l1.min(l2);
        
        (lighter + 0.05) / (darker + 0.05)
    }
    
    /// Check WCAG AA compliance (4.5:1 for normal text, 3:1 for large)
    pub fn meets_aa(&self, other: &BX3Color, is_large_text: bool) -> bool {
        let ratio = self.contrast_with(other);
        if is_large_text {
            ratio >= 3.0
        } else {
            ratio >= 4.5
        }
    }
    
    /// Check WCAG AAA compliance (7:1 for normal text, 4.5:1 for large)
    pub fn meets_aaa(&self, other: &BX3Color, is_large_text: bool) -> bool {
        let ratio = self.contrast_with(other);
        if is_large_text {
            ratio >= 4.5
        } else {
            ratio >= 7.0
        }
    }
    
    /// Shift hue in OKLAB space (perceptually uniform)
    pub fn shift_hue(&self, degrees: f32) -> Self {
        let [l, a, b] = self.oklab.into_components();
        let hue = b.atan2(a) + degrees.to_radians();
        let chroma = (a * a + b * b).sqrt();
        
        let new_a = chroma * hue.cos();
        let new_b = chroma * hue.sin();
        
        let new_oklab = Oklab::new(l, new_a, new_b);
        let new_srgb: Srgb<f32> = new_oklab.into_color();
        
        Self {
            srgb: new_srgb,
            oklab: new_oklab,
        }
    }
    
    /// To hex string
    pub fn to_hex(&self) -> String {
        let [r, g, b] = [
            (self.srgb.red * 255.0) as u8,
            (self.srgb.green * 255.0) as u8,
            (self.srgb.blue * 255.0) as u8,
        ];
        format!("#{:02X}{:02X}{:02X}", r, g, b)
    }
    
    /// To wgpu color format
    #[cfg(feature = "wgpu")]
    pub fn to_wgpu(&self) -> wgpu::Color {
        wgpu::Color {
            r: self.srgb.red as f64,
            g: self.srgb.green as f64,
            b: self.srgb.blue as f64,
            a: 1.0,
        }
    }
}

impl fmt::Display for BX3Color {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "{}", self.to_hex())
    }
}

#[derive(Debug, Clone, PartialEq)]
pub enum ColorError {
    InvalidHex,
    InvalidFormat,
}

impl fmt::Display for ColorError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            ColorError::InvalidHex => write!(f, "Invalid hex color"),
            ColorError::InvalidFormat => write!(f, "Invalid color format"),
        }
    }
}

impl std::error::Error for ColorError {}

/// Predefined brand colors
pub mod brand {
    use super::BX3Color;
    
    pub const ZOSPACE_PRIMARY: BX3Color = BX3Color::from_srgb8(123, 31, 162);
    pub const AGENTOS_PRIMARY: BX3Color = BX3Color::from_srgb8(27, 94, 32);
    pub const VPC_PRIMARY: BX3Color = BX3Color::from_srgb8(0, 77, 64);
    pub const IRRIG8_PRIMARY: BX3Color = BX3Color::from_srgb8(21, 101, 192);
    
    pub const SURFACE: BX3Color = BX3Color::from_srgb8(26, 26, 46);
    pub const ON_SURFACE: BX3Color = BX3Color::from_srgb8(224, 224, 224);
    pub const ERROR: BX3Color = BX3Color::from_srgb8(207, 102, 121);
    pub const SUCCESS: BX3Color = BX3Color::from_srgb8(126, 231, 135);
}

#[cfg(test)]
mod tests {
    use super::*;
    
    #[test]
    fn test_contrast() {
        let white = BX3Color::from_srgb8(255, 255, 255);
        let black = BX3Color::from_srgb8(0, 0, 0);
        
        // White vs black should have high contrast
        assert!(white.contrast_with(&black) > 20.0);
    }
    
    #[test]
    fn test_wcag_aa() {
        let white = BX3Color::from_srgb8(255, 255, 255);
        let black = BX3Color::from_srgb8(0, 0, 0);
        
        assert!(white.meets_aa(&black, false)); // Normal text
        assert!(white.meets_aaa(&black, false)); // Should pass AAA too
    }
    
    #[test]
    fn test_hex_parsing() {
        let color = BX3Color::from_hex("#7B1FA2").unwrap();
        assert_eq!(color.srgb.red, 123.0 / 255.0);
        assert_eq!(color.srgb.green, 31.0 / 255.0);
        assert_eq!(color.srgb.blue, 162.0 / 255.0);
    }
}
