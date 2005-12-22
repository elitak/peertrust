package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * This class specifies a resource request. It basicaly hold
 * the object related to and necessary to serve the request.
 * 
 * @author Patrice Congo (token77)
 */
public class ResourceRequestSpec {
	/**
	 * Represents the servlet request
	 */
	private ServletRequest servletRequest;
	
	/**
	 * The servlet response to respond to the resource request
	 */
	private ServletResponse servletResponse;
	
	/**
	 * The filter chain which represent the handling pipeline
	 * of the request.
	 */
	private FilterChain filterChain;
	
	/**
	 * Represents the requested resource 
	 */
	private Resource resource;
	
	/**
	 * The name of the requesting peer
	 */
	private String peerName;
	
	/**
	 * conatains the policies which protect the resource
	 */
	private Vector attachedPolicies;
	
	
	/**
	 * the servlet context of the gate keeping filter or servlet 
	 */
	ServletContext servletContext;
	
	/**
	 * Create a Resource Request specification from the the web container 
	 * request objects.
	 * 
	 * @param servletRequest
	 * @param servletResponse
	 * @param filterChain
	 */
	public ResourceRequestSpec(
							String peerName,
							ServletRequest servletRequest,
							ServletResponse servletResponse,
							FilterChain filterChain,
							ServletContext servletContext)
	{
		this.servletContext=servletContext;
		this.peerName=peerName;
		this.servletRequest=servletRequest;
		this.servletResponse=servletResponse;
		this.filterChain=filterChain;
	}

	/**
	 * @return Returns the filterChain.
	 */
	public FilterChain getFilterChain() {
		return filterChain;
	}

	/**
	 * @param filterChain The filterChain to set.
	 */
	public void setFilterChain(FilterChain filterChain) {
		this.filterChain = filterChain;
	}

	/**
	 * @return Returns the resource.
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * @param resource The resource to set.
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * @return Returns the servletRequest.
	 */
	public ServletRequest getServletRequest() {
		return servletRequest;
	}

	/**
	 * @param servletRequest The servletRequest to set.
	 */
	public void setServletRequest(ServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	

	/**
	 * @return Returns the servletResponse.
	 */
	public ServletResponse getServletResponse() {
		return servletResponse;
	}

	/**
	 * @param servletResponse The servletResponse to set.
	 */
	public void setServletResponse(ServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	/**
	 * @return Returns the peerName.
	 */
	public String getPeerName() {
		return peerName;
	}

	/**
	 * @param peerName The peerName to set.
	 */
	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	/**
	 * @return Returns the attachedPolicies.
	 */
	public Vector getAttachedPolicies() {
		return attachedPolicies;
	}

	/**
	 * @param attachedPolicies The attachedPolicies to set.
	 */
	public void setAttachedPolicies(Vector attachedPolicies) {
		this.attachedPolicies = attachedPolicies;
	}

	/**
	 * @return Returns the servletContext.
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext The servletContext to set.
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	
	
}
