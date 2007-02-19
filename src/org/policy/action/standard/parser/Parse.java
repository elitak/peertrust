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

package org.policy.action.standard.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.policy.action.standard.tools.Functions;
/**
 * <p>
 * this parse is only for this project. 
 * From a strsing the essential informations will be obtainted.  
 * </p><p>
 * $Id: Parse.java,v 1.1 2007/02/19 09:01:28 dolmedilla Exp $
 * <br/>
 * Date: 18-May-2006
 * <br/>
 * Last changed: $Date: 2007/02/19 09:01:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */

public class Parse {
	private Map<String, Object> infoMap = null;
	private String expression = ""; 
	
	/**
	 * constructs a new parse
	 */
	public Parse(){
		this("");
	}
	
	/**
	 * constructs a new parse.
	 * @param expression the expression which should be parsed
	 */
	public Parse(String expression){
		super();
		excuteParse(expression);
	}
	
	/**
	 * the parse will be executed
	 * @param expression the expression which should be parsed
	 */
	public void excuteParse(String expression){
		this.expression = expression;
		if (expression == null)
			return;
		if (expression.equals(""))
			return;
		infoMap = getInformation(this.expression);
	}
	
	
	//in(user(Name), rdbms:query(�select name from users�, �db_users�))
	private Map getInformation(String expression){
		Map<String, Object> retMap = new HashMap<String, Object>();
		int  index = -1;
		
		index = expression.indexOf("(");
		if (index != -1){
			String statementName = expression.substring(0, index).trim();
			retMap.put(Functions.IN, statementName);
		}
		
		index = expression.indexOf("(", index + 1);
		int indexInVar = expression.indexOf(")", index);
		if((index != -1) && (indexInVar != -1)){
			String invarString = expression.substring(index + 1, indexInVar).trim();
			String[] invars = invarString.split(",");
			for (int i = 0; i < invars.length; i ++){
				invars[i] = invars[i].trim();
			}
			retMap.put(Functions.INPUTVARS, invars);
		}
		
		int indexPackage1 = expression.indexOf(",", indexInVar + 1);
		int indexPackage2 = expression.indexOf(":", indexInVar + 1);
		String packageName = expression.substring(indexPackage1 + 1, indexPackage2).trim();
		retMap.put(Functions.PACKAGENAME, packageName);
		//" in (user(Name, Address), rdbms: query('select name, address from test', 'project'))";
		int indexFun = expression.indexOf("(", indexPackage2);
		String functionName = expression.substring(indexPackage2 + 1, indexFun ).trim();
		retMap.put(Functions.FUNCTIONNAME, functionName);
		
		int indexEnd = expression.indexOf(")", indexFun);
		String teilStr = expression.substring(indexFun + 1, indexEnd);
		int index1 = teilStr.indexOf("'");
		int index2 = teilStr.indexOf("'", index1+1);
		String arg = teilStr.substring(index1 + 1, index2);
		List<String> argList = new ArrayList<String>();
		argList.add(arg);
		int index3 = teilStr.indexOf(",");
		while (index3 != -1){
			index1 = teilStr.indexOf("'", index2 + 1);
			index2 = teilStr.indexOf("'", index1 + 1);
			arg = teilStr.substring(index1 + 1, index2);
			argList.add(arg);
			index3 = teilStr.indexOf(",", index2);
		}
		retMap.put(Functions.ARGUMENTS, Functions.toArray(argList));
		return retMap;
	}
	
	/**
	 * get statement name
	 * @return the name of statement
	 */
	public String getStatementName(){
		return (String) infoMap.get(Functions.IN);
	}
	
	/**
	 * get all input variables
	 * @return a array of input variables
	 */
	public String[] getInVarsName(){
		return (String[]) infoMap.get(Functions.INPUTVARS);
	}
	
	/**
	 * get the package name
	 * @return the name of used package
	 */
	public String getPackageName(){
		return (String) infoMap.get(Functions.PACKAGENAME);
	}
	
	/**
	 * get function name
	 * @return the name of functions
	 */
	public String getFunctionName(){
		return (String) infoMap.get(Functions.FUNCTIONNAME);
	}
	
	/**
	 * get arguments
	 * @return a array of arguments
	 */
	public String[] getArgument(){
		return (String[]) infoMap.get(Functions.ARGUMENTS);
	}
	
	/**
	 * get variables in select statement in my_sql
	 * @param selectStatement a select statement
	 * @return a array of variables
	 */
	public String[] getSelectVars(String selectStatement){
		String temp = selectStatement.toLowerCase();
		String select = "select";
		int index0 = temp.indexOf(select);
		if (index0 != -1){
			int index = index0 + select.length();
			int index1 = temp.indexOf("from");
			if (index1 != -1){
				String vars = selectStatement.substring(index, index1);
				String[] selectVars = vars.split(","); 
				for (int i = 0; i < selectVars.length; i ++){
					selectVars[i] = selectVars[i].trim();
				}
				return selectVars;
			}
	
		}
		return null;
	}
	
	/**
	 * Extracts parts of a string which are separated by commatas.
	 * @param string
	 * @return array of strings
	 */
	public static String[] stringToArray(String string) {
		String[] attributesArray = string.split(",");
		return attributesArray;
	}

}
