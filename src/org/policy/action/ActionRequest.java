/**
 * @(#) ActionRequest.java
 */

package org.policy.action;

public class ActionRequest
{
	private String function;
	
	private String arguments;
	
	private String inputVars;
	
	protected ActionRequest( )
	{
		
	}
	
	/**
	 * Constructor that builds an ActionResponse out of the arguments (given with strings and RDF strings)
	 */
	public ActionRequest( final String function, final String arguments, final String inputVars ) throws IllegalArgumentException
	{
		
	}
	
	public String getFunction( )
	{
		return null;
	}
	
	public String getArgument( int i )
	{
		return null;
	}
	
	public int getNumberInputVars( )
	{
		return 0;
	}
	
	public String getInputVar( final int i )
	{
		return null;
	}
	
	public void setFunction( final String function )
	{
		
	}
	
	protected void addArgument( final String argument )
	{
		
	}
	
	protected void addInputVariable( final String inputVariable )
	{
		
	}
	
	protected void getActionRequest( )
	{
		
	}
	
	public int getNumberArguments( )
	{
		return 0;
	}
	
	
}
