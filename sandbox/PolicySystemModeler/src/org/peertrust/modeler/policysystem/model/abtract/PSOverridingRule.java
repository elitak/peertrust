package org.peertrust.modeler.policysystem.model.abtract;

import java.util.Collection;



/**
 *The interface to implement for model overriding rule object classes.
 *An overriding rules specify a replace of a policy by another in the
 *resource policy inheritance hierarchy.
 * 
 * @see org.peertrust.modeler.policysystem.model.abtract.PSPolicy
 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource
 * 
 * @author Patrice Congo
 */
public interface PSOverridingRule extends PSModelObject 
{
	
	/**
	 * @return The PSPolicy to be overridden
	 */
	public PSPolicy getHasOverridden();
	
	
	/**
	 * Sets a new PSPolicy to override
	 * @param policy -- the new ps policy to override
	 */
	public void setHasOverriden(PSPolicy policy);
	
	
	/**
	 * Get the policy that willl replace the overriden policy
	 * @return the replacing ps policy 
	 */
	public PSPolicy getHasOverridder();
	
	/**
	 * Sets a new overriding policy
	 * @param policy -- the new overriding policy 
	 */
	public void setHasOverrider(PSPolicy policy);
	
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#getModelObject()
	 */
	public Object getModelObject();
	
	/**
	 * Performs the overriding. I.e. removes the overiden policy and add 
	 * the overrider to the policies collection
	 * @param policies
	 */
	public void performOverridding(Collection policies);
	
}
