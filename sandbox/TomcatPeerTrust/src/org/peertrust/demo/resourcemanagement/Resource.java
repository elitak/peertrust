/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

/**
 * Resource represents a resource identified by an url.
 * It contains the mechanism used to to serve requests. 
 * @author Patrice Congo(token77)
 *
 */
abstract public class Resource {
	/**
	 * the resource url
	 */
	protected String url=null;
	/**
	 * a tring represneting the matching strategy to use
	 */
	protected String matchingStrategy=null;
	
	/**
	 * themechanism name to use to serve request for this resource
	 */
	protected String requestServingMechanimName="default";
	
	/**
	 * Inits the resource  matchingStrategy name and  
	 * url of this resource.
	 * 
	 * @param 	matchingStrategy -- the key for the strategy used to match
	 * 			the url 
	 * @param url -- the resource url or url pattern
	 */
	public Resource(String matchingStrategy, String url){//String realURL, String virtualURL) {
		super();
		if(matchingStrategy==null || url==null){
			throw new NullPointerException("Any paramerter must not be null");
		}
		
		
		this.matchingStrategy = matchingStrategy;
		this.url=url;		
	}

	/**
	 * 
	 * @return the matching strategy of this resource
	 */
	public String getMatchingStrategy() {
		return matchingStrategy;
	}

	/**
	 * sets the matchingStrategy of this resource
	 * @param matchingStrategy -- the new matching strategy
	 */
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

	
	
	/**
	 * @return Returns the requestServingMechanimName.
	 */
	public String getRequestServingMechanimName() {
		return requestServingMechanimName;
	}

	/**
	 * @param requestServingMechanimName The requestServingMechanimName to set.
	 */
	public void setRequestServingMechanimName(String requestServingMechanimName) {
		this.requestServingMechanimName = requestServingMechanimName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"url:"+url+
				" matchingStrategy:"+ matchingStrategy+
				" requestServingMechanimName:"+requestServingMechanimName;
		
	}

			
}
