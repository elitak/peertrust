package org.peertrust.pteditor;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.peertrust.pteditor.views.InheritedPoliciesView;
import org.peertrust.pteditor.views.PolicyView;
import org.peertrust.pteditor.views.SelectFileView;

public class FilePerspective implements IPerspectiveFactory {
	public static final String ID="org.peertrust.pteditor.perspective.file";
	
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		boolean bTitle=true;
		layout.addActionSet("PTEditor.actionSet.SelectFile");
		layout.addStandaloneView(SelectFileView.ID,bTitle,IPageLayout.LEFT,0.3f,
			layout.getEditorArea());
		layout.addStandaloneView(InheritedPoliciesView.ID,bTitle,IPageLayout.RIGHT,
				0.8f,layout.getEditorArea());
		layout.addStandaloneView(PolicyView.ID,bTitle,IPageLayout.TOP,0.2f,
			layout.getEditorArea());
		layout.addStandaloneView(IConsoleConstants.ID_CONSOLE_VIEW,bTitle,
			IPageLayout.BOTTOM,0.7f,layout.getEditorArea());
		layout.addPerspectiveShortcut(CredentialPerspective.ID);
	}
}
