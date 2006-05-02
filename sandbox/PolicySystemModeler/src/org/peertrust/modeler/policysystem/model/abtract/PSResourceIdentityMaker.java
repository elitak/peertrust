/**
 * 
 */
package org.peertrust.modeler.policysystem.model.abtract;

/**
 * Interface to implement to provide a mechanism to create 
 * compute resource identity
 * 
 * @author Patrice Congo
 *
 */
public interface PSResourceIdentityMaker 
{
	/**
	 * To get a resource identity
	 * 
	 * @param resource -- the resource
	 * @return the resource identity
	 */
	public String makeIdentity(Object resource);
	
	public String makeLabel(Object resource);
}
