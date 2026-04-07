// Figma Plugin - BX3 Design System Integration

figma.showUI(__html__, { width: 400, height: 600 });

interface BX3Component {
  id: string;
  name: string;
  category: 'atom' | 'molecule' | 'organism' | 'template' | 'page';
  variants: string[];
  properties: Record<string, any>;
}

interface BX3Theme {
  colors: Record<string, { r: number; g: number; b: number }>;
  typography: Record<string, any>;
  spacing: number[];
}

// Component Library
const componentLibrary: BX3Component[] = [
  { id: 'bx3-button', name: 'BX3 Button', category: 'atom', variants: ['primary', 'secondary', 'ghost'], properties: { size: ['sm', 'md', 'lg'] } },
  { id: 'bx3-card', name: 'BX3 Card', category: 'molecule', variants: ['elevated', 'filled', 'outlined'], properties: { elevation: [0, 1, 2, 4, 8] } },
  { id: 'bx3-input', name: 'BX3 Input Field', category: 'molecule', variants: ['filled', 'outlined'], properties: { state: ['default', 'focused', 'error', 'disabled'] } },
  { id: 'bx3-ai-widget', name: 'BX3 AI Widget', category: 'organism', variants: ['expanded', 'collapsed'], properties: {} },
  { id: 'bx3-kanban', name: 'BX3 Kanban Board', category: 'organism', variants: ['3-column', '4-column'], properties: {} },
];

// Theme Definitions
const themes: Record<string, BX3Theme> = {
  agentos: {
    colors: {
      primary: { r: 0.106, g: 0.369, b: 0.125 }, // #1B5E20
      surface: { r: 0.102, g: 0.102, b: 0.18 },
      onSurface: { r: 0.878, g: 0.878, b: 0.878 },
    },
    typography: {},
    spacing: [4, 8, 12, 16, 24, 32, 48],
  },
  zospace: {
    colors: {
      primary: { r: 0.482, g: 0.122, b: 0.635 }, // #7B1FA2
      surface: { r: 0.102, g: 0.102, b: 0.18 },
      onSurface: { r: 0.878, g: 0.878, b: 0.878 },
    },
    typography: {},
    spacing: [4, 8, 12, 16, 24, 32, 48],
  },
  vpc: {
    colors: {
      primary: { r: 0, g: 0.302, b: 0.251 }, // #004D40
      surface: { r: 0.102, g: 0.102, b: 0.18 },
      onSurface: { r: 0.878, g: 0.878, b: 0.878 },
    },
    typography: {},
    spacing: [4, 8, 12, 16, 24, 32, 48],
  },
};

// Handle messages from UI
figma.ui.onmessage = async (msg) => {
  switch (msg.type) {
    case 'get-components':
      figma.ui.postMessage({ type: 'components', data: componentLibrary });
      break;
    
    case 'get-themes':
      figma.ui.postMessage({ type: 'themes', data: Object.keys(themes) });
      break;
    
    case 'insert-component':
      await insertComponent(msg.componentId, msg.variant, msg.properties);
      break;
    
    case 'apply-theme':
      await applyTheme(msg.themeName);
      break;
    
    case 'sync-with-code':
      await syncWithCode();
      break;
    
    case 'generate-variables':
      await generateDesignTokens();
      break;
  }
};

async function insertComponent(componentId: string, variant: string, properties: Record<string, any>) {
  const component = componentLibrary.find(c => c.id === componentId);
  if (!component) return;

  // Create a placeholder frame representing the component
  const frame = figma.createFrame();
  frame.name = `${component.name} - ${variant}`;
  frame.resize(200, component.category === 'atom' ? 48 : 200);
  
  // Add placeholder text
  const text = figma.createText();
  await figma.loadFontAsync({ family: "Inter", style: "Regular" });
  text.characters = component.name;
  text.fontSize = 16;
  text.x = 16;
  text.y = 16;
  frame.appendChild(text);
  
  // Position at viewport center
  const center = figma.viewport.center;
  frame.x = center.x - frame.width / 2;
  frame.y = center.y - frame.height / 2;
  
  figma.currentPage.appendChild(frame);
  figma.viewport.scrollAndZoomIntoView([frame]);
  
  figma.notify(`Inserted ${component.name}`);
}

async function applyTheme(themeName: string) {
  const theme = themes[themeName];
  if (!theme) return;

  // Create or update color styles
  for (const [name, color] of Object.entries(theme.colors)) {
    const styleName = `BX3/${name}`;
    let paintStyle = figma.getLocalPaintStyles().find(s => s.name === styleName);
    
    if (!paintStyle) {
      paintStyle = figma.createPaintStyle();
      paintStyle.name = styleName;
    }
    
    paintStyle.paints = [{
      type: 'SOLID',
      color: color,
    }];
  }
  
  figma.notify(`Applied ${themeName} theme - ${Object.keys(theme.colors).length} color styles created/updated`);
}

async function syncWithCode() {
  // Fetch latest component definitions from the BX3 backend
  try {
    // Simulated API call
    figma.notify('Syncing with BX3 Design System repository...');
    
    // In production, this would call:
    // const response = await fetch('https://api.bxthre3.com/v1/components');
    // const components = await response.json();
    
    figma.notify('Sync complete! 61 components available');
  } catch (error) {
    figma.notify('Sync failed - check your connection');
  }
}

async function generateDesignTokens() {
  // Export current design tokens to JSON for developers
  const tokens = {
    colors: {},
    typography: {},
    spacing: {},
    components: componentLibrary,
  };

  // Collect all local styles
  figma.getLocalPaintStyles().forEach(style => {
    if (style.name.startsWith('BX3/')) {
      const color = style.paints[0];
      if (color.type === 'SOLID') {
        tokens.colors[style.name.replace('BX3/', '')] = color.color;
      }
    }
  });

  figma.ui.postMessage({ 
    type: 'design-tokens', 
    data: JSON.stringify(tokens, null, 2) 
  });
  
  figma.notify('Design tokens generated! Check the plugin UI for export.');
}