package org.peertrust.modeler.policysystem.control;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.peertrust.modeler.policysystem.model.abtract.ModelObjectWrapper;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;




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
	private ModelObjectSelectionMechanism createItemMechanism;
	
	private Class modelObjectType ;
	
	
	private Composite parent;
	
	private ListElementDelMonitor listElementDelMonitor; 
	
	/**
	 * Deafult creator
	 *
	 */
	private PSModelWrapperListEditor() 
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
		listElementDelMonitor=makeItemDeleteMonitor();
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
	
	
	
	/**
	 * Adds the passed model objects in the list editor
	 * @param modelItems -- an array containing the model 
	 * 			object wrappers
	 */
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
				else
				{
					System.out.println("currentLabel in List:"+currentLabel);
				}
				
			}		
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
			List list=getListControl(parent);
			
			String[] itemsToIgnore=null;
			if(list!=null)
			{
				itemsToIgnore=list.getItems();
			}
			return createItemMechanism.select(this,modelObjectType,itemsToIgnore);
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

	
	/**
	 * Utility method to make a ListElementDelMonitor 
	 * to monitor for list element delation  
	 * @return the created ListElementMonitor
	 */
	private ListElementDelMonitor makeItemDeleteMonitor()
	{
		ListElementDelMonitor mon=
			new ListElementDelMonitor(this,modelItemTable);
		return mon;
	}
	
	/**
	 * make ModelObjectSelectionMechanism used to create
	 * new list items
	 * @return a ModelObjectSelectionMechanism
	 */
	public static ModelObjectSelectionMechanism 
						makePSPolicyCreateItemMechanism()
	{
		return new ModelObjectSelectionMechanism()
		{ 
			
			public String select(	
						PSModelWrapperListEditor listEditor,
						Class modelObjectType,
						String[] itemsToIgnore) 
			{
				
				ModelObjectWrapper[] policies= 
					//ChooserWizardPage.choosePlicies(listEditor.getShell());
					ChooserWizardPage.chooseModelObjects(
								listEditor.getShell(),
								modelObjectType,//PSPolicy.class,
								itemsToIgnore);
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

					listEditor.addList(policies);
					return null;
					
				}
				
			}

					
		};
		
	}
	
	/**
	 * Class to monitior for list element deletion.
	 * And perform this deletion in the model element table
	 * @author pat_dev
	 *
	 */
	class ListElementDelMonitor implements 	SelectionListener,
											Listener
	{
		/**
		 * A array containing the lastly selected items
		 */
		private String[] oldSelection;
		
		/**
		 * The list control that hold the items
		 */
		private List list;
		
		/**
		 * The model item table whose labels are shown in the list
		 */
		private Hashtable modelItemTable;
		
		
		/**
		 * Create a ListElementDelMonitor to monitor the provided list
		 * and update the associated modlItemTable
		 * @param list -- the list control to monitro for items delection
		 * @param modelItemTable -- the associated model items, whose labels
		 * 			are shown in the list 
		 */
		public ListElementDelMonitor(PSModelWrapperListEditor listEditor, Hashtable modelItemTable)
		{
			if(	listEditor==null || 
				modelItemTable==null)
			{
				return;
			}
			else
			{
				String rmText=JFaceResources.getString("ListEditor.remove");
				Composite composite=
					listEditor.getButtonBoxControl(listEditor.parent);
				Control[] children=composite.getChildren();
				for(int i=0; i<children.length;i++)
				{
					if(children[i] instanceof Button)
					{
						if( ((Button)children[i]).getText().equals(rmText))
						{
							((Button)children[i]).addListener(
												SWT.Deactivate,
												this);
						}
					}
				}
				this.list=listEditor.getListControl(listEditor.parent);
				this.modelItemTable=modelItemTable;
				list.addSelectionListener(this);
				//list.addListener(SWT.Modify,this);
				//list.addListener(SWT.Selection,this);
			}
		}
		
		
		/**
		 * Update the model item table by removing the deleted list items
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) 
		{
			checkDelete();
		}

		/**
		 * Update the model item table by removing the deleted list items
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) 
		{
			widgetSelected(e);
		}


		/**
		 * Update the model item table by removing the deleted list items
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		public void handleEvent(Event event) 
		{
			checkDelete();
		}
		
		
		/**
		 * Utility method to update the model item table by removing 
		 * the deleted list items
		 */
		private void checkDelete()
		{
			String[] currentSelection=
				list.getSelection();
			if(oldSelection==null)
			{
				oldSelection=currentSelection;
			}
			else
			{
				for(int i=0; i<oldSelection.length;i++)
				{
					if(list.indexOf(oldSelection[i])<0)
					{
						modelItemTable.remove(oldSelection[i]);
					}
				}
				oldSelection=currentSelection;
			}
		}
	}
}
