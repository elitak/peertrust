/**
 * 
 */
package org.peertrust.modeler.policysystem.model.abtract;

/**
 * @author pat_dev
 *
 */
public interface PSModelElementIDGenerator 
{
	public String generateID(Class type, String label);
}
