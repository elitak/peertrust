package policysystem.model;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

public class PSModelWrapperListEditor extends ListEditor {

	public PSModelWrapperListEditor() 
	{
		super();
		super.adjustForNumColumns(20);
	}

	public PSModelWrapperListEditor(String name, String labelText,
			Composite parent) 
	{
		super(name, labelText, parent);
		
	}

	protected String createList(String[] items) 
	{
		return null;
	}

	protected String getNewInputObject() 
	{
		super.adjustForNumColumns(20);
		return "new"+System.currentTimeMillis();
	}

	protected String[] parseString(String stringList) 
	{
		return null;
	}

}
