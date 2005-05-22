/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package org.peertrust.protege.plugin;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import edu.stanford.smi.protege.event.ClsEvent;
import edu.stanford.smi.protege.event.ClsListener;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protege.event.FrameListener;
import edu.stanford.smi.protege.event.InstanceEvent;
import edu.stanford.smi.protege.event.InstanceListener;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;

/**
 * <p>
 * Table model for cls policy;
 * Its models a table  with 5 column and uses vectors to store the very data.
 * </p><p>
 * $Id: ClsPolicyTableModel.java,v 1.2 2005/05/22 17:56:45 dolmedilla Exp $
 * <br/>
 * Date: 30-Oct-2004
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:45 $
 * by $Author: dolmedilla $
 * </p>
 * @author Patrice Congo 
 */
public class ClsPolicyTableModel extends AbstractTableModel implements ClsListener, InstanceListener,FrameListener//,SlotListener
{
	//Nr Type Pol DefCls
	private static int COLUMN_COUNT=5;
	
	/** the analysed class */
	private Cls modelCls;
	
	/** a vector of PolicyData objects that represent the policies, which apply to the analysed class */
	private Vector policyData;
	
	/** a vector of PolicyData objects that represent the policies, which apply to super classes but have been overridden*/ 
	private Vector policyDataNotOverridden;
	
	/** the policy frame work.*/
	private PolicyFrameworkModel policyFrameworkModel;
	
	/**
	 * Build a virgin ClsPolicyTableModel object
	 * @param policyFrameworkModel
	 */
	public ClsPolicyTableModel(PolicyFrameworkModel policyFrameworkModel){
		super();
		policyData= new Vector(20);
		policyDataNotOverridden= new Vector(20);
		this.policyFrameworkModel= policyFrameworkModel;
		return;
	}
	
	/**
	 * To set the class which policies are to be examined.
	 * @param cls
	 */
	public void setCls(Cls cls){
		
		if(cls==null){
			System.out.println("new Model cls is null!");
			modelCls=null;
			this.policyData.removeAllElements();
			this.policyDataNotOverridden.removeAllElements();
			return; //TODO BETTER WAY
		}
		
		if(cls.isDeleted()){
			System.out.println("new Model cls is deleted!");
			modelCls=null;
			this.policyData.removeAllElements();
			this.policyDataNotOverridden.removeAllElements();
			return; //TODO BETTER WAY1
		}
		//clean up before setting a new class a class is already set 
		if(this.modelCls!=null){
			this.modelCls.removeClsListener(this);
			String modelClsName= modelCls.getName();
			if(modelClsName!= null){
				System.out.println("modelClsdd:"+modelCls.getName());
				Instance clsInst = modelCls.getKnowledgeBase().getInstance(modelCls.getName());
				if(clsInst!=null){
					clsInst.removeInstanceListener(this);
				}
			}
		}
				
		///cache the class object, make the model data and notify for changes.
		this.modelCls=cls;
		TableModelEvent tableModelEvent= new TableModelEvent(this);
		makeModelRow();
		this.fireTableChanged(tableModelEvent);
		
		return;
		
	}
	
	/**
	 * Recalculate the model data and notify the changes to listener.
	 */
	public void resetTableModel(){
		TableModelEvent tableModelEvent= new TableModelEvent(this);
		makeModelRow();
		this.fireTableChanged(tableModelEvent);
		return;
	}
	
	/**
	 * Find the policies that apply to the classes.
	 * I.e. To building the policy data vector and the policyNotOvveridden vector.
	 */
	private void makeModelRow(){
		//Slot clsPolicySlot= modelCls.getDirectOwnSlotValue(slot);
		if(modelCls!=null){			
			//stop listening and clean up if a class exists
			Instance instance=null;
			for(Iterator it=policyData.iterator();it.hasNext();){
				instance=((PolicyFrameworkModel.PolicyData)it.next()).policyInst;
				instance.removeInstanceListener(this);
				instance.removeFrameListener(this);				
			}			
			policyData.removeAllElements();
			this.policyDataNotOverridden.removeAllElements();
			
			//build the new policy data vector
			policyData.addAll(policyFrameworkModel.getAllPolicies(this.modelCls));			
			//find the policy which have not been overridden.
			policyDataNotOverridden.addAll(policyData);
			Vector alreadyOverridden= new Vector(10);			
			for(Iterator it=policyData.iterator();it.hasNext();){
				PolicyFrameworkModel.PolicyData polData=((PolicyFrameworkModel.PolicyData)it.next());
				if(polData.policyOverridden!=null){
					alreadyOverridden.add(polData.policyOverridden);
				}
			}
			policyDataNotOverridden.removeAll(alreadyOverridden);
			
			//start listening
			for(Iterator it=policyData.iterator();it.hasNext();){
				instance=((PolicyFrameworkModel.PolicyData)it.next()).policyInst;
				instance.addInstanceListener(this);
				instance.addFrameListener(this);
				
			}
		}else{
			//Clean upn
			Instance instance=null;
			for(Iterator it=policyData.iterator();it.hasNext();){
				instance=((PolicyFrameworkModel.PolicyData)it.next()).policyInst;
				instance.removeInstanceListener(this);
				instance.removeFrameListener(this);				
			}
			policyData.removeAllElements();
			policyDataNotOverridden.removeAllElements();
			return;
		}
		return;
	}
	
	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if(policyDataNotOverridden==null){
			return 0;
		}else{
			return policyDataNotOverridden.size();
		}
		
	}

	/**
	 * To get the cell value.
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 * @param row - the cell row
	 * @param col - the cell col
	 * @return the cell value. if col is:
	 * 	<ul>
	 * 		<li> 0 the policy name.
	 * 		<li> 1 the policy type
	 * 		<li> 2 the policy
	 * 		<li> 3 the defining class for a policy
	 * 		<li> 4 the class that the policy has overridden.
	 * 		<li> otherwise the pair (row,col)
	 * </ul>
	 */
	public Object getValueAt(int row, int col) {
		switch(col){
		 case 0:  return ((PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row)).policyName;//Integer.toString(row);
		 case 1:  return ((PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row)).policyType;
		 case 2:  return ((PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row)).policy;
		 case 3:  return ((PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row)).definingClassName;
		 case 4: return  ((PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row)).policyOverridden;
		 default: return "("+row+","+col+")";
		}		
	}
	
	/**
	 * To get the column name.
	 * @param colIndex - is the index of the column 
	 * @return the column name.
	 */
	public String getColumnName(int colIndex) {
		switch(colIndex){
			case 0: return "Name";
			case 1: return "Type";
			case 2: return "Policy";
			case 3: return "Defining Cls";
			case 4: return "Overridden Policy";
			default:return super.getColumnName(colIndex);
		}
		//return super.getColumnName(arg0);
	}

/**
 * To check whether the policy at a specific row is locally defined or inheritated.
 * This flag is available in the PolicyData object.
 * @param row - the number of the row.
 * @return true if the policy is defined in class to examine or false.
 */	
public boolean getIsLocallyLocal(int row){
	return ((PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row)).isLocallyDefined;
}

/**
 * To remove a localy defined policy at specific row.
 * 
 * @param row
 */
public void removePolicy(int row){
	PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row);
	if(polData.isLocallyDefined){
		policyFrameworkModel.changeLocalPolicy(polData.policyInst,PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME," ",null);
		fireTableDataChanged();
	}
	return;
}

/**
 * To get the policy, which may be overridden by the policy in the current row.
 * @param row
 * @return a collection of overridable policies.
 */
public Collection getOverriddables(int row){
	PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row);
	Cls cls=policyFrameworkModel.getKnowledgeBase().getCls(polData.definingClassName);
	return policyFrameworkModel.getOverriddablePolicies(cls);
	
}

/**
 * To get the name of an instance.
 * @param inst
 * @return
 */
public String getInstanceName(Instance inst){
	if(inst!=null){
		return inst.getName();
	}else{
		return null;
	}
}

/**
 * To add a new policy to a class.
 */
public void addNewPolicy(){
	policyFrameworkModel.createLocalPolicy(this.modelCls,PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME,"My Policy","M");
}
/**
 * @see javax.swing.table.TableModel#isCellEditable(int, int)
 */
public boolean isCellEditable(int row, int column) {
	if(((PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row)).isLocallyDefined){
		if(column == 0){
			return true;
		}else if(column == 1){
			return true;
		}else if(column==2){
			return true;
		}else if(column==4){
			return true;
		}else{
			return false;
		}
	}else{
		return false;
	}
}
	
	/**
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object newValue, int row, int column) {
		if(column == 0){//set Name
			PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row);
			try{
				policyFrameworkModel.setPolicyName(polData.policyInst,""+newValue);
			}catch(IllegalArgumentException ilArgEx){
				
			}
			resetTableModel();
			return;
		}else if(column == 1){//set type
			PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row);
			policyFrameworkModel.changeLocalPolicy(polData.policyInst,
						PolicyFrameworkModel.DEFAULT_POLICY_TYPE_SLOT_NAME,polData.policyType,newValue.toString());
			resetTableModel();
			return;
		}else if(column==2){//set policy
			PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row);
			policyFrameworkModel.changeLocalPolicy(polData.policyInst,
						//PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME,polData.policy,newValue.toString());
					PolicyFrameworkModel.DEFAULT_POLICY_SLOT_VALUE,polData.policy,newValue.toString());
			this.resetTableModel();
			return;
		}else if(column==4){//set policy
			PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row);
			policyFrameworkModel.setPolicyOverridden(polData.policyInst,(Instance)newValue);
			this.resetTableModel();
			return;
		}else{
			return;
		}
		
	}

	/*********************** CLSLISTENERFUNCTIONS *****************************************/
	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#directInstanceAdded(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void directInstanceAdded(ClsEvent event) {
		resetTableModel();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#directInstanceRemoved(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void directInstanceRemoved(ClsEvent event) {
		resetTableModel();
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#directSubclassAdded(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void directSubclassAdded(ClsEvent event) {
		resetTableModel();		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#directSubclassMoved(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void directSubclassMoved(ClsEvent event) {
		resetTableModel();		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#directSubclassRemoved(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void directSubclassRemoved(ClsEvent event) {
				
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#directSuperclassAdded(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void directSuperclassAdded(ClsEvent event) {
		resetTableModel();		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#directSuperclassRemoved(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void directSuperclassRemoved(ClsEvent event) {
		resetTableModel();		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#templateFacetAdded(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void templateFacetAdded(ClsEvent event) {
		resetTableModel();		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#templateFacetRemoved(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void templateFacetRemoved(ClsEvent event) {
		resetTableModel();
		return;
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#templateFacetValueChanged(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void templateFacetValueChanged(ClsEvent event) {
		resetTableModel();		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#templateSlotAdded(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void templateSlotAdded(ClsEvent event) {
		resetTableModel();
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#templateSlotRemoved(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void templateSlotRemoved(ClsEvent event) {
		resetTableModel();
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.ClsListener#templateSlotValueChanged(edu.stanford.smi.protege.event.ClsEvent)
	 */
	public void templateSlotValueChanged(ClsEvent event) {
		resetTableModel();		
	}
	/********************************************************************************/

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.InstanceListener#directTypeAdded(edu.stanford.smi.protege.event.InstanceEvent)
	 */
	public void directTypeAdded(InstanceEvent event) {
		resetTableModel();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.InstanceListener#directTypeRemoved(edu.stanford.smi.protege.event.InstanceEvent)
	 */
	public void directTypeRemoved(InstanceEvent event) {
		resetTableModel();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#browserTextChanged(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void browserTextChanged(FrameEvent event) {
		resetTableModel();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#deleted(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void deleted(FrameEvent event) {
		resetTableModel();
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#nameChanged(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void nameChanged(FrameEvent event) {
		resetTableModel();
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#ownFacetAdded(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void ownFacetAdded(FrameEvent event) {
		resetTableModel();
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#ownFacetRemoved(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void ownFacetRemoved(FrameEvent event) {
		resetTableModel();
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#ownFacetValueChanged(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void ownFacetValueChanged(FrameEvent event) {
		resetTableModel();
		System.out.println("ownSlotValueChanged4");
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#ownSlotAdded(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void ownSlotAdded(FrameEvent event) {
		resetTableModel();		
		System.out.println("ownSlotValueChanged3");
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#ownSlotRemoved(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void ownSlotRemoved(FrameEvent event) {
		resetTableModel();
		System.out.println("ownSlotValueChanged2");
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#ownSlotValueChanged(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void ownSlotValueChanged(FrameEvent event) {
		System.out.println("ownSlotValueChanged");
		resetTableModel();		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.FrameListener#visibilityChanged(edu.stanford.smi.protege.event.FrameEvent)
	 */
	public void visibilityChanged(FrameEvent event) {
		resetTableModel();
		System.out.println("ownSlotValueChanged1");
	}
	/************************************************************************************/

//	/* (non-Javadoc)
//	 * @see edu.stanford.smi.protege.event.SlotListener#templateSlotClsAdded(edu.stanford.smi.protege.event.SlotEvent)
//	 */
//	public void templateSlotClsAdded(SlotEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see edu.stanford.smi.protege.event.SlotListener#templateSlotClsRemoved(edu.stanford.smi.protege.event.SlotEvent)
//	 */
//	public void templateSlotClsRemoved(SlotEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see edu.stanford.smi.protege.event.SlotListener#directSubslotAdded(edu.stanford.smi.protege.event.SlotEvent)
//	 */
//	public void directSubslotAdded(SlotEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see edu.stanford.smi.protege.event.SlotListener#directSubslotRemoved(edu.stanford.smi.protege.event.SlotEvent)
//	 */
//	public void directSubslotRemoved(SlotEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see edu.stanford.smi.protege.event.SlotListener#directSubslotMoved(edu.stanford.smi.protege.event.SlotEvent)
//	 */
//	public void directSubslotMoved(SlotEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see edu.stanford.smi.protege.event.SlotListener#directSuperslotAdded(edu.stanford.smi.protege.event.SlotEvent)
//	 */
//	public void directSuperslotAdded(SlotEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see edu.stanford.smi.protege.event.SlotListener#directSuperslotRemoved(edu.stanford.smi.protege.event.SlotEvent)
//	 */
//	public void directSuperslotRemoved(SlotEvent event) {
//		// TODO Auto-generated method stub
//		
//	}
}
