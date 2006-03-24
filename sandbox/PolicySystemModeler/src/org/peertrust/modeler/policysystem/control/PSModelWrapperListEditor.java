package org.peertrust.modeler.policysystem.control;

import java.util.ArrayList;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.peertrust.modeler.policysystem.model.abtract.ModelObjectWrapper;


import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class PSModelWrapperListEditor extends ListEditor 
{
	private static final String SEPARATOR="|$§|"; 
	private Hashtable modelItemTable= new Hashtable(); 
	
	public PSModelWrapperListEditor() 
	{
		super();
		super.adjustForNumColumns(20);
	}

	public PSModelWrapperListEditor(
							String name, 
							String labelText,
							Composite parent) 
	{
		super(name, labelText, parent);
		
	}

	protected String createList(String[] items) 
	{
		if(items==null)
		{
			return null;
		}
		
		StringBuffer buffer= new StringBuffer();
		for(int i=0;i<items.length;i++)
		{
			buffer.append(items[i]);
			buffer.append(SEPARATOR);
		}
		return null;
	}

	protected String createList(ModelObjectWrapper[] modelItems) 
	{
		if(modelItems==null)
		{
			return null;
		}
		else
		{
			String stringItems[]= new String[modelItems.length];
			String currentLabel;
			for(int i=0; i<modelItems.length;)
			{
				currentLabel= modelItems[i].getLabel();
				modelItemTable.put(currentLabel,modelItems[i]);
				stringItems[i]=currentLabel;
			}		
			return createList(stringItems);
		}
	}
	
	protected String getNewInputObject() 
	{
		//super.adjustForNumColumns(20);
		return "new"+System.currentTimeMillis();
	}

	/**
	 * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
	 */
	protected String[] parseString(String stringList) 
	{
		if(stringList==null)
		{
			return new String[]{};
		}
		return stringList.split(SEPARATOR);
		//return null;
	}

}
