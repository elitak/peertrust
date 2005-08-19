/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

/**
 * @author pat_dev
 *
 */
public interface PolicyEvaluator {
	static final public int SUCCESS_FLAG=-1;
	/**
	 * To evaluate the policies in the vector;
	 * 
	 * @param policyVector -- vector of policy to evaluate
	 * @param negotiatingPeerName TODO
	 * @return 	the index of the first policy that has failed 
	 * 			or SUCCESS_FLAG=-1 if all policy are successfully evaluated.   
	 */
	public int eval(final Vector policyVector, String negotiatingPeerName);
}
