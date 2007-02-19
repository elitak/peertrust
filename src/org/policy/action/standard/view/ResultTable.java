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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
USA
*/

package org.policy.action.standard.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import org.policy.action.standard.ActionResult;
import org.policy.action.standard.Result;
/**
 * <p>
 * this table contains the values of result.
 * </p><p>
 * $Id: ResultTable.java,v 1.1 2007/02/19 09:01:28 dolmedilla Exp $
 * <br/>
 * Date: 18-May-2006
 * <br/>
 * Last changed: $Date: 2007/02/19 09:01:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */
@SuppressWarnings("unchecked")
public class ResultTable{  
	private JTable table = new JTable(); 
	private JScrollPane scroll = null;  
	private String[] variable = new String[2];   
	private List <Result> result =new ArrayList();  
	private AbstractTableModel tm;  
	private AddData data;    
	
	/**
	 * constructs a table, which will show the exection result
	 * @param scroll  a scrollpanel, which contains this table
	 */
	public ResultTable(JScrollPane scroll) {
		this.scroll=scroll;
	}
	
	public void init(ActionResult actionResult){    
		
		tm = new AbstractTableModel(){      
			public int getColumnCount(){        
				return variable.length;       
			}      
			
			public int getRowCount(){        
				return result.size();       
			}      
			
			public Object getValueAt(int row,int column){
				if(!result.isEmpty()){          
					return ((Result)result.get(row)).getBinding(column); 
			    }else{
			    	return null;        
			    }      
			}      
			
			public void setValueAt(Object value,int row,int column){
				
			}      
			public String getColumnName(int column){ 
				return variable[column];      
			}      
			
			public boolean isCellEditable(int row,int column){
				return false;      
			}      
			
			@SuppressWarnings("unchecked")
			public Class getColumnClass(int c){
				return getValueAt(0,c).getClass();      
			}      
		};    

		table=new JTable(tm);
		table.setFont(new Font("Verdana", 0, 12));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);   
		table.setCellSelectionEnabled(false);    
		table.setShowHorizontalLines(true);    
		table.setShowVerticalLines(true);    
		table.setRowSelectionAllowed(true);    
		table.setGridColor(Color.GRAY);
		data = new AddData(this);    
		data.add(actionResult);
	}
	
	public JScrollPane getJTablePane() {

		scroll.getViewport().removeAll();
		scroll.setBorder(BorderFactory.createTitledBorder(null, "Result", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Verdana", Font.BOLD, 12), new Color(51, 51, 51)));
		scroll.setBounds(new Rectangle(0, 110, 432, 150));
		scroll.getViewport().add(table, BorderLayout.CENTER);
		scroll.getViewport().setBackground(Color.white);
		scroll.setAutoscrolls(true);
		scroll.setHorizontalScrollBar(new JScrollBar());
		return scroll;
	}
	
	public String[] getVariable(){
		return variable;
	}
	
	public  void setVariable(String[] variable){
		this.variable = variable;
	}
	
	public List <Result> getResult(){
		return result;
	}
	
	public  void setResult(List <Result> result){
		this.result = result;
	}
	
	public AbstractTableModel getTM(){
		return tm;
	}
}