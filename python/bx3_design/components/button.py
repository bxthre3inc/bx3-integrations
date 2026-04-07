"""BX3 Button component with multiple backend support."""

from abc import ABC, abstractmethod
from typing import Optional, Callable, Any
from enum import Enum
import warnings

from bx3_design.theme import BX3Theme


class BX3ButtonVariant(Enum):
    PRIMARY = "primary"
    SECONDARY = "secondary"
    TERTIARY = "tertiary"
    GHOST = "ghost"
    DESTRUCTIVE = "destructive"


class BX3ButtonSize(Enum):
    SMALL = "sm"
    MEDIUM = "md"
    LARGE = "lg"


class BX3ButtonBackend(ABC):
    """Abstract base for button backends."""
    
    @abstractmethod
    def create(self, parent: Any, text: str, callback: Callable, 
               variant: BX3ButtonVariant, size: BX3ButtonSize, 
               theme: BX3Theme, disabled: bool) -> Any:
        """Create button widget."""
        pass
    
    @abstractmethod
    def update_theme(self, button: Any, theme: BX3Theme):
        """Update button styling for new theme."""
        pass


class TkinterBackend(BX3ButtonBackend):
    """Tkinter backend for BX3Button."""
    
    def create(self, parent, text: str, callback: Callable,
               variant: BX3ButtonVariant, size: BX3ButtonSize,
               theme: BX3Theme, disabled: bool) -> Any:
        import tkinter as tk
        from tkinter import ttk
        
        colors = theme.colors
        
        # Style mapping
        bg_colors = {
            BX3ButtonVariant.PRIMARY: colors.primary,
            BX3ButtonVariant.SECONDARY: colors.secondary,
            BX3ButtonVariant.TERTIARY: colors.surface_variant,
            BX3ButtonVariant.GHOST: colors.background,
            BX3ButtonVariant.DESTRUCTIVE: colors.error,
        }
        
        fg_colors = {
            BX3ButtonVariant.PRIMARY: colors.on_primary,
            BX3ButtonVariant.SECONDARY: colors.on_secondary,
            BX3ButtonVariant.TERTIARY: colors.on_surface,
            BX3ButtonVariant.GHOST: colors.primary,
            BX3ButtonVariant.DESTRUCTIVE: "#FFFFFF",
        }
        
        # Size mapping
        paddings = {
            BX3ButtonSize.SMALL: (8, 4),
            BX3ButtonSize.MEDIUM: (16, 8),
            BX3ButtonSize.LARGE: (24, 12),
        }
        
        btn = tk.Button(
            parent,
            text=text,
            command=callback if not disabled else None,
            bg=bg_colors[variant],
            fg=fg_colors[variant],
            activebackground=colors.primary_container,
            activeforeground=colors.on_primary_container,
            disabledforeground=colors.on_surface_variant,
            cursor="hand2" if not disabled else "",
            state="disabled" if disabled else "normal",
            padx=paddings[size][0],
            pady=paddings[size][1],
            relief=tk.FLAT,
            borderwidth=0,
        )
        
        return btn
    
    def update_theme(self, button, theme: BX3Theme):
        """Update Tkinter button colors."""
        colors = theme.colors
        button.configure(
            bg=colors.primary,
            fg=colors.on_primary,
            activebackground=colors.primary_container,
            activeforeground=colors.on_primary_container,
        )


class QtBackend(BX3ButtonBackend):
    """PyQt6/PySide6 backend for BX3Button."""
    
    def create(self, parent, text: str, callback: Callable,
               variant: BX3ButtonVariant, size: BX3ButtonSize,
               theme: BX3Theme, disabled: bool) -> Any:
        try:
            from PyQt6.QtWidgets import QPushButton
            from PyQt6.QtCore import Qt
            from PyQt6.QtGui import QColor, QPalette
        except ImportError:
            from PySide6.QtWidgets import QPushButton
            from PySide6.QtCore import Qt
            from PySide6.QtGui import QColor, QPalette
        
        btn = QPushButton(text, parent)
        btn.clicked.connect(callback)
        btn.setEnabled(not disabled)
        
        # Apply styling
        self._apply_style(btn, variant, size, theme)
        
        return btn
    
    def _apply_style(self, button, variant: BX3ButtonVariant, 
                     size: BX3ButtonSize, theme: BX3Theme):
        """Apply QSS stylesheet."""
        colors = theme.colors
        
        bg_map = {
            BX3ButtonVariant.PRIMARY: colors.primary,
            BX3ButtonVariant.SECONDARY: colors.secondary,
            BX3ButtonVariant.TERTIARY: colors.surface_variant,
            BX3ButtonVariant.GHOST: "transparent",
            BX3ButtonVariant.DESTRUCTIVE: colors.error,
        }
        
        fg_map = {
            BX3ButtonVariant.PRIMARY: colors.on_primary,
            BX3ButtonVariant.SECONDARY: colors.on_secondary,
            BX3ButtonVariant.TERTIARY: colors.on_surface,
            BX3ButtonVariant.GHOST: colors.primary,
            BX3ButtonVariant.DESTRUCTIVE: "#FFFFFF",
        }
        
        height_map = {
            BX3ButtonSize.SMALL: 32,
            BX3ButtonSize.MEDIUM: 40,
            BX3ButtonSize.LARGE: 48,
        }
        
        style = f"""
            QPushButton {{
                background-color: {bg_map[variant]};
                color: {fg_map[variant]};
                border: none;
                border-radius: 8px;
                padding: 8px 16px;
                min-height: {height_map[size]}px;
                font-weight: 500;
            }}
            QPushButton:hover {{
                background-color: {colors.primary_container};
            }}
            QPushButton:pressed {{
                background-color: {colors.on_primary_container};
            }}
            QPushButton:disabled {{
                background-color: {colors.surface_variant};
                color: {colors.on_surface_variant};
            }}
        """
        button.setStyleSheet(style)
    
    def update_theme(self, button, theme: BX3Theme):
        """Reapply stylesheet with new theme."""
        # Extract current variant from object property
        variant = getattr(button, '_bx3_variant', BX3ButtonVariant.PRIMARY)
        size = getattr(button, '_bx3_size', BX3ButtonSize.MEDIUM)
        self._apply_style(button, variant, size, theme)


class GTKBackend(BX3ButtonBackend):
    """GTK/PyGObject backend for BX3Button."""
    
    def create(self, parent, text: str, callback: Callable,
               variant: BX3ButtonVariant, size: BX3ButtonSize,
               theme: BX3Theme, disabled: bool) -> Any:
        from gi.repository import Gtk, Gdk
        
        btn = Gtk.Button.new_with_label(text)
        btn.connect("clicked", lambda _: callback())
        btn.set_sensitive(not disabled)
        
        # Add CSS class
        btn.get_style_context().add_class("bx3-button")
        btn.get_style_context().add_class(f"bx3-button-{variant.value}")
        btn.get_style_context().add_class(f"bx3-button-{size.value}")
        
        # Store references for theme updates
        btn._bx3_variant = variant
        btn._bx3_size = size
        
        return btn
    
    def update_theme(self, button, theme: BX3Theme):
        """Update GTK button styling."""
        # GTK uses CSS providers that should be updated at application level
        pass


class BX3Button:
    """BX3 Button with automatic backend detection."""
    
    _backends: dict = {}
    
    def __init__(self, text: str, callback: Optional[Callable] = None,
                 variant: str = "primary", size: str = "md",
                 theme: Optional[BX3Theme] = None, disabled: bool = False,
                 parent: Any = None, backend: Optional[str] = None):
        """
        Create a BX3 button.
        
        Args:
            text: Button label
            callback: Function to call on click
            variant: primary, secondary, tertiary, ghost, destructive
            size: sm, md, lg
            theme: BX3Theme instance (uses singleton if None)
            disabled: Whether button is disabled
            parent: Parent widget
            backend: Force specific backend (tkinter, qt, gtk)
        """
        self._text = text
        self._callback = callback or (lambda: None)
        self._variant = BX3ButtonVariant(variant)
        self._size = BX3ButtonSize(size)
        self._theme = theme or BX3Theme()
        self._disabled = disabled
        self._parent = parent
        self._backend_name = backend
        self._widget: Any = None
        self._backend: Optional[BX3ButtonBackend] = None
        
        # Auto-detect backend if not specified
        self._detect_backend()
        
        # Create widget
        if parent is not None:
            self.create(parent)
        
        # Subscribe to theme changes
        self._theme.add_observer(self._on_theme_change)
    
    def _detect_backend(self):
        """Auto-detect best available backend."""
        if self._backend_name:
            backend_order = [self._backend_name]
        else:
            backend_order = ["qt", "gtk", "tkinter"]
        
        for name in backend_order:
            if name == "qt":
                try:
                    import PyQt6 or PySide6  # noqa
                    self._backend = QtBackend()
                    self._backend_name = "qt"
                    return
                except ImportError:
                    pass
            elif name == "gtk":
                try:
                    from gi.repository import Gtk  # noqa
                    self._backend = GTKBackend()
                    self._backend_name = "gtk"
                    return
                except ImportError:
                    pass
            elif name == "tkinter":
                try:
                    import tkinter  # noqa
                    self._backend = TkinterBackend()
                    self._backend_name = "tkinter"
                    return
                except ImportError:
                    pass
        
        raise RuntimeError("No supported GUI backend found")
    
    def create(self, parent) -> Any:
        """Create the actual widget."""
        self._parent = parent
        self._widget = self._backend.create(
            parent, self._text, self._callback,
            self._variant, self._size,
            self._theme, self._disabled
        )
        # Store variant/size for theme updates
        self._widget._bx3_variant = self._variant
        self._widget._bx3_size = self._size
        return self._widget
    
    def _on_theme_change(self, variant: str):
        """Handle theme changes."""
        if self._widget:
            self._backend.update_theme(self._widget, self._theme)
    
    @property
    def widget(self) -> Any:
        """Get the underlying widget."""
        return self._widget
    
    @property
    def text(self) -> str:
        return self._text
    
    @text.setter
    def text(self, value: str):
        self._text = value
        if self._widget:
            if self._backend_name == "tkinter":
                self._widget.configure(text=value)
            elif self._backend_name in ("qt",):
                self._widget.setText(value)
            elif self._backend_name == "gtk":
                self._widget.set_label(value)
    
    @property
    def disabled(self) -> bool:
        return self._disabled
    
    @disabled.setter
    def disabled(self, value: bool):
        self._disabled = value
        if self._widget:
            if self._backend_name == "tkinter":
                self._widget.configure(
                    state="disabled" if value else "normal"
                )
            elif self._backend_name in ("qt",):
                self._widget.setEnabled(not value)
            elif self._backend_name == "gtk":
                self._widget.set_sensitive(not value)
    
    def destroy(self):
        """Clean up widget and remove theme observer."""
        self._theme.remove_observer(self._on_theme_change)
        if self._widget:
            if self._backend_name == "tkinter":
                self._widget.destroy()
            elif self._backend_name in ("qt",):
                self._widget.deleteLater()
            elif self._backend_name == "gtk":
                self._widget.destroy()
