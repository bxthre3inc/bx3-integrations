import { useContext, createContext, useState, useEffect } from 'react';
import { BX3Colors, BX3ColorScheme } from '../types';

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

interface BX3ThemeContextType {
  colors: BX3Colors;
  isDark: boolean;
  prefersReducedMotion: boolean;
  highContrast: boolean;
  setColorScheme: (scheme: BX3ColorScheme) => void;
}

const BX3ThemeContext = createContext<BX3ThemeContextType | null>(null);

export const BX3ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [colors, setColors] = useState(defaultColors);
  const [isDark, setIsDark] = useState(true);
  const [prefersReducedMotion, setPrefersReducedMotion] = useState(false);
  const [highContrast, setHighContrast] = useState(false);

  useEffect(() => {
    // Detect system preferences
    const mediaQuery = window.matchMedia('(prefers-reduced-motion: reduce)');
    setPrefersReducedMotion(mediaQuery.matches);
    
    const contrastQuery = window.matchMedia('(prefers-contrast: high)');
    setHighContrast(contrastQuery.matches);

    const darkQuery = window.matchMedia('(prefers-color-scheme: dark)');
    setIsDark(darkQuery.matches);

    const handlers = [
      { query: mediaQuery, handler: (e: MediaQueryListEvent) => setPrefersReducedMotion(e.matches) },
      { query: contrastQuery, handler: (e: MediaQueryListEvent) => setHighContrast(e.matches) },
      { query: darkQuery, handler: (e: MediaQueryListEvent) => setIsDark(e.matches) },
    ];

    handlers.forEach(({ query, handler }) => query.addEventListener('change', handler));
    
    return () => {
      handlers.forEach(({ query, handler }) => query.removeEventListener('change', handler));
    };
  }, []);

  const setColorScheme = (scheme: BX3ColorScheme) => {
    // Apply scheme from BX3 backend or local storage
    setColors(scheme.colors);
  };

  return (
    <BX3ThemeContext.Provider value={{ 
      colors, 
      isDark, 
      prefersReducedMotion, 
      highContrast, 
      setColorScheme 
    }}>
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