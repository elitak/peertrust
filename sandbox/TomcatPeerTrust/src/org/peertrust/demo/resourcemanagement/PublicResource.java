/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

/**
 * @author pat_dev
 *
 */
public class PublicResource extends Resource {

	/**
	 * @param matchingStrategy
	 * @param realURL
	 * @param virtualURL
	 */
	public PublicResource(String matchingStrategy,String url) {
		super(matchingStrategy, url);
	}

	public PublicResource getCopy(String url){
		PublicResource res= new PublicResource(getMatchingStrategy(),url);
		res.setRequestServingMechanimName(this.getRequestServingMechanimName());
		return res;
	}
}
