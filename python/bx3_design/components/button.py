"""BX3 Button component with multiple GUI backend support"""
from __future__ import annotations
import tkinter as tk
from tkinter import ttk
from typing import Callable, Optional, Literal
from ..theme import BX3Theme, BX3Colors
from ..utils import check_contrast_ratio

Variant = Literal['primary', 'secondary', 'ghost', 'danger']
Size = Literal['small', 'medium', 'large']

class BX3Button:
    """Cross-platform button supporting Tkinter, PyQt6, and GTK"""
    
    def __init__(
        self,
        parent,
        text: str,
        on_click: Optional[Callable] = None,
        variant: Variant = 'primary',
        size: Size = 'medium',
        disabled: bool = False,
        loading: bool = False,
        full_width: bool = False,
        theme: Optional[BX3Theme] = None
    ):
        self.text = text
        self.on_click = on_click
        self.variant = variant
        self.size = size
        self.disabled = disabled or loading
        self.loading = loading
        self.full_width = full_width
        self.theme = theme or BX3Theme()
        self.colors = self.theme.colors
        self._widget = None
        self._backend = self._detect_backend(parent)
        
    def _detect_backend(self, parent) -> str:
        """Auto-detect GUI framework from parent widget"""
        module = type(parent).__module__
        if 'PyQt6' in module or 'PySide6' in module:
            return 'qt'
        elif 'gi' in module:  # PyGObject
            return 'gtk'
        else:
            return 'tkinter'  # Default
    
    def create_tkinter(self, parent) -> tk.Widget:
        """Create Tkinter button"""
        from tkinter import Button
        
        bg, fg = self._get_colors()
        
        btn = Button(
            parent,
            text=self.text,
            command=self._handle_click,
            bg=bg,
            fg=fg,
            font=self._get_font(),
            relief='flat',
            cursor='hand2' if not self.disabled else '',
            state='disabled' if self.disabled else 'normal'
        )
        
        # Apply size
        padding = {'small': (8, 4), 'medium': (12, 8), 'large': (16, 12)}[self.size]
        btn.config(padx=padding[0], pady=padding[1])
        
        if self.full_width:
            btn.config(width=parent.winfo_width())
        
        # Accessibility: keyboard navigation
        btn.bind('<Return>', lambda e: self._handle_click())
        btn.bind('<space>', lambda e: self._handle_click())
        
        self._widget = btn
        return btn
    
    def create_qt(self, parent):
        """Create PyQt6/PySide6 button"""
        from PyQt6.QtWidgets import QPushButton, QSizePolicy
        from PyQt6.QtGui import QFont, QColor, QPalette
        from PyQt6.QtCore import Qt
        
        btn = QPushButton(self.text, parent)
        btn.setEnabled(not self.disabled)
        
        # Styling
        bg, fg = self._get_colors()
        btn.setStyleSheet(f"""
            QPushButton {{
                background-color: {bg};
                color: {fg};
                border: none;
                border-radius: 8px;
                padding: {'4px 8px' if self.size == 'small' else '8px 12px' if self.size == 'medium' else '12px 16px'};
                font-weight: 500;
            }}
            QPushButton:hover:!disabled {{
                opacity: 0.9;
            }}
            QPushButton:pressed {{
                opacity: 0.8;
            }}
            QPushButton:focus {{
                border: 2px solid {self.colors.onSurface};
            }}
            QPushButton:disabled {{
                opacity: 0.5;
            }}
        """)
        
        btn.clicked.connect(self._handle_click)
        
        if self.full_width:
            btn.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        
        self._widget = btn
        return btn
    
    def create_gtk(self, parent):
        """Create GTK button via PyGObject"""
        import gi
        gi.require_version('Gtk', '4.0')
        from gi.repository import Gtk, Gdk
        
        btn = Gtk.Button(label=self.text)
        btn.set_sensitive(not self.disabled)
        
        # Size
        if self.size == 'small':
            btn.add_css_class('small')
        elif self.size == 'large':
            btn.add_css_class('large')
        
        # Styling via CSS
        bg, fg = self._get_colors()
        css = f"""
            button {{
                background: {bg};
                color: {fg};
                border-radius: 8px;
                font-weight: 500;
            }}
        """
        
        provider = Gtk.CssProvider()
        provider.load_from_data(css.encode())
        btn.get_style_context().add_provider(provider, Gtk.STYLE_PROVIDER_PRIORITY_APPLICATION)
        
        btn.connect('clicked', lambda _: self._handle_click())
        
        self._widget = btn
        return btn
    
    def _get_colors(self) -> tuple[str, str]:
        """Get background and foreground colors for current variant"""
        c = self.colors
        return {
            'primary': (c.primary, c.on_primary),
            'secondary': (c.surface, c.on_surface),
            'ghost': ('transparent', c.primary),
            'danger': (c.error, c.on_primary),
        }[self.variant]
    
    def _get_font(self) -> tuple:
        """Get font configuration for Tkinter"""
        sizes = {'small': 12, 'medium': 14, 'large': 16}
        return ('system-ui', sizes[self.size])
    
    def _handle_click(self):
        """Handle button click event"""
        if self.disabled or self.loading:
            return
        if self.on_click:
            self.on_click()
    
    def render(self, parent) -> object:
        """Render button using detected backend"""
        if self._backend == 'qt':
            return self.create_qt(parent)
        elif self._backend == 'gtk':
            return self.create_gtk(parent)
        else:
            return self.create_tkinter(parent)
    
    def set_loading(self, loading: bool):
        """Update loading state"""
        self.loading = loading
        self.disabled = loading or self.disabled
        if self._widget:
            if self._backend == 'tkinter':
                self._widget.config(text='Loading...' if loading else self.text)
            elif self._backend == 'qt':
                self._widget.setEnabled(not loading)
    
    @property
    def widget(self):
        return self._widget
