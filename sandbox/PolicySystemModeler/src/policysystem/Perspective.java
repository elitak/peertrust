package policysystem;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import policysystem.views.FileSystemView;
import policysystem.views.MesgView;
import policysystem.views.PolicySystemGraphView;
import policysystem.views.PolicySystemView;
import policysystem.views.ResourcePolicyView;
import policysystem.views.WarningsView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		String editorArea = layout.getEditorArea();
		IFolderLayout left = 
			layout.createFolder(
						"left", IPageLayout.LEFT/*BOTTOM*/, 0.80f, editorArea);
		left.addView(PolicySystemView.ID);
		
//		IFolderLayout leftBottom = 
//			layout.createFolder("leftbottom", IPageLayout.BOTTOM, 0.2f, "left");
//		leftBottom.addView(FileSystemView.ID);
		
		IFolderLayout top = 
			layout.createFolder("top", IPageLayout.RIGHT, 0.2f, "left");
		//top.addView(PolicySystemGraphView.ID);
		top.addView(ResourcePolicyView.ID);
		top.addView(PolicySystemGraphView.ID);
		IFolderLayout leftTop = 
			layout.createFolder("topLeft", IPageLayout.RIGHT, 0.7f, "top");
		leftTop.addView(WarningsView.ID);
		
		IFolderLayout bottom = 
			layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, "top");
		bottom.addView(MesgView.ID);
		//bottom.addView(WarningsView.ID);
		IFolderLayout leftBottom = 
			layout.createFolder("leftbottom", IPageLayout.BOTTOM, 0.3f, "left");
		leftBottom.addView(FileSystemView.ID);
	}
	
	
}
