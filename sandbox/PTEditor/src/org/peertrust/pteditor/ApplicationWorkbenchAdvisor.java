package org.peertrust.pteditor;

import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.peertrust.pteditor.console.PTErrorConsole;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "org.peertrust.pteditor.perspective.file";

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}
	
	public void initialize(IWorkbenchConfigurer configurer) {
		ConsolePlugin.getDefault().getConsoleManager().
			addConsoles(new IConsole[]{new PTErrorConsole()});
		PlatformUI.getPreferenceStore().setValue(
			IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
			"TOP_LEFT");
		PlatformUI.getPreferenceStore().setValue(
			IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS,
			FilePerspective.ID+","+CredentialPerspective.ID);
	}
}
