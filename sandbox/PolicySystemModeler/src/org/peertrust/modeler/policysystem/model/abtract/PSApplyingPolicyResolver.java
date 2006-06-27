/**
 * 
 */
package org.peertrust.modeler.policysystem.model.abtract;

import java.util.List;

/**
 * The interface to implement to provide a mechanism to find 
 * all Policies that applies to a resource
 *  
 * @author Patrice Congo
 *
 */
public interface PSApplyingPolicyResolver 
{
	
	/**
	 * Get the policies for the given resource
	 * @param psResource -- the ps resource
	 * @return the applying policies a a vector
	 */
	List getApplyingPolicies(PSResource psResource);
	
	/**
	 * Get the applying policies for the entity with the
	 * given identity.
	 * @param identity -- the identity of the resource
	 * @return the applying policies for this resource
	 */
	List getApplyingPolicies(String identity);
}
