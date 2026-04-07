import { LitElement, html, css, unsafeCSS } from 'lit';
import { customElement, property } from 'lit/decorators.js';
import { BX3ThemeController } from '../controllers/theme-controller';
import { defaultTheme } from '../themes/default';

@customElement('bx3-button')
export class BX3Button extends LitElement {
  static styles = css`
    :host {
      display: inline-flex;
    }
    
    .button {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      font-family: inherit;
      font-weight: 500;
      border: none;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.2s ease;
      outline: none;
    }
    
    .button:disabled {
      cursor: not-allowed;
      opacity: 0.5;
    }
    
    .button--primary {
      background: var(--bx3-primary, ${unsafeCSS(defaultTheme.colors.primary)});
      color: var(--bx3-on-primary, white);
    }
    
    .button--secondary {
      background: var(--bx3-surface, ${unsafeCSS(defaultTheme.colors.surface)});
      color: var(--bx3-on-surface, ${unsafeCSS(defaultTheme.colors.onSurface)});
      border: 1px solid var(--bx3-on-surface-variant, ${unsafeCSS(defaultTheme.colors.onSurfaceVariant)});
    }
    
    .button--ghost {
      background: transparent;
      color: var(--bx3-on-surface, ${unsafeCSS(defaultTheme.colors.onSurface)});
    }
    
    .button--sm {
      padding: 8px 16px;
      font-size: 12px;
    }
    
    .button--md {
      padding: 12px 24px;
      font-size: 14px;
    }
    
    .button--lg {
      padding: 16px 32px;
      font-size: 16px;
    }
    
    .button:hover:not(:disabled) {
      box-shadow: 0 4px 12px rgba(var(--bx3-primary-rgb, 123, 31, 162), 0.25);
    }
    
    .button:active:not(:disabled) {
      transform: scale(0.98);
    }
    
    .loading {
      width: 16px;
      height: 16px;
      border: 2px solid transparent;
      border-top-color: currentColor;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }
    
    @keyframes spin {
      to { transform: rotate(360deg); }
    }
    
    @media (prefers-reduced-motion: reduce) {
      .button {
        transition: none;
      }
      .loading {
        animation: none;
      }
    }
  `;

  @property({ type: String }) variant: 'primary' | 'secondary' | 'ghost' = 'primary';
  @property({ type: String }) size: 'sm' | 'md' | 'lg' = 'md';
  @property({ type: Boolean }) loading = false;
  @property({ type: Boolean }) disabled = false;
  
  private themeController = new BX3ThemeController(this);

  render() {
    return html`
      <button
        class="button button--${this.variant} button--${this.size}"
        ?disabled="${this.disabled || this.loading}"
        @click="${this._handleClick}"
      >
        ${this.loading ? html`<div class="loading"></div>` : ''}
        <slot></slot>
      </button>
    `;
  }

  private _handleClick(e: Event) {
    if (this.disabled || this.loading) {
      e.preventDefault();
      return;
    }
    
    this.dispatchEvent(new CustomEvent('bx3-click', {
      bubbles: true,
      composed: true,
      detail: { timestamp: Date.now() }
    }));
  }

  connectedCallback() {
    super.connectedCallback();
    this.setAttribute('role', 'button');
  }
}

declare global {
  interface HTMLElementTagNameMap {
    'bx3-button': BX3Button;
  }
}