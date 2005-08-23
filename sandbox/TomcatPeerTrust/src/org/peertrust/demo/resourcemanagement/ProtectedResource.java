/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

/**
 * @author pat_dev
 *
 */
public class ProtectedResource extends Resource {

	private String policyName;
	
	boolean canAccess=false;
	private String reason=null;
	
	/**
	 * @param matchingStrategy
	 * @param realURL
	 * @param virtualURL
	 */
	public ProtectedResource(String matchingStrategy, String url) {
		super(matchingStrategy, url);
		// TODO Auto-generated constructor stub
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



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	super.toString()+
				" policyName:"+policyName+
				" canAccess:"+canAccess+
				" reason:"+reason+"\n";
	}

	public ProtectedResource getCopy(String url){
		ProtectedResource res=
			new ProtectedResource(getMatchingStrategy(),url);
		res.setCanAccess(getCanAccess());
		res.setMatchingStrategy(getMatchingStrategy());
		res.setPolicyName(getPolicyName());
		res.setReason(getReason());
		return res;
	}
	
	
}
