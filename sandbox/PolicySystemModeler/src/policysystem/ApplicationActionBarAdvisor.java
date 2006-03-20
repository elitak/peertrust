package policysystem;



import java.io.File;
import java.io.IOException;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
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


import policysystem.control.CreateNewProjectPage;
import policysystem.control.NewProjectDlg;

import policysystem.model.ProjectConfig;
import policysystem.model.abtract.PSPolicy;
import policysystem.views.PolicySystemView;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    		menuBar.add(createFileMenu(null));
    }
    
    private MenuManager createFileMenu(IWorkbenchWindow window) {
	    MenuManager menu = 
	    	new MenuManager("File");//, IWorkbenchActionConstants.M_FILE);
	    //menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
	    //menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	    menu.add(new OpenAction());
	    menu.add(new NewAction());
	    //menu.add(ActionFactory.QUIT.create(window));
	    //menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
	    return menu;
    }
    
    
    
    
    
    
    ////////////////////////////////////////////////////////////////////////
    
    class OpenAction extends Action {
    	  /**
    	   * OpenAction constructor
    	   */
    	  public OpenAction() {
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
			System.out.println("fileNammmmmme:"+fileName);
			if (fileName != null) {
				ProjectConfig.getInstance().setProjectFile(fileName);				
			}
    	  }
    	}//end open action
    
    class NewAction extends Action {
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
  	  }

  	  /**
  	   * Opens an existing file
  	   */
  	  public void run() {
  	    // Use the file dialog
  		IWorkbench wb=PlatformUI.getWorkbench();
  		Shell shell=
  			wb.getActiveWorkbenchWindow().getShell();
  		try {
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
}
