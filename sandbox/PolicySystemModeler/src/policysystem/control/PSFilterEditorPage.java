package policysystem.control;

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

import policysystem.model.abtract.PSFilter;
import policysystem.model.abtract.PSPolicy;

public class PSFilterEditorPage extends Page 
{
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
		setButton= new Button(top,SWT.NONE);
		setButton.setText("comit");
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
			valueFieldEditor.setStringValue(psFilter.getHasCondition().toString());
		}
	}
	
	private void saveEdit()
	{
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
	}
}
