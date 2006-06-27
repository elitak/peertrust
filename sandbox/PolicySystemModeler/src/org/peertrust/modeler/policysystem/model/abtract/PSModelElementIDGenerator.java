package org.peertrust.modeler.policysystem.model.abtract;

/**
 * Interface to be implemented by a model object id generator
 * @author Patrice Congo
 */
public interface PSModelElementIDGenerator 
{
	/**
	 * Generates the ID 
	 * @param type -- the type of the model element e.g. <code>PSPolicy</code>
	 * @param label -- the label
	 * @return
	 */
	public String generateID(Class type, String label);
}
