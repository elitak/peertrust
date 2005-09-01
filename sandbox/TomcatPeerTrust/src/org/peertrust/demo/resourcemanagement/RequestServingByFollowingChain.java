package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class RequestServingByFollowingChain implements RequestServingMechanism {
	private String name;
	
	public void serveRequest(HttpServletRequest req, HttpServletResponse resp,
			FilterChain chain, Resource resource) throws IOException, ServletException {
		chain.doFilter(req,resp);
	}

	public void setup(Node mechanismNode)throws SetupException {
		NamedNodeMap attrs=mechanismNode.getAttributes();			
		this.name=
			attrs.getNamedItem(RequestServingMechanismPool.ATTRIBUTE_NAME).getTextContent();
	}

	public String getMechanismName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"\nRequestServingByFollowingChain:"+
				" name:"+name+"\n";
	}

}
