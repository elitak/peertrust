package org.peertrust.pteditor;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.peertrust.pteditor.views.InheritedPoliciesView;
import org.peertrust.pteditor.views.PolicyView;
import org.peertrust.pteditor.views.SelectCredentialView;

public class CredentialPerspective implements IPerspectiveFactory {
	public static final String ID="org.peertrust.pteditor.perspective.credential";
	
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.addView(SelectCredentialView.ID,IPageLayout.LEFT,0.3f,
			layout.getEditorArea());
		layout.addView(InheritedPoliciesView.ID,IPageLayout.RIGHT,
				0.8f,layout.getEditorArea());
		layout.addView(PolicyView.ID,IPageLayout.TOP,0.2f,
			layout.getEditorArea());
		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW,
			IPageLayout.BOTTOM,0.7f,layout.getEditorArea());
		layout.addPerspectiveShortcut(FilePerspective.ID);
	}

}
