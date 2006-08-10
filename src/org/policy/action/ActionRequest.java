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
 * A ActionRequest contains a name of function and two string arrays. 
 * An Array saves arguments and another saves input variables.
 * </p><p>
 * $Id: ActionRequest.java,v 1.2 2006/08/10 10:10:34 dolmedilla Exp $
 * <br/>
 * Date: 05-May-2006
 * <br/>
 * Last changed: $Date: 2006/08/10 10:10:34 $
 * by $Author: dolmedilla $
 * </p>
 * @author C.Jin && M. Li
 */
public class ActionRequest {
	private String function;
	private String[] arguments = null;
	private String[] inputVars = null;
	
	/**
	 * Constructs a new ActionRequest
	 */
	protected ActionRequest(){
			super();
	}
	
	/**
	 * Constructs a new ActionRequest
	 * @param function name of function
	 * @param arguments a string array of all arguments
	 * @param inputVars a string array of all input variables
	 */
	public ActionRequest(final String function, final String[] arguments, final String[] inputVars){
		this.function = function;
		
		if (arguments != null){
			for (int i = 0; i < arguments.length; i++){
				this.addArgument(arguments[i]);
			}
		}
		
		if (inputVars != null){
			for (int i = 0; i < inputVars.length; i++){
				this.addInputVariable(inputVars[i]);
			}
		}
		
	}
	
	/**
	 * get the number of arguments.
	 * @return the number of argument
	 * */
	public int getNumberArguments(){
		if (arguments == null)
			return 0;
		return arguments.length;
	}
	
	/**
	 * get the number of input variables.
	 * @return the number of input variables
	 * */
	public int getNumberInputVars(){
		if (inputVars == null)
			return 0;
		return inputVars.length;
	}
	
	/**
	 * get an argument at the specified position in this array.
	 * @param  i index of argument
	 * @return an argument at the specified position in this array
	 * */
	public String getArgument(int i){
		if (arguments == null)
			return null;
		if (i < 0)
			return null;
		if (i >= arguments.length)
			return null;
		else
			return arguments[i];
	}
	
	/**
	 * get a input variable at the specified position in this array.
	 * @param  i index of input variable
	 * @return an input varialbe at the specified position in this array
	 * */
	public String getInputVar(int i){
		if (inputVars == null)
			return null;
		if (i < 0)
			return null;
		if (i >= inputVars.length)
			return null;
		else
			return inputVars[i];
	}
	
	/**
	 * get the function name.
	 * @return get the function name. default value is ""
	 * */
	public String getFunction(){
		if (function == null)
			function = "";
		return function;
	}
	
	/**
	 * set the function name.
	 * @param  function name of the function
	 * */
	public void setFunction(final String function){
		if (function == null)
			this.function = "";
		else
			this.function = function;
	}
	
	/**
	 * Appends a argument in the end of this array.
	 * @param  argument
	 * */	
	protected void addArgument(final String argument){
		int length = getNumberArguments();
		String[] temp = new String[length + 1];
		for (int i = 0; i < length; i ++){
			temp[i] = arguments[i];
		}
		temp[length] = argument;
		this.arguments = temp;
	}
	
	/**
	 * Appends an input variable in the end of this array.
	 * @param  inputVariable
	 * */	
	protected void addInputVariable(final String inputVariable){
		int length = getNumberInputVars();
		String[] temp = new String[length + 1];
		for (int i = 0; i < length; i ++){
			temp[i] = inputVars[i];
		}
		temp[length] = inputVariable;
		this.inputVars = temp;
	}
	
	/**
	 * get ActionRequest.
	 * @return ActionRequest
	 * */
	protected ActionRequest getActionRequest(){
		return this;
	}
	
	/**
	 * get all input variables.
	 * @return an string array of all input variables
	 * */
	protected String[] getInputsVar(){
		return inputVars;
	}
	
}
