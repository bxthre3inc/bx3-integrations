# BX3 Design System Integrations

Multi-platform distribution of the BX3 Design System.

## Status

| Platform | Status | Components | Tests | CI/CD |
|----------|--------|------------|-------|-------|
| **React** | ✅ Production Ready | 1 (Button) | ✅ Jest + RTL | ✅ GitHub Actions |
| **Vue** | 🚧 Stubs Only | 0 | ❌ | ❌ |
| **Python** | 🚧 Stubs Only | 0 | ❌ | ❌ |
| **Rust** | 🚧 Stubs Only | 0 | ❌ | ❌ |
| **Linux (.deb)** | 🚧 Stubs Only | 0 | N/A | ❌ |
| **Web Components** | 🚧 Stubs Only | 0 | ❌ | ❌ |

## Quick Start (React)

```bash
npm install @bxthre3/bx3-react
```

```tsx
import { BX3ThemeProvider, BX3Button, useBX3Theme } from '@bxthre3/bx3-react';

function App() {
  const { setTheme } = useBX3Theme();
  
  return (
    <BX3ThemeProvider defaultTheme="zospace">
      <BX3Button variant="primary" onClick={() => setTheme('agentos')}>
        Switch to AgentOS Theme
      </BX3Button>
    </BX3ThemeProvider>
  );
}
```

## Features

### Implemented

| Feature | React | Vue | Python | Rust | Linux |
|---------|-------|-----|--------|------|-------|
| **Theme System** | ✅ | 🚧 | 🚧 | 🚧 | 🚧 |
| **WCAG 2.1 AA** | ✅ | — | — | — | — |
| **Accessibility Hooks** | ✅ | — | — | — | — |
| **Storybook** | ✅ | — | — | — | — |
| **Test Suite** | ✅ | — | — | — | — |
| **TypeScript** | ✅ | 🚧 | — | — | — |

### Available Now

**React Components:**
- `BX3Button` — Primary, secondary, ghost, danger variants with loading, disabled states
- `BX3ThemeProvider` — Runtime theme switching (zospace, agentos, vpc, irrig8)
- `useAccessibility` — Detect screen readers, reduced motion, high contrast

**React Developer Tools:**
- Storybook with a11y addon
- Jest test suite with 70%+ coverage requirement
- ESLint + TypeScript strict mode
- Bundle size monitoring (<50KB gzipped)

## Example Application

See `examples/react-example/` for a working Vite + React demo.

```bash
cd examples/react-example
npm install
npm run dev
```

## Roadmap

### Phase 1 (Now) — React Production
- [x] BX3Button with full variants
- [x] Theme system
- [x] Accessibility hooks
- [x] Test suite
- [ ] BX3Card, BX3Input, BX3Spinner (Week 2)
- [ ] BX3Dialog, BX3Dropdown (Week 3)

### Phase 2 (Q2 2026) — Vue + Python
- [ ] Port React components to Vue 3
- [ ] Python Tkinter/PyQt/GTK backends
- [ ] pytest test suite

### Phase 3 (Q3 2026) — Rust + Systems
- [ ] Rust wgpu rendering
- [ ] Linux .deb package with GTK theme
- [ ] FFI bindings for Python/Node

### Phase 4 (Q4 2026) — DevTools
- [ ] Figma plugin (code → design sync)
- [ ] VS Code extension
- [ ] CLI code generation

## License

- **Core Components (atoms):** MIT License
- **Premium Components (organisms, templates):** Commercial License — contact sales@bxthre3inc.com

## Patent Notice

The following innovations are patent-pending:
- Runtime adaptive theming with accessibility awareness
- Predictive input with UI context
- Spatial drag-drop with similarity prediction

Use of this software does not grant patent rights. Commercial licenses include patent coverage.