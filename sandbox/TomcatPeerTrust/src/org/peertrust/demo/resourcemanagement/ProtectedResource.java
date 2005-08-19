/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

/**
 * @author pat_dev
 *
 */
public class ProtectedResource extends Resource {

	private String action;
	private String policyName;
	
	boolean canAccess=false;
	private String reason=null;
	
	/**
	 * @param matchingStrategy
	 * @param realURL
	 * @param virtualURL
	 */
	public ProtectedResource(String matchingStrategy, String realURL,
			String virtualURL) {
		super(matchingStrategy, realURL, virtualURL);
		// TODO Auto-generated constructor stub
	}

	

	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}



	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}



	/**
	 * @return Returns the policyName.
	 */
	public String getPolicyName() {
		return policyName;
	}

	/**
	 * @param policyName The policyName to set.
	 */
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}



	/**
	 * @return Returns the canAccess.
	 */
	public boolean getCanAccess() {
		return canAccess;
	}



	/**
	 * @param canAccess The canAccess to set.
	 */
	public void setCanAccess(boolean canAccess) {
		this.canAccess = canAccess;
	}



	/**
	 * @return Returns the reason.
	 */
	public String getReason() {
		return reason;
	}



	/**
	 * @param reason The reason to set.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	
	
	
}
