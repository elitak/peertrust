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
			attrs.getNamedItem(RequestServingMechanismPool.ATTRIBUTE_NAME).getTextContent();
		this.forwardTo=
			attrs.getNamedItem(RequestServingMechanismPool.ATTRIBUTE_FORWARD_TO).getTextContent();
	}

	public String getMechanismName() {
		return name;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"\nRequestServingByForwarding:"+
				"\tname:"+name+
				"\tforwardTo:"+forwardTo+"\n";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
