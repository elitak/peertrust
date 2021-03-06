/**
 * 
 */
package org.peertrust.modeler.policysystem.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.peertrust.modeler.policysystem.PolicysystemPlugin;
import org.peertrust.modeler.policysystem.gui.control.CreateNewProjectPage;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;


/**
 * Theworkbench action to create a new policy system project
 * 
 * @author Patrice Congo
 *
 */
public class NewAction extends Action 
{
	/** the default id of the action*/
	static final String DEFAULT_ID="NewAction";
	
  /**
   * OpenAction constructor
   */
	public NewAction() 
	{
		super(
				"&New", 
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
	 * Creata wizard which helps create a new project
	 */
	public void run() 
	{
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
			dlg.create();
			dlg.setTitle("Create New Poject");
			wiz.setWindowTitle("Create New Project");
			dlg.open();
		
		} 
		catch (Exception e) 
		{
			PolicysystemPlugin.getDefault().showMessage(
					"Exception while creating a new project:"+e.getMessage());
		}
  }
}