/**
 * 
 */
package org.peertrust.modeler.policysystem.gui.control;

import org.eclipse.jface.wizard.Wizard;

/**
 * Works with a ChooserWizardPage to select a model
 * object.
 * 
 * @author Patrice Congo
 *
 */
public class ChooserWizard extends Wizard
{
	
	/**
	 * The name of the chooser page 
	 */
	private String chooserWizPageName;
	
	/**
	 * Holds the selected object 
	 */
	private Object selected;
	
	/**
	 * creates a new ChooserWizard to work with the ChooserWizardPage
	 * of the spezified name
	 * 
	 * @param chooserWizPageName -- the name of the chooser page from which the
	 * 			the selected object will be taken.
	 */
	public ChooserWizard(String chooserWizPageName)
	{
		this.chooserWizPageName=chooserWizPageName;
		this.selected=null;
	}
	
	/**
	 * hidden default constructor
	 */
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
	 * To ge the selected object, model object.
	 * The selected object is cached localy when the framework
	 * calls performFisch()
	 * @return the selected object
	 */
	public Object getSelected() {
		return selected;
	}
	
}