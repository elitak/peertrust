/**
 * 
 */
package org.peertrust.modeler.policysystem.model.exceptions;

/**
 * @author pat_dev
 *
 */
public class BadConfigFileException extends Exception {

	/**
	 * 
	 */
	public BadConfigFileException() {
		super();
	}

	/**
	 * @param message
	 */
	public BadConfigFileException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BadConfigFileException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public BadConfigFileException(Throwable cause) {
		super(cause);
	}

}
