package org.peertrust.modeler.policysystem.model.abtract;

/**
 * Interface to implement to listenfor policy model 
 * @author Patrice Congo
 *
 */
public interface PSModelChangeEventListener 
{
	/**
	 * Called to notify model change
	 * 
	 * @param event -- event specifying the model change
	 */
	public void onPSModelChange(PSModelChangeEvent event);
}
