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
	//protected String virtualURL=null;
	//protected String realURL=null;
	protected String url=null;
	protected String matchingStrategy=null;
	
	public Resource(String matchingStrategy, String url){//String realURL, String virtualURL) {
		super();
		if(matchingStrategy==null || url==null){
			throw new NullPointerException("Any paramerter must not be null");
		}
		
		
		this.matchingStrategy = matchingStrategy;
		this.url=url;
//		this.realURL = realURL;
//		this.virtualURL = virtualURL;
		
	}

	public String getMatchingStrategy() {
		return matchingStrategy;
	}

	public void setMatchingStrategy(String matchingStrategy) {
		this.matchingStrategy = matchingStrategy;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"url:"+url+
				" matchingStrategy:"+ matchingStrategy;
		
	}

			
}
