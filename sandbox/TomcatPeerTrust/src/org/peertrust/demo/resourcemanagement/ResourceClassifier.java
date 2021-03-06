/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * The ResourceClassifier specifies the interface a class must
 * implement to provided mechanism to classify resources.
 *  
 * @author Patrice Congo (token77)
 *
 */
public interface ResourceClassifier {
	/**
	 * To get the resource entity associated with the virtual resource url.
	 * 
	 * @param resourceVirtualAbsURL -- the virtual url of the resource to get.
	 * @return the resource object
	 */
	public Resource getResource(String resourceVirtualAbsURL);
	
	/**
	 * To setup the resource classifier using the specified config file.
	 * E.g. loading a resource classification from the an ontology
	 * The config file format must have the folowing format:
	 * <ResourceClassifier type="..">
	 *  ... type depandent
	 * </ResourceClassifier>
	 * 
	 * @param urlOfXMLConfigFile -- url of the config file
	 * @throws IOException -- if any IO exception 
	 * @throws UnsupportedFormatException -- if xml config file type is not supported
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 */
	public void setup(StringWrapper urlOfXmlConfigFile) throws IOException, UnsupportedFormatException, ParserConfigurationException, SAXException;
}
