package org.peertrust.demo.resourcemanagement;

public class SetupException extends Exception {
	/**
	 * 
	 */
	public SetupException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SetupException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public SetupException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SetupException(Throwable cause) {
		super(cause);
	}

}
