package org.policy.action.standard;

import org.policy.action.ActionExecutionException;


public class WrongArgumentsException extends ActionExecutionException
{
	/**
	 * 
	 */
	public WrongArgumentsException() {
		super();
	}

	/**
	 * @param message
	 */
	public WrongArgumentsException(String message) {
		super(message);
	}
	
	/**
	 * @param arg0
	 */
	public WrongArgumentsException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param arg0
	 * @param arg1
	 */
	public WrongArgumentsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}

