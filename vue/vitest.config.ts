import { defineConfig } from 'vitest/config';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  test: {
    environment: 'happy-dom',
    globals: true,
    coverage: {
      reporter: ['text', 'html', 'lcov'],
      branches: 70,
      functions: 70,
      lines: 70,
      statements: 70,
    },
  },
});
