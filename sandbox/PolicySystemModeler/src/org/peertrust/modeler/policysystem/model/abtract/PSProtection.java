package org.peertrust.modeler.policysystem.model.abtract;

/**
 * The interface to implemen by class  which are abtracting a
 * conditional guarding i.e. guarding only if a given condition applies.
 * 
 * @author Patrice Congo
 *
 */
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
	
	/**
	 * Perform overriding using the given overriding rule 
	 * @param oRule the overriding rule to apply
	 */
	public void override(PSOverridingRule oRule);
}
