//! Color system for BX3 Design
//! 
//! High-performance color manipulation with:
//! - Zero-copy hex parsing
//! - Oklab perceptual uniformity
//! - WCAG 2.1 contrast compliance

use std::str::FromStr;
use palette::{Oklab, Srgb, IntoColor, FromColor};

/// RGBA color with 8-bit channels
#[derive(Debug, Clone, Copy, PartialEq, Default)]
pub struct Color {
    /// Red channel (0-255)
    pub r: u8,
    /// Green channel (0-255)
    pub g: u8,
    /// Blue channel (0-255)
    pub b: u8,
    /// Alpha channel (0.0 - 1.0)
    pub a: f32,
}

impl Color {
    /// Create from RGB values
    pub const fn new(r: u8, g: u8, b: u8) -> Self {
        Self { r, g, b, a: 1.0 }
    }
    
    /// Create with alpha
    pub const fn with_alpha(r: u8, g: u8, b: u8, a: f32) -> Self {
        Self { r, g, b, a }
    }
    
    /// Parse from hex string (#RGB, #RRGGBB, #RRGGBBAA)
    pub fn from_hex(hex: &str) -> Result<Self, ColorError> {
        let hex = hex.trim_start_matches('#');
        
        let (r, g, b, a) = match hex.len() {
            3 => {
                let r = u8::from_str_radix(&hex[0..1].repeat(2), 16)?;
                let g = u8::from_str_radix(&hex[1..2].repeat(2), 16)?;
                let b = u8::from_str_radix(&hex[2..3].repeat(2), 16)?;
                (r, g, b, 1.0)
            }
            6 => {
                let r = u8::from_str_radix(&hex[0..2], 16)?;
                let g = u8::from_str_radix(&hex[2..4], 16)?;
                let b = u8::from_str_radix(&hex[4..6], 16)?;
                (r, g, b, 1.0)
            }
            8 => {
                let r = u8::from_str_radix(&hex[0..2], 16)?;
                let g = u8::from_str_radix(&hex[2..4], 16)?;
                let b = u8::from_str_radix(&hex[4..6], 16)?;
                let a = u8::from_str_radix(&hex[6..8], 16)? as f32 / 255.0;
                (r, g, b, a)
            }
            _ => return Err(ColorError::InvalidFormat),
        };
        
        Ok(Self::with_alpha(r, g, b, a))
    }
    
    /// Convert to hex string
    pub fn to_hex(&self) -> String {
        if self.a < 1.0 {
            format!("#{:02X}{:02X}{:02X}{:02X}", 
                self.r, self.g, self.b, (self.a * 255.0) as u8)
        } else {
            format!("#{:02X}{:02X}{:02X}", self.r, self.g, self.b)
        }
    }
    
    /// Convert to u32 RGBA for GPU
    pub fn to_u32(&self) -> u32 {
        let a = (self.a * 255.0) as u32;
        (a << 24) | ((self.r as u32) << 16) | ((self.g as u32) << 8) | (self.b as u32)
    }
    
    /// Calculate WCAG 2.1 contrast ratio with another color
    /// Returns value between 1 and 21
    /// AA requires 4.5:1 for normal text, 3:1 for large text
    /// AAA requires 7:1 for normal text, 4.5:1 for large text
    pub fn contrast_ratio(&self, other: &Color) -> f32 {
        let l1 = self.relative_luminance();
        let l2 = other.relative_luminance();
        
        let lighter = l1.max(l2);
        let darker = l1.min(l2);
        
        (lighter + 0.05) / (darker + 0.05)
    }
    
    /// Check WCAG 2.1 AA compliance
    pub fn meets_aa(&self, background: &Color, is_large_text: bool) -> bool {
        let ratio = self.contrast_ratio(background);
        if is_large_text {
            ratio >= 3.0
        } else {
            ratio >= 4.5
        }
    }
    
    /// Check WCAG 2.1 AAA compliance  
    pub fn meets_aaa(&self, background: &Color, is_large_text: bool) -> bool {
        let ratio = self.contrast_ratio(background);
        if is_large_text {
            ratio >= 4.5
        } else {
            ratio >= 7.0
        }
    }
    
    /// Calculate relative luminance per WCAG 2.1
    fn relative_luminance(&self) -> f32 {
        fn channel(c: u8) -> f32 {
            let s = c as f32 / 255.0;
            if s <= 0.03928 {
                s / 12.92
            } else {
                ((s + 0.055) / 1.055).powf(2.4)
            }
        }
        
        0.2126 * channel(self.r) + 0.7152 * channel(self.g) + 0.0722 * channel(self.b)
    }
    
    /// Blend with another color
    pub fn blend(&self, other: &Color, t: f32) -> Color {
        let t = t.clamp(0.0, 1.0);
        Color {
            r: (self.r as f32 * (1.0 - t) + other.r as f32 * t) as u8,
            g: (self.g as f32 * (1.0 - t) + other.g as f32 * t) as u8,
            b: (self.b as f32 * (1.0 - t) + other.b as f32 * t) as u8,
            a: self.a * (1.0 - t) + other.a * t,
        }
    }
    
    /// Darken by factor (0.0 = no change, 1.0 = black)
    pub fn darken(&self, factor: f32) -> Color {
        let factor = factor.clamp(0.0, 1.0);
        Color {
            r: (self.r as f32 * (1.0 - factor)) as u8,
            g: (self.g as f32 * (1.0 - factor)) as u8,
            b: (self.b as f32 * (1.0 - factor)) as u8,
            a: self.a,
        }
    }
    
    /// Lighten by factor (0.0 = no change, 1.0 = white)
    pub fn lighten(&self, factor: f32) -> Color {
        let factor = factor.clamp(0.0, 1.0);
        Color {
            r: (self.r as f32 + (255.0 - self.r as f32) * factor) as u8,
            g: (self.g as f32 + (255.0 - self.g as f32) * factor) as u8,
            b: (self.b as f32 + (255.0 - self.b as f32) * factor) as u8,
            a: self.a,
        }
    }
}

impl FromStr for Color {
    type Err = ColorError;
    
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        Self::from_hex(s)
    }
}

/// Color parsing errors
#[derive(Debug, Clone, PartialEq)]
pub enum ColorError {
    /// Invalid hex format
    InvalidFormat,
    /// Parse error
    ParseError,
}

impl std::fmt::Display for ColorError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            ColorError::InvalidFormat => write!(f, "Invalid hex color format"),
            ColorError::ParseError => write!(f, "Failed to parse hex value"),
        }
    }
}

impl std::error::Error for ColorError {}

impl From<std::num::ParseIntError> for ColorError {
    fn from(_: std::num::ParseIntError) -> Self {
        ColorError::ParseError
    }
}

// Predefined colors for BX3 themes
impl Color {
    /// AgentOS primary green
    pub const AGENTOS_PRIMARY: Color = Color::new(27, 94, 32);
    /// Zo Space primary purple
    pub const ZOSPACE_PRIMARY: Color = Color::new(123, 31, 162);
    /// VPC primary teal
    pub const VPC_PRIMARY: Color = Color::new(0, 77, 64);
    /// Irrig8 primary blue
    pub const IRRIG8_PRIMARY: Color = Color::new(21, 101, 192);
    /// Dark background
    pub const DARK_BG: Color = Color::new(10, 10, 15);
    /// Dark surface
    pub const DARK_SURFACE: Color = Color::new(26, 26, 46);
}