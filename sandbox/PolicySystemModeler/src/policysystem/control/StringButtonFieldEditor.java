package policysystem.control;

import org.eclipse.swt.widgets.Composite;

public class StringButtonFieldEditor extends
		org.eclipse.jface.preference.StringButtonFieldEditor {
	private FieldValueProvider fieldValueProvider;
	
	
	public StringButtonFieldEditor() {
		super();
	}


	public StringButtonFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}


	protected String changePressed() 
	{
		if(fieldValueProvider==null)
		{
			return null;
		}
		return fieldValueProvider.getFieldValue();
	}


	public FieldValueProvider getFieldValueProvider() {
		return fieldValueProvider;
	}


	public void setFieldValueProvider(FieldValueProvider fieldValueProvider) 
	{
		this.fieldValueProvider = fieldValueProvider;
	}

	
}
