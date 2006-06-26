/**
 * 
 */
package org.peertrust.modeler.policysystem.model.abtract;

import java.net.URI;

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
	public URI makeIdentity(Object resource);
	
	public String makeLabel(Object resource);
	
	public boolean canHaveChild(Object resource);
	
	public boolean isRoot(Object resource);
	
	public URI toURI(Object resource);
	
	public Object getParent(Object resource);
}
