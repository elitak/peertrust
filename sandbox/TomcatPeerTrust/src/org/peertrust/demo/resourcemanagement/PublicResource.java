
package org.peertrust.demo.resourcemanagement;

/**
 * A PublicResource is a resource which does not require
 * peertrust negotiation.
 * @author Patrice Congo (token77)
 *
 */
public class PublicResource extends Resource {

	/**
	 * Creates a new PunlicResource objects
	 * @param 	matchingStrategy -- the matching strategy to use when matching
	 * 			the url
	 * @param url the url of the resource
	 */
	public PublicResource(String matchingStrategy,String url) {
		super(matchingStrategy, url);
	}

	/**
	 * To clone this resource and customized it with the provided 
	 * url.
	 * @param url -- the url of the clone resource
	 * @return
	 */
	public PublicResource getCopy(String url){
		PublicResource res= new PublicResource(getMatchingStrategy(),url);
		res.setRequestServingMechanimName(this.getRequestServingMechanimName());
		return res;
	}
}
