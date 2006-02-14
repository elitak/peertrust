package policysystem;



import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.ui.internal.Workbench;

import policysystem.model.ProjectConfig;
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
    	    super("&Open...@Ctrl+O", 
    	    		ImageDescriptor.createFromFile(
    	    				OpenAction.class, "/images/open.gif"));
    	    setDisabledImageDescriptor(ImageDescriptor.createFromFile(
    	        OpenAction.class, "/images/disabledOpen.gif"));
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
    	}
}
