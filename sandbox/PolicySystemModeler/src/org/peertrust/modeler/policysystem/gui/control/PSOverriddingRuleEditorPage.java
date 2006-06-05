package org.peertrust.modeler.policysystem.gui.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;

/**
 * Page to edit an overriding rule model Object 
 * @author Patrice Congo
 *
 */
public class PSOverriddingRuleEditorPage 
					extends Page
{
	
	/**
	 * The top container panel for the editor controls
	 */
	private Composite top;
	
	/**
	 * The overriding rule editor 
	 */
	private PSOverriddingRuleEditControl orControl;
	
	/**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) 
	{
		
		top = new Composite(parent, SWT.LEFT);
		top.setLayout(new GridLayout());
		orControl= new PSOverriddingRuleEditControl(true,true);
		orControl.createControl(top);
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
	 * To get the overriding rule which is being edited
	 * @return return the overriding rule
	 */
	public PSOverridingRule getOverrindingRule() 
	{
		//return overrindingRule;
		return (PSOverridingRule)orControl.getModelObject();
	}

	/**
	 * Sets the overrinding rule to edit
	 * @param overrindingRule -- the overriding rule to edit
	 */
	public void setOverrindingRule(PSOverridingRule overrindingRule) 
	{
		orControl.setOverridingRule(overrindingRule);
	}
	
	
}
