# BX3 Design System for Python

A comprehensive design system for Python desktop applications supporting Tkinter, PyQt6/PySide6, and GTK.

## Installation

```bash
pip install bx3-design
```

With specific backends:
```bash
pip install bx3-design[qt]      # PyQt6/PySide6
pip install bx3-design[gtk]     # GTK/PyGObject
pip install bx3-design[all]     # All backends
```

## Quick Start

```python
from bx3_design import BX3Theme, BX3Button

# Apply theme
theme = BX3Theme("agentos")

# Create button with BX3 styling
button = BX3Button(
    text="Click Me",
    variant="primary",
    theme=theme
)
```

## Supported Frameworks

| Framework | Status | Performance |
|-----------|--------|-------------|
| Tkinter | ✅ Full | Native |
| PyQt6 | ✅ Full | Hardware-accelerated |
| PySide6 | ✅ Full | Hardware-accelerated |
| GTK 4 | ✅ Full | Native |

## Features

- **Runtime theme switching** (agentos, zospace, vpc, irrig8)
- **WCAG 2.1 AA accessibility** compliance
- **Cross-framework consistency** — same API, different backends
- **Accessibility detection** — high contrast, reduced motion

## CLI

```bash
bx3-theme --list              # List available themes
bx3-theme --apply agentos     # Apply AgentOS theme
bx3-theme --export css        # Export to CSS variables
```

## License

MIT License — Commercial licensing available for enterprise features.
