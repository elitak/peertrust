package org.peertrust.modeler.policysystem.control;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jface.preference.FieldEditor;
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
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;


public class PSFilterEditorPage extends Page 
{
	public static final String CONDITION_SEP=";";
	private Composite top;
	private PSFilter psFilter;
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
		header.setText("Filter");
		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		///
		
		labelFiledEditor=
			new StringFieldEditor("labelFieldEditor","Label",top);
		valueFieldEditor=
			new StringFieldEditor("valueFieldEditor","Value",top);
		headerdd= new Label(top,SWT.NONE);
		headerdd.setText("Policies");
		Composite com= new Composite(top,SWT.NONE);
		headerdd= new Label(com,SWT.NONE);
		headerdd.setText(
				"                                            ");
		headerdd= new Label(com,SWT.NONE);
		PSModelWrapperListEditor le=
			new PSModelWrapperListEditor("namele","",com,PSPolicy.class);
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

	public PSFilter getPSFilter() {
		return psFilter;
	}

	public void setPSFilter(PSFilter psFilter) 
	{
		this.psFilter = psFilter;
		if(psFilter!=null)
		{
			labelFiledEditor.setStringValue(psFilter.getLabel());
			String conditionsAsString=
				conditionsToString(psFilter.getHasCondition());
			valueFieldEditor.setStringValue(
									conditionsAsString);
		}
		
	}
	
	/**
	 * Convert a condition to a string.
	 * The following format is used:<br/>
	 * cond1|cond2|cond3|
	 * @param conditions -- a vectorcontaining the conditions
	 * @return a string containing all the conditions
	 */
	private String conditionsToString(Vector conditions)
	{
		StringBuffer buffer= new StringBuffer();
		for(Iterator it=conditions.iterator(); it.hasNext();)
		{
			buffer.append(it.next());
			buffer.append(CONDITION_SEP);
		}
		
		return buffer.toString();	
	}
	
	
	/**
	 * Convert a String into an array of condition
	 * 
	 * @see PSFilterEditorPage#conditionsToString(Vector)
	 * @param condString -- the string representing the condition
	 * @return an array containing the computed conditions
	 */
	private String[] stringToCondition(String condString)
	{
		if(condString==null)
		{
			return new String[]{};
		}
		return condString.split(CONDITION_SEP);
		
	}
	
	/**
	 * Use to save the editor changes
	 */
	private void saveEdit()
	{
		try {
			if(psFilter==null)
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
			psFilter.setLabel(newLabel);
			
			psFilter.removeAllConditions();
			String[] conditions=stringToCondition(newValue);
			for(int i=0; i<conditions.length;i++)
			{
				psFilter.addHasCondition(conditions[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
