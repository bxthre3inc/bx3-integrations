import { useState, useEffect, useCallback } from 'react';

export interface AccessibilityState {
  prefersReducedMotion: boolean;
  prefersHighContrast: boolean;
  screenReaderActive: boolean;
  fontSize: 'normal' | 'large' | 'larger';
  touchTargetSize: 'normal' | 'large';
}

export const useAccessibility = (): AccessibilityState => {
  const [state, setState] = useState<AccessibilityState>({
    prefersReducedMotion: false,
    prefersHighContrast: false,
    screenReaderActive: false,
    fontSize: 'normal',
    touchTargetSize: 'normal'
  });

  useEffect(() => {
    // Check for reduced motion preference
    const motionQuery = window.matchMedia('(prefers-reduced-motion: reduce)');
    
    // Check for high contrast
    const contrastQuery = window.matchMedia('(prefers-contrast: more)');
    
    // Check for forced colors (Windows high contrast)
    const forcedColorsQuery = window.matchMedia('(forced-colors: active)');
    
    // Font size (approximate via pixel ratio check)
    const getFontSize = (): AccessibilityState['fontSize'] => {
      const rootFontSize = parseFloat(getComputedStyle(document.documentElement).fontSize);
      if (rootFontSize >= 20) return 'larger';
      if (rootFontSize >= 18) return 'large';
      return 'normal';
    };
    
    // Screen reader detection (heuristic)
    const detectScreenReader = (): boolean => {
      // Check for common screen reader indicators
      const indicators = [
        'navigator.userAgent.includes("NVDA")',
        'window.navigator.userAgent.includes("JAWS")',
        'document.documentElement.getAttribute("data-at-shortcutkeys")', // VoiceOver
        'window.matchMedia("(prefers-reduced-transparency: reduce)").matches' // Often paired
      ];
      
      // Also check if DOM has been modified in screen-reader-like ways
      const ariaLiveElements = document.querySelectorAll('[aria-live]');
      const hasScreenReaderBehavior = ariaLiveElements.length > 0;
      
      return hasScreenReaderBehavior || 
             ('ontouchstart' in window && window.innerWidth > 1200); // Large touch device often indicates a11y tools
    };

    const updateState = () => {
      setState({
        prefersReducedMotion: motionQuery.matches,
        prefersHighContrast: contrastQuery.matches || forcedColorsQuery.matches,
        screenReaderActive: detectScreenReader(),
        fontSize: getFontSize(),
        touchTargetSize: window.innerWidth < 768 ? 'large' : 'normal'
      });
    };

    // Initial check
    updateState();

    // Listen for changes
    motionQuery.addEventListener('change', updateState);
    contrastQuery.addEventListener('change', updateState);
    forcedColorsQuery.addEventListener('change', updateState);
    window.addEventListener('resize', updateState);

    // Periodic screen reader check
    const interval = setInterval(updateState, 5000);

    return () => {
      motionQuery.removeEventListener('change', updateState);
      contrastQuery.removeEventListener('change', updateState);
      forcedColorsQuery.removeEventListener('change', updateState);
      window.removeEventListener('resize', updateState);
      clearInterval(interval);
    };
  }, []);

  return state;
};

// Utility hook for focus management
export const useFocusManagement = () => {
  const [focusTrap, setFocusTrap] = useState<HTMLElement | null>(null);

  const trapFocus = useCallback((element: HTMLElement) => {
    setFocusTrap(element);
    
    const focusableElements = element.querySelectorAll(
      'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
    );
    
    const firstElement = focusableElements[0] as HTMLElement;
    const lastElement = focusableElements[focusableElements.length - 1] as HTMLElement;
    
    const handleTabKey = (e: KeyboardEvent) => {
      if (e.key === 'Tab') {
        if (e.shiftKey && document.activeElement === firstElement) {
          e.preventDefault();
          lastElement.focus();
        } else if (!e.shiftKey && document.activeElement === lastElement) {
          e.preventDefault();
          firstElement.focus();
        }
      }
      
      if (e.key === 'Escape') {
        element.dispatchEvent(new CustomEvent('bx3:close'));
      }
    };
    
    element.addEventListener('keydown', handleTabKey);
    firstElement?.focus();
    
    return () => {
      element.removeEventListener('keydown', handleTabKey);
    };
  }, []);

  const releaseFocus = useCallback(() => {
    setFocusTrap(null);
  }, []);

  return { trapFocus, releaseFocus, focusTrap };
};

// Announce to screen readers
export const announceToScreenReader = (message: string, priority: 'polite' | 'assertive' = 'polite') => {
  const announcer = document.createElement('div');
  announcer.setAttribute('aria-live', priority);
  announcer.setAttribute('aria-atomic', 'true');
  announcer.className = 'bx3-sr-announcer';
  announcer.style.cssText = `
    position: absolute;
    left: -10000px;
    width: 1px;
    height: 1px;
    overflow: hidden;
  `;
  
  document.body.appendChild(announcer);
  
  // Delay to ensure screen reader notices the change
  setTimeout(() => {
    announcer.textContent = message;
    setTimeout(() => document.body.removeChild(announcer), 1000);
  }, 100);
};
