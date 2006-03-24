package org.peertrust.modeler.policysystem.model;

/**
 * The interface to implements for a model check mechanism
 * @author Patrice Congo
 */
public interface PSModelCheck 
{
	/**
	 * To perform the model check.
	 * @return true if check passed otherwise false
	 */
	boolean doCheck();
	
	/**
	 * Get the failure message, describing the check.
	 * @return
	 */
	String getMessage();
	
	
}
