package org.peertrust.modeler.policysystem.control;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.jmx.LayoutDynamicMBean;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.Page;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.ResourcePolicyContentProvider;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEvent;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEventListener;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;
import org.peertrust.modeler.policysystem.views.ModelObjectArrayViewContentProvider;

/**
 * Provides a Page to edit a PSResource Policy protection
 * 
 * @author pat_dev
 *
 */
public class PSResourcePolicyEditorPage 
							extends Page 
							implements IDoubleClickListener	
{
	private final static String PSRESOURCE_LABEL_PREFIX="Resource: ";
	/** main container composite*/
	private Composite composite;
	
	/** used to show the local Policies*/
	private TableViewer localPolicyView;
	
	/** a label for showing the identity of the ps resource*/
	private Label psResourceLabel;
	
	private ToolBar toolBar;
	private ToolBarManager toolBarMng;
	
	/** the logger for the PSResourcePolicyEditorPage class*/
	private Logger logger=
		Logger.getLogger(PSResourcePolicyEditorPage.class);
	
	/**
	 * The tree view to show the model resource overrridding rules
	 */
	TreeViewer oRulesTree;

	/**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) 
	{
		
		composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new GridLayout());
//		composite.setLayout(new FormLayout());
//		
//		Composite top= new Composite(composite,SWT.NONE);
//		Composite tableComposite= new Composite(composite,SWT.NONE);
//		final int HEIGHT=30;
//		
//
//		
//		FormData tFD=new FormData();
//		tFD.top=new FormAttachment(0,HEIGHT+5);//0,7);//,10,SWT.BOTTOM);
//		tFD.left= new FormAttachment(0);
//		tFD.right= new FormAttachment(100);
//		tFD.bottom= new FormAttachment(100);
//		tableComposite.setLayoutData(tFD);
//		
//		FormData formData= new FormData();
//		formData.top= new FormAttachment(0,0);
//		formData.left= new FormAttachment(0,0);
//		formData.right= new FormAttachment(100);
//		formData.height= HEIGHT;//new FormAttachment(5);
//		top.setLayoutData(formData);
		
//		///table Composite 
//		tableComposite.setLayout(new GridLayout());
		psResourceLabel= 
			new Label(
					composite,//tableComposite,
					SWT.LEFT|SWT.WRAP|SWT.HORIZONTAL);
		psResourceLabel.setText(PSRESOURCE_LABEL_PREFIX);
		psResourceLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label separator=new Label(
				composite,//tableComposite,
				SWT.LEFT|SWT.WRAP|SWT.SEPARATOR|SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		
//		localPolicyView=makeTable(tableComposite);
		//toolBar=makeActions(top);
		
//		Composite folderComposite= new Composite(tableComposite,SWT.NONE);
//		folderComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
//		folderComposite.setLayout(new GridLayout());
		TabFolder tabFolder= 
						new TabFolder(
								composite,//tableComposite,
								SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		TabItem tableItem= new TabItem(tabFolder,SWT.NONE);
		tableItem.setText("Policies and Filters");
		Composite policyViewComposite=makePoliciesFiltersView(tabFolder);
		tableItem.setControl(policyViewComposite);//localPolicyView.getControl());
		
		TabItem oRuleItem= new TabItem(tabFolder,SWT.NONE);
		oRuleItem.setText("Overridding Rules");
//		Button button= new Button(tabFolder,SWT.NONE);		
//		oRuleItem.setControl(button);
//		button.setText("DADADADADADADADADAD");
		Composite oRuleViewComposite=
			makeORuleView(tabFolder);
		oRuleItem.setControl(oRuleViewComposite);
		
//		Button but= new Button(tableComposite,SWT.NONE);
//		but.setText("dididdidididid");
//		but.setLayoutData(new GridData(GridData.FILL_BOTH));
		
//		PolicySystemRDFModel rdfModel=
//			PolicySystemRDFModel.getInstance();
//		rdfModel.addPSModelChangeEventListener(this);
	}

	public Control getControl() 
	{
		return composite;
	}

	public void setFocus() 
	{
		
	}
	
	

	/**
	 * @see org.eclipse.jface.viewers.ContentViewer#setInput(java.lang.Object)
	 */
	public void setInput(Object input) 
	{
		
		logger.info("input set:"+input);
		try {
			localPolicyView.setInput(input);
//		PSResource psResource=
//			PolicySystemRDFModel.getInstance().getResource(input,false));
			if(input instanceof File)
			{
				String identity=((File)input).getCanonicalPath();
				PSResource psResource=
					PolicySystemRDFModel.getInstance().getResource(
										identity,true,null);
				oRulesTree.setInput(psResource);
				psResourceLabel.setText(
						PSRESOURCE_LABEL_PREFIX+
						identity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception while setting new input:"+input);
		}
		
		
	
	}

	private Composite makePoliciesFiltersView(Composite parent)
	{
		
		///layout
		Composite policiesTab=
					new Composite(parent,SWT.NONE);
		policiesTab.setLayout(new GridLayout());
		
		//Composite top= new Composite(policiesTab,SWT.NONE);
		//top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		toolBar=makePoliciesFiltersActions(policiesTab);
		toolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
//		parent.setLayout(new GridLayout());
		////make table
		TableViewer tv= 
			new TableViewer(policiesTab,SWT.FILL|SWT.BORDER|SWT.FULL_SELECTION);		
		GridData gData= 
			new GridData(
					GridData.FILL_BOTH);
		tv.getControl().setLayoutData(gData);
		
		//data
		ResourcePolicyContentProvider provider=
				new ResourcePolicyContentProvider();
		tv.setContentProvider(provider);
		tv.setLabelProvider(provider);
		TableLayout layout= new TableLayout();
		layout.addColumnData(new ColumnWeightData(33,true));
		layout.addColumnData(new ColumnWeightData(33,true));
		layout.addColumnData(new ColumnWeightData(34,true));
		
		Table table=tv.getTable();
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
	
		tv.setInput(PolicySystemRDFModel.LNAME_PROP_HAS_NAME);
		
		//
//		ISelectionChangedListener sl=null;
//		tv.addSelectionChangedListener(sl);
//		IDoubleClickListener dl;
		tv.addDoubleClickListener(this);
		//return tv;
		this.localPolicyView=tv;
		return policiesTab;
	}
	
	private ToolBar makePoliciesFiltersActions(Composite parent) 
	{
		///manager
		toolBarMng= new ToolBarManager(toolBar);
		///
		Action addAction = new Action() {
			public void run() 
			{
				IStructuredSelection sel=
					(IStructuredSelection)localPolicyView.getSelection();
				System.out.println("Sell:"+sel.getFirstElement());
			}
		};
		addAction.setText("save");
		addAction.setToolTipText("Action 1 tooltip");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		toolBarMng.add(addAction);
		
		Action removeAction = new Action() {
			public void run() {
				
			}
		};
		
		removeAction.setText("Action 2");
		removeAction.setToolTipText("Action 2 tooltip");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		toolBarMng.add(removeAction);
		
		parent.setLayout(new GridLayout());
		GridData gd= new GridData(GridData.FILL_BOTH);
		ToolBar tb= toolBarMng.createControl(parent);
		tb.setLayoutData(gd);
		return tb;
	}
	
	
	private Composite makeORuleView(Composite parent)
	{
		
		///layout
		Composite oRulesTab=
					new Composite(parent,SWT.NONE);
		oRulesTab.setLayout(new GridLayout());
		ToolBar toolBar=makeORuleActions(oRulesTab);
		toolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GridData gData= 
			new GridData(
					GridData.FILL_BOTH);
//		Button but= 
//			new Button(oRulesTab,SWT.FILL|SWT.BORDER|SWT.FULL_SELECTION);		
//		but.setLayoutData(gData);
		
		oRulesTree= new TreeViewer(oRulesTab);
		oRulesTree.getControl().setLayoutData(gData);
		ModelObjectArrayViewContentProvider provider=
				new ModelObjectArrayViewContentProvider(
										null,
										PSOverridingRule.class);
		oRulesTree.setContentProvider(provider);
		////
		oRulesTree.addDoubleClickListener(this);
		////
		return oRulesTab;
	}
	
	private ToolBar makeORuleActions(Composite parent) 
	{
		///manager
		ToolBarManager toolBarMng= new ToolBarManager(null);
		///
		Action addAction = new Action() {
			public void run() 
			{
				IStructuredSelection sel=
					(IStructuredSelection)localPolicyView.getSelection();
				System.out.println("Sell:"+sel.getFirstElement());
				
			}
		};
		addAction.setText("save");
		addAction.setToolTipText("Action 1 tooltip");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		toolBarMng.add(addAction);
		
		Action removeAction = new Action() {
			public void run() {
				
			}
		};
		
		removeAction.setText("Action 2");
		removeAction.setToolTipText("Action 2 tooltip");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		toolBarMng.add(removeAction);
		
		parent.setLayout(new GridLayout());
		GridData gd= new GridData(GridData.FILL_BOTH);
		ToolBar tb= toolBarMng.createControl(parent);
		tb.setLayoutData(gd);
		return tb;
	}
	
	public void onPSModelChange(PSModelChangeEvent event) 
	{
		oRulesTree.setInput(oRulesTree.getInput());
		localPolicyView.setInput(localPolicyView.getInput());
	}

	/**
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
	public void doubleClick(DoubleClickEvent event) {
		ISelection sel=event.getSelection();
		if(sel==null)
		{
			return;
		}
		if(sel instanceof IStructuredSelection)
		{
			IStructuredSelection ssel= (IStructuredSelection)sel;
			Object ele0=ssel.getFirstElement();
			if(ele0==null)
			{
				logger.warn("first selection is null");
				return;
			}
			else if(ele0 instanceof PSPolicy)
			{
				PSPolicyEditDialog dlg=
					new PSPolicyEditDialog(composite.getShell(),PSPolicy.class);
				dlg.create();
				dlg.setModelObject((PSPolicy)ele0);
				dlg.open();
			}
			else if(ele0 instanceof PSOverridingRule)
			{
				//logger.warn("oRule not supported jet");
				PSPolicyEditDialog dlg=
					new PSPolicyEditDialog(composite.getShell(),PSOverridingRule.class);
				dlg.create();
				dlg.setModelObject((PSOverridingRule)ele0);
				dlg.open();
				
			}
			else
			{
				logger.warn(
					"Cannot handle this kind of selection element:"+ele0);
			}
		}
		else
		{
			logger.warn("Cannot handle this kind auf selection:"+sel);
		}	
		
	}
	
	
}
