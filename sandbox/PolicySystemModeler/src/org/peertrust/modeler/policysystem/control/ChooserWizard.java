/**
 * 
 */
package org.peertrust.modeler.policysystem.control;

import org.eclipse.jface.wizard.Wizard;

public class ChooserWizard extends Wizard
{
	
	/**
	 * The name of the chooser page 
	 */
	private String chooserWizPageName;
	
	/**
	 * Holds the selected object 
	 */
	Object selected;
	
	/**
	 * @param chooserWizPageName -- the name of the chooser page from which the
	 * 			the selected object will be taken.
	 */
	public ChooserWizard(String chooserWizPageName)
	{
		this.chooserWizPageName=chooserWizPageName;
	}
	
	private ChooserWizard()
	{
		chooserWizPageName="choose policy";
	}
	
	/**
	 * get the ChooserWizard and save the selected object.
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() 
	{
		ChooserWizardPage wizardPage=
			(ChooserWizardPage)	getPage(chooserWizPageName);
		if(wizardPage!=null)
		{
			selected=wizardPage.getSelected();;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @return the selected object
	 */
	public Object getSelected() {
		return selected;
	}
	
}