/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

/**
 * Implementation of PolicyEvaluator interface to provide
 * mechanism to evaluate policies during trust negotiation . 
 * @author Patrice Congo (token77)
 */
public interface PolicyEvaluator {
	static final public int SUCCESS_FLAG=-1;
	/**
	 * To evaluate the policies in the vector;
	 * 
	 * @param policyVector -- vector of policy to evaluate
	 * @param 	negotiatingPeerName -- the name of the remote negotiating
	 * 			peer
	 * @return 	the index of the first policy that has failed 
	 * 			or SUCCESS_FLAG=-1 if all policy are successfully evaluated.   
	 */
	public int eval(
				final Vector policyVector, 
				String negotiatingPeerName);
}
