# BX3 Design System Integrations

Multi-platform distribution of the BX3 Design System.

## 📦 Packages

| Package | Platform | Status | Install |
|---------|----------|--------|---------|
| `@bxthre3/bx3-react` | React 18+ | ✅ | `npm i @bxthre3/bx3-react` |
| `@bxthre3/bx3-vue` | Vue 3 | ✅ | `npm i @bxthre3/bx3-vue` |
| `@bxthre3/bx3-web-components` | Web Components | ✅ | CDN or npm |
| `@bxthre3/bx3-angular` | Angular 15+ | 🚧 | Coming Q2 2026 |
| `bx3_design` (pub.dev) | Flutter | 🚧 | Coming Q2 2026 |
| `bx3-design-ios` | SwiftUI | 🚧 | Coming Q2 2026 |

## 🔧 Developer Tools

| Tool | Platform | Status |
|------|----------|--------|
| **Figma Plugin** | Figma/FigJam | ✅ |
| **VS Code Extension** | VS Code | ✅ |
| **Storybook** | Web | ✅ |
| **CLI** | Node.js | 🚧 Coming Q3 2026 |

## 🚀 Quick Start

### React
```tsx
import { BX3Button, BX3ThemeProvider } from '@bxthre3/bx3-react';

function App() {
  return (
    <BX3ThemeProvider theme="zospace">
      <BX3Button variant="primary" onClick={() => console.log('clicked')}>
        Get Started
      </BX3Button>
    </BX3ThemeProvider>
  );
}
```

### Vue
```vue
<template>
  <BX3ThemeProvider theme="zospace">
    <BX3Button variant="primary" @click="handleClick">
      Get Started
    </BX3Button>
  </BX3ThemeProvider>
</template>

<script setup>
import { BX3Button, BX3ThemeProvider } from '@bxthre3/bx3-vue';
</script>
```

### Web Components (CDN)
```html
<script type="module" src="https://unpkg.com/@bxthre3/bx3-web-components"></script>

<bx3-theme-provider theme="zospace">
  <bx3-button variant="primary">Get Started</bx3-button>
</bx3-theme-provider>
```

### Android (Kotlin)
```kotlin
import com.bxthre3.design.atoms.BX3Button
import com.bxthre3.design.theme.BX3Theme

@Composable
fun MyScreen() {
    BX3Theme(variant = ThemeVariant.ZOSPACE) {
        BX3Button(
            text = "Get Started",
            variant = BX3ButtonVariant.PRIMARY,
            onClick = { /* action */ }
        )
    }
}
```

## 🎨 Theming

All platforms support runtime theme switching:

| Theme | Primary | Use Case |
|-------|---------|----------|
| `agentos` | `#1B5E20` (Green) | AI workforce platform |
| `zospace` | `#7B1FA2` (Purple) | Personal dashboard |
| `vpc` | `#004D40` (Teal) | Gaming/club platform |
| `irrig8` | `#1565C0` (Blue) | Agriculture (reserved) |

## ♿ Accessibility

All integrations include:
- **WCAG 2.1 AA compliance**
- **Screen reader detection** with dynamic UI adaptation
- **Reduced motion support** via `prefers-reduced-motion`
- **High contrast mode** via `prefers-contrast: high`
- **RTL language support** (Arabic, Hebrew)

## 💰 Licensing

| Tier | Components | Price |
|------|------------|-------|
| **MIT** | Core atoms (Button, Text, Icon, etc.) | Free |
| **Commercial** | Organisms, templates, pages | $15K/year/app |
| **AI-Native** | PredictiveInput, AIWidget, CommandPalette | $35K/year/app |
| **Enterprise** | Kanban, VirtualizedList, DataGrid | $50K/year/app |

## 🔬 Patent-Pending Innovations

| Innovation | Package | Patent Status |
|------------|---------|---------------|
| Predictive Input with UI Context | All | Filing 2026-05-15 |
| Spatial Drag-Drop with Similarity | React, Vue, Web | Filing 2026-05-15 |
| Windowed Rendering with Prefetch | All | Filing 2026-05-15 |
| Intent-Aware AI Widget | All | Filing 2026-05-15 |
| Temporal Urgency Encoding | All | Filed |

## 📦 Package Sizes

| Package | Minified | Gzipped |
|---------|----------|---------|
| @bxthre3/bx3-react | 45KB | 12KB |
| @bxthre3/bx3-vue | 42KB | 11KB |
| @bxthre3/bx3-web-components | 38KB | 10KB |
| bx3_design (Flutter) | 120KB | N/A |

## 🤝 Contributing

Each package has its own repository under `bxthre3inc/bx3-integrations`.

See individual package READMEs for development setup.

## 📄 License

See individual package LICENSE files. Core atoms are MIT. Premium components require commercial license.

---

**BX3 Design System** — Built by [Bxthre3 Inc](https://github.com/bxthre3inc)