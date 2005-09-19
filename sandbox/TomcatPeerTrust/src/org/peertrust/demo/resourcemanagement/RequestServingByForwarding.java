package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class RequestServingByForwarding implements RequestServingMechanism{
	private String name;
	private String forwardTo;
	private String matchingPattern;
	
	public void serveRequest(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, Resource resource) throws IOException, ServletException {
		req.setAttribute("resource",resource);
		System.out.println("=============================FORWARDING TO=======================");
		System.out.println("destination:"+forwardTo+ "\nres:"+req.getAttribute("resource"));
		System.out.println("=============================FORWARDING TO END=======================");
		RequestDispatcher dispatcher = 
			req.getRequestDispatcher(forwardTo);		
		dispatcher.forward(req, resp);
		return;		
	}

	public void setup(Node mechanismNode) throws SetupException{
		NamedNodeMap attrs=mechanismNode.getAttributes();			
		this.name=
			attrs.getNamedItem(ATTRIBUTE_NAME).getTextContent();
		this.forwardTo=
			attrs.getNamedItem(ATTRIBUTE_FORWARD_TO).getTextContent();
		try {
			matchingPattern=
				attrs.getNamedItem(ATTRIBUTE_MATCHING_PATTERN).getTextContent();
			
		} catch (Exception e) {
			throw new SetupException("Could not  get attribute:"+ATTRIBUTE_MATCHING_PATTERN,e);
		}
	}

	public String getMechanismName() {
		return name;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.resourcemanagement.RequestServingMechanism#getMatchingPattern()
	 */
	public String getMatchingPattern() {
		return matchingPattern;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"\nRequestServingByForwarding:"+
				"\tname:"+name+
				"\tforwardTo:"+forwardTo+"\n";
	}


}
