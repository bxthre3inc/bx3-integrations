import React, { useState, useCallback } from 'react';
import { useBX3Theme } from '../hooks/useBX3Theme';
import { useAccessibility } from '../hooks/useAccessibility';

interface BX3ButtonProps {
  children: React.ReactNode;
  onClick?: () => void;
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  size?: 'small' | 'medium' | 'large';
  disabled?: boolean;
  loading?: boolean;
  fullWidth?: boolean;
  type?: 'button' | 'submit' | 'reset';
  ariaLabel?: string;
  className?: string;
}

export const BX3Button: React.FC<BX3ButtonProps> = ({
  children,
  onClick,
  variant = 'primary',
  size = 'medium',
  disabled = false,
  loading = false,
  fullWidth = false,
  type = 'button',
  ariaLabel,
  className = ''
}) => {
  const { theme, colors } = useBX3Theme();
  const { prefersReducedMotion, highContrast } = useAccessibility();
  const [pressed, setPressed] = useState(false);

  const handleMouseDown = useCallback(() => setPressed(true), []);
  const handleMouseUp = useCallback(() => setPressed(false), []);
  const handleMouseLeave = useCallback(() => setPressed(false), []);

  const isDisabled = disabled || loading;
  
  // High contrast override
  const effectiveVariant = highContrast ? 'primary' : variant;
  
  const baseStyles: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '8px',
    border: 'none',
    borderRadius: '8px',
    fontFamily: 'system-ui, -apple-system, sans-serif',
    fontWeight: 500,
    cursor: isDisabled ? 'not-allowed' : 'pointer',
    opacity: isDisabled ? 0.5 : 1,
    width: fullWidth ? '100%' : 'auto',
    transition: prefersReducedMotion ? 'none' : 'all 0.15s ease-out',
    transform: pressed && !isDisabled ? 'scale(0.96)' : 'scale(1)',
    outline: 'none',
    position: 'relative',
    overflow: 'hidden'
  };

  const sizeStyles: Record<string, React.CSSProperties> = {
    small: { padding: '8px 12px', fontSize: '14px', height: '32px' },
    medium: { padding: '12px 16px', fontSize: '16px', height: '40px' },
    large: { padding: '16px 24px', fontSize: '18px', height: '48px' }
  };

  const variantStyles: Record<string, React.CSSProperties> = {
    primary: {
      backgroundColor: colors.primary,
      color: colors.onPrimary
    },
    secondary: {
      backgroundColor: colors.surface,
      color: colors.onSurface,
      border: `1px solid ${colors.onSurfaceVariant}`
    },
    ghost: {
      backgroundColor: 'transparent',
      color: colors.primary
    },
    danger: {
      backgroundColor: colors.error,
      color: '#FFFFFF'
    }
  };

  const combinedStyles: React.CSSProperties = {
    ...baseStyles,
    ...sizeStyles[size],
    ...variantStyles[effectiveVariant]
  };

  // Focus ring styles
  const focusStyles = `
    .bx3-button:focus-visible::after {
      content: '';
      position: absolute;
      inset: -2px;
      border-radius: 10px;
      border: 2px solid ${colors.primary};
      pointer-events: none;
    }
  `;

  return (
    <>
      <style>{focusStyles}</style>
      <button
        type={type}
        onClick={onClick}
        onMouseDown={handleMouseDown}
        onMouseUp={handleMouseUp}
        onMouseLeave={handleMouseLeave}
        disabled={isDisabled}
        aria-label={ariaLabel}
        aria-busy={loading}
        className={`bx3-button ${className}`}
        style={combinedStyles}
        data-variant={variant}
        data-size={size}
        data-theme={theme}
      >
        {loading && (
          <span 
            className="bx3-spinner" 
            style={{
              width: size === 'small' ? 14 : size === 'medium' ? 16 : 20,
              height: size === 'small' ? 14 : size === 'medium' ? 16 : 20,
              border: `2px solid ${variant === 'primary' || variant === 'danger' ? 'rgba(255,255,255,0.3)' : 'rgba(0,0,0,0.1)'}`,
              borderTopColor: variant === 'primary' || variant === 'danger' ? '#FFFFFF' : colors.primary,
              borderRadius: '50%',
              animation: prefersReducedMotion ? 'none' : 'bx3-spin 0.8s linear infinite'
            }}
          />
        )}
        {children}
      </button>
    </>
  );
};

// Keyframes injection
const spinnerStyles = `
  @keyframes bx3-spin {
    to { transform: rotate(360deg); }
  }
`;

if (typeof document !== 'undefined') {
  const style = document.createElement('style');
  style.textContent = spinnerStyles;
  document.head.appendChild(style);
}
