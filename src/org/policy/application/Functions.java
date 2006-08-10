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

package org.policy.application;

import java.util.List;

/**
 * <p>
 * Functions contains constant value, which are used in this project.
 * </p><p>
 * $Id: Functions.java,v 1.1 2006/08/10 10:10:35 dolmedilla Exp $
 * <br/>
 * Date: 05-May-2006
 * <br/>
 * Last changed: $Date: 2006/08/10 10:10:35 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */
public class Functions {
	
	public static String FUNCTIONNAME = "functionName";
	public static final String ARGUMENTS = "arguments";
	public static final String INPUTVARS = "inputVars";
	public static final String IN = "in";
	public static final String PACKAGENAME = "package_name";
	public static final String QUERY = "query";
	public static final String REGEXP_IN_FILE = "regExpInFile";
	public static final String ENTRY_IN_DIRECTORY = "search";
	public static final String PACKAGE_REGEX = "regex";
	public static final String PACKAGE_JDBC = "rdbms";
	public static final String PACKAGE_LDAP = "ldap";
	
	/**
	 * a list will be changed to a array
	 * @param list a list
	 * @return a string array
	 */
	public static String[] toArray(List list){
		if (null == list)
			return null;
		
		int count = list.size();
		String[] strArray = new String[count];
		for (int i = 0; i < count; i++){
			strArray[i] = (String) list.get(i);
		}
		return strArray;
		
	}
}
