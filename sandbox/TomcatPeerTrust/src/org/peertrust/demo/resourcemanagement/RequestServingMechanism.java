package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Node;

/**
 * Classes which implements this interface provides a mechanism to generate the appropriate
 * response to a request. such a mechnism is e.g. necessary when filter chain is not directly follow 
 * but the request is forwarded to a serving jsp.
 *   
 * @author pat_dev
 *
 */
public interface RequestServingMechanism {
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
						Resource resource) throws IOException, ServletException;
	
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
}
