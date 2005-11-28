/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * A PolicySystem sytem is a pool of policies.
 * A policy system maybe crustruct from an xml specification
 * In that case the setup() method must be implemented accoding
 * to the chosen xml tag.  
 *  
 * @author Patrice Congo (token77)
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
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public void setup(String xmlSetupFileName) throws UnsupportedFormatException, ParserConfigurationException, SAXException, IOException;
}
