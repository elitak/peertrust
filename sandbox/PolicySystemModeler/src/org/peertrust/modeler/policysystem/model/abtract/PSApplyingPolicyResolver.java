/**
 * 
 */
package org.peertrust.modeler.policysystem.model.abtract;

import java.util.Vector;

/**
 * The interface to implement to privide a mechanism to find 
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
	Vector getApplyingPolicies(PSResource psResource);
	
	/**
	 * Get the applying policies for the entity with the
	 * given identity.
	 * @param identity -- the identity of the resource
	 * @return the applying policies for this resource
	 */
	Vector getApplyingPolicies(String identity);
}
