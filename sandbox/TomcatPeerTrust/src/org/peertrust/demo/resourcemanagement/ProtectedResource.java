/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

/**
 * @author pat_dev
 *
 */
public class ProtectedResource extends Resource {

	private String policyName;
	
	boolean canAccess=false;
	private String reason=null;
	private Vector credentials=new Vector(8);
	
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
				" reason:"+reason+
				"credentials:"+credentials+
				"\n";
	}

	public ProtectedResource getCopy(String url){
		ProtectedResource res=
			new ProtectedResource(getMatchingStrategy(),url);
		res.setCanAccess(getCanAccess());
		res.setMatchingStrategy(getMatchingStrategy());
		res.setPolicyName(getPolicyName());
		res.setReason(getReason());
		res.setCredentials(getCredentials());
		return res;
	}
	
	public void addCredential(String cred){
		if(cred!=null){
			credentials.add(cred);
		}
		return;
	}
	
	public Vector getCredentials(){
		Vector v=new Vector(credentials.capacity());
		boolean bol=v.addAll(credentials);
		System.out.println("addAll:"+bol+"v:"+v+" credentials:"+credentials.size());
		return v;
	}



	/**
	 * @param credentials The credentials to set.
	 */
	public void setCredentials(Vector credentials) {
		this.credentials = credentials;
	}
	
}
