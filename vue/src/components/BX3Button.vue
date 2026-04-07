<template>
  <button
    :class="buttonClasses"
    :disabled="disabled || loading"
    @click="handleClick"
    @keydown.enter.space="handleClick"
  >
    <span v-if="loading" class="bx3-button__spinner" aria-hidden="true" />
    <span :class="{ 'bx3-button__text--invisible': loading }">
      <slot />
    </span>
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useBX3Theme } from '../composables/useBX3Theme';
import { useAccessibility } from '../composables/useAccessibility';

export type ButtonVariant = 'primary' | 'secondary' | 'ghost' | 'danger';
export type ButtonSize = 'small' | 'medium' | 'large';

export interface BX3ButtonProps {
  variant?: ButtonVariant;
  size?: ButtonSize;
  disabled?: boolean;
  loading?: boolean;
  fullWidth?: boolean;
}

const props = withDefaults(defineProps<BX3ButtonProps>(), {
  variant: 'primary',
  size: 'medium',
  disabled: false,
  loading: false,
  fullWidth: false,
});

const emit = defineEmits<{
  click: [event: MouseEvent];
}>();

const { colors, reduceMotion } = useBX3Theme();
const { screenReaderEnabled } = useAccessibility();

const buttonClasses = computed(() => [
  'bx3-button',
  `bx3-button--${props.variant}`,
  `bx3-button--${props.size}`,
  {
    'bx3-button--disabled': props.disabled || props.loading,
    'bx3-button--loading': props.loading,
    'bx3-button--full-width': props.fullWidth,
    'bx3-button--screen-reader': screenReaderEnabled.value,
  },
]);

const handleClick = (event: MouseEvent) => {
  if (!props.disabled && !props.loading) {
    emit('click', event);
  }
};
</script>

<style scoped>
.bx3-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border: none;
  border-radius: 8px;
  font-family: system-ui, -apple-system, sans-serif;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  overflow: hidden;
}

.bx3-button:focus-visible {
  outline: 2px solid currentColor;
  outline-offset: 2px;
}

.bx3-button--small {
  padding: 8px 12px;
  font-size: 14px;
  min-height: 32px;
}

.bx3-button--medium {
  padding: 12px 16px;
  font-size: 16px;
  min-height: 40px;
}

.bx3-button--large {
  padding: 16px 24px;
  font-size: 18px;
  min-height: 48px;
}

.bx3-button--primary {
  background: v-bind('colors.primary');
  color: v-bind('colors.onPrimary');
}

.bx3-button--secondary {
  background: v-bind('colors.surface');
  color: v-bind('colors.onSurface');
  border: 1px solid v-bind('colors.surfaceVariant');
}

.bx3-button--ghost {
  background: transparent;
  color: v-bind('colors.primary');
}

.bx3-button--danger {
  background: v-bind('colors.error');
  color: v-bind('colors.onPrimary');
}

.bx3-button--disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.bx3-button--full-width {
  width: 100%;
}

.bx3-button--screen-reader {
  min-height: 44px;
}

.bx3-button__spinner {
  width: 16px;
  height: 16px;
  border: 2px solid currentColor;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.bx3-button__text--invisible {
  opacity: 0;
}
</style>
