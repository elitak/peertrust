package org.peertrust.pteditor.views;

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.part.ViewPart;
import org.peertrust.pteditor.MyImageRegistry;
import org.peertrust.pteditor.actions.AddPolicyAction;
import org.peertrust.pteditor.console.PTErrorConsole;
import org.peertrust.pteditor.model.IMyResourceChangedListener;
import org.peertrust.pteditor.model.MyResource;

public class SelectFileView extends ViewPart {
	public static final String ID="PTEditor.view.selectfile";
	private MyResource root=new MyResource("root",null,MyResource.TYPE_ROOT);
	private IMyResourceChangedListener listenerMyResource=new IMyResourceChangedListener() {
		public void notifyResourceChanged(MyResource res) {
			treeviewerFiles.refresh();
			treeviewerFiles.expandToLevel(2);
		}
	};
	
	class MyResourceContentTreeProvider implements ITreeContentProvider {
		public Object[] getChildren(Object parentElement) {
			List<MyResource> kids=((MyResource)parentElement).getChildren();
		    return kids == null ? new Object[0] : kids.toArray(new MyResource[0]);
	    }

		public Object getParent(Object element) {
			return ((MyResource)element).getParent();
		}

		public boolean hasChildren(Object element) {
			return (getChildren(element).length>0);
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	public class FileTreeLabelProvider extends LabelProvider {
		public String getText(Object element) {
			return ((MyResource)element).toString();
		}
		
		public Image getImage(Object element) {
			int nType=((MyResource)element).getType();
			if(nType==MyResource.TYPE_ROOT)
				return MyImageRegistry.getImageRegistry().get("root");
			else if(nType==MyResource.TYPE_FILE_ROOT)
				return MyImageRegistry.getImageRegistry().get("file_root");
			else if(nType==MyResource.TYPE_DIRECTORY)
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			else if(nType==MyResource.TYPE_FILE)
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			return null;
		}
		
	}
	
	private TreeViewer treeviewerFiles;

	public SelectFileView() {
		super();
	}

	public void createPartControl(Composite parent) {
		new MyResource("file roots",root,MyResource.TYPE_ROOT);
		treeviewerFiles=new TreeViewer(parent,SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
		getSite().setSelectionProvider(treeviewerFiles);
		treeviewerFiles.setContentProvider(new MyResourceContentTreeProvider());
		treeviewerFiles.setLabelProvider(new FileTreeLabelProvider());
		treeviewerFiles.setInput(root);
treeviewerFiles.addDoubleClickListener(new IDoubleClickListener() {

public void doubleClick(DoubleClickEvent event) {
((PTErrorConsole)ConsolePlugin.getDefault().getConsoleManager().getConsoles()[0]).
showErrors(MyResource.checkFileHierarchy(root));
}
	
});//23BF87
		MenuManager menumgr=new MenuManager("selectFilePopup");
		menumgr.add(new AddPolicyAction(getSite().getWorkbenchWindow()));
		menumgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		Menu menu=menumgr.createContextMenu(treeviewerFiles.getControl());
		treeviewerFiles.getControl().setMenu(menu);
		getSite().registerContextMenu(menumgr,treeviewerFiles);
		root.addMyResourceListener(listenerMyResource);
		setFocus();
	}

	public void setFocus() {
		treeviewerFiles.getControl().setFocus();
		treeviewerFiles.setSelection(new StructuredSelection(root.getChildren().get(0)));
	}
	
	public void dispose() {
		root.removeMyResourceListener(listenerMyResource);
	}
}
