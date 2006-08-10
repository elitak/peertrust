package org.policy.action;


public class NoSuchFunctionException extends ActionExecutionException
{
	public NoSuchFunctionException() {
		super("no such function!");
	}
}

