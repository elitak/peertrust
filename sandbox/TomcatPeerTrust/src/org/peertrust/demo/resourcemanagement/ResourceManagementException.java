/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

/**
 * Exception throws to signal failure of resource management tasks.
 * 
 * @author Patrice Congo (token77)
 */
public class ResourceManagementException extends Exception {

	/**
	 * 
	 */
	public ResourceManagementException() {
		super();
	}

	/**
	 * @param message
	 */
	public ResourceManagementException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ResourceManagementException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ResourceManagementException(Throwable cause) {
		super(cause);
	}

}
