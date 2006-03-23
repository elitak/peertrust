package policysystem;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javax.management.openmbean.OpenDataException;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.framework.adaptor.BundleOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.part.services.PluginResources;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;
import org.osgi.framework.Bundle;


import policysystem.control.CreateNewProjectPage;
import policysystem.control.NewProjectDlg;

import policysystem.model.PolicySystemRDFModel;
import policysystem.model.ProjectConfig;
import policysystem.model.ProjectConfigChangeListener;
import policysystem.model.abtract.PSPolicy;
import policysystem.views.PolicySystemView;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private Action openAction;
	private Action newAction;
	private Action saveAction; 
	RecentlyOpenContributionItem recentlyOpenedCI;
	
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
        
    }

    protected void makeActions(IWorkbenchWindow window) 
    {
    	openAction= new OpenAction();
    	register(openAction);
    	
    	newAction= new NewAction();
    	register(newAction);
    	
    	saveAction= new SaveAction();
    	register(saveAction);
    	
    	recentlyOpenedCI= new RecentlyOpenContributionItem();
    	    	
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    		menuBar.add(createFileMenu(null));
    		//menuBar.add(new RecentlyOpenContributionItem());
    	
    	
    }
    
    
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
		//super.fillCoolBar(coolBar);
    	IToolBarManager toolbar = 
    		new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(
        	new ToolBarContributionItem(toolbar, "FileToolbar"));  
        
		toolbar.add(openAction);//new OpenAction());
		toolbar.add(newAction);//new NewAction());
		toolbar.add(saveAction);
		
	}

	private MenuManager createFileMenu(IWorkbenchWindow window) {
	    MenuManager menuMng = 
	    	new MenuManager("File");//, IWorkbenchActionConstants.M_FILE);
	    //menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
	    //menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	    menuMng.add(openAction);//new OpenAction());
	    menuMng.add(newAction);//new NewAction());
	    menuMng.add(saveAction);//new SaveAction());
	    //IContributionItem ci;
	    //MenuItem mi;
	    menuMng.add(recentlyOpenedCI);//new RecentlyOpenContributionItem());
	    return menuMng;
    }
    
    
    
    
    ////////////////////////////////////////////////////////////////////////
    class SaveAction extends Action 
    {
    	final static String DEFAULT_ID="SaveAction";
    	
    	/**
  	   * OpenAction constructor
  	   */
  	  public SaveAction() {
  	    super("&Save", //"&Open...@Ctrl+O", 
  	    		PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
      	    			ISharedImages.IMG_TOOL_UP));
  	    
  	    ImageDescriptor id=
  	    	PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
  	    			ISharedImages.IMG_OBJ_FOLDER);
  	    setDisabledImageDescriptor(id);
  	    
  	    setToolTipText("Save");
  	    super.setId(DEFAULT_ID);
  	  }

  	  /**
  	   * Opens an existing file
  	   */
  	  public void run() {
  	    try {
			// Use the file dialog
			 ProjectConfig pConfig= ProjectConfig.getInstance();
			String rdfModelFile=  pConfig.getProperty(ProjectConfig.RDF_MODEL_FILE);
			File saveTmp= new File(rdfModelFile+".tmp");
			
			saveTmp.createNewFile();
			PolicySystemRDFModel.getInstance().saveTo(saveTmp.getCanonicalPath());
			File old= new File(rdfModelFile);
			old.renameTo(new File(rdfModelFile+".old"));
			saveTmp.renameTo(new File(rdfModelFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
  	  }
  	}//end save action
    
    ////////////////////////////////////////////////////////////////////////
    
    class OpenAction extends Action {
    	final static String DEFAULT_ID="OpenAction";
    	  /**
    	   * OpenAction constructor
    	   */
    	  public OpenAction() 
    	  {
    	   	  super("&Open", //"&Open...@Ctrl+O", 
//    	    		ImageDescriptor.createFromFile(
//    	    				OpenAction.class, "/images/open.gif")
    	    		PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
        	    			ISharedImages.IMG_OBJ_FOLDER));
    	    
//    	    setDisabledImageDescriptor(ImageDescriptor.createFromFile(
//    	        OpenAction.class, "/images/disabledOpen.gif"));
    	    ImageDescriptor id=
    	    	PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
    	    			ISharedImages.IMG_OBJ_FOLDER);
    	    setDisabledImageDescriptor(id);
    	    
    	    setToolTipText("Open");
    	    setId(DEFAULT_ID);
    	  }

    	  /**
    	   * Opens an existing file
    	   */
    	  public void run() {
    	    // Use the file dialog
    		IWorkbench wb=PlatformUI.getWorkbench();
    		Shell shell=
    			wb.getActiveWorkbenchWindow().getShell();
    	    FileDialog dlg = 
    	    	new FileDialog(shell,SWT.OPEN);
			String fileName = dlg.open();
			if (fileName != null) {
				PolicySystemRDFModel.getInstance().clearRDFModel();
				ProjectConfig.getInstance().setProjectFile(fileName);				
			}
    	  }
    	}//end open action
    
    class NewAction extends Action 
    {
    	static final String DEFAULT_ID="NewAction";
  	  /**
  	   * OpenAction constructor
  	   */
  	  public NewAction() {
  	    super("&New", 
  	    		PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
      	    			ISharedImages.IMG_TOOL_NEW_WIZARD));
  	    

  	    ImageDescriptor id=
  	    	PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
  	    			ISharedImages.IMG_TOOL_NEW_WIZARD_DISABLED);
  	    setDisabledImageDescriptor(id);  	    
  	    setToolTipText("New");
  	    setId(DEFAULT_ID);
  	  }

  	  /**
  	   * Opens an existing file
  	   */
  	  public void run() {
  	    // Use the file dialog
  		IWorkbench wb=PlatformUI.getWorkbench();
  		Shell shell=
  			wb.getActiveWorkbenchWindow().getShell();
  		try 
  		{
  			PolicySystemRDFModel.getInstance().clearRDFModel();
			CreateNewProjectWizard wiz= new CreateNewProjectWizard();
			
			CreateNewProjectPage npp= new CreateNewProjectPage("New Project");
			
			wiz.addPage(npp);
			wiz.createPageControls(shell);
			WizardDialog dlg=
				new WizardDialog(
						shell,
						wiz);
			dlg.setTitle("Create New Poject");
			wiz.setWindowTitle("Create New Project");
			dlg.create();
			dlg.open();
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
  	  }
  	}
    
    class CreateNewProjectWizard extends Wizard
    {

		public boolean performFinish() 
		{
			try {
				CreateNewProjectPage npp=
					(CreateNewProjectPage)getPage("New Project");
				String pjtName=npp.getNewProjectName();
				String pjtFolder=npp.getNewProjectFolder();
				String polBase=npp.getPolicySystemBaseFolder();
				if(pjtName==null || pjtFolder==null || polBase==null)
				{
					return false;
				}
				
				if(	"".equals(pjtName) ||
						"".equals(pjtFolder) ||
						"".equals(polBase))
				{
					PolicysystemPlugin.getDefault().showMessage("Fields must not be empty");
					return false;
				}
				
				File pjtFolderFile= new File(pjtFolder);
				if(!pjtFolderFile.exists())
				{
					PolicysystemPlugin.getDefault().showMessage(
										"Project Folder File doe not exist");
					return false;
				}
				File polBaseFile= new File(polBase);
				if(!polBaseFile.exists())
				{
					PolicysystemPlugin.getDefault().showMessage(
										"Policy System base folder does not exist");
					return false;
				}
				
				ProjectConfig pConf=ProjectConfig.getInstance();
				File pjtFile=new File(pjtFolderFile,pjtName);
				if(pjtFile.exists())
				{
					boolean ans=PolicysystemPlugin.getDefault().askQuestion(
							"File already exists do you want to override it?");
					if(!ans)
					{
						return false;
					}
				}
				pConf.createNewProjectConfigFile(pjtName,pjtFolderFile,polBaseFile);
				
				return true;
			} catch (Exception e) {
				PolicysystemPlugin.getDefault().showMessage(
				"Exception while setting new project:"+e.getMessage());
				return false;
			}
		}
    	
    	
    }
    
    class NewProjectDialogPage extends DialogPage
    {
    	private Composite top;
		public void createControl(Composite parent) {
			top= new Composite(parent, SWT.LEFT);
			super.setControl(top);
		}
    	
    }
    
    /////////////////////////////////////////////////////////////////
    /////////////////////recently open///////////////////////////////
    /////////////////////////////////////////////////////////////////
    /**
     * This Class provides a Menu Item that hold the 4 most recently
     * open projects  
     */
    class RecentlyOpenContributionItem 	
    				extends ContributionItem//ControlContribution
    				implements ProjectConfigChangeListener,
    							SelectionListener
    {
    	private final String KEYS[]= {"1","2","3","4"};
    	private final String DEFAULT_IDS[]=
    						{	"RECENTLY_OPENED_0",
    							"RECENTLY_OPENED_1",
    							"RECENTLY_OPENED_2",
    							"RECENTLY_OPENED_3"}; 
    	//MenuItem mi;
    	public static final String DEFAULT_ID="RECENTY_OPEN_PJT";
    	
    	public static final String STORE_FILE_NAME="recently_opened.prop";
    	public static final String STORE_FILE_PATH="/"+STORE_FILE_NAME;
    	
    	private List list;
    	private URL storeURL;
    	private Properties recentlyOpened=new Properties();
    	
    	private MenuItem menuItems[]= new MenuItem[4];
    	
    	protected RecentlyOpenContributionItem() 
		{
			super(DEFAULT_ID);
		}
    	
		protected RecentlyOpenContributionItem(String id) 
		{
			super(id);
		}

		
		public void fill(Menu menu, int index) {
			//super.fill(menu, index);
			new MenuItem(menu,SWT.SEPARATOR);
//			MenuItem mi= new MenuItem(menu,SWT.CASCADE);
//			Menu cascadeMenu= new Menu(menu);
//			mi.setMenu(cascadeMenu);
//			mi.setText("mi");
			menuItems[0]= new MenuItem(menu,SWT.NONE);
			menuItems[0].setText("1:");
			menuItems[0].setEnabled(false);
			menuItems[0].addSelectionListener(this);
			//menuItems[0].getMenu().
			menuItems[1]= new MenuItem(menu,SWT.NONE);
			menuItems[1].setText("2:");
			menuItems[1].setEnabled(false);
			menuItems[1].addSelectionListener(this);
			
			menuItems[2]= new MenuItem(menu,SWT.NONE);
			menuItems[2].setText("3:");
			menuItems[2].setEnabled(false);
			menuItems[2].addSelectionListener(this);
			
			menuItems[3]= new MenuItem(menu,SWT.NONE);
			menuItems[3].setText("4:");
			menuItems[3].setEnabled(false);
			menuItems[3].addSelectionListener(this);
			
			loadRecentlyOpened();
			ProjectConfig.getInstance().addProjectConfigChangeListener(this);
			doFillList();
		}

		protected Control createControl(Composite parent) 
		{
			list= new List(parent,SWT.NONE);
			ProjectConfig.getInstance().addProjectConfigChangeListener(this);
			loadRecentlyOpened();
			return list;
		}
    	
		
		/**
		 *Loads the most recently opened projects into the 
		 *recentlyOpened property object from the 
		 *propriety bundle file with this path 
		 *<code>STORE_FILE_NAME</code>. 
		 */
		private void loadRecentlyOpened()
		{
			try {
				File storeFile= 
					new File(
							Platform.getInstanceLocation().getURL().getPath(),
							STORE_FILE_NAME);
				if(!storeFile.exists())
				{
					storeFile.createNewFile();
				}
				storeURL=storeFile.toURL();
				recentlyOpened.load(storeURL.openStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			Bundle bundle=
//				PolicysystemPlugin.getDefault().getBundle();
//			storeURL= bundle.getEntry(STORE_FILE_PATH);
//			if(storeURL!=null)
//			{
//				try 
//				{
//					recentlyOpened.load(storeURL.openStream());
//					
//				} 
//				catch (IOException e) 
//				{
//					e.printStackTrace();
//				}
//			}
//			else
//			{
//				try 
//				{
//					URL bundleRoot=
//						PolicysystemPlugin.getDefault().getBundle().getEntry(
//																"/");
//					File storeFile= 
//						new File(bundleRoot.getPath(),STORE_FILE_NAME);
//					storeFile.createNewFile();
//					storeURL=storeFile.toURL();
//				} 
//				catch (IOException e) 
//				{
//					e.printStackTrace();
//				}
//			}
			
		}
	    
		/**
		 *To fill the list with the element in the property object
		 *File0=/path/to/dotconf will be entered in the list as
		 *0 /path/to/dotconf
		 */
		private void doFillList()
	    {
			
			final int SIZE=KEYS.length;
			
	    	for( int i=0;i<KEYS.length;i++)
	    	{
	    		String filePath=recentlyOpened.getProperty(KEYS[i]);
	    		if(filePath==null)
	    		{
	    			break;
	    		}
	    		else
	    		{
	    			menuItems[i].setText((i+1)+": "+filePath);
	    			menuItems[i].setEnabled(true);
	    		}
	    		
	    	}
	    	
	    }
		
		/**
		 * Gets the new loaded project and add the path of its config
		 * file to the recently opened project files and update the
		 * list.
		 * @param projectConfig
		 */
		private void doUpdate(ProjectConfig projectConfig)
		{
			String currentProjectFile=
				ProjectConfig.getInstance().getProjectFile();
			String lastProjectFile=
				currentProjectFile;
			
			System.out.println("lastPjtFile:"+lastProjectFile);
			//remove old entry			
			String nextProjectFile;
			int size=recentlyOpened.size()+1;
			size=(size>KEYS.length)?KEYS.length:size;
			int skipOffset=0;
			for(	int i=0;
					(i<size) && (i<KEYS.length) && (lastProjectFile!=null);
					i++)
			{
				nextProjectFile=recentlyOpened.getProperty(KEYS[i]);
				if(currentProjectFile.equals(nextProjectFile))
				{
					skipOffset=1;
				}
				else
				{
					recentlyOpened.put(KEYS[i-skipOffset],lastProjectFile);
					lastProjectFile=nextProjectFile;
				}
			}
			doFillList();
			try {
				recentlyOpened.store(
						new FileOutputStream(storeURL.getPath()),
						"Recently opened Project Files");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	
		/**
		 * @see policysystem.model.ProjectConfigChangeListener#projectConfigChanged(policysystem.model.ProjectConfig)
		 */
		public void projectConfigChanged(ProjectConfig config) 
		{
			doUpdate(config);
		}

		
		
		
		public void widgetSelected(SelectionEvent e) 
		{
			ProjectConfig projectConf=ProjectConfig.getInstance();
			if(e.getSource()==menuItems[0])
			{
				projectConf.setProjectFile(
						recentlyOpened.getProperty(KEYS[0]));
			}
			else if(e.getSource()==menuItems[1])
			{
				projectConf.setProjectFile(
						recentlyOpened.getProperty(KEYS[1]));
			}
			else if(e.getSource()==menuItems[2])
			{
				projectConf.setProjectFile(
						recentlyOpened.getProperty(KEYS[2]));
			}
			else if(e.getSource()==menuItems[3])
			{
				projectConf.setProjectFile(
						recentlyOpened.getProperty(KEYS[3]));
			}
			else
			{
				System.out.println("SelectionEvent:"+e);
			}
		}

		
		public void widgetDefaultSelected(SelectionEvent e) 
		{
			
		}
    
    }
}
