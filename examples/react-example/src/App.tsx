import { useState } from 'react';
import { BX3Button, BX3ThemeProvider, useBX3Theme } from '@bxthre3/bx3-react';

function ThemeSelector() {
  const { theme, setTheme } = useBX3Theme();
  
  const themes: Array<'zospace' | 'agentos' | 'vpc' | 'irrig8'> = ['zospace', 'agentos', 'vpc', 'irrig8'];
  
  return (
    <div style={{ marginBottom: '24px' }}>
      <h2 style={{ color: '#E0E0E0', marginBottom: '16px' }}>Select Theme</h2>
      <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
        {themes.map((t) => (
          <BX3Button
            key={t}
            variant={theme === t ? 'primary' : 'secondary'}
            onClick={() => setTheme(t)}
          >
            {t.charAt(0).toUpperCase() + t.slice(1)}
          </BX3Button>
        ))}
      </div>
    </div>
  );
}

function VariantShowcase() {
  const [clicked, setClicked] = useState<string | null>(null);
  
  return (
    <div style={{ marginBottom: '24px' }}>
      <h2 style={{ color: '#E0E0E0', marginBottom: '16px' }}>Button Variants</h2>
      <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
        <BX3Button onClick={() => setClicked('Primary')}>Primary</BX3Button>
        <BX3Button variant="secondary" onClick={() => setClicked('Secondary')}>Secondary</BX3Button>
        <BX3Button variant="ghost" onClick={() => setClicked('Ghost')}>Ghost</BX3Button>
        <BX3Button variant="danger" onClick={() => setClicked('Danger')}>Danger</BX3Button>
      </div>
      {clicked && (
        <p style={{ color: '#7EE787', marginTop: '16px' }}>Clicked: {clicked}</p>
      )}
    </div>
  );
}

function SizeShowcase() {
  return (
    <div style={{ marginBottom: '24px' }}>
      <h2 style={{ color: '#E0E0E0', marginBottom: '16px' }}>Button Sizes</h2>
      <div style={{ display: 'flex', gap: '16px', alignItems: 'center', flexWrap: 'wrap' }}>
        <BX3Button size="small">Small</BX3Button>
        <BX3Button size="medium">Medium</BX3Button>
        <BX3Button size="large">Large</BX3Button>
      </div>
    </div>
  );
}

function StateShowcase() {
  const [isLoading, setIsLoading] = useState(false);
  
  const handleLoadingClick = () => {
    setIsLoading(true);
    setTimeout(() => setIsLoading(false), 2000);
  };
  
  return (
    <div style={{ marginBottom: '24px' }}>
      <h2 style={{ color: '#E0E0E0', marginBottom: '16px' }}>Button States</h2>
      <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
        <BX3Button loading={isLoading} onClick={handleLoadingClick}>
          {isLoading ? 'Loading...' : 'Click to Load'}
        </BX3Button>
        <BX3Button disabled>Disabled</BX3Button>
        <BX3Button fullWidth style={{ marginTop: '16px' }}>Full Width Button</BX3Button>
      </div>
    </div>
  );
}

function AccessibilityDemo() {
  return (
    <div style={{ marginBottom: '24px' }}>
      <h2 style={{ color: '#E0E0E0', marginBottom: '16px' }}>Accessibility</h2>
      <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
        <BX3Button ariaLabel="Close modal dialog">✕</BX3Button>
        <BX3Button variant="secondary">Keyboard Accessible (Tab)</BX3Button>
        <BX3Button variant="ghost">Screen Reader Ready</BX3Button>
      </div>
      <p style={{ color: '#B0B0C0', marginTop: '16px', fontSize: '14px' }}>
        Try: Tab navigation, Enter/Space activation, screen reader announcement
      </p>
    </div>
  );
}

function App() {
  return (
    <BX3ThemeProvider defaultTheme="zospace">
      <div style={{ 
        minHeight: '100vh', 
        backgroundColor: '#0A0A0F',
        padding: '32px',
        fontFamily: 'system-ui, -apple-system, sans-serif'
      }}>
        <h1 style={{ 
          color: '#E0E0E0', 
          marginBottom: '32px',
          fontSize: '32px',
          fontWeight: 600
        }}>
          BX3 Design System — React Example
        </h1>
        
        <ThemeSelector />
        <VariantShowcase />
        <SizeShowcase />
        <StateShowcase />
        <AccessibilityDemo />
        
        <div style={{ 
          marginTop: '48px', 
          paddingTop: '24px', 
          borderTop: '1px solid #2D2D44',
          color: '#B0B0C0',
          fontSize: '14px'
        }}>
          <p>Repository: github.com/bxthre3inc/bx3-integrations</p>
          <p>License: MIT (Core) | Commercial (Premium Components)</p>
        </div>
      </div>
    </BX3ThemeProvider>
  );
}

export default App;
