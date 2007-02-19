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

package org.policy.action.standard;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A ResultSet contains a list of result and a array of variables.
 * </p><p>
 * $Id: ResultSet.java,v 1.1 2007/02/19 09:01:27 dolmedilla Exp $
 * <br/>
 * Date: 05-May-2006
 * <br/>
 * Last changed: $Date: 2007/02/19 09:01:27 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */

public class ResultSet {
	private List <Result> bindings = new ArrayList<Result>();
	private String[] variableList =  null;
	
	/**
	 * constructs a new resultSet
	 * @param variableList a array of variables
	 */
	protected ResultSet(final String[] variableList){
		this.variableList = variableList;
	}
	
	
	/**
	 * Appends a result in the end of the list.
	 * @param result a result
	 * @throws FunctionFailureException if the number of input variables is more than
	 * the number of results
	 */
	public /*protected*/ void addResult(final Result result) throws FunctionFailureException{
		if(result.getNumberBindings() >= variableList.length){
			bindings.add(result);
		}
		else {
			throw new FunctionFailureException("the number of input variables is more");
		}
	
	}
	
	/**
	 * get the number of results
	 * @return the number of results
	 */
	public int getNumberResults(){
		if (bindings == null)
			return 0;
		
		return bindings.size(); 
	}
	
	
	/**
	 * get a result at the specified position in this list.
	 * @param i index of the result
	 * @return a result at the specified position in this list 
	 */
	public Result getResult(int i){
		if (bindings == null)
			return null;
		else if (i < 0)
			return null;
		else if (i >= bindings.size())
			return null;
		else
			return bindings.get(i);
	}
	
	/**
	 * get the number of variables.
	 * @return the number of variables
	 */
	public int getNumberVariables(){
		if (variableList == null)
			return 0;
		
		return variableList.length;
	}
	
	/**
	 * get a variable at the specified position in this list.
	 * @param i index of the variable
	 * @return a variable at the specified position in this list
	 */
	public String getVariable(final int i){
		
		if (variableList == null)
			return null;
		else if (i < 0)
			return null;
		else if (i >= variableList.length)
			return null;
		else
			return variableList[i];
	}

	/**
	 * set variable list
	 * @param variableList a array of variables
	 */
	public void setVarialbeList(String[] variableList) {
		if (variableList == null)
			variableList = new String[2];
		this.variableList = variableList;
	}
	
	/**
	 * get variable list.
	 * @return the variable list
	 */
	public String[] getVariableList(){
		return variableList;
	}
	
	/**
	 * get result list
	 * @return the result list
	 */
	public List <Result> getBindings(){
		return bindings;
	}
}
