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

import java.awt.Component;
import java.net.URL;
import java.util.Collection;

import javax.swing.*;
import javax.swing.JTable;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import edu.stanford.smi.protege.model.FrameSlotCombination;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.DefaultRenderer;

/**
 * @author Congo Patrice
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClsPolicyTableCellRenderer implements TableCellRenderer {

	Icon mandatoryIcon;
	Icon defaultIcon;
	Icon noPolicyIcon;
	Icon badTypeIcon;
	
	Icon locallyDefinedIcon;
	Icon inheritatedIcon;
	
	PolicyFrameworkModel policyFrameworkModel;
	JCheckBox templateSlotPolicyCheckBox= new JCheckBox();
	JLabel rendererNrLabel= new JLabel();
	JLabel rendererTypeLabel= new JLabel();
	JLabel rendererPolicyLabel= new JLabel();
	JLabel rendererDefiningClsLabel= new JLabel();
	JLabel rendererOverriddenClsLabel= new JLabel();
	
	public ClsPolicyTableCellRenderer(PolicyFrameworkModel policyFrameworkModel){
		this.policyFrameworkModel=policyFrameworkModel;
		templateSlotPolicyCheckBox.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
		try{
			URL url = getClass().getResource("res/Default.gif");			
			defaultIcon= new ImageIcon(url);
			
			url = getClass().getResource("res/Mandatory.gif");			
			mandatoryIcon= new ImageIcon(url);
			
			url = getClass().getResource("res/NoPolicy.gif");			
			noPolicyIcon= new ImageIcon(url);
			
			url = getClass().getResource("res/Ugly.gif");			
			badTypeIcon= new ImageIcon(url);
			
			url = getClass().getResource("res/LocalPolicy.gif");			
			locallyDefinedIcon= new ImageIcon(url);
			
			url = getClass().getResource("res/Blank.gif");			
			inheritatedIcon= new ImageIcon(url);
			
		}catch(Throwable th){
			th.printStackTrace();
		}
	}
	public void setPolicyFrameworkModel(PolicyFrameworkModel policyFrameworkModel){
		this.policyFrameworkModel=policyFrameworkModel;
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		String colName= table.getColumnName(column);
		if(colName=="Name"){
			return getNrRendererComponent(table,value,isSelected,hasFocus,row,column);
		}else if(colName=="Type"){
			return getTypeRendererComponent(table,value,isSelected,hasFocus,row,column);
		}else if(colName=="Policy"){
			return getPolicyRendererComponent(table,value,isSelected,hasFocus,row,column);
		}else if(colName == "Defining Cls" ){
			return getDefiningClsRendererComponent(table,value,isSelected,hasFocus,row,column);
		}else if(colName == "Overridden Policy" ){
			return getOverriddenClsRendererComponent(table,value,isSelected,hasFocus,row,column);
		}else{
			return new JLabel("badColumn"+colName);
		}
		
	}
	
	private Component getNrRendererComponent(	JTable table, Object value, 
												boolean isSelected, boolean hasFocus, 
												int row, int column){
		boolean isLocallyDefined=((ClsPolicyTableModel)table.getModel()).getIsLocallyLocal(row);
		if(isLocallyDefined){
			rendererNrLabel.setIcon(locallyDefinedIcon);
		}else{
			rendererNrLabel.setIcon(inheritatedIcon);
			//System.out.println("********************************inherited:"+inheritatedIcon);
		}
		rendererNrLabel.setText(value.toString());
		return rendererNrLabel;
	}
	

	private Component getTypeRendererComponent(	JTable table, Object value, 
												boolean isSelected, boolean hasFocus, 
												int row, int column){
		String type= value.toString();
		if(type.equals("M")){
			rendererTypeLabel.setIcon(mandatoryIcon);
		}else if(type.equals("D")){
			rendererTypeLabel.setIcon(defaultIcon);
		}else{
			rendererTypeLabel.setIcon(badTypeIcon);
		}
		return rendererTypeLabel;
	}
	
	private Component getPolicyRendererComponent(	JTable table, Object value, 
			boolean isSelected, boolean hasFocus, 
			int row, int column){
		
		rendererPolicyLabel.setText(value.toString());
		return rendererPolicyLabel;
	}
	
	private Component getDefiningClsRendererComponent(	JTable table, Object value, 
			boolean isSelected, boolean hasFocus, 
			int row, int column){
		rendererDefiningClsLabel.setText(value.toString());
		return rendererDefiningClsLabel;
	}
	
	private Component getOverriddenClsRendererComponent(	
										JTable table, Object value, 
										boolean isSelected, boolean hasFocus, 
										int row, int column){
		String text="Nothing";
		if(value!=null){
			text= (String)((Instance)value).getOwnSlotValue(policyFrameworkModel.getPolicySlotName());
			
		}
		rendererOverriddenClsLabel.setText(text);
		return rendererOverriddenClsLabel;
	}
	
}
