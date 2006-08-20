package org.peertrust.modeler.policysystem.model.abtract;


/**
 * The interface to be implement by classes which abtract
 * a policy in the policy system model.
 * A policy basically has a name a value and can portect a resource
 * 
 * @author Patrice Congo
 *
 */
public interface PSPolicy extends PSModelObject 
{
	
	/**
	 * Gets the hasValue property of the model object
	 * as String.
	 * @return the hasValue property
	 */
	public String getHasValue();
	
	/**
	 * Sets the hasValue property
	 * @param value -- the new value
	 */
	public void setHasValue(String value);
	
	
//	public Object getModelObject();
	/**
	 * Gets the model object which is guarded by this policy
	 *  
	 * @return the guarded model object
	 */
	public PSModelObject getGuarded();
	
	/**
	 * Sets the model object which is guarded by this policy
	 * @param guarded -- the model object which is guarded by this policy
	 */
	public void setGuarded(PSModelObject guarded);
	
}
