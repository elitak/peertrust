package org.protune.core;

/**
 * A notification that an attempt to carry out an {@link org.protune.core.Action} was performed in the
 * wrong way.
 * @author jldecoi
 */
public class ActionWrongPerformed extends Notification {
	
	static final long serialVersionUID = 62;
	
	public ActionWrongPerformed(Action a){
		action = a;
	}

}
