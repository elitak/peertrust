package org.peertrust.modeler.policysystem.views;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.PageLayout;
import org.eclipse.ui.internal.layout.CellLayout;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.PageBook.PageBookLayout;
import org.peertrust.modeler.policysystem.ApplicationWorkbenchAdvisor;
import org.peertrust.modeler.policysystem.control.NewProjectDlgEditor;
import org.peertrust.modeler.policysystem.control.PSFilterEditorPage;
import org.peertrust.modeler.policysystem.control.PSHierarchyVisualizationPage;
import org.peertrust.modeler.policysystem.control.PSOverriddingRuleEditorPage;
import org.peertrust.modeler.policysystem.control.PSPolicyEditorPage;
import org.peertrust.modeler.policysystem.control.PSResourcePolicyEditorPage;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.ResourcePolicyContentProvider;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;



public class ResourcePolicyView extends ViewPart 
				implements ISelectionListener
{
	static final public String ID="org.peertrust.modeler.policysystem.ResourcePolicyView";
	
	private TableViewer localPolicyView;
	
	private TableViewer inheritedPolcyView;
	
	private NewProjectDlgEditor pjtEd;
	private Page blankPage;
	private PSHierarchyVisualizationPage resPolViz;
	private PSOverriddingRuleEditorPage overriddingRuleEditorPage;
	
	private Action addAction;
	private Action removeAction;
	private Logger logger;
	private PageBook pageBook;
	private PSHierarchyVisualizationPage vizPage;
	private PSPolicyEditorPage policyEditorPage;
	private PSResourcePolicyEditorPage resourcePolicyEditorPage;
	private PSFilterEditorPage filterEditorPage;
	
	public ResourcePolicyView() 
	{
		super();
		logger=Logger.getLogger(ResourcePolicyView.class);
	}

	public void createPartControl(Composite parent) 
	{
		Layout l;
		pageBook= new PageBook(parent,SWT.NONE);
		
		localPolicyView= 
			new TableViewer(pageBook,
							SWT.BORDER|SWT.FULL_SELECTION);
		inheritedPolcyView= new TableViewer(pageBook);
		makeTable();
		makeActions();
		//localPolicyView.getControl()
		//hookContextMenu();
		//hookDoubleClickAction();
		contributeToActionBars();
		getSite().getPage().addSelectionListener(
				PSResourceView.ID,
				(ISelectionListener)this);
		getSite().getPage().addSelectionListener(
				PolicySystemView.ID,
				(ISelectionListener)this);
		pjtEd=new NewProjectDlgEditor();
		pjtEd.createControl(pageBook);
		
		resPolViz= new PSHierarchyVisualizationPage();
		resPolViz.createControl(pageBook);
		
		vizPage= new PSHierarchyVisualizationPage();
		vizPage.createControl(pageBook);
		
		blankPage=createBlankViewPage();
		blankPage.createControl(pageBook);
		
		policyEditorPage= new PSPolicyEditorPage();
		policyEditorPage.createControl(pageBook);
		
		overriddingRuleEditorPage=
			new PSOverriddingRuleEditorPage();
		overriddingRuleEditorPage.createControl(pageBook);
		//pageBook.showPage(blankPage.getControl());
		//pageBook.showPage(overriddingRuleEditorPage.getControl());
		resourcePolicyEditorPage=
			new PSResourcePolicyEditorPage();
		resourcePolicyEditorPage.createControl(pageBook);
		
		filterEditorPage= new PSFilterEditorPage();
		filterEditorPage.createControl(pageBook);
		
		pageBook.showPage(resourcePolicyEditorPage.getControl());
		////
		PageBookView pbv;
		Viewer v;
		MenuBarCreator.createMenubar(
				//getViewSite().getPart(),
				this.getViewSite(),
				(IDoubleClickListener)null,
				new Action[]{addAction,removeAction},
				null,//pageBook,//treeView,
				localPolicyView.getControl(),//.treeView.getControl(),
				(ISelectionProvider)null,//treeView,
				"");
	}

	public void setFocus() 
	{
		
	}

	//////////////////////////////////////////////////////////////////////////
	//////////////////////table///////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	private void makeTable()
	{
		ResourcePolicyContentProvider provider=
				new ResourcePolicyContentProvider();
		localPolicyView.setContentProvider(provider);
		localPolicyView.setLabelProvider(provider);
		TableLayout layout= new TableLayout();
		layout.addColumnData(new ColumnWeightData(33,true));
		layout.addColumnData(new ColumnWeightData(33,true));
		layout.addColumnData(new ColumnWeightData(34,true));
		
		Table table=localPolicyView.getTable();
		table.setLayout(layout);
		TableColumn nameC=
				new TableColumn(
						table,
						SWT.CENTER,
						0);
		nameC.setText(PolicySystemRDFModel.LNAME_PROP_HAS_NAME);
		
		TableColumn valueC=
			new TableColumn(
					table,
					SWT.CENTER,
					1);
		valueC.setText(PolicySystemRDFModel.LNAME_PROP_HAS_VALUE);
		
		TableColumn filterC=
			new TableColumn(
					table,
					SWT.CENTER,
					2);
		filterC.setText(PolicySystemRDFModel.LNAME_PROP_HAS_FILTER);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);		
		localPolicyView.setInput(PolicySystemRDFModel.LNAME_PROP_HAS_NAME);
		
	}
	
	///////////////////////////////////////////////////////////////////////////
	/////////////////////ACTIONS///////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	private void contributeToActionBars() 
	{
		IActionBars bars = getViewSite().getActionBars();
		//fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	private void fillLocalPullDown(IMenuManager manager) 
	{
		manager.add(addAction);
		manager.add(new Separator());
		manager.add(removeAction);
	}

	private void fillContextMenu(IMenuManager manager) 
	{
		manager.add(addAction);
		manager.add(removeAction);
		manager.add(new Separator());
//		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addAction);
		manager.add(removeAction);
		manager.add(new Separator());
//		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		addAction = new Action() {
			public void run() {
				//showMessage("Action 1 executed");
//				try {
//					PolicySystemRDFModel psRDFModel=
//						PolicySystemRDFModel.getInstance();
//					String fileName=ApplicationWorkbenchAdvisor.rdfFileName+".bk";
//					psRDFModel.saveTo(fileName);
//				} catch (Exception e) {
//					showMessage(e.getMessage());
//				}
			}
		};
		addAction.setText("save");
		addAction.setToolTipText("Action 1 tooltip");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		removeAction = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		removeAction.setText("Action 2");
		removeAction.setToolTipText("Action 2 tooltip");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
//		doubleClickAction = new Action() {
//			public void run() {
//				ISelection selection = viewer.getSelection();
//				Object obj = ((IStructuredSelection)selection).getFirstElement();
//				String strRepr=(obj==null)?null:obj.toString();
//				if(obj==null){
//					return;
//				}else if(POLICY_SYSTEM_RES_ASSERTIONS.equals(strRepr))
//				{
//					showMessage("Double-click detected on "+obj.toString());
//					
//				}
//				else if(POLICY_SYSTEM_RES_CONFLICT_RESOLUTION.equals(strRepr))
//				{
//					showMessage("Double-click detected on "+strRepr);
//				}
//				else if(POLICY_SYSTEM_RES_POLICIES.equals(strRepr))
//				{
//					showMessage("Double-click detected on "+obj.toString());
//				}
//				else if(POLICY_SYSTEM_RES_RESOURCES.equals(strRepr))
//				{
//					//Workbench.getInstance().getViewRegistry().getViews()
//					//PlatformUI.getWorkbench().
//				}
//			}
//		};
	}
	
	private void showMessage(String message) {
		MessageDialog.openInformation(
			this.getSite().getShell(),//viewer.getControl().getShell(),
			"PolicySystemView",
			message);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		
		if(selection==null)
		{
			logger.warn("param selection is null");
			return;
		}
		
		Object sel0=((StructuredSelection)selection).getFirstElement();
		
		if(sel0==null)
		{
			logger.warn("selection first element is null");
			return;
		}
		
		logger.info("==>Selection changed:"+
				"\n\tpart="+part +
				"\n\tselection="+selection +
				"\n\tselectionclass:"+sel0.getClass());
		
		try {
			
			
			if(part instanceof PolicySystemView)
			{
				pageBook.showPage(blankPage.getControl());
			}
			else if(sel0 instanceof PSPolicy)
			{			
					
					if(policyEditorPage!=null)
					{
						pageBook.showPage(policyEditorPage.getControl());
						policyEditorPage.setPsPolicy(
								(PSPolicy)sel0);
					}
			}
			else if(sel0 instanceof PSOverridingRule)
			{
				pageBook.showPage(overriddingRuleEditorPage.getControl());
				overriddingRuleEditorPage.setOverrindingRule(
						(PSOverridingRule)sel0);
			}
			else if(sel0 instanceof PSFilter)
			{
				pageBook.showPage(filterEditorPage.getControl());
				filterEditorPage.setPSFilter((PSFilter)sel0);
			}
			else if(sel0 instanceof File)
			{
				File selFile= (File)sel0;
//				if(selFile.isFile())
//				{
//					int decision=askYesNoQuestion(
//						"You have selected a file not a directory.\n"+
//						"Do you want show the parent directory",
//						part.getSite().getShell());
//					if(decision==SWT.YES)
//					{
//						selFile=selFile.getParentFile();
//					}
//					else
//					{
//						return;
//					}
//				}
				
//			localPolicyView.setInput(selFile);
//			pageBook.showPage(localPolicyView.getControl());
				System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCC");
				resourcePolicyEditorPage.setInput(selFile);
				pageBook.showPage(resourcePolicyEditorPage.getControl());
			}
			else if(sel0 instanceof PSResource)
			{
				resourcePolicyEditorPage.setInput(sel0);
				pageBook.showPage(resourcePolicyEditorPage.getControl());
			}
			else
			{
				//logger.warn("Cannoet handle selection of this class:"+sel0.getClass());
				pageBook.showPage(blankPage.getControl());
			}
		} 
		catch (Exception e) 
		{
			logger.warn("could nor change view according to selection:",e);
		}
		
		return;
	}
	
	/**
	 * Ask a yes no question to the user.
	 * 
	 * @param message 
	 * @param parent
	 * @return  
	 */
	static final int askYesNoQuestion(String message, Shell parent)
	{
		int ret=SWT.CANCEL;
		if(message==null || parent==null)
		{
			throw new IllegalArgumentException(
					"params must not be null: message= "+message +" parent="+parent);
		}
		MessageBox mb=
				new MessageBox(parent, SWT.YES|SWT.NO|SWT.CANCEL);
		mb.setMessage(message);
		ret=mb.open();
		return ret;
	}
////////////////////////////////////////////////////////////////////////////////
//////////////////page book/////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	public static final Page createBlankViewPage()
	{
		return new Page()
		{
			Composite c;
			
			public void createControl(Composite parent) 
			{
				c= new Composite(parent,SWT.NONE);
			}

			public Control getControl() 
			{
				return c;
			}

			public void setFocus() 
			{	
			}
			
		};
	}
	
}

