package org.peertrust.modeler.policysystem.control;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.peertrust.modeler.policysystem.model.abtract.ModelObjectWrapper;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;




/**
 * List editor for a wrapper objects
 * 
 * @author Patrice Congo
 *
 */
public class PSModelWrapperListEditor extends ListEditor 
{
	/** the separator use to create a string 
	 * representation of the list elements*/
	private static final String SEPARATOR="|$§|"; 
	
	/** 
	 * Contains the model wrapper object.
	 * their respective labels are used hase key
	 */
	private Hashtable modelItemTable= new Hashtable(); 
	
	/**
	 * The mechanism used to create new List Items
	 */
	private PSModelWrapperListEditorCreateItemMechanism createItemMechanism;
	
	private Class modelObjectType ;
	
	
	private Composite parent;
	
	/**
	 * Deafult creator
	 *
	 */
	public PSModelWrapperListEditor() 
	{
		super();
		super.adjustForNumColumns(20);
	}

	
	/**
	 * Creates a PSModelWrapperListEditor 
	 * @param name -- the name of the editor
	 * @param labelText -- the label for the editor control
	 * @param parent -- the parent control
	 * @param modelObjectType -- the class of the model Object in the list
	 */
	public PSModelWrapperListEditor(
							String name, 
							String labelText,
							Composite parent,
							Class modelObjectType) 
	{
		super(name, labelText, parent);
		this.modelObjectType=modelObjectType;
		createItemMechanism= 
			makePSPolicyCreateItemMechanism();
		this.parent=parent;
		
	}

	/**
	 * @see org.eclipse.jface.preference.ListEditor#createList(java.lang.String[])
	 */
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

	
	/**
	 * Create a list from based on the provided model object wrappers
	 * @param modelItems -- an array ob model object wrappers
	 * @return a string representing the model wrapper list
	 */
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
			for(int i=0; i<modelItems.length;i++)
			{
				currentLabel= modelItems[i].getLabel();
				modelItemTable.put(currentLabel,modelItems[i]);
				stringItems[i]=currentLabel;
				
			}		
			return createList(stringItems);
		}
	}
	
	
	
	protected void addList(ModelObjectWrapper[] modelItems) 
	{
		if(modelItems==null)
		{
			return;
		}
		else
		{
			List list=super.getListControl(parent);
			String stringItems[]= new String[modelItems.length];
			String currentLabel;
			for(int i=0; i<modelItems.length;i++)
			{
				currentLabel= modelItems[i].getLabel();
				if(!modelItemTable.containsKey(currentLabel))
				{
					modelItemTable.put(currentLabel,modelItems[i]);
					list.add(currentLabel);
				}
				
			}		
//			list.removeAll();
//			ModelObjectWrapper mow;
//			for(	Iterator it=modelItemTable.values().iterator();
//					it.hasNext();
//					)
//			{
//				mow.hashCode()
//			}
			return;
		}
	}
	
	/**
	 * @see org.eclipse.jface.preference.ListEditor#getNewInputObject()
	 */
	protected String getNewInputObject() 
	{
		try {
			if(createItemMechanism==null)
			{
				return null;
			}
			
			return createItemMechanism.create(this,modelObjectType);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
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
	}

	public static PSModelWrapperListEditorCreateItemMechanism 
										makePSPolicyCreateItemMechanism()
	{
		return new PSModelWrapperListEditorCreateItemMechanism()
		{ 

			public String create(	PSModelWrapperListEditor listEditor,
									Class modelObjectType) 
			{
				
				ModelObjectWrapper[] policies= 
					//ChooserWizardPage.choosePlicies(listEditor.getShell());
					ChooserWizardPage.chooseModelObjects(
								listEditor.getShell(),
								PSPolicy.class);;
				if(policies==null)
				{
					return null;
				}
				else if(policies.length==0)
				{
					return null;
				}
				else
				{
//					PSPolicy policy;
//					for(int i=0;i<policies.length;i++)
//					{
//						policy=policies[i];
//						modelWrapperStore.put(policy.getLabel(),policy);
//						
//					}
					listEditor.addList(policies);
					return null;
					
				}
				
			}

		
		};
		
	}
}
