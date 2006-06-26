package org.peertrust.modeler.policysystem;



import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.peertrust.modeler.policysystem.gui.actions.NewAction;
import org.peertrust.modeler.policysystem.gui.actions.OpenAction;
import org.peertrust.modeler.policysystem.gui.actions.RecentlyOpenContributionItem;
import org.peertrust.modeler.policysystem.gui.actions.SaveAction;





/**
 * The Actionbar Advisor for the policy System modeller application
 * @author Patrice Congo
 *
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	/**
	 * Action that opens a new project file
	 */
	private Action openAction;
	
	/**
	 * action that create a new project file
	 */
	private Action newAction;
	
	/**
	 * Action that save the current project
	 */
	private Action saveAction; 
	
	/**
	 * Contibutes with the menu item which allows to
	 * open th most recently opened project
	 */
	private RecentlyOpenContributionItem recentlyOpenedCI;
	
	
    /**
     * Contruct a new ApplicationActionBarAdvisor
     * @param configurer -- the bar configurer
     */
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) 
    {
        super(configurer);        
    }

    /**
     * @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.IWorkbenchWindow)
     */
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

    
    /**
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface.action.IMenuManager)
     */
    protected void fillMenuBar(IMenuManager menuBar) 
    {
    		menuBar.add(createFileMenu(null));    		  	
    }
    
    
    
    /**
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface.action.ICoolBarManager)
     */
    protected void fillCoolBar(ICoolBarManager coolBar) 
    {
		IToolBarManager toolbar = 
    		new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(
        	new ToolBarContributionItem(toolbar, "FileToolbar"));  
        
		toolbar.add(openAction);//new OpenAction());
		toolbar.add(newAction);//new NewAction());
		toolbar.add(saveAction);
		
	}

    
	/**
	 * Create the File menu manager
	 * @param window -- the workbench window
	 * @return the created menu manager
	 */
	private MenuManager createFileMenu(IWorkbenchWindow window) 
	{
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
    
    
    
    
    class NewProjectDialogPage extends DialogPage
    {
    	private Composite top;
		public void createControl(Composite parent) {
			top= new Composite(parent, SWT.LEFT);
			super.setControl(top);
		}
    	
    }
}
