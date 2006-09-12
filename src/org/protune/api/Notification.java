package org.protune.api;

/**
 * A notification that an attempt to carry out an action was performed. The attempt could have
 * different results, each one should be represented as a subclass of
 * {@link org.protune.api.Notification}.<br />
 * <b>OPEN ISSUE:</b> At present just two subclasses are developed:
 * {@link org.protune.api.ActionWellPerformed} and
 * {@link org.protune.api.ActionWrongPerformed}.  
 * @author jldecoi
 */
public abstract class Notification {
	
	protected Action action;
	
	public Action getAction(){
		return action;
	}
	
}