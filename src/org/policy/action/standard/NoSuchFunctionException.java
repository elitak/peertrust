package org.policy.action.standard;

import org.policy.model.ActionExecutionException;


public class NoSuchFunctionException extends ActionExecutionException
{
	public NoSuchFunctionException() {
		super("no such function!");
	}
}

