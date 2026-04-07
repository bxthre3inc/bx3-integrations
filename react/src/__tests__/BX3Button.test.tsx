import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BX3Button } from '../components/BX3Button';
import { BX3ThemeProvider } from '../hooks/useBX3Theme';

const renderWithTheme = (ui: React.ReactElement) => {
  return render(<BX3ThemeProvider defaultTheme="zospace">{ui}</BX3ThemeProvider>);
};

describe('BX3Button', () => {
  test('renders button with text', () => {
    renderWithTheme(<BX3Button>Click me</BX3Button>);
    expect(screen.getByRole('button', { name: /click me/i })).toBeInTheDocument();
  });

  test('handles click events', async () => {
    const handleClick = jest.fn();
    renderWithTheme(<BX3Button onClick={handleClick}>Click</BX3Button>);
    
    fireEvent.click(screen.getByRole('button'));
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  test('disabled button does not respond to clicks', () => {
    const handleClick = jest.fn();
    renderWithTheme(<BX3Button onClick={handleClick} disabled>Disabled</BX3Button>);
    
    fireEvent.click(screen.getByRole('button'));
    expect(handleClick).not.toHaveBeenCalled();
    expect(screen.getByRole('button')).toBeDisabled();
  });

  test('loading state shows spinner and disables button', () => {
    renderWithTheme(<BX3Button loading>Loading</BX3Button>);
    
    const button = screen.getByRole('button');
    expect(button).toBeDisabled();
    expect(button).toHaveAttribute('aria-busy', 'true');
    // Spinner should be present
    expect(button.querySelector('.bx3-spinner')).toBeInTheDocument();
  });

  test('applies aria-label when provided', () => {
    renderWithTheme(<BX3Button ariaLabel="Close dialog">X</BX3Button>);
    expect(screen.getByLabelText(/close dialog/i)).toBeInTheDocument();
  });

  test('supports different variants', () => {
    const { rerender } = renderWithTheme(<BX3Button variant="primary">Primary</BX3Button>);
    expect(screen.getByRole('button')).toHaveAttribute('data-variant', 'primary');
    
    rerender(<BX3ThemeProvider defaultTheme="zospace"><BX3Button variant="secondary">Secondary</BX3Button></BX3ThemeProvider>);
    expect(screen.getByRole('button')).toHaveAttribute('data-variant', 'secondary');
    
    rerender(<BX3ThemeProvider defaultTheme="zospace"><BX3Button variant="ghost">Ghost</BX3Button></BX3ThemeProvider>);
    expect(screen.getByRole('button')).toHaveAttribute('data-variant', 'ghost');
  });

  test('supports different sizes', () => {
    const { rerender } = renderWithTheme(<BX3Button size="small">Small</BX3Button>);
    expect(screen.getByRole('button')).toHaveAttribute('data-size', 'small');
    
    rerender(<BX3ThemeProvider defaultTheme="zospace"><BX3Button size="large">Large</BX3Button></BX3ThemeProvider>);
    expect(screen.getByRole('button')).toHaveAttribute('data-size', 'large');
  });

  test('full width applies correct style', () => {
    renderWithTheme(<BX3Button fullWidth>Full</BX3Button>);
    const button = screen.getByRole('button');
    expect(button.style.width).toBe('100%');
  });

  test('button is keyboard accessible', () => {
    renderWithTheme(<BX3Button>Accessible</BX3Button>);
    const button = screen.getByRole('button');
    
    button.focus();
    expect(button).toHaveFocus();
    
    fireEvent.keyDown(button, { key: 'Enter' });
    fireEvent.keyUp(button, { key: 'Enter' });
  });
});

describe('BX3Button Theme Integration', () => {
  test('renders with correct theme colors', () => {
    renderWithTheme(<BX3Button>Theme Test</BX3Button>);
    const button = screen.getByRole('button');
    expect(button).toHaveAttribute('data-theme', 'zospace');
  });
});
