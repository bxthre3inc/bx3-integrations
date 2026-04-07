import * as vscode from 'vscode';
import { ComponentProvider } from './componentProvider';
import { ThemeGenerator } from './themeGenerator';
import { FigmaSync } from './figmaSync';

export function activate(context: vscode.ExtensionContext) {
  const componentProvider = new ComponentProvider();
  const themeGenerator = new ThemeGenerator();
  const figmaSync = new FigmaSync();

  // Register tree data provider for component explorer
  vscode.window.registerTreeDataProvider('bx3Components', componentProvider);

  // Register commands
  context.subscriptions.push(
    vscode.commands.registerCommand('bx3.insertComponent', async () => {
      const editor = vscode.window.activeTextEditor;
      if (!editor) {
        vscode.window.showErrorMessage('No active editor');
        return;
      }

      // Show component picker
      const components = await componentProvider.getAllComponents();
      const selected = await vscode.window.showQuickPick(
        components.map(c => ({
          label: c.name,
          description: c.category,
          detail: c.description,
          component: c,
        })),
        { placeHolder: 'Select a BX3 component to insert' }
      );

      if (selected) {
        const snippet = componentProvider.generateSnippet(selected.component, editor.document.languageId);
        editor.insertSnippet(new vscode.SnippetString(snippet));
        
        if (vscode.workspace.getConfiguration('bx3').get('autoImport')) {
          await componentProvider.addImport(selected.component, editor);
        }
      }
    }),

    vscode.commands.registerCommand('bx3.openDocs', () => {
      vscode.env.openExternal(vscode.Uri.parse('https://github.com/bxthre3inc/bx3-design-android'));
    }),

    vscode.commands.registerCommand('bx3.generateTheme', async () => {
      const theme = vscode.workspace.getConfiguration('bx3').get('theme') as string;
      const generated = themeGenerator.generate(theme);
      
      const doc = await vscode.workspace.openTextDocument({
        content: generated,
        language: 'json',
      });
      await vscode.window.showTextDocument(doc);
    }),

    vscode.commands.registerCommand('bx3.syncWithFigma', async () => {
      const fileKey = vscode.workspace.getConfiguration('bx3').get('figmaFileKey') as string;
      if (!fileKey) {
        vscode.window.showErrorMessage('Please set bx3.figmaFileKey in settings');
        return;
      }

      vscode.window.withProgress(
        { location: vscode.ProgressLocation.Notification, title: 'Syncing with Figma...' },
        async () => {
          await figmaSync.sync(fileKey);
          vscode.window.showInformationMessage('Figma sync complete!');
        }
      );
    }),

    vscode.commands.registerCommand('bx3.previewTheme', () => {
      const panel = vscode.window.createWebviewPanel(
        'bx3ThemePreview',
        'BX3 Theme Preview',
        vscode.ViewColumn.Two,
        { enableScripts: true }
      );

      const theme = vscode.workspace.getConfiguration('bx3').get('theme') as string;
      panel.webview.html = themeGenerator.generatePreview(theme);
    })
  );

  // Auto-completion provider
  context.subscriptions.push(
    vscode.languages.registerCompletionItemProvider(
      ['typescript', 'typescriptreact', 'vue', 'kotlin'],
      {
        provideCompletionItems(document, position) {
          return componentProvider.provideCompletions(document.languageId);
        },
      },
      'BX3'
    )
  );
}

export function deactivate() {}