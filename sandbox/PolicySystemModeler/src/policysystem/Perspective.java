package policysystem;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import policysystem.views.PSResourceView;
import policysystem.views.MesgView;
import policysystem.views.PolicySystemGraphView;
import policysystem.views.PolicySystemView;
import policysystem.views.ResourcePolicyView;
import policysystem.views.WarningsView;

public class Perspective implements IPerspectiveFactory {

//	public void createInitialLayout(IPageLayout layout) {
//		layout.setEditorAreaVisible(false);
//		String editorArea = layout.getEditorArea();
//		IFolderLayout left = 
//			layout.createFolder(
//						"left", IPageLayout.LEFT/*BOTTOM*/, 0.80f, editorArea);
//		left.addView(PolicySystemView.ID);
//		IFolderLayout bottom = 
//			layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, "top");
//		bottom.addView(MesgView.ID);
////		IFolderLayout leftBottom = 
////			layout.createFolder("leftbottom", IPageLayout.BOTTOM, 0.2f, "left");
////		leftBottom.addView(FileSystemView.ID);
//		
//		IFolderLayout top = 
//			layout.createFolder("top", IPageLayout.RIGHT, 0.2f, "left");
//		//top.addView(PolicySystemGraphView.ID);
//		top.addView(ResourcePolicyView.ID);
//		top.addView(PolicySystemGraphView.ID);
//		IFolderLayout leftTop = 
//			layout.createFolder("topLeft", IPageLayout.RIGHT, 0.7f, "top");
//		leftTop.addView(WarningsView.ID);
//		
////		IFolderLayout bottom = 
////			layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, "top");
////		bottom.addView(MesgView.ID);
//		//bottom.addView(WarningsView.ID);
//		IFolderLayout leftBottom = 
//			layout.createFolder("leftbottom", IPageLayout.BOTTOM, 0.3f, "left");
//		leftBottom.addView(FileSystemView.ID);
//	}
	
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		String editorArea = layout.getEditorArea();
		///left
		IFolderLayout left = 
			layout.createFolder(
						"left", IPageLayout.LEFT/*BOTTOM*/, 0.20f, editorArea);
		left.addView(PolicySystemView.ID);
		IFolderLayout leftBottom = 
		layout.createFolder("leftbottom", IPageLayout.BOTTOM, 0.3f, "left");
		leftBottom.addView(PSResourceView.ID);
		
		///Center
		IFolderLayout centerBottom = 
			layout.createFolder(
					"centerBottom", IPageLayout.RIGHT, 0.8f, 
					IPageLayout.ID_EDITOR_AREA);
		centerBottom.addView(MesgView.ID);
		
		IFolderLayout centerTopLeft = 
			layout.createFolder(
					"centerTopLeft", IPageLayout.TOP, 0.8f, "centerBottom");
		centerTopLeft.addView(PolicySystemGraphView.ID);
		centerTopLeft.addView(ResourcePolicyView.ID);
		IFolderLayout centerTopRight = 
			layout.createFolder(
					"centerTopRight", IPageLayout.RIGHT, 0.8f, "centerTopLeft");
		centerTopRight.addView(WarningsView.ID);
		
//		IFolderLayout top = 
//			layout.createFolder("top", IPageLayout.RIGHT, 0.2f, "left");
//		//top.addView(PolicySystemGraphView.ID);
//		top.addView(ResourcePolicyView.ID);
//		top.addView(PolicySystemGraphView.ID);
//		IFolderLayout leftTop = 
//			layout.createFolder("topLeft", IPageLayout.RIGHT, 0.7f, "top");
//		leftTop.addView(WarningsView.ID);
//		
////		IFolderLayout bottom = 
////			layout.createFolder("bottom", IPageLayout.BOTTOM, 0.8f, "top");
////		bottom.addView(MesgView.ID);
//		//bottom.addView(WarningsView.ID);
//		IFolderLayout leftBottom = 
//			layout.createFolder("leftbottom", IPageLayout.BOTTOM, 0.3f, "left");
//		leftBottom.addView(FileSystemView.ID);
	}
}
