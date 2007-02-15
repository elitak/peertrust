package org.protune.api;

import java.io.Serializable;

/**
 * A notification that an attempt to carry out an {@link org.protune.api.Action} was performed. The
 * attempt could have different results, each one should be represented as a subclass of
 * {@link org.protune.api.Notification}. At present just two subclasses are developed:
 * {@link org.protune.api.ActionWellPerformed} and
 * {@link org.protune.api.ActionWrongPerformed}.  
 * @author jldecoi
 */
public abstract class Notification implements Serializable {
	
	protected Action action;
	
	public Action getAction(){
		return action;
	}
	
	String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}