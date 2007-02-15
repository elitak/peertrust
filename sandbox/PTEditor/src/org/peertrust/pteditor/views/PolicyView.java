package org.peertrust.pteditor.views;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.peertrust.pteditor.MyImageRegistry;
import org.peertrust.pteditor.actions.AddPolicyAction;
import org.peertrust.pteditor.actions.EditPolicyAction;
import org.peertrust.pteditor.actions.RemovePolicyAction;
import org.peertrust.pteditor.model.IMyResourceChangedListener;
import org.peertrust.pteditor.model.MyPolicy;
import org.peertrust.pteditor.model.MyResource;

public class PolicyView extends ViewPart {
	private MyResource myres=null;
	private EditPolicyAction actionPolicyEdit=null;
	
	private IMyResourceChangedListener listenerMyResource=new IMyResourceChangedListener() {
		public void notifyResourceChanged(MyResource res) {
			if(!myres.equals(res))
				return;
			showPolicies();
		}
	};
	
	private ISelectionListener listenerSelection=new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if(!(selection instanceof IStructuredSelection))
				return;
			IStructuredSelection sel=(IStructuredSelection)selection;
			if((sel.size()==1)&&(sel.getFirstElement()!=null)&&
				(sel.getFirstElement() instanceof MyResource)) {
				if(myres!=null)
					myres.removeMyResourceListener(listenerMyResource);
				myres=(MyResource)sel.getFirstElement();
				showPolicies();
				myres.addMyResourceListener(listenerMyResource);
			}
		}
	};
	
	private IStructuredContentProvider contentproviderTable=new IStructuredContentProvider() {

		public Object[] getElements(Object inputElement) {
			List<Object> list=new LinkedList<Object>();
			if(!(inputElement instanceof MyResource))
				return new Object[0];
			MyResource res=(MyResource)inputElement;
			for(int i=0;i<res.getPolicies().size();i++)
				list.add(res.getPolicies().get(i));
			return list.isEmpty() ? new Object[0] : list.toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
	};
	
	private ITableLabelProvider labelproviderTable=new ITableLabelProvider() {
		
		public Image getColumnImage(Object element, int columnIndex) {
			if((columnIndex!=0)||(!(element instanceof MyPolicy)))
				return null;
			MyPolicy policy=(MyPolicy)element;
			if(policy.getDefault())
				return MyImageRegistry.getImageRegistry().get("policy_default");
			else
				return MyImageRegistry.getImageRegistry().get("policy_mandatory");
		}

		public String getColumnText(Object element, int columnIndex) {
			return element.toString();
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
		
	};
	
	public static final String ID="PTEditor.view.policies"; 
	
	private TableViewer tableviewerCredentials;

	public PolicyView() {
		super();
	}

	public void createPartControl(Composite parent) {
		actionPolicyEdit=new EditPolicyAction(getSite().getWorkbenchWindow());
		tableviewerCredentials=new TableViewer(parent);
		tableviewerCredentials.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				actionPolicyEdit.run();
			}
			
		});
		getSite().setSelectionProvider(tableviewerCredentials);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listenerSelection);
		
		tableviewerCredentials.setContentProvider(contentproviderTable);
		tableviewerCredentials.setLabelProvider(labelproviderTable);

		MenuManager menumgr=new MenuManager("policyViewPopup");
		menumgr.add(new AddPolicyAction(getSite().getWorkbenchWindow()));
		menumgr.add(new EditPolicyAction(getSite().getWorkbenchWindow()));
		menumgr.add(new RemovePolicyAction(getSite().getWorkbenchWindow()));
		menumgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		Menu menu=menumgr.createContextMenu(tableviewerCredentials.getControl());
		tableviewerCredentials.getControl().setMenu(menu);
		getSite().registerContextMenu(menumgr,tableviewerCredentials);
	}

	public void setFocus() {
		tableviewerCredentials.getControl().setFocus();
	}
	
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listenerSelection);
	}

	private void showPolicies() {
		tableviewerCredentials.setInput(myres);
	}
}