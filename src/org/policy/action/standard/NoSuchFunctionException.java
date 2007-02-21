package org.policy.action.standard;

import org.policy.action.ActionExecutionException;


public class NoSuchFunctionException extends ActionExecutionException
{
	/**
	 * 
	 */
	public NoSuchFunctionException() {
		super();
	}

	/**
	 * @param message
	 */
	public NoSuchFunctionException(String message) {
		super(message);
	}
	
	/**
	 * @param arg0
	 */
	public NoSuchFunctionException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param arg0
	 * @param arg1
	 */
	public NoSuchFunctionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
}

