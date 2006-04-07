package org.peertrust.modeler.policysystem.model.abtract;

import java.util.Vector;

public interface PSPolicySystem 
{
	
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
	
	public Vector getInheritedPolicies(PSResource resource);
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	public Vector getFilters(PSResource resource);
	
	/**
	 * 
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
	
	public Vector getDirectChilds(PSModelObject parent);
	
	public Vector getDirectParents(PSModelObject child);
	
	public Vector getPathToAncestorRoots(PSModelObject node);
	
	public PSResource createResource(
						String label,
						String identity);
	
	public PSFilter createFilter(
						String label,
						String[] conditions,
						PSPolicy[] policies);
	
	public PSPolicy createPolicy(
						String label,
						String value);
	
	public PSOverridingRule createOverriddingRule(
						String label,
						PSPolicy overridder,
						PSPolicy overridden);
	
	public void addPSModelChangeEventListener(PSModelChangeEventListener l);
	public void removePSModelChangeEventListener(PSModelChangeEventListener l);
}
