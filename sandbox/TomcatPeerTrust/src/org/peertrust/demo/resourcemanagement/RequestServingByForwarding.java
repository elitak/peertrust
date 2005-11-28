package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * RequestServingByForwarding is a request serving mechanism that
 * forward the requests to another wed application url
 * 
 * @author Patrice Congo (token77)
 *
 */
public class RequestServingByForwarding 
					implements RequestServingMechanism
{
	/**
	 * The name of the mechanism.
	 */
	private String name;
	
	/**
	 * the url to forward the request to.
	 */
	private String forwardTo;
	
	/**
	 * The regular expression used to match the request url 
	 */
	private String matchingPattern;
	
	/**
	 * the web application context path
	 */
	private String context;
	
	/**
	 * Serves the passed request. The request is forwarded
	 * to another url.
	 * @param req -- the request object
	 * @param resp -- to req the associated response object
	 * @param chain -- the filter chain for the request
	 * @param servletContext -- the context of the serving servlet
	 * @see RequestServingMechanism#serveRequest(HttpServletRequest, HttpServletResponse, FilterChain, Resource, ServletContext)  
	 */
	public void serveRequest(	HttpServletRequest req, 
								HttpServletResponse resp, 
								FilterChain chain, 
								Resource resource,
								ServletContext servletContext) 
								throws IOException, ServletException 
	{
		req.setAttribute("resource",resource);
		System.out.println("=============================FORWARDING TO=======================");
		System.out.println("destination:"+forwardTo+ "\nres:"+req.getAttribute("resource"));
		System.out.println("=============================FORWARDING TO END=======================");
		if(context==null){
			RequestDispatcher dispatcher = 
				req.getRequestDispatcher(forwardTo);		
			dispatcher.forward(req, resp);
		}else{
			ServletContext targetContext= 
				servletContext.getContext(context);
			RequestDispatcher dispatcher=
				targetContext.getRequestDispatcher(forwardTo);
			dispatcher.forward(req,resp);
		}
		return;		
	}
	
	/**
	 * Build the mechanism from the passed xml document node
	 * @param mechanismNode -- an xml node representing mechanism  
	 */
	public void setup(Node mechanismNode) throws SetupException{
		NamedNodeMap attrs=mechanismNode.getAttributes();			
		this.name=
			attrs.getNamedItem(ATTRIBUTE_NAME).getTextContent();
		this.forwardTo=
			attrs.getNamedItem(ATTRIBUTE_FORWARD_TO).getTextContent();
		Node att=attrs.getNamedItem(ATTRIBUTE_CONTEXT);
		if(att!=null){
			this.context=att.getTextContent();
		}
		try {
			matchingPattern=
				attrs.getNamedItem(ATTRIBUTE_MATCHING_PATTERN).getTextContent();
			
		} catch (Exception e) {
			throw new SetupException("Could not  get attribute:"+ATTRIBUTE_MATCHING_PATTERN,e);
		}
	}

	/**
	 * @return returns the mechanism name
	 */
	public String getMechanismName() {
		return name;
	}
	
	
	
	/**
	 * @see org.peertrust.demo.resourcemanagement.RequestServingMechanism#getMatchingPattern()
	 */
	public String getMatchingPattern() {
		return matchingPattern;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"\nRequestServingByForwarding:"+
				"\tname:"+name+
				"\tcontext:"+context+
				"\tforwardTo:"+forwardTo+"\n";
	}


}
