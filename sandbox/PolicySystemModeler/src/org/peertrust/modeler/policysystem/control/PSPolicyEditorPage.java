package org.peertrust.modeler.policysystem.control;


import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


import org.eclipse.ui.part.Page;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;



public class PSPolicyEditorPage extends Page 
{
	private Composite top;
	StringFieldEditor valueFieldEditor;
	Button setButton;
	private PSPolicyEditorControl policyEditorControl;
	public void createControl(Composite parent) 
	{
		top = new Composite(parent, SWT.LEFT|SWT.FILL);
		top.setLayout(new GridLayout());
		policyEditorControl= new PSPolicyEditorControl(true);
		policyEditorControl.createControl(top);
	}

	public Control getControl() 
	{
		return top;
	}

	public void setFocus() 
	{
		
	}

	public PSPolicy getPsPolicy() {
		//return psPolicy;
		return (PSPolicy)policyEditorControl.getModelObject();
	}

	public void setPsPolicy(PSPolicy psPolicy) {
		policyEditorControl.setModelObject(psPolicy);
	}
	
	
}
