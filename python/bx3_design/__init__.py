"""BX3 Design System for Python - Multi-backend UI components."""

from bx3_design.theme import BX3Theme, BX3Colors, BX3Typography
from bx3_design.components.button import BX3Button
from bx3_design.components.card import BX3Card
from bx3_design.components.input import BX3Input
from bx3_design.components.text import BX3Text
from bx3_design.accessibility import BX3AccessibilityManager

__version__ = "1.0.0"
__all__ = [
    "BX3Theme",
    "BX3Colors", 
    "BX3Typography",
    "BX3Button",
    "BX3Card",
    "BX3Input",
    "BX3Text",
    "BX3AccessibilityManager",
]