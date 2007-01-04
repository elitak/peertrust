package org.protune.api;

/**
 * A notification that an attempt to carry out an {@link org.protune.api.Action} was performed in the
 * right way.
 * @author jldecoi
 */
public class ActionWellPerformed extends Notification {
	
	public ActionWellPerformed(Action a){
		action = a;
	}

}
