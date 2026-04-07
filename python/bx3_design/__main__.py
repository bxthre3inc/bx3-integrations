#!/usr/bin/env python3
"""BX3 Design System CLI"""
import argparse
import sys
from .theme import BX3Theme

def main():
    parser = argparse.ArgumentParser(description='BX3 Design System CLI')
    parser.add_argument('--list', action='store_true', help='List available themes')
    parser.add_argument('--apply', choices=['zospace', 'agentos', 'vpc', 'irrig8'], help='Apply theme')
    parser.add_argument('--export', choices=['css', 'json', 'xml'], help='Export theme to format')
    parser.add_argument('--output', '-o', help='Output file')
    parser.add_argument('--check-contrast', nargs=2, metavar=('COLOR1', 'COLOR2'), 
                       help='Check WCAG contrast ratio between two colors')
    
    args = parser.parse_args()
    
    if args.list:
        print("Available themes:")
        for theme in ['zospace', 'agentos', 'vpc', 'irrig8']:
            t = BX3Theme(theme)
            print(f"  - {theme}: {t.colors.primary}")
        return 0
    
    if args.apply:
        t = BX3Theme(args.apply)
        t.save_to_file()
        print(f"Theme '{args.apply}' applied and saved to ~/.bx3_theme")
        return 0
    
    if args.export:
        t = BX3Theme()  # Uses saved or default
        if args.output:
            with open(args.output, 'w') as f:
                f.write(t.export(args.export))
            print(f"Exported to {args.output}")
        else:
            print(t.export(args.export))
        return 0
    
    if args.check_contrast:
        from .utils import check_contrast_ratio
        ratio = check_contrast_ratio(args.check_contrast[0], args.check_contrast[1])
        print(f"Contrast ratio: {ratio:.2f}:1")
        if ratio >= 7:
            print("✅ AAA (enhanced)")
        elif ratio >= 4.5:
            print("✅ AA (standard), AAA (large text)")
        elif ratio >= 3:
            print("⚠️ AA (large text only)")
        else:
            print("❌ Fail")
        return 0
    
    parser.print_help()
    return 1

if __name__ == '__main__':
    sys.exit(main())
