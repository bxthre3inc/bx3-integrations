import * as vscode from 'vscode';

export interface BX3Component {
  id: string;
  name: string;
  category: string;
  description: string;
  platforms: string[];
  props: Array<{ name: string; type: string; required: boolean; default?: any }>;
}

export class ComponentProvider implements vscode.TreeDataProvider<vscode.TreeItem> {
  private components: BX3Component[] = [
    {
      id: 'bx3-button',
      name: 'BX3Button',
      category: 'Atom',
      description: 'Primary action component with adaptive variants',
      platforms: ['react', 'vue', 'android'],
      props: [
        { name: 'variant', type: 'primary | secondary | ghost', required: false, default: 'primary' },
        { name: 'size', type: 'sm | md | lg', required: false, default: 'md' },
        { name: 'loading', type: 'boolean', required: false, default: false },
        { name: 'onClick', type: '() => void', required: false },
      ],
    },
    {
      id: 'bx3-card',
      name: 'BX3Card',
      category: 'Molecule',
      description: 'Container with temporal awareness and urgency encoding',
      platforms: ['react', 'vue', 'android'],
      props: [
        { name: 'elevation', type: '0 | 1 | 2 | 4 | 8', required: false, default: 1 },
        { name: 'urgency', type: 'high | medium | low | none', required: false, default: 'none' },
        { name: 'timestamp', type: 'number', required: false },
      ],
    },
    {
      id: 'bx3-ai-widget',
      name: 'BX3AIWidget',
      category: 'Organism',
      description: 'Spatially-attached AI interface with context ingestion',
      platforms: ['react', 'vue', 'android'],
      props: [
        { name: 'contextSnapshot', type: 'string', required: true },
        { name: 'onPrompt', type: '(prompt: string) => void', required: true },
        { name: 'suggestedPrompts', type: 'SuggestedPrompt[]', required: false },
      ],
    },
    {
      id: 'bx3-predictive-input',
      name: 'BX3PredictiveInput',
      category: 'Organism',
      description: 'Intent-aware text input with AI suggestions (patent-pending)',
      platforms: ['react', 'vue', 'android'],
      props: [
        { name: 'value', type: 'string', required: true },
        { name: 'onValueChange', type: '(value: string) => void', required: true },
        { name: 'predictionContext', type: 'string[]', required: false },
        { name: 'onPredictionAccept', type: '(prediction: string) => void', required: false },
      ],
    },
    {
      id: 'bx3-kanban',
      name: 'BX3KanbanBoard',
      category: 'Organism',
      description: 'Draggable task board with spatial prediction (patent-pending)',
      platforms: ['react', 'vue'],
      props: [
        { name: 'columns', type: 'KanbanColumn[]', required: true },
        { name: 'onDragEnd', type: '(result: DragResult) => void', required: true },
        { name: 'wipLimits', type: 'Record<string, number>', required: false },
      ],
    },
    {
      id: 'bx3-virtualized-list',
      name: 'BX3VirtualizedList',
      category: 'Pattern',
      description: 'Windowed rendering for 10K+ items (patent-pending)',
      platforms: ['react', 'vue', 'android'],
      props: [
        { name: 'items', type: 'T[]', required: true },
        { name: 'renderItem', type: '(item: T, index: number) => ReactNode', required: true },
        { name: 'itemHeight', type: 'number', required: true },
        { name: 'bufferSize', type: 'number', required: false, default: 5 },
      ],
    },
  ];

  getTreeItem(element: vscode.TreeItem): vscode.TreeItem {
    return element;
  }

  getChildren(): vscode.TreeItem[] {
    return this.components.map(c => {
      const item = new vscode.TreeItem(c.name, vscode.TreeItemCollapsibleState.None);
      item.description = c.category;
      item.tooltip = `${c.name}\n${c.description}\nPlatforms: ${c.platforms.join(', ')}`;
      item.command = {
        command: 'bx3.insertComponent',
        title: 'Insert Component',
        arguments: [c],
      };
      return item;
    });
  }

  async getAllComponents(): Promise<BX3Component[]> {
    return this.components;
  }

  generateSnippet(component: BX3Component, language: string): string {
    switch (language) {
      case 'typescriptreact':
        return this.generateReactSnippet(component);
      case 'vue':
        return this.generateVueSnippet(component);
      case 'kotlin':
        return this.generateKotlinSnippet(component);
      default:
        return '';
    }
  }

  private generateReactSnippet(component: BX3Component): string {
    const props = component.props
      .filter(p => p.required)
      .map(p => `  ${p.name}={${this.getDefaultValue(p)}}`)
      .join('\n');

    return `<${component.name}
${props}
>
  {/* content */}
</${component.name}>`;
  }

  private generateVueSnippet(component: BX3Component): string {
    const props = component.props
      .filter(p => p.required)
      .map(p => `:${p.name}="${this.getDefaultValue(p)}"`)
      .join(' ');

    return `<${component.name.replace(/([A-Z])/g, '-$1').toLowerCase().substring(1)} ${props}>
  <!-- content -->
</${component.name.replace(/([A-Z])/g, '-$1').toLowerCase().substring(1)}>`;
  }

  private generateKotlinSnippet(component: BX3Component): string {
    const props = component.props
      .filter(p => p.required)
      .map(p => `${p.name} = ${this.getKotlinDefault(p)}`)
      .join(',\n  ');

    return `${component.name}(
  ${props}
)`;
  }

  private getDefaultValue(prop: { type: string; default?: any }): string {
    if (prop.default !== undefined) return JSON.stringify(prop.default);
    if (prop.type === 'boolean') return 'false';
    if (prop.type === 'number') return '0';
    if (prop.type === 'string') return '""';
    return 'null';
  }

  private getKotlinDefault(prop: { type: string; default?: any }): string {
    if (prop.default !== undefined) return prop.default.toString();
    if (prop.type === 'boolean') return 'false';
    if (prop.type === 'number') return '0';
    if (prop.type === 'string') return '""';
    return 'Unit';
  }

  async addImport(component: BX3Component, editor: vscode.TextEditor): Promise<void> {
    const importStatement = this.getImportStatement(component, editor.document.languageId);
    
    // Find the best place to insert import
    const document = editor.document;
    const firstLine = document.lineAt(0);
    
    await editor.edit(editBuilder => {
      editBuilder.insert(firstLine.range.start, importStatement + '\n');
    });
  }

  private getImportStatement(component: BX3Component, language: string): string {
    switch (language) {
      case 'typescriptreact':
        return `import { ${component.name} } from '@bxthre3/bx3-react';`;
      case 'vue':
        return `import { ${component.name} } from '@bxthre3/bx3-vue';`;
      case 'kotlin':
        return `import com.bxthre3.design.${component.name}`;
      default:
        return '';
    }
  }

  provideCompletions(language: string): vscode.CompletionItem[] {
    return this.components.map(c => {
      const item = new vscode.CompletionItem(c.name, vscode.CompletionItemKind.Class);
      item.detail = `${c.category} - ${c.platforms.join(', ')}`;
      item.documentation = new vscode.MarkdownString(c.description);
      item.insertText = new vscode.SnippetString(this.generateSnippet(c, language));
      return item;
    });
  }
}