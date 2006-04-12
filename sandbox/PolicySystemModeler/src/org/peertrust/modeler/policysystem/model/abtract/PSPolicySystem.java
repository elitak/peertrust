package org.peertrust.modeler.policysystem.model.abtract;

import java.util.Vector;

/**
 * This is the interface a policy system must implement.
 * 
 * @author Patrice Congo 
 *
 */
public interface PSPolicySystem 
{
	
	/**
	 * returns the resource in the policy system model
	 * @return all resource in the policy system model
	 */
	public Vector getResources();
	
	/**
	 * Returns the Policies define at this Resource
	 * 
	 * @param resource -- the PSResource which loacal
	 * 			policies is been retrieved
	 * @return the local policies appling to a resource
	 * 			if resource is non null else return all
	 * 			policies in the system
	 */
	public Vector getLocalPolicies(PSResource resource);
	
	/**
	 * Returns all the policies a resource has inherited from
	 * its parent resources 
	 * @param resource -- the resource which inherited policy is to be returned
	 * @return the inherited policies for the given resource
	 */
	public Vector getInheritedPolicies(PSResource resource);
	
	/**
	 * Get the  filters defined under the given resource
	 * @param resource  -- the resource which filter is to be retrieved
	 * @return the filters for the given resource
	 */
	public Vector getFilters(PSResource resource);
	
	/**
	 * To get the overriding rules defined for this resource
	 * @param resource
	 * @return
	 */
	public Vector getOverriddingRules(PSResource resource);
	
	/**
	 * 
	 * @param modelObjectWrapper
	 * @return
	 */
	public Vector getRoots(Class modelObjectWrapper);
	
	/**
	 * To get the direct children of the a model object if applyable; 
	 * i.e where a parent hierarchy is defined. e.g. for resources
	 * E.g. for policies
	 * 
	 * @param parent -- the model object which chids are to be returned
	 * @return
	 */
	public Vector getDirectChildren(PSModelObject parent);
	
	/**
	 * Get the direct parents of a ps model object if applyable e.g. 
	 * where a parent child hierarchy is defined
	 * 
	 * @param child -- the model object with parents are to be returmed
	 * @return thedirect parents ob the model object
	 */
	public Vector getDirectParents(PSModelObject child);
	
	public Vector getPathToAncestorRoots(PSModelObject node);
	
	/**
	 * creates a resource with the provided label and identity.
	 * 
	 * @param label -- the label for the new resource
	 * @param identity -- the identity for the new resource
	 * @return
	 */
	public PSResource createResource(
						String label,
						String identity);
	
	/**
	 * creates a filter with the specified label, conditions and policies
	 * @param label -- the label for the policy
	 * @param conditions -- the filter conditions 
	 * @param policies -- the filter policies
	 * @return
	 */
	public PSFilter createFilter(
						String label,
						String[] conditions,
						PSPolicy[] policies);
	
	/**
	 * Creates a policy with the given label and value
	 * @param label -- the label for the policy
	 * @param value -- the policy value
	 * @return the created policy
	 */
	public PSPolicy createPolicy(
						String label,
						String value);
	
	/**
	 * Create a overriding rule with the given label and policies 
	 * @param label -- the label for this rule
	 * @param overridder -- the overriding policy
	 * @param overridden -- the overridden policy
	 * 
	 * @return the created overriding rule
	 */
	public PSOverridingRule createOverriddingRule(
						String label,
						PSPolicy overridder,
						PSPolicy overridden);
	
	/**
	 * To register a PSModelChangeEventListener to list for model changes
	 * 
	 * @param l -- the listener to register
	 */
	public void addPSModelChangeEventListener(PSModelChangeEventListener l);
	
	/**
	 * To removed a listener
	 * @param l -- the listener to remove
	 */
	public void removePSModelChangeEventListener(PSModelChangeEventListener l);
	
	/**
	 * Return a Vector of model Object which is linked to a given model object:
	 * <ul>
	 * 	<li><code>getLinkedModelObject(aPSPolicy,PSFilter.class)</code> will return all the
	 * filters that contains the given psPoliy
	 * 		<li><code>getLinkedModelObject(aPSPolicy,PSOverridingRule.class)</code> returns 
	 * 			all the overriding rule which contains the given policy as overridder or overriden.
	 * 		<li> and so on	
	 * 		<li><code>getLinkedModelObject(aPSPolicy,null)</code> will return all kind of 
	 * 			linked model objects
	 * </ul>
	 * 
	 * @param psModelObject -- the model object
	 * @param linkedObectjType -- the type of model linked objects to return
	 * @return a vector of linked objects
	 */
	public Vector getLinkedModelObjects(
						PSModelObject psModelObject, 
						Class linkedObectjType);
}
