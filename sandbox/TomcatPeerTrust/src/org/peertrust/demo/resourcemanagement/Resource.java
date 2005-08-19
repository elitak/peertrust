/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

/**
 * @author pat_dev
 *
 */
abstract public class Resource {
	//final static public String PROTECTION_
	protected String virtualURL=null;
	protected String realURL=null;
	protected String matchingStrategy=null;
	
	public Resource(String matchingStrategy, String realURL, String virtualURL) {
		super();
		if(matchingStrategy==null || realURL==null || virtualURL==null){
			throw new NullPointerException("Any paramerter must not be null");
		}
		
		
		this.matchingStrategy = matchingStrategy;
		this.realURL = realURL;
		this.virtualURL = virtualURL;
		
	}

	public String getMatchingStrategy() {
		return matchingStrategy;
	}

	public void setMatchingStrategy(String matchingStrategy) {
		this.matchingStrategy = matchingStrategy;
	}

	public String getRealURL() {
		return realURL;
	}

	public void setRealURL(String realURL) {
		this.realURL = realURL;
	}

	public String getVirtualURL() {
		return virtualURL;
	}

	public void setVirtualURL(String virtualURL) {
		this.virtualURL = virtualURL;
	}		
}
