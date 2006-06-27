package org.peertrust.modeler.policysystem.model.abtract;

/**
 * The base interface to implements by classes which abtract model object
 * 
 * @author Patrice Congo
 *
 */
public interface PSModelObject {
	//public static final String ROLE_RESOURCE_POLICY="ROLE_RESOURCE_POLICY";
	
	/**
	 * Key for role of the overriding policy in an overriding rule
	 */
	public static final String ROLE_ORULE_OVERRIDDER="ROLE_ORULE_OVERRIDDER";
	
	/**
	 * The key for the overridden policy in an overriding rule
	 */
	public static final String ROLE_ORULE_OVERRIDDEN="ROLE_ORULE_OVERRIDDEN";
	
	/**
	 * @return the original model object
	 */
	public Object getModelObject();
	
	/**
	 * @return the model object label
	 */
	public PSModelLabel getLabel();
	
	/**
	 * Sets the model object label
	 * @param label -- the new label to set
	 */
	public void setLabel(String label);
	
	/**
	 * Gets the role played by this object.
	 * E.g. <code>PSModelObject#ROLE_RESOURCE_POLICY</code>
	 * @return
	 */
	public String getRole();
	
	/**
	 * Sets a new role for this model object
	 * @param role -- the new role
	 */
	public void setRole(String role);
}