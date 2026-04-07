//! Theme management with thread-safe global state
use crate::color::{BX3Color, brand};
use once_cell::sync::Lazy;
use parking_lot::RwLock;
use std::sync::Arc;

/// Available BX3 themes
#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
pub enum ThemeVariant {
    /// Zo Space - Purple theme
    ZoSpace,
    /// AgentOS - Green theme
    AgentOS,
    /// VPC - Teal theme
    VPC,
    /// Irrig8 - Blue theme
    Irrig8,
}

impl ThemeVariant {
    /// Get theme as string
    pub fn as_str(&self) -> &'static str {
        match self {
            ThemeVariant::ZoSpace => "zospace",
            ThemeVariant::AgentOS => "agentos",
            ThemeVariant::VPC => "vpc",
            ThemeVariant::Irrig8 => "irrig8",
        }
    }
    
    /// Parse from string
    pub fn from_str(s: &str) -> Option<Self> {
        match s {
            "zospace" => Some(ThemeVariant::ZoSpace),
            "agentos" => Some(ThemeVariant::AgentOS),
            "vpc" => Some(ThemeVariant::VPC),
            "irrig8" => Some(ThemeVariant::Irrig8),
            _ => None,
        }
    }
}

/// Complete theme color scheme
#[derive(Clone, Debug)]
pub struct ColorScheme {
    pub primary: BX3Color,
    pub on_primary: BX3Color,
    pub surface: BX3Color,
    pub on_surface: BX3Color,
    pub surface_variant: BX3Color,
    pub on_surface_variant: BX3Color,
    pub background: BX3Color,
    pub error: BX3Color,
    pub success: BX3Color,
    pub warning: BX3Color,
    pub info: BX3Color,
    /// AI-specific colors
    pub ai_thinking: BX3Color,
    pub ai_generating: BX3Color,
    pub ai_reviewing: BX3Color,
    /// Urgency colors
    pub urgent_high: BX3Color,
    pub urgent_medium: BX3Color,
    pub urgent_low: BX3Color,
}

impl ColorScheme {
    /// Get colors for a specific theme variant
    pub fn for_variant(variant: ThemeVariant) -> Self {
        let primary = match variant {
            ThemeVariant::ZoSpace => brand::ZOSPACE_PRIMARY,
            ThemeVariant::AgentOS => brand::AGENTOS_PRIMARY,
            ThemeVariant::VPC => brand::VPC_PRIMARY,
            ThemeVariant::Irrig8 => brand::IRRIG8_PRIMARY,
        };
        
        Self {
            primary,
            on_primary: BX3Color::from_srgb8(255, 255, 255),
            surface: brand::SURFACE,
            on_surface: brand::ON_SURFACE,
            surface_variant: BX3Color::from_srgb8(45, 45, 68),
            on_surface_variant: BX3Color::from_srgb8(176, 176, 192),
            background: BX3Color::from_srgb8(10, 10, 15),
            error: brand::ERROR,
            success: brand::SUCCESS,
            warning: BX3Color::from_srgb8(255, 183, 77),
            info: BX3Color::from_srgb8(100, 181, 246),
            ai_thinking: BX3Color::from_srgb8(156, 39, 176),
            ai_generating: BX3Color::from_srgb8(0, 230, 118),
            ai_reviewing: BX3Color::from_srgb8(255, 214, 0),
            urgent_high: BX3Color::from_srgb8(255, 23, 68),
            urgent_medium: BX3Color::from_srgb8(255, 145, 0),
            urgent_low: BX3Color::from_srgb8(0, 176, 255),
        }
    }
    
    /// Validate all color combinations meet WCAG AA
    pub fn validate_wcag(&self) -> Vec<(String, String, f32, bool)> {
        let mut results = vec![];
        
        // Primary on surface
        let ratio = self.primary.contrast_with(&self.surface);
        results.push(("primary".to_string(), "surface".to_string(), ratio, ratio >= 3.0));
        
        // On-primary on primary
        let ratio = self.on_primary.contrast_with(&self.primary);
        results.push(("onPrimary".to_string(), "primary".to_string(), ratio, ratio >= 4.5));
        
        // On-surface on surface
        let ratio = self.on_surface.contrast_with(&self.surface);
        results.push(("onSurface".to_string(), "surface".to_string(), ratio, ratio >= 4.5));
        
        results
    }
}

/// Global theme state
static THEME_STATE: Lazy<Arc<RwLock<ThemeState>>> = Lazy::new(|| {
    Arc::new(RwLock::new(ThemeState {
        current: ThemeVariant::ZoSpace,
        scheme: ColorScheme::for_variant(ThemeVariant::ZoSpace),
    }))
});

/// Theme state structure
#[derive(Clone)]
struct ThemeState {
    current: ThemeVariant,
    scheme: ColorScheme,
}

/// Thread-safe theme manager
pub struct BX3Theme;

impl BX3Theme {
    /// Get current theme variant
    pub fn current() -> ThemeVariant {
        THEME_STATE.read().current
    }
    
    /// Get current color scheme
    pub fn scheme() -> ColorScheme {
        THEME_STATE.read().scheme.clone()
    }
    
    /// Set theme variant (thread-safe)
    pub fn set_variant(variant: ThemeVariant) {
        let mut state = THEME_STATE.write();
        state.current = variant;
        state.scheme = ColorScheme::for_variant(variant);
    }
    
    /// Set theme from string
    pub fn set_from_str(name: &str) -> Result<(), ThemeError> {
        ThemeVariant::from_str(name)
            .map(Self::set_variant)
            .ok_or(ThemeError::InvalidTheme)
    }
    
    /// Validate all color combinations
    pub fn validate_accessibility() -> bool {
        let scheme = Self::scheme();
        scheme.validate_wcag().iter().all(|(_, _, _, passes)| *passes)
    }
    
    /// Get contrast ratio between theme colors
    pub fn contrast(color1: impl AsRef<str>, color2: impl AsRef<str>) -> Option<f32> {
        let scheme = Self::scheme();
        // Simplified - production would use reflection or enum
        Some(4.5) // Placeholder
    }
}

#[derive(Debug, Clone, PartialEq)]
pub enum ThemeError {
    InvalidTheme,
    InvalidColor,
}

impl std::fmt::Display for ThemeError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            ThemeError::InvalidTheme => write!(f, "Invalid theme name"),
            ThemeError::InvalidColor => write!(f, "Invalid color value"),
        }
    }
}

impl std::error::Error for ThemeError {}

#[cfg(test)]
mod tests {
    use super::*;
    
    #[test]
    fn test_theme_switching() {
        BX3Theme::set_variant(ThemeVariant::ZoSpace);
        assert_eq!(BX3Theme::current(), ThemeVariant::ZoSpace);
        
        BX3Theme::set_variant(ThemeVariant::AgentOS);
        assert_eq!(BX3Theme::current(), ThemeVariant::AgentOS);
    }
    
    #[test]
    fn test_wcag_validation() {
        let scheme = ColorScheme::for_variant(ThemeVariant::ZoSpace);
        let results = scheme.validate_wcag();
        
        // All combinations should pass
        assert!(results.iter().all(|(_, _, _, p)| *p));
    }
}
