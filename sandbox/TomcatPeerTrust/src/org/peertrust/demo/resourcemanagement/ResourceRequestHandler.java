package org.peertrust.demo.resourcemanagement;

/**
 * Classes which implement this interface can participate in the
 * chain of responsability used to serve a resource request.
 * 
 * @author congo
 *
 */
public interface ResourceRequestHandler {
	
	/**
	 *Implements to handle a resoure request 
	 * @param requestSpec
	 */
	public void handle(Object requestSpec) throws ResourceManagementException;
	
	/**
	 * Set next handler in the chain
	 * @param nextHandler
	 */
	public void setNextHandle(ResourceRequestHandler nextHandler);
}
