package org.peertrust.modeler.policysystem.model.abtract;

/**
 * The interface to beimplement by statement class 
 * for the policy system model.
 * 
 * @author Patrice Congo
 *
 */
public interface PSModelStatement 
{
	/**
	 * Gets the statement subject
	 * @return the PSModelOject acting as the Subject in the statement
	 */
	public PSModelObject getSubject();
	
	/**
	 * To set the model object that plays the subject role in this 
	 * statement
	 * @param subject -- the new statement subject
	 */
	public void setSubject(PSModelObject subject);
	
	/**
	 * Get the statement object
	 * @return the PSModelObject acting as object in this statement
	 */
	public Object getObject();
	
	
	/**
	 * To set the model object that plays the role of object in 
	 * this statement
	 * @param object
	 */
	public void setObject(Object object);
	
	/**
	 * Return a string representing the statement property
	 * 
	 * @return a tring representing the statement property
	 */
	public String getProperty();
	
	/**
	 * To set a string identifying the property of this statement
	 * @param property -- the property id
	 */ 
	public void setProperty(String property);
	
}
