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

package org.policy.action;
/**
 * <p>
 * A ActionResult contains a resultset and a flag. if this result is execution result, 
 * the flag will be set as true, otherweise be false.
 * </p><p>
 * $Id: ActionResult.java,v 1.3 2006/08/16 21:12:37 gdenisa Exp $
 * <br/>
 * Date: 05-May-2006
 * <br/>
 * Last changed: $Date: 2006/08/16 21:12:37 $
 * by $Author: gdenisa $
 * </p>
 * @author C. Jin && M. Li
 */


public class ActionResult {
	private boolean executionResult = false;
	private ResultSet variableBindings = null;
	
	/**
	 * constucts an new ActionResult
	 * @param either true or false, describing whether the query was succesfull.
	 * @param variableList a variable list
	 */
	public ActionResult(final boolean executionResult, final String[] variableList){
		this.executionResult = executionResult;
		setVariableList(variableList);
	}
	
	/**
	 * constructs an new ActionResult
	 * @param either true or false, describing whether the query was succesfull.
	 */
	public ActionResult( final boolean executionResult ){
		this.executionResult = executionResult;
	}
	
	/**
	 * set a variable list.
	 * @param variableList a variable list
	 * */
	public void setVariableList( final String[] variableList ){
		if (variableBindings == null){
			variableBindings = new ResultSet(variableList);
		}else
			variableBindings.setVarialbeList(variableList);
	}
	
	
	/**
	 * get the number of variables.
	 * @return the number of variables
	 * */
	public int getNumberVariables(){
		if (variableBindings == null)
			return 0;
		
		return variableBindings.getNumberVariables();
	}
	
	
	/**
	 * get a variable at the specified position in this list.
	 * @param  i index of variable
	 * @return a variable at the specified position in this list
	 * */
	public String getVariable( final int i ){
		if (variableBindings == null)
			return null;
		return variableBindings.getVariable(i);
	}
	
	
	/**
	 * get the result set.
	 * @return result set
	 * */
	public ResultSet getVariableBindings(){
		return variableBindings;
	}
	
	/**
	 * get the number of results.
	 * @return the number of results
	 * */
	public int getNumberResults( ){
		if (variableBindings == null)
			return 0;
		return variableBindings.getNumberResults();
	}
	
	/**
	 * get a result at the specified position in this list.
	 * @param  i index of result
	 * @return a result at the specified position in this list
	 * */
	public Result getResult( final int i ){
		if (variableBindings == null)
			return null;
		return variableBindings.getResult(i);
	}
	
	/**
	 * get value of executionResult.
	 * @return the value of executionResult
	 * */
	public boolean getExecutionResult( ){
		return executionResult;
	}
	
	//-------------------------------------------------------------------------------------------
	public String toString(){
		
		StringBuffer sb=new StringBuffer();
		
		if(this.executionResult==false){
			sb.append("false.\n");
		}
		else{
			sb.append("true.\n");
			
			for(int i=0; i<this.getNumberResults();i++){
				
				sb.append((i+1)+":\n");
				
				for(int j=0; j<this.getNumberVariables(); j++){
					
					sb.append(this.getVariable(j));
					sb.append("=");
					sb.append(this.getResult(i).getBinding(j));
					sb.append("\n");
				}
			}
		}
		
		return sb.toString();
	}
	
}
