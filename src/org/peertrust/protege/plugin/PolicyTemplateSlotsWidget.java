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

import edu.stanford.smi.protege.widget.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

//import test.tab.PolicySlotViewPanel;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.resource.*;
import edu.stanford.smi.protege.util.*;
 
/** 
 * This class extends the TemplateSlotsWidget to displays Policy Information for policy tagged slotgs
 * @author Patrice Congo 
 * @date 30-Oct-2004
 * Last changed  $Date: 2005/04/01 10:04:05 $
 * by $Author: dolmedilla $
 * $Id: PolicyTemplateSlotsWidget.java,v 1.1 2005/04/01 10:04:05 dolmedilla Exp $
 */
 public class PolicyTemplateSlotsWidget extends TemplateSlotsWidget{//AbstractTableWidget {

 public  void setPolicyFrameworkModel(PolicyFrameworkModel policyFrameworkModel){
 	this.policyFrameworkModel=policyFrameworkModel;
 }
//	private AllowableAction _viewAction;
//    private AllowableAction _viewAtClsAction;
//    protected AllowableAction _createAction;
protected PolicyFrameworkModel policyFrameworkModel;
protected AllowableAction createPolicyAction;

    public TableModel createTableModel() {
    	//System.out.println("creating table model");
        List slots;
        Cls cls = (Cls) getInstance();
        if (cls == null) {
            slots = Collections.EMPTY_LIST;
        } else {
            slots = getSlots(cls);
        }
        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        model.addColumn("Name");
        model.addColumn("Type");
        model.addColumn("Cardinality");
        model.addColumn("Other Facets");
        model.addColumn("Policy info");
        Iterator i = slots.iterator();
        while (i.hasNext()) {
            Slot slot = (Slot) i.next();
            FrameSlotCombination o = new FrameSlotCombination(cls, slot);
            model.addRow(new Object[] { o, o, o, o, o, o });
        }
        return model;
    }


    protected Action getCreatePolicySlotAction() {
//       URL url = getClass().getResource("/res/Create.gif");
//        
//        Icon icon= null;
//        if (url != null) {
//            icon = new ImageIcon(url);
//        }
        Icon icon= Icons.getCreateSlotIcon();
		createPolicyAction= new AllowableAction("Create slot with policy",icon,null) {
			public void actionPerformed(ActionEvent arg0) {
					onCreate();
			}
			public void onCreate() {
                Cls cls = (Cls) getInstance();//getBoundCls();
                if(cls==null){
                	return;
                }
                if (cls.isEditable()) {
                	policyFrameworkModel.addPolicyTaggedSlot(cls);
                }
            }
			
		}; 
		return createPolicyAction;
        
    }
    

    private int getInitialMaxWidth() {
        int tableWidth = getTable().getWidth();
        int viewPortWidth = getWidth() - (getInsets().left + getInsets().right + 3);
        int currentWidth = getTable().getColumnModel().getColumn(4).getWidth();
        int initialWidth = currentWidth + (viewPortWidth - tableWidth);
        return initialWidth;
    }





    public void initialize() {
    	super.initialize();
    	System.out.println("adding PolicyTableCellRenderer");
    	policyFrameworkModel = new PolicyFrameworkModel(getKnowledgeBase());
    	
    	PolicyTableCellRenderer pcRenderer=new PolicyTableCellRenderer(policyFrameworkModel);
    	//addColumn(200, pcRenderer);
    	addColumn(200,new ResourceKey("Policy info"),pcRenderer);
    	return;
    }

		
	protected Action getCreateSlotAction() {
		//return super.getCreateSlotAction();
		_createAction=(AllowableAction)getCreatePolicySlotAction();
		//return getCreatePolicySlotAction();
		return _createAction;
	}
	
	
    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        Slot templateSlotsSlot = slot.getKnowledgeBase().getSlot(Model.Slot.DIRECT_TEMPLATE_SLOTS);
        return slot.equals(templateSlotsSlot) || slot.hasSuperslot(templateSlotsSlot);
    }



    private void setOtherFacetsWidth() {
        JTable table = getTable();
        TableColumn tc= new TableColumn();
        tc.setHeaderValue("Policy");
        //System.out.println("adding policy column"+table);
        
        
        if (table != null && table.getColumnCount() == 6 && table.getWidth() > 0) {
        	System.out.println("adding policy column"+table);
        	table.addColumn(tc);
        	TableColumn column = table.getColumnModel().getColumn(5);
            TableCellRenderer renderer = column.getCellRenderer();
            int maxWidth = getInitialMaxWidth();
            int col = 5;
            int nRows = table.getRowCount();
            for (int row = 0; row < nRows; ++row) {
                FrameSlotCombination c = (FrameSlotCombination) table.getValueAt(row, col);
                Component component = renderer.getTableCellRendererComponent(table, c, false, false, row, col);
                Dimension d = component.getPreferredSize();
                maxWidth = Math.max(maxWidth, d.width);
            }
            column.setPreferredWidth(maxWidth);
        }
    }

    public String toString() {
        return "TemplateSlotsWidget";
    }

    public void validateTree() {
        super.validateTree();
        setOtherFacetsWidth();
    }
}
