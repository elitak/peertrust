package org.peertrust.pteditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.peertrust.pteditor.MyImageRegistry;
import org.peertrust.pteditor.model.MyPolicy;
import org.peertrust.pteditor.model.MyResource;

public class RemovePolicyAction extends Action implements IWorkbenchAction,
		ISelectionListener {

	private final IWorkbenchWindow window;
	public static final String ID="org.peertrust.pteditor.removePolicy";
	private IStructuredSelection selectionResource=null;
	private IStructuredSelection selectionPolicy=null;

	public RemovePolicyAction(IWorkbenchWindow window) {
		this.window=window;
		setId(ID);
		setText("&Remove Policy");
		setToolTipText("Removes a policy");
		setImageDescriptor(MyImageRegistry.getImageRegistry().getDescriptor("remove_policy"));
		window.getSelectionService().addSelectionListener(this);
		setEnabled(false);
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection sel=(IStructuredSelection)selection;
			if(sel.size()==1) {
				if(sel.getFirstElement() instanceof MyResource)
					selectionResource=sel;
				else if(sel.getFirstElement() instanceof MyPolicy)
					selectionPolicy=sel;
			}
		}
		else
			selectionPolicy=selectionResource=null;
		setEnabled((selectionResource!=null)&&(selectionPolicy!=null));
	}
	
	public void run() {
		Object item=selectionResource.getFirstElement();
		if((item==null)||(!(item instanceof MyResource)))
			return;
		MyResource myres=(MyResource)item;
		item=selectionPolicy.getFirstElement();
		if((item==null)||(!(item instanceof MyPolicy)))
			return;
		MyPolicy policy=(MyPolicy)item;
		if(!myres.getPolicies().contains(policy))
			return;
		if(MessageDialog.openQuestion(window.getShell(),"Question","Do you really want to remove the selected policy?")) {
			myres.removePolicy(policy);
			selectionPolicy=null;
		}
	}
	
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
