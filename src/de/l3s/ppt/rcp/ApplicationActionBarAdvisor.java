package de.l3s.ppt.rcp;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

	protected void fillCoolBar(ICoolBarManager cbManager) {
		cbManager.addLocal(new GroupMarker("group.file")); //$NON-NLS-1$
		{ // File Group
			IToolBarManager fileToolBar = new ToolBarManager(cbManager.getStyle());
			fileToolBar.addLocal(new Separator(IWorkbenchActionConstants.NEW_GROUP));
			fileToolBar.addLocal(new GroupMarker(IWorkbenchActionConstants.OPEN_EXT));
			fileToolBar.addLocal(new GroupMarker(IWorkbenchActionConstants.SAVE_GROUP));
			fileToolBar.addLocal(getAction(ActionFactory.SAVE.getId()));
			
			fileToolBar.addLocal(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			
			// Add to the cool bar manager
			cbManager.addLocal(new ToolBarContributionItem(fileToolBar,IWorkbenchActionConstants.TOOLBAR_FILE));
		}
		
		cbManager.addLocal(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		
		cbManager.addLocal(new GroupMarker(IWorkbenchActionConstants.GROUP_EDITOR));
	}
	
	protected void fillMenuBar(IMenuManager menubar) {
		menubar.addLocal(createFileMenu());
		menubar.addLocal(createEditMenu());
		menubar.addLocal(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menubar.addLocal(createHelpMenu());
	}
	/**
	 * Creates and returns the File menu.
	 */
	private MenuManager createFileMenu() {
		MenuManager menu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE); //$NON-NLS-1$
		
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
		menu.addLocal(getAction(ActionFactory.CLOSE.getId()));
		menu.addLocal(getAction(ActionFactory.CLOSE_ALL.getId()));
		
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
		menu.addLocal(new Separator());
		menu.addLocal(getAction(ActionFactory.SAVE.getId()));
		menu.addLocal(getAction(ActionFactory.SAVE_AS.getId()));
		menu.addLocal(getAction(ActionFactory.SAVE_ALL.getId()));
		
		menu.addLocal(getAction(ActionFactory.REVERT.getId()));
		menu.addLocal(ContributionItemFactory.REOPEN_EDITORS.create(getActionBarConfigurer().getWindowConfigurer().getWindow()));
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.MRU));
		menu.addLocal(new Separator());
		menu.addLocal(getAction(ActionFactory.QUIT.getId()));
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.FILE_END));

		return menu;
	}

	/**
	 * Creates and returns the Edit menu.
	 */
	private MenuManager createEditMenu() {
		MenuManager menu = new MenuManager("&Edit", IWorkbenchActionConstants.M_EDIT); //$NON-NLS-1$
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.EDIT_START));

		menu.addLocal(getAction(ActionFactory.UNDO.getId()));
		menu.addLocal(getAction(ActionFactory.REDO.getId()));
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));

		menu.addLocal(getAction(ActionFactory.CUT.getId()));
		menu.addLocal(getAction(ActionFactory.COPY.getId()));
		menu.addLocal(getAction(ActionFactory.PASTE.getId()));
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));

		menu.addLocal(getAction(ActionFactory.SELECT_ALL.getId()));
		menu.addLocal(new Separator());

		menu.addLocal(getAction(ActionFactory.FIND.getId()));
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.FIND_EXT));

		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.ADD_EXT));

		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
		menu.addLocal(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		return menu;
	}

	/**
	 * Creates and returns the Help menu.
	 */
	private MenuManager createHelpMenu() {
		MenuManager menu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP); //$NON-NLS-1$
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.HELP_START));
		menu.addLocal(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.addLocal(new GroupMarker(IWorkbenchActionConstants.HELP_END));
		menu.addLocal(new Separator("about")); //$NON-NLS-1$
		menu.addLocal(getAction(ActionFactory.ABOUT.getId()));
		return menu;
	}

	protected void makeActions(IWorkbenchWindow window) {
		registerAsGlobal(ActionFactory.SAVE.create(window));
		registerAsGlobal(ActionFactory.SAVE_AS.create(window));
		registerAsGlobal(ActionFactory.ABOUT.create(window));
		registerAsGlobal(ActionFactory.SAVE_ALL.create(window));
		registerAsGlobal(ActionFactory.UNDO.create(window));
		registerAsGlobal(ActionFactory.REDO.create(window));
		registerAsGlobal(ActionFactory.CUT.create(window));
		registerAsGlobal(ActionFactory.COPY.create(window));
		registerAsGlobal(ActionFactory.PASTE.create(window));
		registerAsGlobal(ActionFactory.SELECT_ALL.create(window));
		registerAsGlobal(ActionFactory.FIND.create(window));
		registerAsGlobal(ActionFactory.CLOSE.create(window));
		registerAsGlobal(ActionFactory.CLOSE_ALL.create(window));
		registerAsGlobal(ActionFactory.CLOSE_ALL_SAVED.create(window));
		registerAsGlobal(ActionFactory.REVERT.create(window));
		registerAsGlobal(ActionFactory.QUIT.create(window));
	}
	
	/**
	 * Registers the action as global action and registers it for disposal.
	 * 
	 * @param action the action to register
	 */
	private void registerAsGlobal(IAction action) {
		getActionBarConfigurer().registerGlobalAction(action);
		register(action);
	}
}
