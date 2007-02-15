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
import org.peertrust.pteditor.model.MyResource;

public class AddPolicyAction extends Action implements IWorkbenchAction,
		ISelectionListener {
	private final IWorkbenchWindow window;
	public static final String ID="org.peertrust.pteditor.addPolicy";
	private IStructuredSelection selection=null;

	public AddPolicyAction(IWorkbenchWindow window) {
		this.window=window;
		setId(ID);
		setText("Add P&olicy");
		setToolTipText("Adds a new policy");
		setImageDescriptor(MyImageRegistry.getImageRegistry().getDescriptor("add_policy"));
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
		IWorkbenchPage page=window.getActivePage();
		try {
			page.openEditor(new FileEditorInput(myres),ProtectFileEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
