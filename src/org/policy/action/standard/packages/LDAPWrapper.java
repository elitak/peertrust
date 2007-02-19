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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.policy.action.standard.AbstractExecutorWrapper;
import org.policy.action.standard.ActionRequest;
import org.policy.action.standard.ActionResult;
import org.policy.action.standard.FunctionFailureException;
import org.policy.action.standard.NoSuchFunctionException;
import org.policy.action.standard.Result;
import org.policy.action.standard.parser.Parse;
import org.policy.action.standard.tools.Functions;

/**
 * <p>
 * The LDAPWrapper can get the data from a Directory Server with use of a 
 * logical expression. The data is assigned to the variable X. 
 * For a LDAPWrapper call the package_name should be "ldap" and the function "search". 
 * There are only two arguments in a LDAPWrapper call, one are the attribute names 
 * to search for, and another is the logical expression, which serves as a search
 * filter. 
 * </p><p>
 * $Id: LDAPWrapper.java,v 1.1 2007/02/19 09:01:27 dolmedilla Exp $
 * <br/>
 * Date: 22-JUN-2006
 * <br/>
 * Last changed: $Date: 2007/02/19 09:01:27 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */
public class LDAPWrapper extends AbstractExecutorWrapper {
	Hashtable<String, String> env;
	DirContext ctx;
	
	/**
	 * constructs an new executeAction
	 * @param functionName the name of function
	 * @param arguments a array of arguments
	 * @param inputVars a array of input variables
	 * @return an action result
	 * @exception IllegalArgumentException if the argument is false
	 * @exception NoSuchFunctionException if the function name is wrong
	 * @exception FunctionFailureException if the action executes unsuccessful
	 */
	public ActionResult executeAction(final String functionName, final String[] arguments, final String[] inputVars)
		throws FunctionFailureException, NoSuchFunctionException, IllegalArgumentException
	{
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
	public ActionResult localExecuteAction(ActionRequest actionRequest) throws IllegalArgumentException, FunctionFailureException,
	NoSuchFunctionException  {
		if (!actionRequest.getFunction().equals(Functions.ENTRY_IN_DIRECTORY))
			throw new NoSuchFunctionException();

		if (actionRequest.getNumberArguments() != 2)
			throw new IllegalArgumentException("IllegaArgumentException");

		String allSelectAttributes = actionRequest.getArgument(0);
		String attributesConstraint = actionRequest.getArgument(1);
		String[] inputVars = actionRequest.getInputsVar();
		
		verbindung();
		
		ActionResult actionResult = new ActionResult(true, inputVars);

		actionResult = toResult(search(allSelectAttributes, attributesConstraint,inputVars.length), actionResult);
		return actionResult;
	}
	
	
	
	private void verbindung() throws FunctionFailureException {
		String teilURL = "ldap://localhost:389/";
		env = new Hashtable<String, String>();
		String root = "dc=example,dc=com";
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, teilURL + root);
		env.put(Context.SECURITY_AUTHENTICATION, "none");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Manager" + "," + root);
		env.put(Context.SECURITY_CREDENTIALS, "secret");

		try {
			System.out.println("Create the inital directory context");
			ctx = new InitialDirContext(env);

			
		} catch (NamingException e) {
			throw new FunctionFailureException(e.getMessage());
		}
	
	}

	public Vector search(String ldapEntryAttributes,String attributesConstraint, int varNumber) throws FunctionFailureException{
		Vector<List<String>> rtn = new Vector<List<String>>();
		String[] attributesArray = Parse.stringToArray(ldapEntryAttributes);
		
//		 Create the default search controls
		SearchControls ctls = new SearchControls();
	// Zum Sicher, dass wenn eingabe noch leerzeichen enth�lt	
		String[] attrIDs = new String[attributesArray.length];
		
		for(int i=0; i<attributesArray.length;i++){
			attrIDs[i]=attributesArray[i].trim();
		}
		
		ctls.setReturningAttributes(attrIDs);
		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//		 Specify the search filter to match
//		 Ask for objects that have the attribute "sn" == "Geisel"
//		 and the "mail" attribute
	//vom Parser wird "(" & ")" Zeichen als andere bedeutung erkannt, Deswegen werden in Anfrage mit "[" & "]" ersetzen, hier werden zur�cksetzen	
		String filterTemp = attributesConstraint.replace('[', '(');
		String filter = filterTemp.replace(']', ')');
		
		System.out.println("Search for "+filter);
		System.out.println("to find attributes: "+ldapEntryAttributes);
		System.out.println("in_Variable_Number : "+varNumber);
		
//		 Search for objects using the filter
		try {
			NamingEnumeration<SearchResult> answer = ctx.search("", filter, ctls);
			Attributes attributes;
			while(answer.hasMore()){
				SearchResult entry = (SearchResult)answer.next();
				attributes = entry.getAttributes();
				List<String> entryAttributesList = new ArrayList<String>();
				for(int i=0; i<attrIDs.length;i++){
					String attributeValueString ;
					if(attributes.get(attrIDs[i])==null){
						attributeValueString = ""; 
					}
					else{
						attributeValueString = attributes.get(attrIDs[i]).toString();
					}
					entryAttributesList.add(attributeValueString);
				}
				rtn.add(entryAttributesList);
			}
		} catch (NamingException e) {
			throw new FunctionFailureException(e.getMessage());
		}

		try {
			ctx.close();
		} catch (NamingException e) {
			throw new FunctionFailureException(e.getMessage());
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
