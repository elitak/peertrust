package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;

/**
 * Classes which implements this interface provides a mechanism to generate the appropriate
 * response to a request. such a mechnism is e.g. necessary when filter chain is not directly follow 
 * but the request is forwarded to a serving jsp.
 * <code>
 * 	<mechanism 	name="_mechanism_name_"
				class="_mechanism_class_"
				forwardTo="_url_to_forward_the_request_to"
				context="_web application context path of the forwardto url" 
				matchingPattern="pattern used to match the url">
	</mechanism>
 * </code>
 *   
 * @author Patrice Congo (token77)
 *
 */
public interface RequestServingMechanism 
{
	/**
	 * the name of the <mechanism> attribute that holds the 
	 * the mechanism name
	 */
	static final public String ATTRIBUTE_NAME="name";
	
	
	//static final public String ATTRIBUTE_BASE_DIR="baseDir";
	
	/**
	 * the name of the <mechanism> attribute that holds the 
	 * the mechanism matching pattern
	 */
	static final public String ATTRIBUTE_MATCHING_PATTERN="matchingPattern";
	
	/**
	 * The tag name od the xml element representing the mechnism pool
	 */
	final static public String ROOT_TAG_SERVING_MECHANISM="RequestServingMechanism";
	
	/**
	 * the name of the <mechanism> tag
	 */
	final static public String MECHANISM_TAG="mechanism";
	
	/**
	 * the name of the <mechanism> attribute that holds the 
	 * the mechanism class
	 */
	final static public String ATTRIBUTE_CLASS="class";
	
	/**
	 * the name of the <mechanism> attribute that holds the 
	 * the mechanism url to forward the request to.
	 */
	final static public String ATTRIBUTE_FORWARD_TO="forwardTo";
	
	/**
	 * the name of the dafault <mechanism> 
	 */
	final static public String DEFAULT_NAME="default";
	
	/**
	 * the name of the <mechanism> attribute that holds the 
	 * the mechanism web application context path of the forward to url
	 */
	final static public String ATTRIBUTE_CONTEXT="context";
	
	/**
	 * To respond to a request. the overall resource management framework is 
	 * base a gate TrustFilter. Therefore the corresponding chain must be pass as an argument. 
	 * @param req -- httprequest to serve
	 * @param resp -- the HttpResponse associated with the request.
	 * @param chain -- the reference to FilterChain  
	 * @param resource -- the request reqource
	 * @throws ServletException 
	 * @throws IOException 
	 */
	public void serveRequest(
						HttpServletRequest req, 
						HttpServletResponse resp,
						FilterChain chain, 
						Resource resource,
						ServletContext servletContext) throws IOException, ServletException;
	
	/**
	 * To setup the object internals according to an XML representation.
	 * @param mechanismNode -- the node reqpresenting the mechanism xml representation
	 * @throws SetupException 
	 */
	public void setup(Node mechanismNode) throws SetupException;
	
	/**
	 * Returns the mechanism name.
	 * @return the mechanism name
	 */
	public String getMechanismName();
	
	/**
	 * Returns a matching pattern used to match the url.
	 * @return return the matching pattern used to match the url.
	 */
	public String getMatchingPattern();
}
