package org.peertrust.modeler.policysystem.model.abtract;

import java.util.List;


/**
 * Interface to implements  by classes which abtract the a model
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
	
	//TODO change filter kann only have only policy now
	public List getIsprotectedBy();
	
	public void addIsProtectedBy(PSPolicy policy) ;
	
	public void removeIsProtectedBy(PSPolicy policy);
}
