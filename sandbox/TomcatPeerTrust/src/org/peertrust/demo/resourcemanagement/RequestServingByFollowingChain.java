package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class RequestServingByFollowingChain implements RequestServingMechanism {
	private String name;
	private String matchingPattern;
	
	public void serveRequest(
							HttpServletRequest req, 
							HttpServletResponse resp,
							FilterChain chain, 
							Resource resource,
							ServletContext servletContext) throws IOException, ServletException {
		if(req==null){
			throw new NullPointerException("paramenter req must not be null");
		}
		
		if(resp==null){
			throw new NullPointerException("paramenter resp must not be null");
		}
		
		if(chain==null){
			throw new NullPointerException("paramenter chain must not be null");
		}
		
		chain.doFilter(req,resp);
	}

	public void setup(Node mechanismNode)throws SetupException {
		NamedNodeMap attrs=mechanismNode.getAttributes();			
		this.name=
			attrs.getNamedItem(ATTRIBUTE_NAME).getTextContent();
		
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
		StringBuffer buf= new StringBuffer(128);
		buf.append("RequestServingByFollowingChain: name=");
		buf.append(name);
		buf.append(" matchingPattern=");
		buf.append(matchingPattern);;
		return buf.toString();
	}

}
