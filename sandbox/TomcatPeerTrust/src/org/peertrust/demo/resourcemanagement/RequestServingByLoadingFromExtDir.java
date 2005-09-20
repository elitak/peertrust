package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;


import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class RequestServingByLoadingFromExtDir implements RequestServingMechanism {

	private String baseDir;
	private String name;
	private String matchingPattern;
	
	public void serveRequest(
					HttpServletRequest req, 
					HttpServletResponse resp,
					FilterChain chain, 
					Resource resource) throws IOException,ServletException {
		req.setAttribute("resource",resource);
		//make path
		String path=baseDir;
		String relURL=resource.getUrl();
		if(relURL.startsWith("/")){
			path=baseDir+relURL;
		}else{
			path=baseDir+"/"+relURL;
		}
		
		System.out.println("=============================LOADING FROM=======================");
		System.out.println("baseDir:"+baseDir+ "\nres:"+path);
		System.out.println("=============================FORWARDING TO END=======================");
		
		
		RequestDispatcher dispatcher = 
			req.getRequestDispatcher(path);	
		
		dispatcher.forward(req, resp);
		
		return;
	}

	public void setup(Node mechanismNode) throws SetupException {
		NamedNodeMap attrs=mechanismNode.getAttributes();			
		try {
			this.name=
				attrs.getNamedItem(ATTRIBUTE_NAME).getTextContent();
		} catch (Exception e) {
			throw new SetupException("Could not  get attribute:"+ATTRIBUTE_NAME,e);
		}
		
		try {
			baseDir=
				attrs.getNamedItem(ATTRIBUTE_BASE_DIR).getTextContent();
			if(baseDir.endsWith("/")){
				baseDir=baseDir.substring(0,baseDir.length()-2);
			}
		} catch (Exception e) {
			throw new SetupException("Could not  get attribute:"+ATTRIBUTE_BASE_DIR,e);
		}
		
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

	
	/**
	 * @return Returns the matchingPattern.
	 */
	public String getMatchingPattern() {
		return matchingPattern;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf= new StringBuffer(128);
		buf.append("RequestServingByLoadingFromExtDir: baseDir=");
		buf.append(baseDir);
		buf.append(" name=");
		buf.append(name);
		buf.append(" matchingPattern=");
		buf.append(matchingPattern);;
		return buf.toString();
	}
}
