package egraphviztool.graphvizeditor;

import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class ColorPickerDialog extends Dialog {
	private String selectedColor;
	private Map<String, RGB> colorMapping;
	private IEditorPart activeEditorPart;

	public ColorPickerDialog(Shell parent, Map<String, RGB> map) {
		super(parent);
		colorMapping = map;
		activeEditorPart = null;
	}

	public void setEditor(IEditorPart editorPart) {
		activeEditorPart = editorPart;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(30, true);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.horizontalSpan = 30;
		final Text textTitle = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		textTitle.setLayoutData(gridData);

		for (final Map.Entry<String, RGB> entry : colorMapping.entrySet()) {
			Text text = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
			text.setText(" ");
			text.setBackground(new Color(text.getDisplay(), entry.getValue()));
			text.addMouseListener(new MouseAdapter() {
				public void mouseUp(MouseEvent e) {
					selectedColor = entry.getKey();
					textTitle.setText(selectedColor);
				}
			});
		}

		return composite;
	}

	@Override
	protected void okPressed() {
		if (activeEditorPart != null && activeEditorPart instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor) activeEditorPart;
			IDocumentProvider provider = editor.getDocumentProvider();
			IDocument doc = provider.getDocument(editor.getEditorInput());

			ISelectionProvider selectionProvider = editor.getSelectionProvider();
			ISelection selection = selectionProvider.getSelection();
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				int offset = textSelection.getOffset();
				try {
					doc.replace(offset, 0, selectedColor);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}

		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		selectedColor = "";
		super.cancelPressed();
	}
}
