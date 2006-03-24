package org.peertrust.modeler.policysystem.control;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.part.Page;

public class NewProjectDlgEditor extends Page 
{
	private static final int VERTICAL_DIALOG_UNITS_PER_CHAR = 8;
	private static final int LIST_HEIGHT_IN_CHARS = 10;
	private static final int LIST_HEIGHT_IN_DLUS = 
		LIST_HEIGHT_IN_CHARS * VERTICAL_DIALOG_UNITS_PER_CHAR;

	private List exemptTagsList;
	private Text textField;
	private RadioGroupFieldEditor errors;
	private Button removeTag;
	private Composite top;
	
	public void createControl(Composite parent) 
	{
		top = new Composite(parent, SWT.LEFT);

		// Sets the layout data for the top composite's 
		// place in its parent's layout.
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Sets the layout for the top composite's 
		// children to populate.
		top.setLayout(new GridLayout());
		///////////////////////////////////////////////////////
		
		Composite addRemoveGroup = new Composite(top, SWT.NONE);
		addRemoveGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout addRemoveLayout = new GridLayout();
		addRemoveLayout.numColumns = 2;
		addRemoveLayout.marginHeight = 0;
		addRemoveLayout.marginWidth = 0;
		addRemoveGroup.setLayout(addRemoveLayout);
		
		// Create a composite for the add and remove buttons.
		Composite buttonGroup = new Composite(addRemoveGroup, SWT.NONE);
		buttonGroup.setLayoutData(new GridData());
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;
		buttonGroup.setLayout(buttonLayout);

		Button addTag = new Button(buttonGroup, SWT.NONE);
		addTag.setText("Add Ta&g");
		addTag.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
				//addTag();
			}
		});
		
		removeTag = new Button(buttonGroup, SWT.NONE);
		removeTag.setText("&Remove Tag");
		removeTag.setEnabled(false);
		removeTag.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {	
				exemptTagsList.remove(
					exemptTagsList.getSelectionIndex());
				//selectionChanged();
			}			
		});
		textField = new Text(addRemoveGroup, SWT.BORDER);
		
		GridData textData = new GridData(GridData.FILL_HORIZONTAL);
		textData.verticalAlignment = GridData.BEGINNING;
		textField.setLayoutData(textData);
		
	}

	public Control getControl() 
	{
		return top;
	}

	public void setFocus() 
	{
		
	}

}
