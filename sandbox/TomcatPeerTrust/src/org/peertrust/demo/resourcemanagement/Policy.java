package org.peertrust.demo.resourcemanagement;



/**
 * @author pat_dev
 *
 */
public class Policy {
	/**
	 * the name of the policy
	 */
	private String policyName;
	
	/**
	 * the value of the policy
	 */
	private String policyValue;
	
	/**
	 *Construct a new policy object
	 *@param policyName -- the name of the new policy
	 *@param policyValue -- the value of the new policy 
	 */
	public Policy(String policyName, String policyValue) {
		this.policyName=policyName;
		this.policyValue=policyValue;
	}
	
	/**
	 * @return returns the policy name
	 */
	public String getPolicyName() {
		return policyName;
	}

	/**
	 * return the policy value
	 * @return
	 */
	public String getPolicyValue() {
		return policyValue;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"policyName:"+policyName+
				" policyValue:"+policyValue;//+
				//" includedPolicy:"+includedPolicy;
	}
	
	
}
