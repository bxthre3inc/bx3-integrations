import { createContext, useContext, useState, useEffect, useCallback } from 'react';

export type BX3ThemeVariant = 'agentos' | 'zospace' | 'vpc' | 'irrig8' | 'system';

export interface BX3Colors {
  primary: string;
  onPrimary: string;
  primaryContainer: string;
  onPrimaryContainer: string;
  secondary: string;
  onSecondary: string;
  background: string;
  onBackground: string;
  surface: string;
  onSurface: string;
  surfaceVariant: string;
  onSurfaceVariant: string;
  error: string;
  onError: string;
  success: string;
  warning: string;
  info: string;
  // AI-specific
  aiThinking: string;
  aiGenerating: string;
  aiReviewing: string;
  // Urgency
  urgentHigh: string;
  urgentMedium: string;
  urgentLow: string;
}

export interface BX3Theme {
  variant: BX3ThemeVariant;
  colors: BX3Colors;
  isDark: boolean;
  contrastLevel: 'low' | 'normal' | 'high';
}

const themePresets: Record<Exclude<BX3ThemeVariant, 'system'>, BX3Colors> = {
  agentos: {
    primary: '#1B5E20',
    onPrimary: '#FFFFFF',
    primaryContainer: '#4CAF50',
    onPrimaryContainer: '#FFFFFF',
    secondary: '#81C784',
    onSecondary: '#000000',
    background: '#0A0A0F',
    onBackground: '#E0E0E0',
    surface: '#1A1A2E',
    onSurface: '#E0E0E0',
    surfaceVariant: '#2D2D44',
    onSurfaceVariant: '#B0B0C0',
    error: '#CF6679',
    onError: '#FFFFFF',
    success: '#4CAF50',
    warning: '#FFB74D',
    info: '#64B5F6',
    aiThinking: '#9C27B0',
    aiGenerating: '#00E676',
    aiReviewing: '#FFD600',
    urgentHigh: '#FF1744',
    urgentMedium: '#FF9100',
    urgentLow: '#00B0FF'
  },
  zospace: {
    primary: '#7B1FA2',
    onPrimary: '#FFFFFF',
    primaryContainer: '#E1BEE7',
    onPrimaryContainer: '#4A148C',
    secondary: '#03DAC6',
    onSecondary: '#000000',
    background: '#0A0A0F',
    onBackground: '#E0E0E0',
    surface: '#1A1A2E',
    onSurface: '#E0E0E0',
    surfaceVariant: '#2D2D44',
    onSurfaceVariant: '#B0B0C0',
    error: '#CF6679',
    onError: '#FFFFFF',
    success: '#7EE787',
    warning: '#FFB74D',
    info: '#64B5F6',
    aiThinking: '#9C27B0',
    aiGenerating: '#00E676',
    aiReviewing: '#FFD600',
    urgentHigh: '#FF1744',
    urgentMedium: '#FF9100',
    urgentLow: '#00B0FF'
  },
  vpc: {
    primary: '#004D40',
    onPrimary: '#FFFFFF',
    primaryContainer: '#26A69A',
    onPrimaryContainer: '#000000',
    secondary: '#80CBC4',
    onSecondary: '#000000',
    background: '#0A0A0F',
    onBackground: '#E0E0E0',
    surface: '#1A1A2E',
    onSurface: '#E0E0E0',
    surfaceVariant: '#2D2D44',
    onSurfaceVariant: '#B0B0C0',
    error: '#CF6679',
    onError: '#FFFFFF',
    success: '#26A69A',
    warning: '#FFB74D',
    info: '#64B5F6',
    aiThinking: '#9C27B0',
    aiGenerating: '#00E676',
    aiReviewing: '#FFD600',
    urgentHigh: '#FF1744',
    urgentMedium: '#FF9100',
    urgentLow: '#00B0FF'
  },
  irrig8: {
    primary: '#1565C0',
    onPrimary: '#FFFFFF',
    primaryContainer: '#64B5F6',
    onPrimaryContainer: '#000000',
    secondary: '#90CAF9',
    onSecondary: '#000000',
    background: '#0A0A0F',
    onBackground: '#E0E0E0',
    surface: '#1A1A2E',
    onSurface: '#E0E0E0',
    surfaceVariant: '#2D2D44',
    onSurfaceVariant: '#B0B0C0',
    error: '#CF6679',
    onError: '#FFFFFF',
    success: '#64B5F6',
    warning: '#FFB74D',
    info: '#4FC3F7',
    aiThinking: '#9C27B0',
    aiGenerating: '#00E676',
    aiReviewing: '#FFD600',
    urgentHigh: '#FF1744',
    urgentMedium: '#FF9100',
    urgentLow: '#00B0FF'
  }
};

interface BX3ThemeContextValue {
  theme: BX3ThemeVariant;
  colors: BX3Colors;
  setTheme: (theme: BX3ThemeVariant) => void;
  isDark: boolean;
  contrastLevel: 'low' | 'normal' | 'high';
}

const BX3ThemeContext = createContext<BX3ThemeContextValue | null>(null);

export const BX3ThemeProvider: React.FC<{
  children: React.ReactNode;
  defaultTheme?: BX3ThemeVariant;
}> = ({ children, defaultTheme = 'zospace' }) => {
  const [theme, setThemeState] = useState<BX3ThemeVariant>(defaultTheme);
  const [isDark, setIsDark] = useState(true);
  const [contrastLevel, setContrastLevel] = useState<'low' | 'normal' | 'high'>('normal');

  // Detect system theme
  useEffect(() => {
    if (theme === 'system') {
      const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
      setIsDark(mediaQuery.matches);
      
      const handler = (e: MediaQueryListEvent) => setIsDark(e.matches);
      mediaQuery.addEventListener('change', handler);
      return () => mediaQuery.removeEventListener('change', handler);
    }
    setIsDark(true); // Default to dark for brand themes
  }, [theme]);

  // Detect contrast preference
  useEffect(() => {
    const mediaQuery = window.matchMedia('(prefers-contrast: more)');
    setContrastLevel(mediaQuery.matches ? 'high' : 'normal');
    
    const handler = (e: MediaQueryListEvent) => {
      setContrastLevel(e.matches ? 'high' : 'normal');
    };
    mediaQuery.addEventListener('change', handler);
    return () => mediaQuery.removeEventListener('change', handler);
  }, []);

  // Apply high contrast adjustments
  const colors = (() => {
    const base = theme === 'system' ? themePresets.zospace : themePresets[theme];
    
    if (contrastLevel === 'high') {
      return {
        ...base,
        onSurface: '#FFFFFF',
        onSurfaceVariant: '#E0E0E0',
        surfaceVariant: '#3D3D54'
      };
    }
    return base;
  })();

  const setTheme = useCallback((newTheme: BX3ThemeVariant) => {
    setThemeState(newTheme);
    // Persist to localStorage
    localStorage.setItem('bx3-theme', newTheme);
  }, []);

  // Load from localStorage on mount
  useEffect(() => {
    const saved = localStorage.getItem('bx3-theme') as BX3ThemeVariant | null;
    if (saved && saved !== theme) {
      setThemeState(saved);
    }
  }, []);

  return (
    <BX3ThemeContext.Provider value={{ theme, colors, setTheme, isDark, contrastLevel }}>
      {children}
    </BX3ThemeContext.Provider>
  );
};

export const useBX3Theme = () => {
  const context = useContext(BX3ThemeContext);
  if (!context) {
    throw new Error('useBX3Theme must be used within BX3ThemeProvider');
  }
  return context;
};

export { themePresets };
