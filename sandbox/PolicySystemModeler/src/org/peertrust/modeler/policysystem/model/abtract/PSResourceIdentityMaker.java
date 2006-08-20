package org.peertrust.modeler.policysystem.model.abtract;

import java.net.URI;

/**
 * Interface to implement to provide a mechanism to create 
 * compute resource identity. The implemented class then provides
 * concrete mechanisms 
 * <ul>
 * 	<li>to get a resource uri abolute and relative to root, 
 * 	<li>to get the label of a resource, 
 * 	<li>to get a parent of a resource,
 * 	<li>whether a resource can have a child resource
 * 	<li>whether a resource is a root
 * </ul>
 * 
 * @author Patrice Congo
 *
 */
public interface PSResourceIdentityMaker 
{
	//TODO what with root; return absolute uri
	/**
	 * To get a resource identity, its relative uri within
	 * a root hierarchy
	 * 
	 * @param resource -- the resource
	 * @return the resource identity
	 */
	public URI relativeURI(Object resource);
	
	
	/**
	 * To get the absolute uri of an object
	 * @param resource -- the resource representation
	 * @return an absolute uri representing the uri
	 */
	public URI getAbsoluteURI(Object resource);
	
	/**
	 * To get the label of a resource
	 * @param resource -- an object representing the resource
	 * @return the label of the resource as string
	 */
	public String getLabel(Object resource);
	
	/**
	 * Checks whether a resource can have a child
	 * @param resource -- the potential parent resource
	 * @return true if the resource can have a child otherwise false
	 */
	public boolean canHaveChild(Object resource);
	
	/**
	 * checks whether the resource is a root
	 * 
	 * @param resource -- the resource to test
	 * @return trie if the resource is a root otherwise false
	 */
	public boolean isRoot(Object resource);
	
	
	
	/**
	 * To get the parent of a given resource
	 * @param resource -- the child resource
	 * @return the parent resource or null if the resource is a root
	 */
	public Object getParent(Object resource);
}
