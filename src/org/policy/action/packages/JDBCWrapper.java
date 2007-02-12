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

package org.policy.action.packages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.policy.action.AbstractExecutorWrapper;
import org.policy.action.ActionRequest;
import org.policy.action.ActionResult;
import org.policy.action.FunctionFailureException;
import org.policy.action.NoSuchFunctionException;
import org.policy.action.Result;
import org.policy.action.parser.Parse;
import org.policy.action.tools.Functions;

/**
 * <p>
 * The JDBCWrapper can get the data from the given data base by a SELECT-Statement 
 * in mysql form. The data is assigned to the variable X. 
 * For a JDBCWrapper call the package_name should be �rdbms� and the function �query�. 
 * There are only two arguments in a JDBCWrapper call, one is a select statement, 
 * which can correctly carry out, and another is the existing database name 
 * </p><p>
 * $Id: JDBCWrapper.java,v 1.1 2007/02/12 12:21:46 dolmedilla Exp $
 * <br/>
 * Date: 29-May-2006
 * <br/>
 * Last changed: $Date: 2007/02/12 12:21:46 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */
public class JDBCWrapper extends AbstractExecutorWrapper {

	private static final String DB_PASSWORT = "root4fcde";

	private Connection conn = null;
	
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
	public ActionResult executeAction(String functionName, String[] arguments, String[] inputVars) throws IllegalArgumentException, FunctionFailureException, NoSuchFunctionException {
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
	public ActionResult localExecuteAction( final ActionRequest actionRequest ) throws IllegalArgumentException, NoSuchFunctionException, FunctionFailureException{
		if (!actionRequest.getFunction().equals(Functions.QUERY))
			throw new NoSuchFunctionException();
		
		if (actionRequest.getNumberArguments()> 2)
			throw new IllegalArgumentException("IllegaArgumentException");
		
		String query = actionRequest.getArgument(0);
		String database = actionRequest.getArgument(1);
		String[] inputVars  = actionRequest.getInputsVar();
		
		verbindung(database);
		ActionResult actionResult = new ActionResult(true, inputVars);
		try {
			actionResult = toResult(query(query, inputVars.length), actionResult);
		} catch (SQLException e) {
			throw new FunctionFailureException(e.getMessage());
		}
		return actionResult;
	
	}
	
	
	private void verbindung(String database) throws FunctionFailureException{
		final String URL = "jdbc:mysql://localhost/" + database;
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(ClassNotFoundException e){
			throw new FunctionFailureException(e.getMessage());
		}

		catch(InstantiationException e){
			throw new FunctionFailureException(e.getMessage());
		}

		catch(IllegalAccessException e){
			throw new FunctionFailureException(e.getMessage());
		}

		try{
			System.out.println("Versuch zu verbinden...");
			this.conn = DriverManager.getConnection(URL.trim(), "root", DB_PASSWORT);
			System.out.println("Verbindung aufgebaut...");
		}catch(SQLException e){
			throw new FunctionFailureException(e.getMessage());
		}
	}
	
	
    @SuppressWarnings("unchecked")
	private Vector query(String query, int varNumber) throws SQLException{
		
		PreparedStatement pstmt = null;
		Parse parser = new Parse();
		String[] vars = parser.getSelectVars(query);
		ResultSet rset = null;
		Vector rtn = new Vector();

		if (varNumber > vars.length)
			throw new IllegalArgumentException("the number of the inputVariable is not as same as the number of select-variable!");
		
		pstmt = conn.prepareStatement(query);
		rset = pstmt.executeQuery ();
		while (rset.next()){
			List rowValueisList = new ArrayList();
			for (int i = 0; i < vars.length; i ++){
				String res = rset.getString(vars[i]);
				if (null == res)
					rowValueisList.add("");
				else
					rowValueisList.add(res);
			}
			rtn.add(rowValueisList);
		}
		rset.close();
		pstmt.close ();
		return rtn;
	}
    
	private void abbau() throws SQLException{
		this.conn.close();
	}
	
	/**
	 * constructs an action result
	 * @param rtn the results, which are saved in a vector after execution
	 * @param actionResult a action result, which will save the results 
	 * @return the action result
	 * @exception FunctionFailureException if the action executes unsuccessful
	 */
	public ActionResult toResult(Vector rtn, ActionResult actionResult) throws FunctionFailureException{
		Iterator a = rtn.iterator();

		while (a.hasNext()){
			List temp = (List)a.next();
			String[] bindings = Functions.toArray(temp);
			Result result = new Result(bindings);
			actionResult.getVariableBindings().addResult(result);
		}
		return actionResult;
	}
}
