package org.peertrust.pteditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.peertrust.pteditor.dialog.LoadCredentialsDialog;
import org.peertrust.pteditor.model.MyResource;

public class LoadCredentialsAction extends Action implements IWorkbenchAction,
		ISelectionListener {
	private final IWorkbenchWindow window;
	public static final String ID="org.peertrust.pteditor.addPolicy";
	private IStructuredSelection selection=null;

	public LoadCredentialsAction(IWorkbenchWindow window) {
		this.window=window;
		setId(ID);
		setText("&Load Credentials");
		setToolTipText("Loads and adds new credentials");
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
		LoadCredentialsDialog dialog=new LoadCredentialsDialog(window.getShell());
		if(dialog.open()==IDialogConstants.OK_ID) {
			MyResource resources[]=dialog.getCredentials();
			for(int i=0;i<resources.length;i++)
				resources[i].setParent(myres);
		}
	}
	
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
