/**
 * 
 */
package org.peertrust.modeler.policysystem.actions;

import java.io.File;

import org.eclipse.jface.wizard.Wizard;
import org.peertrust.modeler.policysystem.control.CreateNewProjectPage;
import org.peertrust.modeler.policysystem.model.ProjectConfig;

import policysystem.PolicysystemPlugin;

/**
 * The Wizard for creating a new Project
 * 
 * @author Patrice Congo
 *
 */
public class CreateNewProjectWizard extends Wizard
{

	/**
	 * Ensures that the entered directories 
	 * (policy system root and directory for config file) exists
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
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