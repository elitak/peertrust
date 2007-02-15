package org.peertrust.pteditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.peertrust.pteditor.MyImageRegistry;
import org.peertrust.pteditor.editors.FileEditorInput;
import org.peertrust.pteditor.editors.ProtectFileEditor;
import org.peertrust.pteditor.model.MyPolicy;
import org.peertrust.pteditor.model.MyResource;

public class EditPolicyAction extends Action implements IWorkbenchAction,
	ISelectionListener {

	private final IWorkbenchWindow window;
	public static final String ID="org.peertrust.pteditor.editPolicy";
	private IStructuredSelection selectionResource=null;
	private IStructuredSelection selectionPolicy=null;

	public EditPolicyAction(IWorkbenchWindow window) {
		this.window=window;
		setId(ID);
		setText("&Edit Policy");
		setToolTipText("Edit a policy");
		setImageDescriptor(MyImageRegistry.getImageRegistry().getDescriptor("edit_policy"));
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
myres.getAllPolicies();
		item=selectionPolicy.getFirstElement();
		if((item==null)||(!(item instanceof MyPolicy)))
			return;
		MyPolicy policy=(MyPolicy)item;
		MyResource res=myres;
		if(!policy.getOwner().equals(res))
			res=policy.getOwner();
		if(!res.getPolicies().contains(policy))
			return;
		IWorkbenchPage page=window.getActivePage();
		try {
			page.openEditor(new FileEditorInput(res,policy),ProtectFileEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
