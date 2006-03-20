package policysystem.views;

import java.io.File;

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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ViewSite;
import org.eclipse.ui.part.ViewPart;


import policysystem.ApplicationWorkbenchAdvisor;
import policysystem.model.PolicySystemRDFModel;
import policysystem.model.PolicySystemResTreeContentProvider;
import policysystem.model.ProjectConfig;
import policysystem.model.abtract.ModelObjectWrapper;
import policysystem.model.abtract.PSModelChangeEvent;
import policysystem.model.abtract.PSModelChangeEventListener;
import policysystem.model.abtract.PSOverrindingRule;
import policysystem.model.abtract.PSPolicy;

public class PSResourceView extends ViewPart
							implements 	ISelectionListener,
										PSModelChangeEventListener
{
	static final public String ID="FileSystemView";
	private ITreeContentProvider contentProvider;
	private TreeViewer treeView;
	private Logger logger;
	private Composite composite;
	private ToolBarManager toolbarManager;
	private Action addAction;
	private Action removeAction;
	
	public PSResourceView()
	{
		logger=Logger.getLogger(PSResourceView.class);
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
				new Action[]{addAction,removeAction},
				treeView,
				treeView.getControl(),
				treeView,
				"");
		PolicySystemRDFModel.getInstance().addPSModelChangeEventListener(this);
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
	}
	
	private ToolBar makeToolBar(Composite parent) 
	{
		///manager
		toolbarManager= new ToolBarManager();
		///
		addAction = new Action() {
			public void run() {
				addActionRun();
				System.out.println("blablablablablabblablabala");
			}
		};
		addAction.setText("create");
		addAction.setToolTipText("create");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		
		toolbarManager.add(addAction);
		
		removeAction = new Action() {
			public void run() {
	
			}
		};
		
		removeAction.setText("remove");
		removeAction.setToolTipText("remove");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		toolbarManager.add(removeAction);
		
		parent.setLayout(new GridLayout());
		GridData gd= new GridData(GridData.FILL_BOTH);
		ToolBar tb= toolbarManager.createControl(parent);
		tb.setLayoutData(gd);
		return tb;
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
					treeView.refresh();
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
					treeView.refresh();
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
					treeView.refresh();
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
            else if(element instanceof ModelObjectWrapper)
            {
            	return IMG_MODEL_ELEMENT;
            }
            return null;
        }

        public String getText(Object element) {
            if (element instanceof File) {
                return ((File) element).getName();
            }
            else if(element instanceof PSOverrindingRule)
            {
            	return ((PSOverrindingRule)element).getLabel();
            }
            else
            {
            	return super.getText(element);
            }
        }
    }

	public void onPSModelChange(PSModelChangeEvent event) 
	{
		treeView.refresh();
	}
    
}
