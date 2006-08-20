package org.peertrust.modeler.policysystem.gui.control; 

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.Page;
import org.peertrust.modeler.policysystem.gui.providers.ModelObjectArrayViewContentProvider;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.ResourcePolicyContentProvider;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEvent;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEventListener;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;
import org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker;

/**
 * Provides a Page to edit a PSResource Policy protection
 * 
 * @author pat_dev
 *
 */
public class PSResourcePolicyEditorPage 
							extends Page 
							implements 	IDoubleClickListener,
										PSModelChangeEventListener
{
	/**
	 * The title of the tab that holds the attached filters
	 */
	private static final String TAB_ITEM_POLICIES_AND_FILTERS = 
													"Attached Filters";//Policies and Filters";
	
	/**
	 * The title for the tab that holds the overriding rules
	 */
	private static final String TAB_ITEM_OVERRIDDING_RULES = "Attached Overridding Rules";
	
	/**
	 * The prefix for the resource label
	 */
	private final static String PSRESOURCE_LABEL_PREFIX="Resource: ";
	
	/** main container composite*/
	private Composite composite;
	
	/** the tabfolder to hold the policy and overriding rules views*/
	private TabFolder tabFolder;
	
	/** used to show the local Policies*/
	private TableViewer localPolicyView;

	/** a label for showing the identity of the ps resource*/
	private Label psResourceLabel;
	
	/**
	 * 
	 */
	private ToolBar toolBar;
	
	private ToolBarManager toolBarMng;
	
	/** the logger for the PSResourcePolicyEditorPage class*/
	private Logger logger=
		Logger.getLogger(PSResourcePolicyEditorPage.class);
	
	/**
	 * The tree view to show the model resource overrridding rules
	 */
	private TreeViewer oRulesTree;
	
	/**
	 * Mechanism used to make resource identities
	 */
	private PSResourceIdentityMaker identityMaker;
	
	
	/**
	 * the policy systemmodel
	 */
	private  PolicySystemRDFModel psModel;
	
	
	
	
	/**
	 *Default creator 
	 */
	public PSResourcePolicyEditorPage()
	{
		this.psModel=PolicySystemRDFModel.getInstance();
		this.psModel.addPSModelChangeEventListener(this);
		this.identityMaker=psModel.getPSResourceIdentityMaker(File.class);
		
	}

	/**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) 
	{
		
		composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new GridLayout());

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
		tabFolder= 
				new TabFolder(
						composite,//tableComposite,
						SWT.NONE|SWT.SINGLE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		TabItem tableItem= new TabItem(tabFolder,SWT.NONE);
		tableItem.setText(TAB_ITEM_POLICIES_AND_FILTERS);
		Composite policyViewComposite=makePoliciesFiltersView(tabFolder);
		tableItem.setControl(policyViewComposite);//localPolicyView.getControl());
		
		TabItem oRuleItem= new TabItem(tabFolder,SWT.NONE);
		oRuleItem.setText(TAB_ITEM_OVERRIDDING_RULES);

		Composite oRuleViewComposite=
			makeORuleView(tabFolder);
		oRuleItem.setControl(oRuleViewComposite);
		
	}

	/**
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() 
	{
		return composite;
	}

	/**
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() 
	{
		//empty
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
				//String identity=((File)input).getCanonicalPath();
				URI identity=identityMaker.relativeURI((File)input);
				PSResource psResource=
					psModel.getPSResource(input,true);
				oRulesTree.setInput(psResource);
				psResourceLabel.setText(
						PSRESOURCE_LABEL_PREFIX+
						identity);
			}
			else if(input instanceof PSResource)
			{
				oRulesTree.setInput((PSResource)input);
				String mapping=((PSResource)input).getHasMapping();
				if(mapping==null)
				{
					logger.warn("mapping is null using label for ps resource:"+input);
					mapping=((PSResource)input).getLabel().getValue();
				}
				psResourceLabel.setText(mapping);
			}
			else
			{
				//empty
			}
		} catch (Exception e) {
			logger.error("Exception while setting new input:"+input,e);
		}
		
		
	
	}

	/**
	 * Makes the resource filter tab
	 * @param parent
	 * @return
	 */
	private Composite makePoliciesFiltersView(Composite parent)
	{
		
		///layout
		Composite policiesTab=
					new Composite(parent,SWT.NONE);
		policiesTab.setLayout(new GridLayout());
		
		toolBar=makePoliciesFiltersActions(policiesTab);
		toolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
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
//		Table table=tv.getTableTree().getTable();
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
	
	/**
	 * Makes the policy filter tab toolbad
	 * @param parent -- the container composite
	 * @return the created toolbar
	 */
	private ToolBar makePoliciesFiltersActions(Composite parent) 
	{
		///manager
		toolBarMng= new ToolBarManager(toolBar);
		///add policy
//		Action addPolicyAction = new Action() {
//			public void run() 
//			{
//				addNewResPolicy();				
//			}
//		};
//		addPolicyAction.setText("P");
//		addPolicyAction.setToolTipText("Add policy");
//		toolBarMng.add(addPolicyAction);
		
		///addfilter
		Action addFilterAction = new Action() 
		{
			public void run() 
			{
				addFilterAction();				
			}
		};
		addFilterAction.setText("+");
		addFilterAction.setToolTipText("Add Filter");
//		addFilterAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
//			getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		
		toolBarMng.add(addFilterAction);
		
		///remove action
		Action removeAction = new Action() {
			public void run() {
				removeResLinkedModelObject();//removeResPolicy();
			}
		};
		removeAction.setText("Action 2");
		removeAction.setToolTipText("Remove Policy");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		toolBarMng.add(removeAction);
		
		parent.setLayout(new GridLayout());
		GridData gd= new GridData(GridData.FILL_BOTH);
		ToolBar tb= toolBarMng.createControl(parent);
		tb.setLayoutData(gd);
		return tb;
	}
	
	/**
	 * To remove selected filters or overriding rules 
	 */
	private void removeResLinkedModelObject()
	{
		TabItem[] tabItems=tabFolder.getSelection();
		if(tabItems==null)
		{
			logger.warn("no tab selected");
			return;
		}
		String tabItemText=tabItems[0].getText();
		IStructuredSelection selection=null;
		Object sel0=null;
		if(tabItemText==null)
		{
			logger.warn("tabItem text is null");
			return;
		}
		else if(tabItemText.equals(TAB_ITEM_POLICIES_AND_FILTERS))//localPolicyView.getControl().getVisible())
		{
			logger.info("localPolicyView visible");
			selection=(IStructuredSelection)localPolicyView.getSelection();
			sel0=selection.getFirstElement();
		}
		else if(tabItemText.equals(TAB_ITEM_OVERRIDDING_RULES))//oRulesTree.getControl().getVisible())
		{
			selection=(IStructuredSelection)oRulesTree.getSelection();
			sel0=selection.getFirstElement();
			if(!(sel0 instanceof PSOverridingRule))
			{
				logger.info("no overriding rule selected");
				return;
			}
		}
		else
		{
			logger.warn("Visible view not expected tabItemText="+tabItemText);
			return;
		}
		
		//Object sel0=selection.getFirstElement();
		logger.info("sel0:"+sel0);
		if(sel0==null)
		{
			logger.info("selection is null");
			return;
		}
		else if(sel0 instanceof PSPolicy)
		{
			Object input=localPolicyView.getInput();
			if(input==null)
			{
				return;
			}
			else if(input instanceof File)
			{
				try 
				{
					//URI identity=identityMaker.makeIdentity(input);
					PSResource psRes=
						psModel.getPSResource(input,true);
					PSFilter filter=(PSFilter)((PSPolicy)sel0).getGuarded();
					if(filter!=null)
					{
						psRes.removeFilter(filter);
					}
					else
					{
						logger.warn(
								"\nPolicy not assosiated to a filter:"+
								"\n\tpolicy="+sel0+
								"\n\tresource="+psRes);
					}
					//psRes.removePolicy((PSPolicy)sel0);
				} catch (Exception e) {
					logger.warn("Exception while removing a filter guarded by a policy",e);
				}
				return;
			}
			else if(input instanceof PSResource)
			{
				PSFilter filter=(PSFilter)((PSPolicy)sel0).getGuarded();
				if(filter!=null)
				{
					((PSResource)input).removeFilter(filter);
				}
				else
				{
					logger.warn(
							"\nNo filter associated to the policy"+
							"\n\tpolicy="+sel0);
				}
			}
			else
			{
				logger.warn("can only remove policies from a psresource");
				return;
			}
		}
		else if(sel0 instanceof PSOverridingRule)
		{
			Object input=localPolicyView.getInput();
			if(input==null)
			{
				return;
			}
			else if(input instanceof File)
			{
				try {
					//URI identity=identityMaker.makeIdentity(input);
					PSResource psRes=
						psModel.getPSResource(input,true);
					psRes.removeOverriddingRule((PSOverridingRule)sel0);
				} catch (Exception e) {
					//TODO remove try catch
					logger.warn("Exception while removing a policy",e);
				}
				return;
			}
			else if(input instanceof PSResource)
			{
				((PSResource)input).removeOverriddingRule((PSOverridingRule)sel0);
			}
			else
			{
				logger.warn(
						"\ncan only remove overriding rules from a psresource"+
						"\n\tresource="+input+
						"\n\ttoremove="+sel0);
				return;
			}
		}
		else if(sel0 instanceof PSFilter)
		{
			Object input=localPolicyView.getInput();
			if(input==null)
			{
				return;
			}
			else if(input instanceof File)
			{
				try {
					URI identity=identityMaker.relativeURI(input);
					PSResource psRes=
						psModel.getPSResource(identity,true);
					psRes.removeFilter((PSFilter)sel0);
				} catch (Exception e) {
					//TODO remove try catch
					logger.warn("Exception while removing a filter",e);
				}
				return;
			}
			else if(input instanceof PSResource)
			{
				((PSResource)input).removeFilter((PSFilter)sel0);
			}
			else
			{
				logger.warn(
						"\ncan only remove policies from a psresource"+
						"\n\tresource="+input+
						"\n\tto remove="+sel0);
				return;
			}
		}
		else
		{
			logger.warn("Sell:"+sel0 +" class:"+sel0.getClass());
			return;
		}
	}
	
//	/**
//	 * To add a policy to a resource
//	 */
//	private void addNewResPolicy()
//	{
//		Object input=localPolicyView.getInput();
//		if(input==null)
//		{
//			logger.warn("localPolicyView input is null");
//			return;
//		}
//		else if(input instanceof File)
//		{
//			try {
//				String identity=identityMaker.makeIdentity(input);
//				PSResource psRes=
//					psModel.getPSResource(identity,true);
//				PSPolicy pol=
//					ChooserWizardPage.choosePolicy(
//							composite.getShell());
//							
//				if(pol!=null)
//				{
//					psRes.addIsProtectedBy(pol);
//				}
//			} catch (Exception e) {
//				//TODO remode catch try
//				logger.warn("error while add policy to resource",e);
//			}
//		}
//		else if(input instanceof PSResource)
//		{
//			PSResource psRes= (PSResource)input;
//			PSPolicy pol=
//				ChooserWizardPage.choosePolicy(
//						composite.getShell());
//						
//			if(pol!=null)
//			{
//				psRes.addIsProtectedBy(pol);
//			}
//		}
//		else
//		{
//			logger.warn(
//					"localPolicyInput:"+input+"\n\tclass="+input.getClass());
//		}
//		
//	}
	
	/**
	 * To add an overriding rule to the policy
	 */
	private void addNewResORule()
	{
		Object input=oRulesTree.getInput();
		if(input==null)
		{
			logger.warn("localPolicyView input is null");
			return;
		}
		else if(input instanceof File)
		{
			try {
				URI identity= identityMaker.relativeURI(input);
				PSResource psRes=
					psModel.getPSResource(identity,true);
				PSOverridingRule oRules[]=
					(PSOverridingRule[])
						ChooserWizardPage.chooseModelObjects(
										composite.getShell(),PSOverridingRule.class,null);
							
				if(oRules!=null)
				{
					for(int i=oRules.length-1;i>0;i--)
					{
						psRes.addIsOverrindingRule(oRules[i]);
					}
				}
			} 
			catch (Exception e) 
			{
				//TODO remoce try catch
				logger.warn("error while add policy to resource",e);
			}
		}
		else if(input instanceof PSResource)
		{
			try {
				PSResource psRes=(PSResource)input;
				PSModelObject oRules[]=
						ChooserWizardPage.chooseModelObjects(
												composite.getShell(),
												PSOverridingRule.class,null);
						
				if(oRules!=null)
				{
					logger.info("ORules:"+Arrays.asList(oRules));
					for(int i=oRules.length-1;i>=0;i--)
					{
						if(oRules[i] instanceof PSOverridingRule)
						{
							logger.info("mo:"+oRules[i].getModelObject());
							psRes.addIsOverrindingRule((PSOverridingRule)oRules[i]);
						}
						else
						{
							logger.warn("Skipping since nor ORule:"+oRules[i]);
						}
					}
				}
			} catch (Exception e) {
				logger.warn("error while add policy to resource",e);
			}
		}
		else
		{
			logger.warn(
					"localPolicyInput:"+input+"\n\tclass="+input.getClass());
		}
		
	}
	
	/**
	 * To add a filter to a resource
	 */
	private void addFilterAction()
	{
		logger.info("Adding Filter");
		Object input=localPolicyView.getInput();
		if(input==null)
		{
			logger.info("treeview input is null");
			return;
		}
		else if(input instanceof File)
		{
			URI identity=null;
			PSResource psRes=null;
			try {
					identity=identityMaker.relativeURI(input);
					psRes=
						psModel.getPSResource((File)input,true);
					List resFilters=psRes.getHasFilter();
					String[] resFiltersNames=null;
					logger.info(
							"\nResource filters:"+
							"\n\tResource:"+psRes+
							"\n\tFilters:"+resFilters);
					if(resFilters!=null)
					{
						int size=resFilters.size();
						resFiltersNames=new String[size];
						PSFilter curFilter;
						for(int i=0; i<size; i++)
						{
							curFilter=(PSFilter)resFilters.get(i);
							resFiltersNames[i]=curFilter.getLabel().getValue();
						}
					}
					PSModelObject filters[]=
							ChooserWizardPage.chooseModelObjects(
										composite.getShell(),
										PSFilter.class,
										resFiltersNames);
					if(filters!=null)
					{
						psRes.addHasFilter((PSFilter)filters[0]);
						logger.info("\n\tFilters:"+
								psRes.getHasFilter()+
								"\n\tResource:"+psRes);
					}
				return;	
			} catch (Exception e) {
				logger.warn(
						"\nError while trying to add filter to the resource"+
						"\n\tinput="+input+
						"\n\tidentity="+identity+
						"\n\tgot PSResource="+psRes,						
						e);
				return;
			}
		}
		else if(input instanceof PSResource)
		{
			try {
					PSResource psRes= (PSResource)input;
					List resFilters=psRes.getHasFilter();
					String[] resFiltersNames=null;
					logger.info("Resource filters:"+
							"\n\tResource:"+psRes+
							"\n\tFilters:"+resFilters);
					if(resFilters!=null)
					{
						int size=resFilters.size();
						resFiltersNames=new String[size];
						PSFilter curFilter;
						for(int i=0; i<size; i++)
						{
							curFilter=(PSFilter)resFilters.get(i);
							resFiltersNames[i]=curFilter.getLabel().getValue();
						}
					}
					PSModelObject filters[]=
							ChooserWizardPage.chooseModelObjects(
										composite.getShell(),
										PSFilter.class,
										resFiltersNames);
					if(filters!=null)
					{
						psRes.addHasFilter((PSFilter)filters[0]);
						logger.info("\n\tFilters:"+
								psRes.getHasFilter()+
								"\n\tResource:"+psRes);
					}
				return;	
			} catch (Exception e) {
				logger.warn("error while trying to add filter to the resource");
				return;
			}
		}
		else
		{
			logger.warn(
					"cannot handle input of this art:"+input+
					"\n\tclass="+input.getClass());
			return;
		}
	}
	
	/**
	 * Makes the overriding rule tab
	 * @param parent
	 * @return
	 */
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
	
	/**
	 * Makes and fills the overriding rule tab toolbar
	 * @param parent -- the container composite
	 * @return the created toolbar
	 */
	private ToolBar makeORuleActions(Composite parent) 
	{
		///manager
		ToolBarManager toolBarMng= new ToolBarManager(null);
		///
		Action addAction = new Action() {
			public void run() 
			{
//				IStructuredSelection sel=
//					(IStructuredSelection)localPolicyView.getSelection();
//				System.out.println("Sell:"+sel.getFirstElement());
				addNewResORule();
				
			}
		};
		addAction.setText("+");
		addAction.setToolTipText("add overriding rule");
//		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
//			getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		toolBarMng.add(addAction);
		
		Action removeAction = new Action() {
			public void run() {
				removeResLinkedModelObject();
			}
		};
		
		removeAction.setText("rm orule");
		removeAction.setToolTipText("remove overriding rule");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		toolBarMng.add(removeAction);
		
		parent.setLayout(new GridLayout());
		GridData gd= new GridData(GridData.FILL_BOTH);
		ToolBar tb= toolBarMng.createControl(parent);
		tb.setLayoutData(gd);
		return tb;
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEventListener#onPSModelChange(org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEvent)
	 */
	public void onPSModelChange(PSModelChangeEvent event) 
	{
		try {
			Object input=oRulesTree.getInput();
			if(input!=null)
			{
				oRulesTree.setInput(oRulesTree.getInput());
			}
			
			input=localPolicyView.getInput();
			if(input!=null)
			{
				localPolicyView.setInput(localPolicyView.getInput());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
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
				PSModelObject guarded=((PSPolicy)ele0).getGuarded();
				logger.info("ele0:"+ele0+" guarded:"+guarded);
				if(guarded instanceof PSFilter)
				{
					PSModelObjectEditDialog dlg=
						new PSModelObjectEditDialog(
								composite.getShell(),
								PSFilter.class);
					dlg.create();
					dlg.setModelObject((PSFilter)guarded);
					dlg.open();
				}
				else
				{
					PSModelObjectEditDialog dlg=
						new PSModelObjectEditDialog(
									composite.getShell(),
									PSPolicy.class);
					dlg.create();
					dlg.setModelObject((PSPolicy)ele0);
					dlg.open();
				}
			}
			else if(ele0 instanceof PSOverridingRule)
			{
				//logger.warn("oRule not supported jet");
				PSModelObjectEditDialog dlg=
					new PSModelObjectEditDialog(
								composite.getShell(),
								PSOverridingRule.class);
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
