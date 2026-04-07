import { ref, computed, provide, inject, onMounted, type InjectionKey } from 'vue';

export type BX3Theme = 'zospace' | 'agentos' | 'vpc' | 'irrig8';

export interface BX3Colors {
  primary: string;
  onPrimary: string;
  surface: string;
  onSurface: string;
  surfaceVariant: string;
  error: string;
  success: string;
  background: string;
}

const themeColors: Record<BX3Theme, BX3Colors> = {
  zospace: {
    primary: '#7B1FA2',
    onPrimary: '#FFFFFF',
    surface: '#1A1A2E',
    onSurface: '#E0E0E0',
    surfaceVariant: '#2D2D44',
    error: '#CF6679',
    success: '#7EE787',
    background: '#0A0A0F',
  },
  agentos: {
    primary: '#1B5E20',
    onPrimary: '#FFFFFF',
    surface: '#1A1A2E',
    onSurface: '#E0E0E0',
    surfaceVariant: '#2D2D44',
    error: '#CF6679',
    success: '#7EE787',
    background: '#0A0A0F',
  },
  vpc: {
    primary: '#004D40',
    onPrimary: '#FFFFFF',
    surface: '#1A1A2E',
    onSurface: '#E0E0E0',
    surfaceVariant: '#2D2D44',
    error: '#CF6679',
    success: '#7EE787',
    background: '#0A0A0F',
  },
  irrig8: {
    primary: '#1565C0',
    onPrimary: '#FFFFFF',
    surface: '#1A1A2E',
    onSurface: '#E0E0E0',
    surfaceVariant: '#2D2D44',
    error: '#CF6679',
    success: '#7EE787',
    background: '#0A0A0F',
  },
};

const BX3ThemeKey: InjectionKey<{
  theme: ReturnType<typeof ref<BX3Theme>>;
  setTheme: (t: BX3Theme) => void;
  colors: ReturnType<typeof computed<BX3Colors>>;
  reduceMotion: ReturnType<typeof ref<boolean>>;
}> = Symbol('bx3-theme');

export function provideBX3Theme(defaultTheme: BX3Theme = 'zospace') {
  const theme = ref<BX3Theme>(defaultTheme);
  const reduceMotion = ref(false);
  
  const colors = computed(() => themeColors[theme.value]);
  
  const setTheme = (newTheme: BX3Theme) => {
    theme.value = newTheme;
    localStorage.setItem('bx3-theme', newTheme);
  };
  
  onMounted(() => {
    const saved = localStorage.getItem('bx3-theme') as BX3Theme;
    if (saved && saved in themeColors) {
      theme.value = saved;
    }
    
    reduceMotion.value = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    
    window.matchMedia('(prefers-reduced-motion: reduce)')
      .addEventListener('change', (e) => {
        reduceMotion.value = e.matches;
      });
  });
  
  provide(BX3ThemeKey, { theme, setTheme, colors, reduceMotion });
  
  return { theme, setTheme, colors, reduceMotion };
}

export function useBX3Theme() {
  const context = inject(BX3ThemeKey);
  if (!context) {
    throw new Error('useBX3Theme must be used within a BX3ThemeProvider');
  }
  return context;
}
