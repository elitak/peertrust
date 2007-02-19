/**
 *Copyright 2004
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
package org.policy.action.standard.packages;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.policy.action.standard.AbstractExecutorWrapper;
import org.policy.action.standard.ActionRequest;
import org.policy.action.standard.ActionResult;
import org.policy.action.standard.FunctionFailureException;
import org.policy.action.standard.NoSuchFunctionException;
import org.policy.action.standard.Result;
import org.policy.action.standard.tools.Functions;

/**
 * <p>
 * The RegExpWrapper can get the data from the file system by use of JNDI in 
 * form of regular expressions. The data is assigned to the variable X. 
 * For a RegExpWrapper call the package_name should be �regex� and the function 
 * �regExpInFile�. There are only two arguments in a RegExpWrapper call, 
 * one is the regular expression, which describes the string to search for, 
 * and the other is the path of the file, in which the string are examined. 
 * </p><p>
 * $Id: RegExpWrapper.java,v 1.1 2007/02/19 09:01:27 dolmedilla Exp $
 * <br/>
 * Date: 08-JUN-2006
 * <br/>
 * Last changed: $Date: 2007/02/19 09:01:27 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */
public class RegExpWrapper extends AbstractExecutorWrapper {

	/**
	 * constructs an new execiteAction
	 * @param functionName the name of function
	 * @param arguments a array of arguments
	 * @param inputVars a array of input variables
	 * @return an action result
	 * @exception IllegalArgumentException if the argument is false
	 * @exception NoSuchFunctionException if the function name is wrong
	 * @exception FunctionFailureException if the action executes unsuccessful
	 */
	public ActionResult executeAction(final String functionName,
			final String[] arguments, final String[] inputVars)
			throws IllegalArgumentException, FunctionFailureException,
			NoSuchFunctionException {
		ActionRequest ar = new ActionRequest(functionName, arguments, inputVars);
		return localExecuteAction(ar);
	}

	/**
	 * execute a local action
	 * @param actionRequest an actionRequest
	 * @return a action result after the exection
	 * @exception IllegalArgumentException if the argument is false
	 * @exception NoSuchFunctionException if the function name is wrong
	 * @exception FunctionFailureException if the action executes unsuccessful
	 */
	public ActionResult localExecuteAction(final ActionRequest actionRequest)
			throws IllegalArgumentException, FunctionFailureException,
			NoSuchFunctionException {
		if (!actionRequest.getFunction().equals(Functions.REGEXP_IN_FILE))
			throw new NoSuchFunctionException();

		if (actionRequest.getNumberArguments() != 2)
			throw new IllegalArgumentException("IllegaArgumentException");

		String regExp = actionRequest.getArgument(0);
		String filePathTemp = actionRequest.getArgument(1);
		String[] inputVars = actionRequest.getInputsVar();
		ActionResult actionResult = new ActionResult(true, inputVars);

		actionResult = toResult(regExpInFile(regExp, filePathTemp), actionResult);
		return actionResult;
	}

	public Vector regExpInFile(String regExp, String filePath) {
		Pattern regExpPattern = Pattern.compile(regExp);
		BufferedReader br = null;
		String input = null;

		Vector<List> rtn = new Vector<List>();
		boolean found = false;
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException fnfe) {
			//  System.out.println(filePath);
			//TODO throw fileNotFoundException
			System.out
					.println("Cannot locate input file! " + fnfe.getMessage());

		}

		try {
			while ((input = br.readLine()) != null) {
				//				System.out.println(input);
				Matcher matcher = regExpPattern.matcher(input);
				List valueList;
				while (matcher.find()) {
					found = true;
					valueList = new ArrayList();
					valueList.add(matcher.group());
					rtn.add(valueList);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!found) {
			System.out.println("No match found.");
		}

		try {
			br.close();
		} catch (IOException ioe) {
		}
		return rtn;
	}
	
	/**
	 * constructs an action result
	 * @param rtn the results, which are saved in a vector after execution
	 * @param actionResult a action result, which will save the results 
	 * @return the action result
	 * @exception FunctionFailureException if the action executes unsuccessful
	 */
	public ActionResult toResult(Vector rtn, ActionResult actionResult)
			throws FunctionFailureException {
		if (rtn != null) {
			Iterator iter = rtn.iterator();

			while (iter.hasNext()) {
				List valueListTemp = (List) iter.next();
				String[] bindings = Functions.toArray(valueListTemp);
				Result result = new Result(bindings);
				actionResult.getVariableBindings().addResult(result);
			}
		} else {
			System.out.println("there is no right match");
		}

		return actionResult;
	}
}
