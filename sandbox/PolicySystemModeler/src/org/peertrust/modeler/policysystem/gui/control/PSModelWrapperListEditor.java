package org.peertrust.modeler.policysystem.gui.control;


import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
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
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;




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
	
	/**
	 * The type of the model object list to create
	 */
	private Class modelObjectType ;
	
	
	/**
	 * The parent compiosite; used to get the list
	 */
	private Composite parent;
	
	/**
	 * Use to monitor for list element deletion
	 */
	private ListElementDelMonitor listElementDelMonitor; 
	
	private static final Logger logger=
		Logger.getLogger(PSModelWrapperListEditor.class);
	
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
	protected String createList(PSModelObject[] modelItems) 
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
				currentLabel= modelItems[i].getLabel().getValue();
				modelItemTable.put(currentLabel,modelItems[i]);
				stringItems[i]=currentLabel;
				
			}		
			return createList(stringItems);
		}
	}
	
	
	/**
	 * Remove all model objects
	 */
	public void clear()
	{
		modelItemTable.clear();
		List list=super.getListControl(parent);
		list.removeAll();
	}
	
	/**
	 * Adds the passed model objects in the list editor
	 * @param modelItems -- an array containing the model 
	 * 			object wrappers
	 */
	protected void addList(PSModelObject[] modelItems) 
	{
		logger.info("adding to list modeilItems:"+modelItems);
		if(modelItems==null)
		{
			logger.warn("modeilItems is null");
			return;
		}
		else
		{
			List list=super.getListControl(parent);
			//String stringItems[]= new String[modelItems.length];
			String currentLabel;
			for(int i=0; i<modelItems.length;i++)
			{
				currentLabel= modelItems[i].getLabel().getValue();
				if(!modelItemTable.containsKey(currentLabel))
				{
					modelItemTable.put(currentLabel,modelItems[i]);
					list.add(currentLabel);
				}
				else
				{
					logger.warn("currentLabel in List:"+currentLabel);
				}
				
			}
			logger.info("Current list items:"+list.getItemCount());
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
	
	public Vector getListModelObject()
	{
		Vector vect= new Vector();
		List list=getListControl(parent);
		int max=list.getItemCount();
		String item;
		for(int i=0; i<max;i++)
		{
			item=list.getItem(i);
			if(item!=null)
			{
				Object obj=modelItemTable.get(item);
				if(obj!=null)
				{
					vect.add(obj);
				}
			}
		}
			
		return vect;
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
				
				PSModelObject[] policies= 
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
