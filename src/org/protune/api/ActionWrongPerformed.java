package org.protune.api;

/**
 * A notification that an attempt to carry out an {@link org.protune.api.Action} was performed in the
 * wrong way.
 * @author jldecoi
 */
public class ActionWrongPerformed extends Notification {
	
	ActionWrongPerformed(Action a){
		action = a;
	}

}
