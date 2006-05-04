/**
 * @(#) ActionResult.java
 */

package org.policy.action;

public class ActionResult
{
	private boolean executionResult;
	
	private ResultSet variableBindings;
	
	public ActionResult( final boolean executionResult, final String variableList )
	{
		
	}
	
	public ActionResult( final boolean executionResult )
	{
		
	}
	
	public void setVariableList( final String variableList )
	{
		
	}
	
	public int getNumberVariables( )
	{
		return 0;
	}
	
	public String getVariable( final int i )
	{
		return null;
	}
	
	public int getNumberResults( )
	{
		return 0;
	}
	
	public Result getResult( final int i )
	{
		return null;
	}
	
	public boolean getExecutionResult( )
	{
		return false;
	}
	
	
}
