package org.peertrust.modeler.policysystem.gui.control;



import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


import org.eclipse.ui.part.Page;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;



/**
 * A page extention to display and edit a polciy
 * @author Patrice Congo
 *
 */
public class PSPolicyEditorPage extends Page 
{
	/**
	 * compsite which holds the editor control
	 */
	private Composite top;
	
	
	/**
	 * The policy editor control
	 */
	private PSPolicyEditorControl policyEditorControl;
	
	/**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) 
	{
		top = new Composite(parent, SWT.LEFT|SWT.FILL);
		top.setLayout(new GridLayout());
		policyEditorControl= new PSPolicyEditorControl(true);
		policyEditorControl.createControl(top);
	}

	/**
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() 
	{
		return top;
	}

	/**
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() 
	{
		//empty
	}

	/**
	 * @return The policy which is being edited
	 */
	public PSPolicy getPsPolicy() {
		return (PSPolicy)policyEditorControl.getModelObject();
	}

	/**
	 * Sets the policy to edit
	 * @param psPolicy
	 */
	public void setPsPolicy(PSPolicy psPolicy) {
		policyEditorControl.setModelObject(psPolicy);
	}
	
	
}
