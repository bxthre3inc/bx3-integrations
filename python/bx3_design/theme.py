"""BX3 Theme system with runtime switching and accessibility support."""

from dataclasses import dataclass
from typing import Dict, Optional, Callable, List
from enum import Enum
import json
import os


class BX3ThemeVariant(Enum):
    AGENTOS = "agentos"
    ZOSPACE = "zospace"
    VPC = "vpc"
    IRRIG8 = "irrig8"


@dataclass(frozen=True)
class BX3ColorSet:
    """Immutable color set for a theme variant."""
    primary: str
    on_primary: str
    primary_container: str
    on_primary_container: str
    secondary: str
    on_secondary: str
    background: str
    surface: str
    surface_variant: str
    on_surface: str
    on_surface_variant: str
    error: str
    success: str = "#00E676"
    warning: str = "#FFB74D"
    info: str = "#64B5F6"
    
    # AI-specific colors
    ai_thinking: str = "#9C27B0"
    ai_generating: str = "#00E676"
    ai_reviewing: str = "#FFD600"


THEME_REGISTRY: Dict[BX3ThemeVariant, BX3ColorSet] = {
    BX3ThemeVariant.AGENTOS: BX3ColorSet(
        primary="#1B5E20",
        on_primary="#FFFFFF",
        primary_container="#C8E6C9",
        on_primary_container="#1B5E20",
        secondary="#03DAC6",
        on_secondary="#000000",
        background="#0A0A0F",
        surface="#1A1A2E",
        surface_variant="#2D2D44",
        on_surface="#E0E0E0",
        on_surface_variant="#B0B0C0",
        error="#CF6679",
    ),
    BX3ThemeVariant.ZOSPACE: BX3ColorSet(
        primary="#7B1FA2",
        on_primary="#FFFFFF",
        primary_container="#E1BEE7",
        on_primary_container="#4A148C",
        secondary="#03DAC6",
        on_secondary="#000000",
        background="#0A0A0F",
        surface="#1A1A2E",
        surface_variant="#2D2D44",
        on_surface="#E0E0E0",
        on_surface_variant="#B0B0C0",
        error="#CF6679",
    ),
    BX3ThemeVariant.VPC: BX3ColorSet(
        primary="#004D40",
        on_primary="#FFFFFF",
        primary_container="#B2DFDB",
        on_primary_container="#004D40",
        secondary="#FF6E40",
        on_secondary="#000000",
        background="#0A0A0F",
        surface="#1A1A2E",
        surface_variant="#2D2D44",
        on_surface="#E0E0E0",
        on_surface_variant="#B0B0C0",
        error="#CF6679",
    ),
    BX3ThemeVariant.IRRIG8: BX3ColorSet(
        primary="#1565C0",
        on_primary="#FFFFFF",
        primary_container="#BBDEFB",
        on_primary_container="#1565C0",
        secondary="#00BCD4",
        on_secondary="#000000",
        background="#0A0A0F",
        surface="#1A1A2E",
        surface_variant="#2D2D44",
        on_surface="#E0E0E0",
        on_surface_variant="#B0B0C0",
        error="#CF6679",
    ),
}


@dataclass
class BX3Typography:
    """Typography scale."""
    font_family: str = "system-ui, -apple-system, sans-serif"
    display_large: int = 57
    display_medium: int = 45
    display_small: int = 36
    headline_large: int = 32
    headline_medium: int = 28
    headline_small: int = 24
    title_large: int = 22
    title_medium: int = 16
    title_small: int = 14
    body_large: int = 16
    body_medium: int = 14
    body_small: int = 12
    label_large: int = 14
    label_medium: int = 12
    label_small: int = 11


class BX3Theme:
    """Main theme class with accessibility detection and runtime switching."""
    
    _instance: Optional['BX3Theme'] = None
    _observers: List[Callable] = []
    
    def __new__(cls, variant: str = "agentos"):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance._initialized = False
        return cls._instance
    
    def __init__(self, variant: str = "agentos"):
        if self._initialized:
            return
            
        self._variant = BX3ThemeVariant(variant)
        self._colors = THEME_REGISTRY[self._variant]
        self._typography = BX3Typography()
        self._high_contrast = False
        self._reduce_motion = False
        self._initialized = True
        
        # Auto-detect accessibility preferences
        self._detect_accessibility()
    
    def _detect_accessibility(self):
        """Detect system accessibility preferences."""
        # Check environment variables (Linux)
        self._high_contrast = os.environ.get('GTK_THEME', '').endswith('-hc') or \
                             os.environ.get('ACCESSIBILITY_ENABLED', '0') == '1'
        self._reduce_motion = os.environ.get('REDUCE_MOTION', '0') == '1'
    
    @property
    def colors(self) -> BX3ColorSet:
        """Get current color set (with accessibility adaptations)."""
        if self._high_contrast:
            # Return high contrast variant
            return BX3ColorSet(
                primary="#FFFFFF" if self._variant == BX3ThemeVariant.ZOSPACE else "#FFFF00",
                on_primary="#000000",
                primary_container="#000000",
                on_primary_container="#FFFFFF",
                secondary="#00FFFF",
                on_secondary="#000000",
                background="#000000",
                surface="#000000",
                surface_variant="#333333",
                on_surface="#FFFFFF",
                on_surface_variant="#CCCCCC",
                error="#FF6666",
            )
        return self._colors
    
    @property
    def typography(self) -> BX3Typography:
        return self._typography
    
    @property
    def variant(self) -> str:
        return self._variant.value
    
    @property
    def reduce_motion(self) -> bool:
        return self._reduce_motion
    
    def set_variant(self, variant: str):
        """Switch theme variant at runtime."""
        old_variant = self._variant
        self._variant = BX3ThemeVariant(variant)
        self._colors = THEME_REGISTRY[self._variant]
        
        if old_variant != self._variant:
            self._notify_observers()
    
    def add_observer(self, callback: Callable):
        """Add callback for theme changes."""
        self._observers.append(callback)
    
    def remove_observer(self, callback: Callable):
        """Remove theme change observer."""
        if callback in self._observers:
            self._observers.remove(callback)
    
    def _notify_observers(self):
        """Notify all observers of theme change."""
        for observer in self._observers:
            try:
                observer(self._variant.value)
            except Exception:
                pass
    
    def to_css_variables(self) -> str:
        """Export theme as CSS variables."""
        css = ":root {\n"
        for name, value in self.colors.__dict__.items():
            css_var = name.replace('_', '-')
            css += f"  --bx3-{css_var}: {value};\n"
        css += "}\n"
        return css
    
    def to_dict(self) -> dict:
        """Export theme as dictionary."""
        return {
            "variant": self.variant,
            "colors": self.colors.__dict__,
            "typography": self.typography.__dict__,
            "accessibility": {
                "high_contrast": self._high_contrast,
                "reduce_motion": self._reduce_motion,
            }
        }
    
    def save(self, path: str):
        """Save theme to JSON file."""
        with open(path, 'w') as f:
            json.dump(self.to_dict(), f, indent=2)
    
    @classmethod
    def load(cls, path: str) -> 'BX3Theme':
        """Load theme from JSON file."""
        with open(path, 'r') as f:
            data = json.load(f)
        theme = cls(data['variant'])
        return theme


# Convenience exports
BX3Colors = BX3ColorSet
