import { ref, computed, provide, inject, onMounted, onUnmounted } from 'vue';
import type { InjectionKey, Ref } from 'vue';
import type { BX3Colors, BX3ThemeState } from '../types';

const defaultColors: BX3Colors = {
  primary: '#7B1FA2',
  onPrimary: '#FFFFFF',
  primaryContainer: '#E1BEE7',
  onPrimaryContainer: '#4A148C',
  secondary: '#03DAC6',
  onSecondary: '#000000',
  background: '#0A0A0F',
  surface: '#1A1A2E',
  surfaceVariant: '#2D2D44',
  onSurface: '#E0E0E0',
  onSurfaceVariant: '#B0B0C0',
  error: '#CF6679',
  success: '#7EE787',
  warning: '#FFB74D',
  info: '#64B5F6',
  aiThinking: '#9C27B0',
  aiGenerating: '#00E676',
  aiReviewing: '#FFD600',
  urgentHigh: '#FF1744',
  urgentMedium: '#FF9100',
  urgentLow: '#00B0FF',
};

export const BX3ThemeKey: InjectionKey<BX3ThemeState> = Symbol('BX3Theme');

export function useBX3Theme() {
  const theme = inject(BX3ThemeKey);
  if (!theme) {
    throw new Error('useBX3Theme must be used within BX3ThemeProvider');
  }
  return theme;
}

export function createBX3Theme() {
  const colors = ref<BX3Colors>(defaultColors);
  const isDark = ref(true);
  const prefersReducedMotion = ref(false);
  const highContrast = ref(false);

  const mediaQueryHandlers: Array<{ query: MediaQueryList; handler: (e: MediaQueryListEvent) => void }> = [];

  onMounted(() => {
    if (typeof window !== 'undefined') {
      const motionQuery = window.matchMedia('(prefers-reduced-motion: reduce)');
      const contrastQuery = window.matchMedia('(prefers-contrast: high)');
      const darkQuery = window.matchMedia('(prefers-color-scheme: dark)');

      const motionHandler = (e: MediaQueryListEvent) => (prefersReducedMotion.value = e.matches);
      const contrastHandler = (e: MediaQueryListEvent) => (highContrast.value = e.matches);
      const darkHandler = (e: MediaQueryListEvent) => (isDark.value = e.matches);

      motionQuery.addEventListener('change', motionHandler);
      contrastQuery.addEventListener('change', contrastHandler);
      darkQuery.addEventListener('change', darkHandler);

      mediaQueryHandlers.push(
        { query: motionQuery, handler: motionHandler },
        { query: contrastQuery, handler: contrastHandler },
        { query: darkQuery, handler: darkHandler }
      );

      // Set initial values
      prefersReducedMotion.value = motionQuery.matches;
      highContrast.value = contrastQuery.matches;
      isDark.value = darkQuery.matches;
    }
  });

  onUnmounted(() => {
    mediaQueryHandlers.forEach(({ query, handler }) => {
      query.removeEventListener('change', handler);
    });
  });

  const setColorScheme = (newColors: Partial<BX3Colors>) => {
    colors.value = { ...colors.value, ...newColors };
  };

  return {
    colors: computed(() => colors.value),
    isDark: computed(() => isDark.value),
    prefersReducedMotion: computed(() => prefersReducedMotion.value),
    highContrast: computed(() => highContrast.value),
    setColorScheme,
  };
}