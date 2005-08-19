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
	public PublicResource(String matchingStrategy, String realURL,
			String virtualURL) {
		super(matchingStrategy, realURL, virtualURL);
	}

}
