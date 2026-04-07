import type { Meta, StoryObj } from '@storybook/react';
import { BX3Button } from './BX3Button';
import { BX3ThemeProvider } from '../hooks/useBX3Theme';

const meta: Meta<typeof BX3Button> = {
  title: 'Atoms/BX3Button',
  component: BX3Button,
  decorators: [
    (Story) => (
      <BX3ThemeProvider defaultTheme="zospace">
        <div style={{ padding: '24px', background: '#0A0A0F' }}>
          <Story />
        </div>
      </BX3ThemeProvider>
    ),
  ],
  argTypes: {
    variant: {
      control: 'select',
      options: ['primary', 'secondary', 'ghost', 'danger'],
    },
    size: {
      control: 'select',
      options: ['small', 'medium', 'large'],
    },
    disabled: { control: 'boolean' },
    loading: { control: 'boolean' },
    fullWidth: { control: 'boolean' },
  },
  parameters: {
    a11y: {
      config: {
        rules: [
          { id: 'button-name', enabled: true },
          { id: 'color-contrast', enabled: true },
        ],
      },
    },
  },
};

export default meta;
type Story = StoryObj<typeof BX3Button>;

export const Primary: Story = {
  args: {
    children: 'Primary Button',
    variant: 'primary',
  },
};

export const Secondary: Story = {
  args: {
    children: 'Secondary Button',
    variant: 'secondary',
  },
};

export const Ghost: Story = {
  args: {
    children: 'Ghost Button',
    variant: 'ghost',
  },
};

export const Danger: Story = {
  args: {
    children: 'Danger Action',
    variant: 'danger',
  },
};

export const Small: Story = {
  args: {
    children: 'Small Button',
    size: 'small',
  },
};

export const Large: Story = {
  args: {
    children: 'Large Button',
    size: 'large',
  },
};

export const Loading: Story = {
  args: {
    children: 'Loading...',
    loading: true,
  },
};

export const Disabled: Story = {
  args: {
    children: 'Disabled',
    disabled: true,
  },
};

export const FullWidth: Story = {
  args: {
    children: 'Full Width Button',
    fullWidth: true,
  },
};

export const AllVariants: Story = {
  render: () => (
    <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
      <BX3Button variant="primary">Primary</BX3Button>
      <BX3Button variant="secondary">Secondary</BX3Button>
      <BX3Button variant="ghost">Ghost</BX3Button>
      <BX3Button variant="danger">Danger</BX3Button>
    </div>
  ),
};

export const AllSizes: Story = {
  render: () => (
    <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
      <BX3Button size="small">Small</BX3Button>
      <BX3Button size="medium">Medium</BX3Button>
      <BX3Button size="large">Large</BX3Button>
    </div>
  ),
};
