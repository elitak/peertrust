package org.peertrust.pteditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.peertrust.pteditor.model.MyResource;

public class NewSiblingClassAction extends Action implements IWorkbenchAction,
		ISelectionListener {
	private final IWorkbenchWindow window;
	public static final String ID="org.peertrust.pteditor.addPolicy";
	private IStructuredSelection selection=null;

	public NewSiblingClassAction(IWorkbenchWindow window) {
		this.window=window;
		setId(ID);
		setText("New S&ibling class");
		setToolTipText("Add a new sibling class");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		window.getSelectionService().addSelectionListener(this);
		setEnabled(false);
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if((selection instanceof IStructuredSelection)&&
			(((IStructuredSelection)selection).size()==1)&&
			(((IStructuredSelection)selection).getFirstElement() instanceof MyResource))
			this.selection=(IStructuredSelection)selection;
		setEnabled((this.selection!=null)&&(this.selection.size()==1)&&
			(this.selection.getFirstElement() instanceof MyResource)&&
			(((MyResource)this.selection.getFirstElement()).getParent()!=null));
	}

	public void run() {
		Object item=selection.getFirstElement();
		if((item==null)||(!(item instanceof MyResource)))
			return;
		MyResource myres=(MyResource)item;
		InputDialog dialog=new InputDialog(window.getShell(),null,"Name of the subclass","",null);
		dialog.open();
		String name=dialog.getValue();
		if((name!=null)&&(name.length()>0)&&(myres.getParent()!=null))
			new MyResource(name,myres.getParent(),MyResource.TYPE_CLASS);
	}
	
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
