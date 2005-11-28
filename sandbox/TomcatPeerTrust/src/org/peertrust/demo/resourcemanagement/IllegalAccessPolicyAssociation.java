/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

/**
 * Exception to throw if a policy name or value is NULL when
 * getting it during the negotiation process.
 * @author Patrice Congo
 *
 */
public class IllegalAccessPolicyAssociation extends Exception {

	/**
	 * Constructs a blank IllegalAccessPolicyAssociation 
	 */
	public IllegalAccessPolicyAssociation() {
		super();
	}

	/**
	 * Constructs a blank IllegalAccessPolicyAssociation with
	 * the pased message.
	 * @param message
	 */
	public IllegalAccessPolicyAssociation(String message) {
		super(message);
	}

	/**
	 * Constructs a blank IllegalAccessPolicyAssociation with a message
	 * and the cause
	 * @param arg0
	 * @param arg1
	 */
	public IllegalAccessPolicyAssociation(
									String message, 
									Throwable cause) 
	{
		super(message, cause);
	}

	/**
	 * Constructs a blank IllegalAccessPolicyAssociation with a cause
	 * @param cause -- the cause of this exception
	 */
	public IllegalAccessPolicyAssociation(Throwable cause) {
		super(cause);
	}

}
