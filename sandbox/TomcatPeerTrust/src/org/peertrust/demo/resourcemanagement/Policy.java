/**
 * 
 */
package org.peertrust.demo.resourcemanagement;



/**
 * @author pat_dev
 *
 */
public class Policy {
	
	private String policyName;
	private String policyValue;
	//private String includedPolicy= null;
	
	/**
	 * 
	 */
	public Policy(String policyName, String policyValue) {
		this.policyName=policyName;
		this.policyValue=policyValue;
	}

//	public void setIncludedPolicy(String policy){
//		includedPolicy=policy;
//	}
//
//	public String getIncludedPolicy(){
//		return includedPolicy;
//	}
	
	public String getPolicyName() {
		return policyName;
	}

	public String getPolicyValue() {
		return policyValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"policyName:"+policyName+
				" policyValue:"+policyValue;//+
				//" includedPolicy:"+includedPolicy;
	}
	
	
}
