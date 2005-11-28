package org.peertrust.demo.resourcemanagement;

/**
 * Exception thrown is a format is not supported.
 * E.g. if a setup from an xml file encounters an unsupported 
 * xml tagrere 
 * 
 * @author Patrice Congo
 *
 */
public class UnsupportedFormatException extends Exception {

	/**
	 * Create a blank new UnsupportedFormatException 
	 */
	public UnsupportedFormatException() {
		super();
	}

	/**
	 * Creates a new UnsupportedFormatException with its
	 * message set to the passed value.
	 * @param message -- message for the new exception
	 */
	public UnsupportedFormatException(String message) {
		super(message);
	}

	/**
	 *Creates a new UnsupportedFormatException and sets its
	 *message ans cause with the passed value 
	 * @param message -- the mesage for the new UnsupportedFormatException 
	 * @param cause -- the cause for the new UnsupportedFormatException
	 */
	public UnsupportedFormatException(String message, Throwable cause) 
	{
		super(message, cause);
	}

	/**
	 * Creates a new UnsupportedFormatException and sets its cause with
	 * the passed value
	 * @param cause -- the cause for the new UnsupportedFormatException
	 */
	public UnsupportedFormatException(Throwable cause) 
	{
		super(cause);
	}

}
