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
package test.org.policy.config;

import java.util.Vector;

import org.policy.config.Configurable;
import org.policy.config.ConfigurationException;

/**
 * $Id: TestClass2.java,v 1.1 2007/02/24 19:20:46 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2007/02/24 19:20:46 $
 * by $Author: dolmedilla $
 * @description
 */
public class TestClass2 implements Configurable {
	private String string1, string2 ;
	
	/**
	 * 
	 */
	public TestClass2() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
	}

	/**
	 * @return 
	 */
	public String getString1() {
		return string1;
	}
	/**
	 * @param 
	 */
	public void setString1(String string) {
		string1 = string;
	}
	/**
	 * @return 
	 */
	public String getString2() {
		return string2;
	}
	/**
	 * @param 
	 */
	public void setString2(String string) {
		string2 = string;
	}
}
