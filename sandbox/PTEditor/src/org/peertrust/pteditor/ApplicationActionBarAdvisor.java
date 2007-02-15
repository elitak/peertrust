package org.peertrust.pteditor;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.peertrust.pteditor.actions.AddPolicyAction;
import org.peertrust.pteditor.actions.EditPolicyAction;
import org.peertrust.pteditor.actions.LoadRDFAction;
import org.peertrust.pteditor.actions.RemovePolicyAction;
import org.peertrust.pteditor.actions.SaveAsRDFAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	private IWorkbenchAction exitAction;
//	private IWorkbenchAction saveAction;
	private SaveAsRDFAction saverdfAction;
	private LoadRDFAction loadrdfAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction helpAction;
	private IContributionItem perspectiveMenu;
	private AddPolicyAction addpolicyAction;
	private RemovePolicyAction removepolicyAction;
	private EditPolicyAction editpolicyAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.

		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);
		aboutAction=ActionFactory.ABOUT.create(window);
		register(aboutAction);
		helpAction=ActionFactory.HELP_CONTENTS.create(window);
		register(helpAction);
//		saveAction=ActionFactory.SAVE.create(window);
//		register(saveAction);
		addpolicyAction=new AddPolicyAction(window);
		register(addpolicyAction);
		removepolicyAction=new RemovePolicyAction(window);
		register(removepolicyAction);
		editpolicyAction=new EditPolicyAction(window);
		register(editpolicyAction);
		saverdfAction=new SaveAsRDFAction(window);
		register(saverdfAction);
		loadrdfAction=new LoadRDFAction(window);
		register(loadrdfAction);
		perspectiveMenu=ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File",
			IWorkbenchActionConstants.M_FILE);
		menuBar.add(fileMenu);
//		fileMenu.add(saveAction);
		fileMenu.add(saverdfAction);
		fileMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		fileMenu.add(addpolicyAction);
		fileMenu.add(removepolicyAction);
		fileMenu.add(editpolicyAction);
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);
		MenuManager layoutMenu=new MenuManager("&Layout","layout");
		menuBar.add(layoutMenu);
		layoutMenu.add(perspectiveMenu);
		MenuManager helpMenu = new MenuManager("&Help",
			IWorkbenchActionConstants.M_HELP);
		menuBar.add(helpMenu);
		helpMenu.add(helpAction);
		helpMenu.add(aboutAction);
	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar=new ToolBarManager(
			coolBar.getStyle()|SWT.BOTTOM);
		coolBar.add(toolbar);
//		ActionContributionItem citem=new ActionContributionItem(saveAction);
//		citem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
//		toolbar.add(citem);
		toolbar.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		ActionContributionItem citem=new ActionContributionItem(addpolicyAction);
		citem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolbar.add(citem);
		citem=new ActionContributionItem(removepolicyAction);
		citem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolbar.add(citem);
		citem=new ActionContributionItem(editpolicyAction);
		citem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolbar.add(citem);
	}
	//credential persp
	//file persp 
}