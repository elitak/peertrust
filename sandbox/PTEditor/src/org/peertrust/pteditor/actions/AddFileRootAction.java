package org.peertrust.pteditor.actions;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.peertrust.pteditor.model.MyResource;

public class AddFileRootAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window=null;
	public static final String ID="org.peertrust.pteditor.addFileRoot";
	private IStructuredSelection selection=null;

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.window=window;
	}
	
	public void run(IAction action) {
		DirectoryDialog dialog=new DirectoryDialog(window.getShell());
		dialog.setMessage("File root auswählen");
		final String strDir=dialog.open();
		if(strDir!=null) {
//			try {
//				PlatformUI.getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {
//
//					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						if((selection.getFirstElement()==null)||(!(selection.getFirstElement() instanceof MyResource)))
							return;
						MyResource myres=(MyResource)selection.getFirstElement();
						while(myres.getParent()!=null)
							myres=myres.getParent();
						myres=myres.getChildren().get(0);
						File fileDir,files[];
						List<MyResource> list=new LinkedList<MyResource>();
						MyResource res=new MyResource(strDir,myres,MyResource.TYPE_FILE_ROOT);
						myres.addChild(res);
						list.add(res);
						while(!list.isEmpty()) {
							res=list.remove(0);
							fileDir=new File(res.getName());
							files=fileDir.listFiles();
							MyResource newres;
							for(int i=0;i<files.length;i++) {
								newres=new MyResource(files[i].getAbsolutePath(),res);
								res.addChild(newres);
								if(files[i].isDirectory())
									list.add(newres);
							}
						}
//					}
//				});
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			this.selection=(IStructuredSelection)selection;
			action.setEnabled((this.selection.size()==1)&&
				(this.selection.getFirstElement() instanceof MyResource));
		}
		else
			action.setEnabled(false);
	}

//	public AddFileRootAction(IWorkbenchWindow window) {
//		this.window=window;
//		setId(ID);
//		setText("A&dd file root");
//		setToolTipText("Adds a file root to the file view");
//		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
//		window.getSelectionService().addSelectionListener(this);
//		setEnabled(false);
//	}
//	
//	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
//		if(selection instanceof IStructuredSelection) {
//			this.selection=(IStructuredSelection)selection;
//			setEnabled((this.selection.size()==1)&&
//				(this.selection.getFirstElement() instanceof MyResource));
//		}
//		else
//			setEnabled(false);
//	}
//
//	public void dispose() {
//		window.getSelectionService().removeSelectionListener(this);
//	}
//
//	public void run() {
//		DirectoryDialog dialog=new DirectoryDialog(window.getShell());
//		dialog.setMessage("File root auswählen");
//		String strDir=dialog.open();
//		if(strDir!=null) {
//			if(selection.getFirstElement()==null)
//				return;
//			MyResource myres=(MyResource)selection.getFirstElement();
//			while(myres.getParent()!=null)
//				myres=myres.getParent();
//			myres=myres.getChildren().get(0);
//			File fileDir,files[];
//			List<MyResource> list=new LinkedList<MyResource>();
//			MyResource res=new MyResource(strDir,myres);
//			myres.addToChildren(res);
//			list.add(res);
//			while(!list.isEmpty()) {
//				res=list.remove(0);
//				fileDir=new File(res.getName());
//				files=fileDir.listFiles();
//				MyResource newres;
//				for(int i=0;i<files.length;i++) {
//					newres=new MyResource(files[i].getAbsolutePath(),myres);
//					res.addToChildren(newres);
//					if(files[i].isDirectory())
//						list.add(newres);
//				}
//			}
//		}
//	}
}
