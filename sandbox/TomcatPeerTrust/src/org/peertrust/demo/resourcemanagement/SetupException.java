package org.peertrust.demo.resourcemanagement;

/**
 * The exception thrown if setup fails.
 * 
 * @author Patrice Congo (token77)
 *
 */
public class SetupException extends Exception {
	/**
	 * construct a blank setup exception
	 */
	public SetupException() {
		super();
	}

	/**
	 * Construct a setup exception with its message and cause
	 * set to the passed one.
	 * 
	 * @param message -- the message of the setup exception
	 * @param cause -- the cause of the new setup exception
	 */
	public SetupException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construct a setup exception with its message sets to the
	 * passed one
	 * @param message -- the setup exception message
	 */
	public SetupException(String message) {
		super(message);
	}

	/**
	 * Construct a new setup exception with its cause set
	 * tp the passed one
	 * @param cause
	 */
	public SetupException(Throwable cause) {
		super(cause);
	}

}
