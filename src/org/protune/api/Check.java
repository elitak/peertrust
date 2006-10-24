package org.protune.api;

/**
 * A check whether an {@link org.protune.api.Action} was really performed. The checking operation could
 * have different results, each one should be represented as a subclass of <tt>Check</tt>. At present
 * just two subclasses are developed:
 * {@link org.protune.api.NotificationReliable} and {@link org.protune.api.NotificationUnreliable}.  
 * @author jldecoi
 */
public abstract class Check {
	
	protected Notification notification;
	
	String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}
