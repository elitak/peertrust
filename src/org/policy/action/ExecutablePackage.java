/**
 * @(#) ExecutablePackage.java
 */

package org.policy.action;

public interface ExecutablePackage
{
	ActionResult executeAction( final String functionName, final String arguments, final String inputVars ) throws IllegalArgumentException, FunctionFailureException, NoSuchFunctionException;
	
	
}
