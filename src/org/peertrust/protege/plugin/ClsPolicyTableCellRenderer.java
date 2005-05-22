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

import javax.swing.*;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.resource.Icons;

/**
 * <p>
 * Provide a contomized renderer for table cell.
 * A specific renderer is return according the table column name. 
 * </p><p>
 * $Id: ClsPolicyTableCellRenderer.java,v 1.2 2005/05/22 17:56:45 dolmedilla Exp $
 * <br/>
 * Date: 30-Oct-2004
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:45 $
 * by $Author: dolmedilla $
 * </p>
 * @author Patrice Congo 
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
	
	/**
	 * create a ClsPolicyTableCellRenderer object. Icon resources are loaded hier
	 * @param policyFrameworkModel - the currently used policy framework.
	 */
	public ClsPolicyTableCellRenderer(PolicyFrameworkModel policyFrameworkModel){
		this.policyFrameworkModel=policyFrameworkModel;
		templateSlotPolicyCheckBox.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
		try{
			URL url = getClass().getResource("/res/Default.gif");			
			defaultIcon= new ImageIcon(url);
			//defaultIcon= Icons.getD;
			url = getClass().getResource("/res/Mandatory.gif");			
			mandatoryIcon= new ImageIcon(url);
			
			url = getClass().getResource("/res/NoPolicy.gif");			
			noPolicyIcon= new ImageIcon(url);
			
//			url = getClass().getResource("/res/Ugly.gif");			
//			badTypeIcon= new ImageIcon(url);
			badTypeIcon= Icons.getUglyIcon();
			url = getClass().getResource("/res/LocalPolicy.gif");			
			locallyDefinedIcon= new ImageIcon(url);
			
//			url = getClass().getResource("/res/Blank.gif");			
//			inheritatedIcon= new ImageIcon(url);
			inheritatedIcon=Icons.getBlankIcon();
		}catch(Throwable th){
			th.printStackTrace();
		}
	}
	
	/**
	 * To set A new Policy Framework model.
	 * @param policyFrameworkModel - the new policy framework
	 */
	public void setPolicyFrameworkModel(PolicyFrameworkModel policyFrameworkModel){
		this.policyFrameworkModel=policyFrameworkModel;
	}
	
	/**
	 * Automaticaly call to get the cell renderer.
	 * The return rendering componenet depends on the column name. Renderer only exists for:
	 * <ul>
	 * 	<li> the Name column to render policy names.
	 * 	<li> the Type column to render policy type 
	 * 	<li> the Defining column to show the classes in which the class policies have been defined
	 * 	<li> the overridden Policy column to show policies which have been overridden bei other.
	 * 	<li> or a renderer that show that indicate an error
	 * </ul>  
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(	JTable table, 
													Object value, 
													boolean isSelected, 
													boolean hasFocus, 
													int row, 
													int column
													) {
		String colName= table.getColumnName(column);
		if(colName=="Name"){
			return getNameRendererComponent(table,value,isSelected,hasFocus,row,column);
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
	
	/**
	 * Utility method that returns the Name coulumn cell renderer.
	 * @param table 
	 * @param value 
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 * @return
	 */
	private Component getNameRendererComponent(	JTable table, Object value, 
												boolean isSelected, boolean hasFocus, 
												int row, int column){
		TableModel tm=table.getModel();
		if(tm instanceof ClsPolicyTableModel){
			boolean isLocallyDefined=((ClsPolicyTableModel)table.getModel()).getIsLocallyLocal(row);
			if(isLocallyDefined){
				rendererNrLabel.setIcon(locallyDefinedIcon);
			}else{
				rendererNrLabel.setIcon(inheritatedIcon);
				//System.out.println("********************************inherited:"+inheritatedIcon);
			}
		}else if(tm instanceof SlotPolicyTableModel){
			rendererNrLabel.setIcon(locallyDefinedIcon);
		}else{
			rendererNrLabel.setIcon(inheritatedIcon);
		}
		rendererNrLabel.setText(value.toString());
		return rendererNrLabel;
	}
	
	/**
	 * Utility method that returns the policy type column cell renderer.
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 * @return
	 */
	private Component getTypeRendererComponent(	
												JTable table, 
												Object value, 
												boolean isSelected, 
												boolean hasFocus, 
												int row, 
												int column
												){
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
	
	/**
	 * Utility method that returns the Policy column table cell rendering component.
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 * @return
	 */
	private Component getPolicyRendererComponent(	
											JTable table, 
											Object value, 
											boolean isSelected, 
											boolean hasFocus, 
											int row, 
											int column
											){		
		rendererPolicyLabel.setText(value.toString());
		return rendererPolicyLabel;
	}
	
	/**
	 * To get thet cell renderer component , which can render the defining class column cells.
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 * @return
	 */
	private Component getDefiningClsRendererComponent(	
												JTable table, 
												Object value,
												boolean isSelected, 
												boolean hasFocus,
												int row, 
												int column){
		rendererDefiningClsLabel.setText(value.toString());
		return rendererDefiningClsLabel;
	}
	
	/**
	 * To get get the renderer that can show the Overridden class column cells
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 * @return
	 */
	private Component getOverriddenClsRendererComponent(	
										JTable table, Object value, 
										boolean isSelected, boolean hasFocus, 
										int row, int column){
		String text="Nothing";
		if(value!=null){
			//text= (String)((Instance)value).getOwnSlotValue(policyFrameworkModel.getPolicySlotName());
			text=(String)((Instance)value).getName();
		}
		rendererOverriddenClsLabel.setText(text);
		return rendererOverriddenClsLabel;
	}
	
}
