package org.peertrust.modeler.policysystem.model.abtract;



/**
 * Interface to implements  by classes which abtract a model
 * filter. A filter is basicaly made of a policy an a condition which
 * conditions the protection the given filter. 
 *  
 * @author Patrice Congo
 *
 */
public interface PSFilter extends PSModelObject 
{

	/**
	 * Gets the filter condition
	 * @return
	 */
	public String getCondition();
	
	/**
	 * sets the condition
	 * @param condition -- a string representing the condition
	 * 
	 */
	public void setHasCondition(String condition);
	
	/**
	 * Check whether a filter contains the condition
	 * @param condition -- condition to check for
	 * @return
	 */
	public boolean containsCondition(String condition);
	
	/**
	 * Removes the condition from a filter
	 * @param condition -- the condition to remove
	 */
	public void removeCondition(String condition);
	
	/**
	 * Removes all conditions
	 */
	public void removeAllConditions();
	
	/**
	 * Return the policy which protect this filter
	 * @return the PSPolicy protection this flter
	 */
	public PSPolicy getIsProtectedBy();
	
	/**
	 * Sets the protecting policy for this filter
	 * @param policy -- the new protecting policy for this filter
	 */
	public void setIsProtectedBy(PSPolicy policy) ;
	
	/**
	 * Remove the given protected filter provided it is attached to this filter
	 * 
	 * @param policy -- the policy to remove for this filter
	 */
	public void removeIsProtectedBy(PSPolicy policy);
}
