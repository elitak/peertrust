package policysystem.views;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;

import policysystem.ApplicationWorkbenchAdvisor;
import policysystem.Perspective;
import policysystem.model.PolicySystemRDFModel;
import policysystem.model.PolicySystemResTreeContentProvider;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class PolicySystemView extends ViewPart 
{
	static final public String ID="policysystem.views.PolicySystemView";
	static final public String POLICY_SYSTEM_RES_POLICIES="Policies";
	static final public String POLICY_SYSTEM_RES_RESOURCES="Resources";
	static final public String POLICY_SYSTEM_RES_ASSERTIONS="Assertions";
	static final public String POLICY_SYSTEM_RES_CONFLICT_RESOLUTION=
											"Inheritance Conflict Resolition";
	static final public String POLICY_SYSTEM_FILTERS="Filters";
	
	
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;

	
	class ViewLabelProvider extends LabelProvider {		
		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof String)
			{
				if(obj.equals(
						PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES))
				{
					imageKey = ISharedImages.IMG_OBJ_FOLDER;
				}
				else if(PolicySystemResTreeContentProvider.isPolicySystemRes((String)obj))
				{
					imageKey=ISharedImages.IMG_OBJ_ELEMENT;
				}
			}
			
			System.out.println("Platform.getInstanceLocation:"+
									Platform.getLocation());
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public PolicySystemView() 
	{
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) 
	{
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		//viewer.setContentProvider(new ViewContentProvider());
		viewer.setContentProvider(new PolicySystemResTreeContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		getSite().setSelectionProvider(viewer);
	
//		getSite().getPage().addSelectionListener(
//				PolicySystemGraphView.ID,
//				(ISelectionListener)this);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				PolicySystemView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				//showMessage("Action 1 executed");
				try {
					PolicySystemRDFModel psRDFModel=
						PolicySystemRDFModel.getInstance();
					String fileName=ApplicationWorkbenchAdvisor.rdfFileName+".bk";
					psRDFModel.saveTo(fileName);
				} catch (Exception e) {
					showMessage(e.getMessage());
				}
			}
		};
		action1.setText("save");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				String strRepr=(obj==null)?null:obj.toString();
				if(obj==null){
					return;
				}else if(POLICY_SYSTEM_RES_ASSERTIONS.equals(strRepr))
				{
					showMessage("Double-click detected on "+obj.toString());
					
				}
				else if(POLICY_SYSTEM_RES_CONFLICT_RESOLUTION.equals(strRepr))
				{
					showMessage("Double-click detected on "+strRepr);
				}
				else if(POLICY_SYSTEM_RES_POLICIES.equals(strRepr))
				{
					showMessage("Double-click detected on "+obj.toString());
				}
				else if(POLICY_SYSTEM_RES_RESOURCES.equals(strRepr))
				{
					//Workbench.getInstance().getViewRegistry().getViews()
					//PlatformUI.getWorkbench().
				}
			}
		};
	}

	private void hookDoubleClickAction() 
	{
		
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"PolicySystemView",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	//////////////////////////////////////////////////////////////////////
	public void setNewFile(String fileName)
	{
		System.out.println("FileName="+fileName);
	}
}