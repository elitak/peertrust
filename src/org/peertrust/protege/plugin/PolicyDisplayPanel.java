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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;
import edu.stanford.smi.protege.widget.InstanceListWidget;

import java.awt.GridLayout;
import java.util.*;

import javax.swing.table.AbstractTableModel;

/**
 * <p>
 * Show selected cls, its super clses and the policy that apply to it.
 * </p><p>
 * $Id: PolicyDisplayPanel.java,v 1.2 2005/05/22 17:56:45 dolmedilla Exp $
 * <br/>
 * Date: 30-Oct-2004
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:45 $
 * by $Author: dolmedilla $
 * </p>
 * @author Patrice Congo 
 */
public class PolicyDisplayPanel extends JPanel implements SelectionListener {
	///////////////////////////////////////////////////////////////////
	/**
	 *Table Model that holds the cls policies  
	 * @author congo
	 *
	 */
	class SlotPolicyTableModel extends AbstractTableModel{
		Vector policyValues= new Vector();
		public void setSlot(Slot policySlot){
			policyValues.removeAllElements();
			if(policySlot==null){
				//System.out.println("SlotPolicyTableModel:policy slot id null!");
				return;
			}
			///find policy slot
			Collection ownSlots= policySlot.getOwnSlots();
			Slot sl=null;
			for(Iterator it=ownSlots.iterator();it.hasNext();){
				sl=(Slot)it.next(); 
				//System.out.println("tablemodel col:"+sl.getName()+ 
				//					"  "+sl.getDirectType().getName());
				if(sl.getName().equals(":SLOT-VALUES")){
					//System.out.println("Found :SLOT-VALUES +++++++++++"+sl.getOwnSlots().size());
					policyValues.addAll(sl.getOwnSlots());
					/*for (Iterator it=sl.getOwnSlots();it.hasNext;){
						policyValues.a
					}/**/
				}
			}
		}
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 2;
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			//System.out.println("policyValues.size:"+policyValues.size());
			return policyValues.size();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(rowIndex==-1){
				return getColumnName(columnIndex);
			}else{
				return ((Slot)policyValues.elementAt(rowIndex));
			}
			
		}

		public String getColumnName(int column){
			switch(column){
				case 0: return "Nr";
				case 1: return "Policy";
				default: return super.getColumnName(column);
			}
		}

	}
	
	//////////////////////////////////////////////////////////////////
	/**
	 * Table to display class policy 
	 * @author congo
	 */
	
	class SlotPolicyTable extends JTable{
		SlotPolicyTableModel model= new SlotPolicyTableModel();
		/**
		 * 
		 */
		public SlotPolicyTable() {
			super();
			setModel(model);
			//this.setTableHeader(new JTableHeader());
			//this.getTableHeader().setVisible(true);
		}
		
//		public void setModelSlot(Slot slot){
//			model.setSlot(slot);
//		}
	}
	//////////////////////////////////////////////////////////////////
	
	
	
	
	JLabel label= new JLabel("old value");
	InstanceListWidget instanceListWidget= new InstanceListWidget();
	SlotPolicyTable policyTable= new SlotPolicyTable();
	PolicyFrameworkModel policyFrameworkModel;//= new PolicyFrameworkModel();
	PolicyClassTreeView policyClsesTreeView;
	
	private PolicyDisplayPanel(KnowledgeBase kb){
		policyFrameworkModel= new PolicyFrameworkModel(kb);
		policyClsesTreeView= new PolicyClassTreeView(kb.getProject());
		this.setLayout(new GridLayout(2,1));
		this.add(policyClsesTreeView);
		//this.add(policyTable);
		return;
	}
	private Slot findSlotPolicyy(Slot slot){
		//System.out.println("finding slotpolicy***********************************");
		if(!slot.getDirectType().getName().equals("PolicyTaggedSlot")){
			//System.out.println("findSlotPolicy: Not PolicyTaggedSlot");
			return null;
		}
		
		Collection col=slot.getOwnSlots();
		Slot sl;
		
		for(Iterator it=col.iterator();it.hasNext();){
			sl=(Slot)it.next();
			System.out.println("col:"+sl.getName()+ 
								"  "+sl.getDirectType().getName());
			if(sl.getName().equals("slotPolicy")){
				//System.out.println("found policy slot+++++++++++++++++++++++++++++++");
				return sl;
			}
		}
		return null;//cannot be reached
	}
	public void setRootCls(Cls rootCls){
		policyClsesTreeView.setRootCls(rootCls);
	}
	
	/* (non-Javadoc)
	 * @see edu.stanford.smi.protege.util.SelectionListener#selectionChanged(edu.stanford.smi.protege.util.SelectionEvent)
	 */
	public void selectionChanged(SelectionEvent event) {
		Date date= new Date();
		//policyFrameworkModel.addPolicyTypeSCls(instanceListWidget.getKnowledgeBase());
		
//		label.setText(date.toString());
//		Collection sels=event.getSelectable().getSelection();
//		Iterator it= sels.iterator();
//		if(it.hasNext()){
//			try{
//				FrameSlotCombination fsCombi=(FrameSlotCombination)it.next();
//				Slot slot= fsCombi.getSlot();
//				Cls cls= policyFrameworkModel.createPolicyCls(null);
//				Slot pSlot=policyFrameworkModel.addPolicyTaggedSlot(cls);
//				policyFrameworkModel.isPolicyCls(cls);
//				policyFrameworkModel.isPolicyTaggedSlot(pSlot);
//				
////				Slot slotPolicy= findSlotPolicy(slot);
////				policyTable.setModelSlot(slotPolicy);
//			}catch(Throwable th){
//				
//			}
//		}
	}

}
