/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

/**
 * A Protected Resource is a resource that requires 
 * peertrust negotiation.
 * 
 * @author pat_dev
 *
 */
public class ProtectedResource extends Resource 
{

	/**
	 * the name of the policy that protects this resource
	 */
	private String policyName;
	
	/**
	 * a flag signaling whether the resource can now be access or 
	 * not. It is set after negitonation. 
	 */
	boolean canAccess=false;
	
	/**
	 *Holds the reason why this resource cannot be access. 
	 */
	private String reason="_No_Nego_Done_Yet_";
	
	/**
	 * Hold the credentials required to get this resource and which
	 * the remote peer must provide.
	 */
	private Vector credentials=new Vector(8);
	
	/**
	 * Contructs a ProtectedResource
	 * @param matchingStrategy -- the expression used to match urls 
	 * @param url -- the url of this resource
	 */
	public ProtectedResource(String matchingStrategy, String url) {
		super(matchingStrategy, url);
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



	/**
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

	/**
	 * To clone this resource and customized it with the provided 
	 * url.
	 * @param url -- the url of the clone resource
	 * @return
	 */
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
	
	/**
	 * Adds a credential name which the remode user must provide
	 * @param credName -- the name of the require credential 
	 */
	public void addCredential(String credName){
		if(credName!=null){
			credentials.add(credName);
		}
		return;
	}
	
	/**
	 * Get a copy of the credentials which the remote
	 * peer must still provide
	 * @return a vector of credential names
	 */
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
