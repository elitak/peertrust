package org.peertrust.pteditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.peertrust.pteditor.model.MyPolicy;
import org.peertrust.pteditor.model.MyResource;

public class IgnorePolicyAction extends Action implements IWorkbenchAction,
	ISelectionListener {

	private final IWorkbenchWindow window;
	public static final String ID="org.peertrust.pteditor.ignorePolicy";
	private IStructuredSelection selectionResource=null;
	private IStructuredSelection selectionPolicy=null;

	public IgnorePolicyAction(IWorkbenchWindow window) {
		super("&Ignore Policy",IAction.AS_CHECK_BOX);
		this.window=window;
		setId(ID);
		setToolTipText("Ignores a policy");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		window.getSelectionService().addSelectionListener(this);
		setEnabled(false);
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection sel=(IStructuredSelection)selection;
			if(sel.size()==1) {
				if(sel.getFirstElement() instanceof MyResource)
					selectionResource=sel;
				else if(sel.getFirstElement() instanceof MyPolicy) {
					if((((MyPolicy)sel.getFirstElement()).getDefault())&&
						(!((MyPolicy)sel.getFirstElement()).getOwner().equals(
						selectionResource.getFirstElement()))&&(selectionResource!=null)) {
						selectionPolicy=sel;
						MyPolicy policy=(MyPolicy)selectionPolicy.getFirstElement();
						setChecked((policy.getExcResources()!=null)&&
							(policy.getExcResources().contains(selectionResource.getFirstElement())));
					}
					else
						selectionPolicy=null;
				}
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
		MyResource res=policy.getOwner();
		if(res.equals(myres))
			return;
		if((policy.getExcResources()!=null)&&(policy.getExcResources().contains(myres)))
			policy.removeExcResource(myres);
		else
			policy.addExcResource(myres);
	}
	
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
