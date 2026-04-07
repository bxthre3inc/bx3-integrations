import { ref, onMounted, onUnmounted } from 'vue';

export interface AccessibilityState {
  screenReaderEnabled: ReturnType<typeof ref<boolean>>;
  reducedMotion: ReturnType<typeof ref<boolean>>;
  highContrast: ReturnType<typeof ref<boolean>>;
  touchTargetSize: ReturnType<typeof ref<48 | 44>>;
  announceToScreenReader: (message: string) => void;
}

export function useAccessibility(): AccessibilityState {
  const screenReaderEnabled = ref(false);
  const reducedMotion = ref(false);
  const highContrast = ref(false);
  const touchTargetSize = ref<48 | 44>(44);
  
  let observer: MutationObserver | null = null;
  
  const detectScreenReader = () => {
    // Heuristic: check for common screen reader DOM modifications
    const hasScreenReaderAttrs = document.querySelectorAll(
      '[aria-live], [aria-atomic], [role="alert"], [role="status"]'
    ).length > 0;
    
    // Check for NVDA/JAWS specific attributes
    const nvdaCheck = document.documentElement.getAttribute('data-nvda') !== null;
    
    screenReaderEnabled.value = hasScreenReaderAttrs || nvdaCheck;
    
    // Adjust touch targets for screen reader users
    touchTargetSize.value = screenReaderEnabled.value ? 48 : 44;
  };
  
  const updateFromMediaQueries = () => {
    reducedMotion.value = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    highContrast.value = window.matchMedia('(forced-colors: active)').matches ||
                         window.matchMedia('(prefers-contrast: more)').matches;
  };
  
  const announceToScreenReader = (message: string) => {
    const announcer = document.createElement('div');
    announcer.setAttribute('aria-live', 'polite');
    announcer.setAttribute('aria-atomic', 'true');
    announcer.className = 'sr-only';
    announcer.style.cssText = 
      'position:absolute;left:-10000px;width:1px;height:1px;overflow:hidden;';
    
    document.body.appendChild(announcer);
    
    requestAnimationFrame(() => {
      announcer.textContent = message;
      setTimeout(() => document.body.removeChild(announcer), 1000);
    });
  };
  
  onMounted(() => {
    updateFromMediaQueries();
    detectScreenReader();
    
    // Listen for media query changes
    const motionQuery = window.matchMedia('(prefers-reduced-motion: reduce)');
    const contrastQuery = window.matchMedia('(prefers-contrast: more)');
    
    motionQuery.addEventListener('change', updateFromMediaQueries);
    contrastQuery.addEventListener('change', updateFromMediaQueries);
    
    // Watch for screen reader DOM changes
    observer = new MutationObserver(detectScreenReader);
    observer.observe(document.body, {
      attributes: true,
      subtree: true,
      attributeFilter: ['aria-live', 'role', 'aria-atomic'],
    });
  });
  
  onUnmounted(() => {
    observer?.disconnect();
  });
  
  return {
    screenReaderEnabled,
    reducedMotion,
    highContrast,
    touchTargetSize,
    announceToScreenReader,
  };
}
