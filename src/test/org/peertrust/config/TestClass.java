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
package test.org.peertrust.config;

import java.util.Vector;

import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;

/**
 * $Id: TestClass.java,v 1.2 2004/11/24 10:24:02 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/11/24 10:24:02 $
 * by $Author: dolmedilla $
 * @description
 */
public class TestClass implements Configurable {
	private String string ;
	private boolean bool ;
	private int integer ;
	private long longinteger ;
	private Vector vector ;
	private TestClass example2 ;
	private Object extraExample ;
	
	/**
	 * 
	 */
	public TestClass() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		integer += 1 ;
		
	}
	
	/**
	 * @return Returns the bool.
	 */
	public boolean isBool() {
		return bool;
	}
	/**
	 * @param bool The bool to set.
	 */
	public void setBool(boolean bool) {
		this.bool = bool;
	}
	/**
	 * @return Returns the example2.
	 */
	public TestClass getExample2() {
		return example2;
	}
	/**
	 * @param example2 The example2 to set.
	 */
	public void setExample2(TestClass example2) {
		this.example2 = example2;
	}
	/**
	 * @return Returns the integer.
	 */
	public int getInteger() {
		return integer;
	}
	/**
	 * @param integer The integer to set.
	 */
	public void setInteger(int integer) {
		this.integer = integer;
	}
	/**
	 * @return Returns the longinteger.
	 */
	public long getLonginteger() {
		return longinteger;
	}
	/**
	 * @param longinteger The longinteger to set.
	 */
	public void setLonginteger(long longinteger) {
		this.longinteger = longinteger;
	}
	/**
	 * @return Returns the string.
	 */
	public String getString() {
		return string;
	}
	/**
	 * @param string The string to set.
	 */
	public void setString(String string) {
		this.string = string;
	}
	/**
	 * @return Returns the vector.
	 */
	public Vector getVector() {
		return vector;
	}
	/**
	 * @param vector The vector to set.
	 */
	public void setVector(Vector vector) {
		this.vector = vector;
	}
	/**
	 * @return Returns the extraExample.
	 */
	public Object getExtraExample() {
		return extraExample;
	}
	/**
	 * @param extraExample The extraExample to set.
	 */
	public void setExtraExample(Object extraExample) {
		this.extraExample = extraExample;
	}
}
