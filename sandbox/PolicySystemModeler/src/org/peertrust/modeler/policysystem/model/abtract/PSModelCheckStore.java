package org.peertrust.modeler.policysystem.model.abtract;

/**
 * Represents a store of model check.
 * Model checks can be register und retrieve with a string key
 * 
 * @author Patrice Congo
 */
public interface PSModelCheckStore 
{

	/**
	 * To get a model check object specified by this provied key 
	 * @param key -- the key for the model check to get
	 * @return a model check associated with the passed key
	 * 			if the key is null null is returned
	 */
	public PSModelCheck getModelCheck(String key);
	
	/**
	 * Registers a model check into the store
	 * @param key -- the key for the new model check
	 * @param newModelCheck -- the new model check to add to the store
	 * 			this will replace the already registered model check
	 */
	public void registerModelCheck(String key, PSModelCheck newModelCheck);

}
