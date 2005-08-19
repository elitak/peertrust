/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

/**
 * @author pat_dev
 *
 */
public class Policy {
	
	private String policyName;
	private String policyValue;
	private Policy includedPolicy= null;
	
	/**
	 * 
	 */
	public Policy(String policyName, String policyValue) {
		this.policyName=policyName;
		this.policyValue=policyValue;
	}

	public void setIncludedPolicy(Policy policy){
		includedPolicy=policy;
	}

	public Policy getIncludedPolicy(){
		return includedPolicy;
	}
	
	public String getPolicyName() {
		return policyName;
	}

	public String getPolicyValue() {
		return policyValue;
	}
}
