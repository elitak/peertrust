/*
 * Created on Oct 30, 2004
 *
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
 * Table model for slot policy; The slot table has 3 column and uses a vector to cache policy data.
 * @author congo
 */
public class SlotPolicyTableModel extends AbstractTableModel implements InstanceListener,FrameListener,SlotListener{
	//Nr Type Pol DefCls
	public	static int COLUMN_COUNT=3;
	public  static int POLICY_COLUMN_NR=2;
 	
	private Slot modelSlot;
	Vector policyData;
	private PolicyFrameworkModel policyFrameworkModel;
	
	public SlotPolicyTableModel(PolicyFrameworkModel policyFrameworkModel){
		super();
		policyData= new Vector(20);
		this.policyFrameworkModel=policyFrameworkModel;
	}
	
	public void setSlot(Slot slot){
		if(slot==null){
			return; //TODO BETTER WAY
		}
		if(this.modelSlot!=null){
					this.modelSlot.removeInstanceListener(this);
					this.modelSlot.removeSlotListener(this);
					this.modelSlot.removeFrameListener(this);
					 Instance clsInst = modelSlot.getKnowledgeBase().getInstance(modelSlot.getName());
					clsInst.removeInstanceListener(this);
				}
				slot.addInstanceListener(this);
				slot.addSlotListener(this);
				slot.addFrameListener(this);
				///cache and listen for events
				this.modelSlot=slot;
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
	
	/**
	 * Utility method which build the policy data vector.
	 *
	 */
	private void makeModelRow(){
		//clean up.
		Instance instance=null;
		for(Iterator it=policyData.iterator();it.hasNext();){
			instance=((PolicyFrameworkModel.PolicyData)it.next()).policyInst;
			instance.removeInstanceListener(this);
			instance.removeFrameListener(this);
			
		}		
		policyData.removeAllElements();
		
		//get new policy data
		policyData.addAll(policyFrameworkModel.getAllPolicies(this.modelSlot));	

		//register for instance and frame events
		for(Iterator it=policyData.iterator();it.hasNext();){
			instance=((PolicyFrameworkModel.PolicyData)it.next()).policyInst;
			instance.addInstanceListener(this);
			instance.addFrameListener(this);
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
		if(policyData==null){
			return 0;
		}else{
			return policyData.size();
		}
	}

	/**
	 * To get the value of the specified cell.
	 * @return the value of the cell. if col:
	 * 	<ul>
	 * 		<li> 0 for the policy Name.
	 * 		<li> 1 for the policy Type.
	 * 		<li> 2 for the policy value
	 * 		<li> 3 for the defining class
	 * 		<li> otherwise the row and column number of the cell.
	 * </ul>
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		switch(col){
		 case 0:  return ((PolicyFrameworkModel.PolicyData)policyData.get(row)).policyName;//Integer.toString(row);
		 case 1:  return ((PolicyFrameworkModel.PolicyData)policyData.get(row)).policyType;
		 case 2:  return ((PolicyFrameworkModel.PolicyData)policyData.get(row)).policy;
		 case 3:  return ((PolicyFrameworkModel.PolicyData)policyData.get(row)).definingClassName;
		 default: return "("+row+","+col+")";
		}
		
	}
	
	/**
	 * To get The column Name.
	 */
	public String getColumnName(int colIndex) {
		switch(colIndex){
			case 0: return "Name";
			case 1: return "Type";
			case 2: return "Policy";
			case 3: return "Defining Cls";
			default:return super.getColumnName(colIndex);
		}
		//return super.getColumnName(arg0);
	}
	
	
	public boolean isCellEditable(int row, int column) {
		
		//return super.isCellEditable(arg0, arg1);
		if(column==0){
			return true;
		}else if(column==1){
			return true;
		}else if(column==2){
			return true;
		}else{
			return false;
		}
	}
	
	
	public void setValueAt(Object newValue, int row, int column) {
		
		if(column == 0){//set Name
			PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyData.get(row);

			try{
				policyFrameworkModel.setPolicyName(polData.policyInst,""+newValue);
			}catch(IllegalArgumentException ilArgEx){
				
			}
			resetTableModel();
			return;
		}else if(column == 1){//set type
			PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyData.get(row);
			policyFrameworkModel.changeLocalPolicy(polData.policyInst,
						PolicyFrameworkModel.DEFAULT_POLICY_TYPE_SLOT_NAME,polData.policyType,newValue.toString());
			resetTableModel();
			return;
		}else if(column==2){//set policy
			PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyData.get(row);
			policyFrameworkModel.changeLocalPolicy(polData.policyInst,
						//PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME,polData.policy,newValue.toString());
					PolicyFrameworkModel.DEFAULT_POLICY_SLOT_VALUE,polData.policy,newValue.toString());
			this.resetTableModel();
			return;
		}else{
			return;
		}
		//this.fireTableDataChanged();
	}
	
	/** 
	 * To add a new policy to the slot.
	 */
	public void addNewPolicy(){
		policyFrameworkModel.createLocalPolicy(
							modelSlot, 
							PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME,
							"policy",PolicyFrameworkModel.TYPE_MANDATORY);
		makeModelRow();
		fireTableDataChanged();
	}
	
	/** 
	 *To remove the policy in the specified row. 
	 * @param selRow
	 */
	public void removePolicy(int selRow){
		PolicyFrameworkModel.PolicyData polData= (PolicyFrameworkModel.PolicyData)policyData.get(selRow);
		if(polData.isLocallyDefined){
			policyFrameworkModel.changeLocalPolicy(polData.policyInst,PolicyFrameworkModel.DEFAULT_POLICY_CLS_POLICY_SLOT_NAME," ",null);
		}		
		makeModelRow();
		fireTableDataChanged();
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
