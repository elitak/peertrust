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
import java.util.*;
import javax.swing.*;
import javax.swing.JTable;
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
public class PolicyTableCellRenderer extends JPanel
									implements TableCellRenderer {

	Icon mandatoryIcon;
	Icon defaultIcon;
	Icon noPolicyIcon;
	Icon badTypeIcon;
	
	PolicyFrameworkModel policyFrameworkModel;
	//JCheckBox templateSlotPolicyCheckBox= new JCheckBox();
	JLabel policyInfoLabel;
	public PolicyTableCellRenderer(PolicyFrameworkModel policyFrameworkModel){
		this.policyFrameworkModel=policyFrameworkModel;
		//templateSlotPolicyCheckBox.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
		policyInfoLabel= new JLabel();
		try{
			URL url = getClass().getResource("res/Default.gif");			
			defaultIcon= new ImageIcon(url);
			
			url = getClass().getResource("res/Mandatory.gif");			
			mandatoryIcon= new ImageIcon(url);
			
			url = getClass().getResource("res/NoPolicy.gif");			
			noPolicyIcon= new ImageIcon(url);
			
			url = getClass().getResource("res/Ugly.gif");			
			badTypeIcon= new ImageIcon(url);
			
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
		if(value instanceof FrameSlotCombination){
			FrameSlotCombination fsCombi= (FrameSlotCombination)value;
			String slotTypeName=fsCombi.getSlot().getDirectType().getName();
			//System.out.println("slotName:"+slotTypeName);
			return getTemplateSlotPolicyBox(table,value,isSelected,hasFocus,row,column);
			
		}
		return this;
	}
	
	public Component getTemplateSlotPolicyBox(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
		if(value instanceof FrameSlotCombination){
			FrameSlotCombination fsCombi= (FrameSlotCombination)value;	
			Slot aSlot= fsCombi.getSlot();
			if(policyFrameworkModel.isPolicyTaggedSlot(aSlot)){//slotTypeName.equals("PolicyTaggedSlot")){
//				///templateSlotPolicyCheckBox.setSelected(true); 
//				int polCount= policyFrameworkModel.getAllPolicies(aSlot).size();
//				//templateSlotPolicyCheckBox.setIcon(defaultIcon);
//				templateSlotPolicyCheckBox.setText(" "+Integer.toString(polCount));
//				if(policyFrameworkModel.getPolicyType(aSlot).equals("M")){
//					templateSlotPolicyCheckBox.setIcon(mandatoryIcon);
//				}else if(policyFrameworkModel.getPolicyType(aSlot).equals("D")){
//					templateSlotPolicyCheckBox.setIcon(defaultIcon);
//				}else{
//					templateSlotPolicyCheckBox.setIcon(badTypeIcon);
//				}
//				/templateSlotPolicyCheckBox.setSelected(true); 
				Collection c=policyFrameworkModel.getAllPolicies(aSlot); 
				int polCount= c.size();
				int mPolCount=0;
				int dPolCount=0;
				String aType;
				for(Iterator it=c.iterator();it.hasNext();){
					if(	((PolicyFrameworkModel.PolicyData)it.next()).policyType=="M"){
						mPolCount++;
					}else{
						dPolCount++;
					}					
				}
				//templateSlotPolicyCheckBox.setIcon(defaultIcon);
				String text= "<html><body><b><font color='red' >M="+mPolCount+
							"</font> <font color='green'>D="+dPolCount+
							"</font></b></body></html>";
				//templateSlotPolicyCheckBox.setText(text);
				policyInfoLabel.setText(text);
				//templateSlotPolicyCheckBox.setText(" "+Integer.toString(polCount));
//				if(policyFrameworkModel.getPolicyType(aSlot).equals("M")){
//					templateSlotPolicyCheckBox.setIcon(mandatoryIcon);
//				}else if(policyFrameworkModel.getPolicyType(aSlot).equals("D")){
//					templateSlotPolicyCheckBox.setIcon(defaultIcon);
//				}else{
//					templateSlotPolicyCheckBox.setIcon(badTypeIcon);
//				}
			}else{
				//templateSlotPolicyCheckBox.setSelected(false);
				//templateSlotPolicyCheckBox.setIcon(noPolicyIcon);
				//templateSlotPolicyCheckBox.setText("");
				policyInfoLabel.setIcon(noPolicyIcon);
				policyInfoLabel.setText("");
			}
			
			
		}
		
		//return templateSlotPolicyCheckBox;
		return policyInfoLabel;
	}
}
