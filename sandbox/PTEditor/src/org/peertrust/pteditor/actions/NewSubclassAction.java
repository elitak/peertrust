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

public class NewSubclassAction extends Action implements IWorkbenchAction,
		ISelectionListener {
	private final IWorkbenchWindow window;
	public static final String ID="org.peertrust.pteditor.addPolicy";
	private IStructuredSelection selection=null;

	public NewSubclassAction(IWorkbenchWindow window) {
		this.window=window;
		setId(ID);
		setText("New &Subclass");
		setToolTipText("Add a new subclass");
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
			(this.selection.getFirstElement() instanceof MyResource));
	}

	public void run() {
		Object item=selection.getFirstElement();
		if((item==null)||(!(item instanceof MyResource)))
			return;
		MyResource myres=(MyResource)item;
		InputDialog dialog=new InputDialog(window.getShell(),null,"Name of the subclass","",null);
		dialog.open();
		String name=dialog.getValue();
		if((name!=null)&&(name.length()>0))
			new MyResource(name,myres,MyResource.TYPE_CLASS);
	}
	
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
