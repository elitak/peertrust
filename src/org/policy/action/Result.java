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
 * A Result contains an array of String.
 * </p><p>
 * $Id: Result.java,v 1.2 2006/08/10 10:10:35 dolmedilla Exp $
 * <br/>
 * Date: 05-May-2006
 * <br/>
 * Last changed: $Date: 2006/08/10 10:10:35 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */
public class Result {
	private String[] bindings = null;
	
	/**
	 * constructs a new result
	 */
	protected Result(){
		super();
	}
	
	/**
	 * constructs a new result
	 * @param bindings a string array of bindings
	 */
	protected Result(final String[] bindings){
		this.bindings = bindings;
	}
	
	/**
	 * append a binding in the end of the array of bindings
	 * @param binding a binding
	 */
	protected void addBinding(final String binding){
		int length = getNumberBindings(); 
		String[] temp = new String[length+ 1];
		for (int i = 0; i < length; i ++){
			temp[i] = bindings[i];
		}
		temp[length] = binding;
		bindings = temp;
	}
	
	/**
	 * get the number of bindings
	 * @return the number of bindings
	 */
	public int getNumberBindings(){
		if (bindings == null)
			return 0;
		return bindings.length;
	}
	
	/**
	 * get a binding at the specified position in this array.
	 * @param i index of the binding
	 * @return a binding at the specified position in this array 
	 */
	public String getBinding(final int i){
		if (bindings == null)
			return null;
		else if (i < 0)
			return null;
		else if (i >= bindings.length)
			return null;
		else
			return bindings[i];
	}
}
