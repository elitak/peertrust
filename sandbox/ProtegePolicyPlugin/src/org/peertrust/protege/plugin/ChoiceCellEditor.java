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

/*
 * Created on Nov 13, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.protege.plugin;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import edu.stanford.smi.protege.model.Instance;

/**
 * @author congo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChoiceCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener{
	private JComboBox comboBox;
	Object[] choices;
	String[] choicesLabels;
	boolean hasFixContent;
	
	public ChoiceCellEditor(String[] choices){
		comboBox= new JComboBox(choices);
		this.choices=choices;
		this.choicesLabels= choices;
		comboBox.setEditable(false);
		//comboBox.addItemListener(this);
		comboBox.addActionListener(this);
		this.hasFixContent=true;
		
	}
	
	public ChoiceCellEditor(){
		choices=null;
		choicesLabels=null;
		hasFixContent=false;
	}
	
	private void configEditor(JTable table, Object selectedValue, boolean arg2, int row, int column){
		ClsPolicyTableModel tModel=(ClsPolicyTableModel)table.getModel();
		
		choices=tModel.getOverriddables(row).toArray();
		//System.out.println("CHOICES:::"+choices.length);
		if(choices!=null){
			choicesLabels= new String[choices.length+1];
			choicesLabels[0]= "Nothing";
			for(int i=0;i<choices.length;i++){
				choicesLabels[i+1]=tModel.getInstanceName((Instance)choices[i]);
			}
		}else{
			choicesLabels=new String[]{"Nothing"};
		}
		
		comboBox= new JComboBox(choicesLabels); 
		comboBox.addActionListener(this);
	}
	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		
		Object selLabel= comboBox.getModel().getSelectedItem();
		int index=((DefaultComboBoxModel)comboBox.getModel()).getIndexOf(selLabel);
		if(hasFixContent || selLabel==null){
			return selLabel;
		}else{
			if(selLabel.equals("Nothing")){
				return null;
			}else{
				return choices[index-1];
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object selectedValue, boolean arg2, int row, int column) {
		//select the show the actual value
		if(hasFixContent){
			comboBox.getModel().setSelectedItem(selectedValue);
		}else{
			configEditor(table,selectedValue,arg2,row,column);
		}
		
		return comboBox;
	}

	  

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent aE) {
		//System.out.println("////////****************************************************///////////////");
		this.fireEditingStopped();
	}	
	

}
