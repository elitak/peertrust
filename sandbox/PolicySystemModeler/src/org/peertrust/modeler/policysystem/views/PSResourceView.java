package org.peertrust.modeler.policysystem.views;

import java.io.File;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ViewSite;
import org.eclipse.ui.part.ViewPart;
import org.peertrust.modeler.policysystem.ApplicationWorkbenchAdvisor;
import org.peertrust.modeler.policysystem.control.ChooserWizardPage;
import org.peertrust.modeler.policysystem.control.PSOverriddingRuleEditorPage.ChooserWizard;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.PolicySystemResTreeContentProvider;
import org.peertrust.modeler.policysystem.model.ProjectConfig;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEvent;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEventListener;
import org.peertrust.modeler.policysystem.model.abtract.PSOverrindingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;


/**
 * View part for showing details about the model object of a same class
 * E.g. for showing Policies
 * 
 * @author Patrice Congo
 *
 */
public class PSResourceView extends ViewPart
							implements 	ISelectionListener,
										PSModelChangeEventListener
{
	static final public String ID="FileSystemView";
	private ITreeContentProvider contentProvider;
	private TreeViewer treeView;
	static final private Logger logger=Logger.getLogger(PSResourceView.class);;
	private Composite composite;
	private ToolBarManager toolbarManager;
	private Action addAction;
	private Action removeAction;
	private Action protectAction;
	private Action addPRuleAction;
	private Action addFilterAction;
	
	public PSResourceView()
	{
	}
	
	public void createPartControl(Composite parent) 
	{
		/////
		treeView= new TreeViewer(parent);
	
		contentProvider= new PSResourceViewContentProvider(true);
		treeView.setContentProvider(contentProvider);
		treeView.setLabelProvider(new FileLabelProvider());
		treeView.setInput(getViewSite());
		getSite().setSelectionProvider(treeView);
		getSite().getPage().addSelectionListener(
				PolicySystemView.ID,
				(ISelectionListener)this);
		parent.setLayout(new GridLayout());
		treeView.getControl().setLayoutData(
				new GridData(GridData.FILL_BOTH));
		
		///toolbar
		makeToolBarActions();
		System.out.println("PARENT="+parent.getClass());
		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView();
		//Workbench.getInstance().getElementFactory();
		//MenuBarCreator creator= new MenuBarCreator();
		MenuBarCreator.createMenubar(
				//getViewSite().getPart(),
				this.getViewSite(),
				(IDoubleClickListener)null,
				new Action[]{	addAction,removeAction,protectAction,
								addPRuleAction,addFilterAction},
				treeView,
				treeView.getControl(),
				treeView,
				"");
		PolicySystemRDFModel rdfModel=
						PolicySystemRDFModel.getInstance();
		rdfModel.addPSModelChangeEventListener(this);
		
	}

	public void setFocus() 
	{
		
	}
	
	private void makeToolBarActions() 
	{
		;
		///
		addAction = new Action() {
			public void run() {
				addActionRun();
			}
		};
		addAction.setText("create");
		addAction.setToolTipText("create");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		
		
		removeAction = new Action() {
			public void run() {
				
			}
		};
		
		removeAction.setText("remove");
		removeAction.setToolTipText("remove");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		
		protectAction = new Action() {
			public void run() {
				protectActionRun();
			}
		};
		protectAction.setText("P");
		protectAction.setToolTipText("Protect");
		
		///add rule action
		addFilterAction = new Action() {
			public void run() {
				addFilterActionRun();
			}
		};
		
		addFilterAction.setText("F");
		addFilterAction.setToolTipText("Add Filter");
		
		///add rule action
		addPRuleAction = new Action() {
			public void run() {
				addPRuleActionRun();
			}
		};
		
		addPRuleAction.setText("R");
		addPRuleAction.setToolTipText("Add Ovverriddin Rule");
		
		
	}
	
	
//	private ToolBar makeToolBar(Composite parent) 
//	{
//		
//	}
	
	//////////////////////////////////////////////////////////////////
	private void protectActionRun()
	{
		Object input=treeView.getInput();
		if(input==null)
		{
			logger.info("treeview input is null");
			return;
		}
		else if(input instanceof String)
		{
			try {
				if(((String)input).equals(
						PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_RESOURCES))
				{
					StructuredSelection structSel=
						(StructuredSelection)treeView.getSelection();
					Object sel0=structSel.getFirstElement();
					if(sel0==null)
					{
						logger.warn("No selection");
						return;
					}
					if(sel0 instanceof File)
					{
						PSResource psRes=
							PolicySystemRDFModel.getInstance().getResource(
									((File)sel0).getCanonicalPath(),true,null);
						PSPolicy pol=
							ChooserWizardPage.choosePlicy(
									treeView.getControl().getShell());
						if(pol!=null)
						{
							psRes.addIsProtectedBy(pol);
						}
					}
					else
					{
						logger.warn("Type:"+sel0.getClass());
						
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	private void addFilterActionRun()
	{
		logger.info("Adding Filter");
		Object input=treeView.getInput();
		if(input==null)
		{
			logger.info("treeview input is null");
			return;
		}
		else if(input instanceof String)
		{
			try {
				if(((String)input).equals(
						PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_RESOURCES))
				{
					StructuredSelection structSel=
						(StructuredSelection)treeView.getSelection();
					Object sel0=structSel.getFirstElement();
					if(sel0==null)
					{
						logger.warn("Selection 0 is null");
						return;
					}
					else if(sel0 instanceof File)
					{
						PSResource psRes=
							PolicySystemRDFModel.getInstance().getResource(
									((File)sel0).getCanonicalPath(),true,null);
						Vector resFilters=psRes.getHasFilter();
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
								curFilter=(PSFilter)resFilters.elementAt(i);
								resFiltersNames[i]=curFilter.getLabel().getValue();
							}
						}
						PSModelObject filters[]=
								ChooserWizardPage.chooseModelObjects(
											treeView.getControl().getShell(),
											PSFilter.class,
											resFiltersNames);
						if(filters!=null)
						{
							psRes.addHasFilter((PSFilter)filters[0]);
							logger.info("\n\tFilters:"+
									psRes.getHasFilter()+
									"\n\tResource:"+psRes);
						}
					}
					else
					{
						logger.warn("Type:"+sel0.getClass());
					}
					
				}
				else 
				{
					logger.warn("Cannot add rule:"+input+
								"\n current inputclass:"+input.getClass());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	private void addPRuleActionRun()
	{
		logger.info("Adding ORule");
		Object input=treeView.getInput();
		if(input==null)
		{
			logger.info("treeview input is null");
			return;
		}
		else if(input instanceof String)
		{
			try {
				if(((String)input).equals(
						PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_RESOURCES))
				{
					StructuredSelection structSel=
						(StructuredSelection)treeView.getSelection();
					Object sel0=structSel.getFirstElement();
					if(sel0==null)
					{
						logger.warn("Selection 0 is null");
						return;
					}
					else if(sel0 instanceof File)
					{
						PSResource psRes=
							PolicySystemRDFModel.getInstance().getResource(
									((File)sel0).getCanonicalPath(),true,null);
						PSModelObject rules[]=
								ChooserWizardPage.chooseModelObjects(
											treeView.getControl().getShell(),
											PSOverrindingRule.class,
											null);
						if(rules!=null)
						{
							psRes.addIsOverrindingRule(
									(PSOverrindingRule)rules[0]);
							logger.info("ORules:"+
									psRes.getIsOverrindingRule()+
									"\n\tResource:"+psRes);
						}
					}
					else
					{
						logger.warn("Type:"+sel0.getClass());
					}
					
				}
				else 
				{
					logger.warn("Cannot add rule:"+input+
								"\n current inputclass:"+input.getClass());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	//////////////////////////////////////////////////////////////////
	private void addActionRun()
	{
		Object input=treeView.getInput();
		if(input==null)
		{
			logger.info("treeview input is null");
			return;
		}
		else if(input instanceof String)
		{
			if(((String)input).equals(
					PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_POLICIES))
			{
				try {
					long time=System.currentTimeMillis();
					PolicySystemRDFModel.getInstance().createPolicy(
																"label"+time,
																"value"+time);
					//treeView.refresh();
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
			else if(((String)input).equals(
					PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_FILTERS))
			{
				try {
					long time=System.currentTimeMillis();
					PolicySystemRDFModel.getInstance().createFilter(
														"label"+time,
														new String[]{"value"+time},
														new PSPolicy[]{});
					//treeView.refresh();
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
			else if(((String)input).equals(
					PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_OVERRIDDING_RULES))
			{
				try {
					long time=System.currentTimeMillis();
					PolicySystemRDFModel.getInstance().createOverriddingRule(
														"label"+time,
														(PSPolicy)null,
														(PSPolicy)null);
					//treeView.refresh();
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
			else
			{
				logger.info("Unsuppoerted:"+input);
				return;
			}
		}
	}
	
	
	///////////////////////////////////////////////////////////////////
	////////////////////SELCTION LISTENER//////////////////////////////
	///////////////////////////////////////////////////////////////////

	public void selectionChanged(IWorkbenchPart part, ISelection selection) 
	{
		//part.getSite().getPage().getViewReferences()[0].
		try {
			if(selection instanceof StructuredSelection)
			{
				Object el=
					((StructuredSelection)selection).getFirstElement();
				if(el instanceof String)
				{
					if(el.equals(
							PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_RESOURCES))
					{
						String rootDir=
							ProjectConfig.getInstance().getProperty("rootDir");
						if(rootDir==null)
						{
							return;
						}
						treeView.setInput(el);//new File(rootDir));
						addAction.setEnabled(false);
						removeAction.setEnabled(false);
					}
					else if(el.equals(
							PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_POLICIES))
					{
						treeView.setInput(el);
						addAction.setEnabled(true);
						removeAction.setEnabled(true);
					}
					else if(el.equals(
							PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_OVERRIDDING_RULES))
					{
						treeView.setInput(el);
						addAction.setEnabled(true);
						removeAction.setEnabled(true);
					}
					else if(el.equals(
							PolicySystemResTreeContentProvider.POLICY_SYSTEM_RES_FILTERS))
					{
						treeView.setInput(el);
						addAction.setEnabled(true);
						removeAction.setEnabled(true);
					}
					else
					{
						treeView.setInput(null);
						addAction.setEnabled(false);
						removeAction.setEnabled(false);
					}
				}
				
				//System.out.println("dadadadad:"+el);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	///////////////////////////////////////////////////////////////////////
    /////////////////////////LABEL PROVIDER////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    /**
     * Label provider for java.io.File objects.
     */
    private static class FileLabelProvider extends LabelProvider {
        private static final Image IMG_FOLDER = PlatformUI.getWorkbench()
                .getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);

        private static final Image IMG_FILE = 
        		PlatformUI.getWorkbench()
                		.getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
        private static final Image IMG_MODEL_ELEMENT = 
				        	PlatformUI.getWorkbench()
				        		.getSharedImages().getImage(
				        				ISharedImages.IMG_OBJ_ELEMENT);
        
        public Image getImage(Object element) {
            if (element instanceof File) {
                File curr = (File) element;
                if (curr.isDirectory()) {
                    return IMG_FOLDER;
                } else {
                    return IMG_FILE;
                }
            }
            else if(element instanceof PSModelObject)
            {
            	return IMG_MODEL_ELEMENT;
            }
            return null;
        }

        public String getText(Object element) {
            if (element instanceof File) {
                return ((File) element).getName();
            }
            else if(element instanceof PSModelObject)
            {
            	return ((PSModelObject)element).getLabel().getValue();
            }
//            else if(element instanceof PSOverrindingRule)
//            {
//            	return ((PSOverrindingRule)element).getLabel();
//            }
            else
            {
            	return super.getText(element);
            }
        }
    }

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEventListener#onPSModelChange(org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEvent)
	 */
	public void onPSModelChange(PSModelChangeEvent event) 
	{
		try {
			//System.out.println("input="+treeView.getInput());
			//treeView.getControl().update();
			treeView.refresh(true);
			//treeView.setInput(treeView.getInput());//refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
}
