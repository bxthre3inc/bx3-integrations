<template>
  <button
    :class="[
      'bx3-button',
      `bx3-button--${variant}`,
      `bx3-button--${size}`,
      {
        'bx3-button--loading': loading,
        'bx3-button--disabled': disabled,
        'bx3-button--hovered': isHovered,
        'bx3-button--pressed': isPressed,
      },
    ]"
    :disabled="disabled || loading"
    @click="handleClick"
    @mouseenter="isHovered = true"
    @mouseleave="isHovered = false"
    @mousedown="isPressed = true"
    @mouseup="isPressed = false"
  >
    <BX3LoadingSpinner v-if="loading" size="sm" />
    <slot />
  </button>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useBX3Theme } from '../composables/useBX3Theme';
import BX3LoadingSpinner from './BX3LoadingSpinner.vue';
import type { BX3ButtonVariant, BX3ButtonSize } from '../types';

interface Props {
  variant?: BX3ButtonVariant;
  size?: BX3ButtonSize;
  loading?: boolean;
  disabled?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  variant: 'primary',
  size: 'md',
  loading: false,
  disabled: false,
});

const emit = defineEmits<{
  click: [event: MouseEvent];
}>();

const { colors, prefersReducedMotion } = useBX3Theme();

const isHovered = ref(false);
const isPressed = ref(false);

const handleClick = (event: MouseEvent) => {
  if (!props.disabled && !props.loading) {
    emit('click', event);
  }
};

const buttonStyles = computed(() => {
  const baseStyles = {
    borderRadius: '8px',
    cursor: props.disabled ? 'not-allowed' : 'pointer',
    opacity: props.disabled ? 0.5 : 1,
    transition: prefersReducedMotion.value ? 'none' : 'all 0.2s ease',
  };

  const variantStyles = {
    primary: {
      background: colors.value.primary,
      color: colors.value.onPrimary,
      border: 'none',
    },
    secondary: {
      background: colors.value.surface,
      color: colors.value.onSurface,
      border: `1px solid ${colors.value.onSurfaceVariant}`,
    },
    ghost: {
      background: 'transparent',
      color: colors.value.onSurface,
      border: 'none',
    },
  };

  const sizeStyles = {
    sm: { padding: '8px 16px', fontSize: '12px' },
    md: { padding: '12px 24px', fontSize: '14px' },
    lg: { padding: '16px 32px', fontSize: '16px' },
  };

  return {
    ...baseStyles,
    ...variantStyles[props.variant],
    ...sizeStyles[props.size],
  };
});
</script>

<style scoped>
.bx3-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-family: inherit;
  font-weight: 500;
  border: none;
  outline: none;
}

.bx3-button--hovered:not(.bx3-button--disabled) {
  box-shadow: 0 4px 12px rgba(123, 31, 162, 0.25);
}

.bx3-button--pressed:not(.bx3-button--disabled) {
  transform: scale(0.98);
}

.bx3-button--loading {
  cursor: wait;
}
</style>