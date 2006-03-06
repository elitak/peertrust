package de.l3s.ppt.rcp;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import de.l3s.ppt.rcp.DocumentProvider;
public class DefaultEditor extends AbstractDecoratedTextEditor{
	public DefaultEditor() {
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
		setDocumentProvider(new DocumentProvider());
	}
}
