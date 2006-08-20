package org.peertrust.modeler.policysystem.model.abtract;

import java.util.List;



/**
 * The interface to implement by classes that abtracts a policy system 
 * resource.
 *  
 * @author Patrice Congo
 *
 */
public interface PSResource extends PSModelObject {
	
	/**
	 * To get the resource mapping
	 * @return the resource mapping
	 */
	public String getHasMapping();
	/**
	 * To set the resource mapping
	 * @param name -- the new name, mapping
	 */
	public void setHasMapping(String name);
	
	
	/**
	 * To get the overriding rules linked to a resource
	 * @return a list of overriding rules
	 */
	public List getIsOverrindingRule();
	
	/**
	 * To add an overridingrule to this resource
	 * @param rule -- the rule to add
	 */
	public void addIsOverrindingRule(PSOverridingRule rule);
	/**
	 * To remove an overriding rule linked to this resource
	 * @param ruleToDel -- the rule to delete
	 */
	public void removeOverriddingRule(PSOverridingRule ruleToDel);
	
	/**
	 * To get the parent resource
	 * @return the parent resource
	 */
	public PSResource getParent();
	
	/**
	 * To set a new parent resource
	 * @param res -- the parent resource
	 */
	public void setParent(PSModelObject res);
	
	/**
	 * To get the children of the resource
	 * @return a list containing the children of this resource 
	 */
	public List getChildren();
	
//	/**
//	 * To get the PSModelObjects protecting this resource 
//	 * @return a vector of protecting PSModelObject
//	 */
//	public List getIsProtectedBy();
	
//	/**
//	 * Protect with the given policy 
//	 * @param policy
//	 */
//	public void addIsProtectedBy(PSPolicy policy);
//	/**
//	 * Remove a protecting policy
//	 * @param policyToDel -- the plicy to remove
//	 */
//	public void removePolicy(PSPolicy policyToDel);
	
	/**
	 * Gets all filters linked to this resource
	 * @return a vector of filters linked to this resource
	 */
	public List getHasFilter();
	
	/**
	 * @param filter
	 */
	public void addHasFilter(PSFilter filter);
	
	/**
	 * Remove a filter
	 * @param filterToRemove -- the filter to remove
	 */
	public void removeFilter(PSFilter filterToRemove);
	
	/**
	 * Test whether this resource can have a child resource or not
	 * 
	 * @return 	true if this resource cann have a child (e.g. a directory);<br/>
	 * 			false if not (e.g. a file)
	 */
	public boolean canHaveChild();
	
	/**
	 * To specify whether this resource can have achild
	 * @param canHaveChild -- a boolean specifying whether the resource
	 * 			can have a child
	 */
	public void setCanHaveChild(boolean canHaveChild);
}
