/**
 * 
 */
package org.peertrust.modeler.policysystem.control;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;

/**
 * @author pat_dev
 *
 */
public class PSModelObjectEditDialog extends TitleAreaDialog 
{
	
	private PSModelObjectEditControl editorControl;
	private Class modelObjectType;
	
	/**
	 * 
	 * @param parentShell
	 */
	public PSModelObjectEditDialog(Shell parentShell, Class modelObjectType) 
	{
		super(parentShell);
		this.modelObjectType=modelObjectType;
		if(modelObjectType==PSPolicy.class)
		{
			editorControl= new PSPolicyEditorControl(false);
		}
		else if(modelObjectType==PSOverridingRule.class)
		{
			editorControl=new PSOverriddingRuleEditControl();
		}
		else if(modelObjectType==PSFilter.class)
		{
			editorControl=new PSFilterEditorControl();
		}
		else
		{
			//empty
		}
		
	}

	protected Control createContents(Composite parent) 
	{
		
		return super.createContents(parent);
	}

	protected Control createDialogArea(Composite parent) 
	{
		Composite compo=(Composite)super.createDialogArea(parent);
		editorControl.createControl(compo);		
		return compo;
		
	}

	protected void cancelPressed() 
	{
		super.cancelPressed();
	}

	
	protected void okPressed() 
	{
		int ret=editorControl.saveEdit();
		if(ret!=PSModelObjectEditControl.SAVE_RESULT_OK)
		{
			///failure
		}
		super.okPressed();
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.control.PSModelObjectEditControl#getModelObject()
	 */
	public PSModelObject getModelObject() 
	{
		return editorControl.getModelObject();
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.control.PSModelObjectEditControl#setModelObject(org.peertrust.modeler.policysystem.model.abtract.PSModelObject)
	 */
	public void setModelObject(PSModelObject psModelObject) 
	{
		editorControl.setModelObject(psModelObject);
	}

	
	
	
}
