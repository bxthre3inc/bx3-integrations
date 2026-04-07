//! GPU-accelerated and software rendering backends
//! 
//! Supports wgpu (GPU) and tiny-skia (software fallback)

use crate::color::BX3Color;

/// Rendering backend abstraction
#[derive(Clone, Copy, Debug, PartialEq)]
pub enum RenderBackend {
    /// GPU-accelerated via wgpu
    #[cfg(feature = "wgpu")]
    Wgpu,
    /// Software rendering via tiny-skia
    #[cfg(feature = "software")]
    Skia,
    /// Automatic selection
    Auto,
}

impl Default for RenderBackend {
    fn default() -> Self {
        Self::Auto
    }
}

impl RenderBackend {
    /// Select appropriate backend for current platform
    pub fn select() -> Self {
        #[cfg(feature = "wgpu")]
        {
            // Check if wgpu is available
            if wgpu::Instance::any() {
                return Self::Wgpu;
            }
        }
        
        #[cfg(feature = "software")]
        {
            return Self::Skia;
        }
        
        Self::Auto
    }
}

/// Render target configuration
pub struct RenderConfig {
    pub width: u32,
    pub height: u32,
    pub backend: RenderBackend,
    pub vsync: bool,
}

impl RenderConfig {
    pub fn new(width: u32, height: u32) -> Self {
        Self {
            width,
            height,
            backend: RenderBackend::select(),
            vsync: true,
        }
    }
    
    /// Disable vsync (for benchmarks)
    pub fn no_vsync(mut self) -> Self {
        self.vsync = false;
        self
    }
}

/// 2D rectangle for rendering
#[derive(Clone, Copy, Debug)]
pub struct Rect {
    pub x: f32,
    pub y: f32,
    pub width: f32,
    pub height: f32,
    pub border_radius: f32,
}

impl Rect {
    pub fn new(x: f32, y: f32, width: f32, height: f32) -> Self {
        Self {
            x,
            y,
            width,
            height,
            border_radius: 0.0,
        }
    }
    
    pub fn with_radius(mut self, radius: f32) -> Self {
        self.border_radius = radius;
        self
    }
}

/// Render command for batching
pub enum RenderCommand {
    /// Fill rectangle with color
    FillRect { rect: Rect, color: BX3Color },
    /// Draw rectangle border
    StrokeRect { rect: Rect, color: BX3Color, width: f32 },
    /// Draw text (glyphs)
    Text { x: f32, y: f32, text: String, color: BX3Color },
    /// Clip to rectangle
    Clip { rect: Rect },
    /// Reset clip
    ResetClip,
}

/// Renderer trait for backend implementations
pub trait Renderer {
    /// Begin frame
    fn begin(&mut self) -> Result<(), RenderError>;
    
    /// Execute render commands
    fn render(&mut self, commands: &[RenderCommand]) -> Result<(), RenderError>;
    
    /// End frame and present
    fn end(&mut self) -> Result<(), RenderError>;
    
    /// Resize render target
    fn resize(&mut self, width: u32, height: u32);
    
    /// Current backend type
    fn backend(&self) -> RenderBackend;
}

#[derive(Debug, Clone, PartialEq)]
pub enum RenderError {
    WgpuError(String),
    SkiaError(String),
    OutOfMemory,
    SurfaceLost,
}

impl std::fmt::Display for RenderError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            RenderError::WgpuError(e) => write!(f, "WGPU error: {}", e),
            RenderError::SkiaError(e) => write!(f, "Skia error: {}", e),
            RenderError::OutOfMemory => write!(f, "Out of memory"),
            RenderError::SurfaceLost => write!(f, "Surface lost"),
        }
    }
}

impl std::error::Error for RenderError {}

#[cfg(test)]
mod tests {
    use super::*;
    
    #[test]
    fn test_render_config() {
        let config = RenderConfig::new(800, 600);
        assert_eq!(config.width, 800);
        assert_eq!(config.height, 600);
        assert!(config.vsync);
    }
    
    #[test]
    fn test_rect_with_radius() {
        let rect = Rect::new(10.0, 20.0, 100.0, 50.0)
            .with_radius(8.0);
        assert_eq!(rect.border_radius, 8.0);
    }
}
