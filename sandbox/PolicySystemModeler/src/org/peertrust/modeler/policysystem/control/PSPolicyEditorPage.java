package org.peertrust.modeler.policysystem.control;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import org.eclipse.ui.part.Page;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;


public class PSPolicyEditorPage extends Page 
{
	private Composite top;
	private PSPolicy psPolicy;
	private StringFieldEditor labelFiledEditor;
	StringFieldEditor valueFieldEditor;
	Button setButton;
	public void createControl(Composite parent) 
	{
		top = new Composite(parent, SWT.LEFT);
		//top.setLayout(new GridLayout());
		///////////////////////////////////////////////////////
		Control c;
		Label headerdd= new Label(top,SWT.NONE);		
		Label header= new Label(top,SWT.BORDER|SWT.HORIZONTAL|SWT.FILL);
		header.setText("Policy");
		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		///
		
		labelFiledEditor=
			new StringFieldEditor("labelFieldEditor","Label",top);
		valueFieldEditor=
			new StringFieldEditor("valueFieldEditor","Value",top);
		headerdd= new Label(top,SWT.NONE);
		setButton= new Button(top,SWT.NONE);
		setButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		setButton.setText("set");
		setButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
				saveEdit();
			}
		});
	}

	public Control getControl() 
	{
		return top;
	}

	public void setFocus() 
	{
		
	}

	public PSPolicy getPsPolicy() {
		return psPolicy;
	}

	public void setPsPolicy(PSPolicy psPolicy) {
		this.psPolicy = psPolicy;
		if(psPolicy!=null)
		{
			labelFiledEditor.setStringValue(psPolicy.getLabel());
			valueFieldEditor.setStringValue(psPolicy.getHasValue());
		}
	}
	
	private void saveEdit()
	{
		if(psPolicy==null)
		{
			return;
		}
		
		String newLabel=
			labelFiledEditor.getStringValue();
		String newValue=
			valueFieldEditor.getStringValue();
		if(newLabel==null || newValue==null)
		{
			return;
		}
		try {
			psPolicy.setLabel(newLabel);
			psPolicy.setHasValue(newValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
