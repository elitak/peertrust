package org.peertrust.modeler.policysystem.model.abtract;

public interface PSProtection 
{
	/**
	 * @return returns the protection policy
	 */
	public PSPolicy getPolicy();
	
	/**
	 * Sets a new protection policy
	 * @param policy -- the new policy
	 */
	public void setPolicy(PSPolicy policy);
	
	/**
	 * @return return the protection condition
	 */
	public String getCondition();
	
	/**
	 * Remoce a protection condition
	 * @param condition -- the condition to remove 
	 */
	public void setCondition(String condition);
	
	public void override(PSOverridingRule oRule);
}
