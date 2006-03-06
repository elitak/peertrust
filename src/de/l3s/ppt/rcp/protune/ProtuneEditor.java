package de.l3s.ppt.rcp.protune;

import org.eclipse.ui.texteditor.AbstractTextEditor;
import de.l3s.ppt.rcp.DocumentProvider;

public class ProtuneEditor extends AbstractTextEditor{
	private ProtuneColorManager colorManager;
	public ProtuneEditor() {
		super();
		// make sure we inherit all the text editing commands (delete line etc).
		setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" });  //$NON-NLS-1$
		internal_init();
	}

	/**
	 * Initializes the document provider and source viewer configuration.
	 * Called by the constructor. Subclasses may replace this method.
	 */
	protected void internal_init() {
		configureInsertMode(SMART_INSERT, false);
		colorManager = new ProtuneColorManager();
		setSourceViewerConfiguration(new ProtuneConfiguration(colorManager));
		setDocumentProvider(new DocumentProvider());
	}
}
