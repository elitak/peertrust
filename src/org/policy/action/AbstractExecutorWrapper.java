/**
 * @(#) AbstractExecutorWrapper.java
 */

package org.policy.action;

public abstract class AbstractExecutorWrapper implements ExecutablePackage
{
	public abstract ActionResult localExecuteAction( final ActionRequest actionRequest );
	
	public ActionResult executeAction( final String functionName, final String arguments, final String inputVars ) throws IllegalArgumentException, FunctionFailureException, NoSuchFunctionException
	{
		return null;
	}
	
	
}
