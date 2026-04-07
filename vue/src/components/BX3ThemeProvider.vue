<template>
  <div class="bx3-theme-provider" :data-theme="theme">
    <slot />
  </div>
</template>

<script setup lang="ts">
import { provideBX3Theme, type BX3Theme } from '../composables/useBX3Theme';

const props = defineProps<{
  defaultTheme?: BX3Theme;
}>();

provideBX3Theme(props.defaultTheme || 'zospace');
</script>

<style>
.bx3-theme-provider {
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  line-height: 1.5;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.bx3-theme-provider[data-theme="zospace"] {
  --bx3-primary: #7B1FA2;
  --bx3-on-primary: #FFFFFF;
}

.bx3-theme-provider[data-theme="agentos"] {
  --bx3-primary: #1B5E20;
  --bx3-on-primary: #FFFFFF;
}

.bx3-theme-provider[data-theme="vpc"] {
  --bx3-primary: #004D40;
  --bx3-on-primary: #FFFFFF;
}

.bx3-theme-provider[data-theme="irrig8"] {
  --bx3-primary: #1565C0;
  --bx3-on-primary: #FFFFFF;
}
</style>
