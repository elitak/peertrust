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
import javax.swing.table.DefaultTableModel;



import edu.stanford.smi.protege.event.ClsEvent;
import edu.stanford.smi.protege.event.ClsListener;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protege.event.FrameListener;
import edu.stanford.smi.protege.event.InstanceEvent;
import edu.stanford.smi.protege.event.InstanceListener;
import edu.stanford.smi.protege.event.SlotEvent;
import edu.stanford.smi.protege.event.SlotListener;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;

/**
 * Table model for cls policy;
 * @author congo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClsPolicyTableModel extends AbstractTableModel implements ClsListener, InstanceListener,FrameListener,SlotListener{
	//Nr Type Pol DefCls
	private static int COLUMN_COUNT=5;
	
	private Cls modelCls;
	private Vector policyData;
	private Vector policyDataNotOverridden;
	
	private PolicyFrameworkModel policyFrameworkModel;
	
	public ClsPolicyTableModel(PolicyFrameworkModel policyFrameworkModel){
		super();
		policyData= new Vector(20);
		policyDataNotOverridden= new Vector(20);
		this.policyFrameworkModel= policyFrameworkModel;
		//this.addColumn("Nr");
		//this.addColumn("Type");
	}
	
	public void setCls(Cls cls){
		if(cls==null){
			System.out.println("new Model cls is null!");
			modelCls=null;
			this.policyData.removeAllElements();
			this.policyDataNotOverridden.removeAllElements();
			return; //TODO BETTER WAY
		}
		if(cls.isDeleted()){
			System.out.println("new Model cls is null!");
			modelCls=null;
			this.policyData.removeAllElements();
			this.policyDataNotOverridden.removeAllElements();
			return; //TODO BETTER WAY1
		}
		
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
				
				///cache and listen for events
				this.modelCls=cls;
				//this.modelCls.addClsListener(this);
				//modelCls.getKnowledgeBase().getInstance(modelCls.getName()).addInstanceListener(this);
				TableModelEvent tableModelEvent= new TableModelEvent(this);
				makeModelRow();
				this.fireTableChanged(tableModelEvent);
		//		System.out.println("PolicyTableModel cls:"+cls.getBrowserText()+
		//							"col1Size:"+policyData.size());
				return;
		
	}
	
	public void resetTableModel(){
		TableModelEvent tableModelEvent= new TableModelEvent(this);
		makeModelRow();
		this.fireTableChanged(tableModelEvent);
		//System.out.println("Resetinggggggggggggggggggggggggggggggggggggggggg");
		return;
	}
	
	private void makeModelRow(){
		//Slot clsPolicySlot= modelCls.getDirectOwnSlotValue(slot);
		if(modelCls!=null){			
			PolicyFrameworkModel frameworkModel= new PolicyFrameworkModel(modelCls.getKnowledgeBase());
			policyData.removeAllElements();
			this.policyDataNotOverridden.removeAllElements();
			policyData.addAll(frameworkModel.getAllPolicies(this.modelCls));
			
			policyDataNotOverridden.addAll(policyData);
			//policyDataNotOverridden.removeAll(frameworkModel.getOverriddablePolicies(this.modelCls));
			Instance instance=null;
			for(Iterator it=policyData.iterator();it.hasNext();){
				instance=((PolicyFrameworkModel.PolicyData)it.next()).policyInst;
				instance.removeInstanceListener(this);
				instance.removeFrameListener(this);
				
			}
			//Filter the data
			//System.out.println("*********************************************************************************");
			Vector alreadyOverridden= new Vector(10);
			
			for(Iterator it=policyData.iterator();it.hasNext();){
				PolicyFrameworkModel.PolicyData polData=((PolicyFrameworkModel.PolicyData)it.next());
				//System.out.println("overRiden:"+polData.policyOverridden);
				//policyDataNotOverridden.remove(polData.policyOverridden);
				if(polData.policyOverridden!=null){
//					policyDataNotOverridden.remove(polData);
					alreadyOverridden.add(polData.policyOverridden);
				}
			}
			policyDataNotOverridden.removeAll(alreadyOverridden);
			//System.out.println("*********************************************************************************");
			
			for(Iterator it=policyData.iterator();it.hasNext();){
				instance=((PolicyFrameworkModel.PolicyData)it.next()).policyInst;
				instance.addInstanceListener(this);
				instance.addFrameListener(this);
				
			}
		}else{
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
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
//		if(policyData==null){
//			return 0;
//		}else{
//			return policyData.size();
//			//return policyDataNoOverridden.size();
//		}
		if(policyDataNotOverridden==null){
			return 0;
		}else{
			return policyDataNotOverridden.size();
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
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
	
public boolean getIsLocallyLocal(int row){
	return ((PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row)).isLocallyDefined;
}

public void removePolicy(int row){
	PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row);
	if(polData.isLocallyDefined){
		policyFrameworkModel.changeLocalPolicy(polData.policyInst,PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME," ",null);
		fireTableDataChanged();
	}
	return;
}

public Collection getOverriddables(int row){
	PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row);
	//policyFrameworkModel.changeLocalPolicy(polData.policyInst,PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME," ",null);
	Cls cls=policyFrameworkModel.getKnowledgeBase().getCls(polData.definingClassName);
	//System.out.println("Overridable for :"+cls);
	return policyFrameworkModel.getOverriddablePolicies(cls);
	//fireTableDataChanged();
	//return;
}

public String getInstanceName(Instance inst){
	Object name= inst.getOwnSlotValue(policyFrameworkModel.getPolicySlotName());
	if(name==null){
		return null;
	}else{
		return name.toString();
	}
}

public void addNewPolicy(){
	policyFrameworkModel.createLocalPolicy(this.modelCls,PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME,"My Policy","M");
}

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
	
	
	public void setValueAt(Object newValue, int row, int column) {
		if(column == 0){//set Name
			PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyDataNotOverridden.get(row);
//			policyFrameworkModel.changeLocalPolicy(polData.policyInst,
//						PolicyFrameworkModel.DEFAULT_POLICY_TYPE_SLOT_NAME,polData.policyType,newValue.toString());
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
//			policyFrameworkModel.changeLocalPolicy(polData.policyInst,
//						//PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME,polData.policy,newValue.toString());
//					PolicyFrameworkModel.DEFAULT_POLICY_SLOT_VALUE,polData.policy,newValue.toString());
			policyFrameworkModel.setPolicyOverridden(polData.policyInst,(Instance)newValue);
			this.resetTableModel();
			return;
		}else{
			return;
		}
		
	}
//public boolean getRowCls(){
//	((PolicyFrameworkModel.PolicyData)policyData.get(row)).
//}
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

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.SlotListener#templateSlotClsAdded(edu.stanford.smi.protege.event.SlotEvent)
	 */
	public void templateSlotClsAdded(SlotEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.SlotListener#templateSlotClsRemoved(edu.stanford.smi.protege.event.SlotEvent)
	 */
	public void templateSlotClsRemoved(SlotEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.SlotListener#directSubslotAdded(edu.stanford.smi.protege.event.SlotEvent)
	 */
	public void directSubslotAdded(SlotEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.SlotListener#directSubslotRemoved(edu.stanford.smi.protege.event.SlotEvent)
	 */
	public void directSubslotRemoved(SlotEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.SlotListener#directSubslotMoved(edu.stanford.smi.protege.event.SlotEvent)
	 */
	public void directSubslotMoved(SlotEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.SlotListener#directSuperslotAdded(edu.stanford.smi.protege.event.SlotEvent)
	 */
	public void directSuperslotAdded(SlotEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.event.SlotListener#directSuperslotRemoved(edu.stanford.smi.protege.event.SlotEvent)
	 */
	public void directSuperslotRemoved(SlotEvent event) {
		// TODO Auto-generated method stub
		
	}
}
