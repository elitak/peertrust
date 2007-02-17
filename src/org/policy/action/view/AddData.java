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

package org.policy.action.view;
/**
 * <p>
 * this class sets the values of a JTable.
 * </p><p>
 * $Id: AddData.java,v 1.1 2007/02/17 16:59:28 dolmedilla Exp $
 * <br/>
 * Date: 18-May-2006
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */

import javax.swing.JOptionPane;

import org.policy.action.ActionResult;
import org.policy.action.ResultSet;

public class AddData{    
	ResultTable test;    
	
	/**
	 * constructor
	 * @param table which will be set
	 */
	public AddData(ResultTable table){      
		this.test = table;  
	}  
	
	/**
	 * set values of the table according to an action result
	 * @param actionResult a action result, whose values will be set in a table
	 */
	public void add(ActionResult actionResult){    
		if (actionResult != null){
			ResultSet resultSet = actionResult.getVariableBindings();    
			test.setResult(resultSet.getBindings());
			test.setVariable(resultSet.getVariableList());
			
			if (test.getResult() == null || test.getVariable() == null){
				 JOptionPane.showMessageDialog(null, "keine Loesung!","Error",
     				    JOptionPane.ERROR_MESSAGE);
     				   return;
			}else{
				test.getTM().fireTableStructureChanged(); 
			}
		}
	}  
}

