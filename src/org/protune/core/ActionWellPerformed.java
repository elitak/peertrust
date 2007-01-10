package org.protune.core;

/**
 * A notification that an attempt to carry out an {@link org.protune.core.Action} was performed in the
 * right way.
 * @author jldecoi
 */
public class ActionWellPerformed extends Notification {
	
	static final long serialVersionUID = 61;
	
	public ActionWellPerformed(Action a){
		action = a;
	}

}
