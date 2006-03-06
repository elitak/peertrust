package de.l3s.ppt.rcp;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

//		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f,
//			editorArea);
//		topLeft.addView(IPageLayout.ID_RES_NAV);
//		topLeft.addPlaceholder(IPageLayout.ID_BOOKMARKS);

		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.75f,
			editorArea);
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
	}
}
