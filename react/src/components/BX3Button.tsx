import React, { useState } from 'react';
import { useBX3Theme } from '../hooks/useBX3Theme';
import { BX3ButtonProps, BX3ButtonVariant } from '../types';

export const BX3Button: React.FC<BX3ButtonProps> = ({
  children,
  variant = BX3ButtonVariant.PRIMARY,
  size = 'md',
  loading = false,
  disabled = false,
  onClick,
  ...props
}) => {
  const { colors, isDark } = useBX3Theme();
  const [isHovered, setIsHovered] = useState(false);
  const [isPressed, setIsPressed] = useState(false);

  const getVariantStyles = () => {
    switch (variant) {
      case BX3ButtonVariant.PRIMARY:
        return {
          background: colors.primary,
          color: colors.onPrimary,
          border: 'none',
        };
      case BX3ButtonVariant.SECONDARY:
        return {
          background: colors.surface,
          color: colors.onSurface,
          border: `1px solid ${colors.onSurfaceVariant}`,
        };
      case BX3ButtonVariant.GHOST:
        return {
          background: 'transparent',
          color: colors.onSurface,
          border: 'none',
        };
      default:
        return {};
    }
  };

  const getSizeStyles = () => {
    switch (size) {
      case 'sm': return { padding: '8px 16px', fontSize: '12px' };
      case 'md': return { padding: '12px 24px', fontSize: '14px' };
      case 'lg': return { padding: '16px 32px', fontSize: '16px' };
      default: return {};
    }
  };

  const styles = {
    ...getVariantStyles(),
    ...getSizeStyles(),
    borderRadius: '8px',
    cursor: disabled ? 'not-allowed' : 'pointer',
    opacity: disabled ? 0.5 : 1,
    transition: 'all 0.2s ease',
    transform: isPressed ? 'scale(0.98)' : 'scale(1)',
    boxShadow: isHovered && !disabled ? `0 4px 12px ${colors.primary}40` : 'none',
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: '8px',
  };

  return (
    <button
      style={styles}
      disabled={disabled || loading}
      onClick={onClick}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      onMouseDown={() => setIsPressed(true)}
      onMouseUp={() => setIsPressed(false)}
      {...props}
    >
      {loading && <BX3LoadingSpinner size="sm" />}
      {children}
    </button>
  );
};