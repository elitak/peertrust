/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

/**
 * @author pat_dev
 *
 */
public interface PolicySystem {
	/**
	 * return a vector of policies associated to a specified policy name
	 *  
	 * @param policyName -- the name of the policy
	 * @return a vector of policies
	 */
	Vector getPolicies(String policyName );
	
	/**
	 * To setup the Policy system. 
	 * The setup process uses the following xml setip file 
	 * 
	 * @param xmlSetupFilePath -- the xml configuration file path
	 */
	public void setup(String xmlSetupFileName) throws UnsupportedFormatException;
}
