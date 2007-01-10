package org.protune.core;

import java.io.Serializable;

import org.protune.api.Mapper;

/**
 * A notification that an attempt to carry out an {@link org.protune.core.Action} was performed. The
 * attempt could have different results, each one should be represented as a subclass of
 * {@link org.protune.core.Notification}. At present just two subclasses are developed:
 * {@link org.protune.core.ActionWellPerformed} and
 * {@link org.protune.core.ActionWrongPerformed}.  
 * @author jldecoi
 */
public abstract class Notification implements Serializable{
	
	protected Action action;
	
	public Action getAction(){
		return action;
	}
	
	public String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}