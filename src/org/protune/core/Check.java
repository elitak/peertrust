package org.protune.core;

/**
 * A check whether an {@link org.protune.core.Action} was really performed. The checking operation could
 * have different results, each one should be represented as a subclass of <tt>Check</tt>. At present
 * just two subclasses are developed:
 * {@link org.protune.core.NotificationReliable} and {@link org.protune.core.NotificationUnreliable}.  
 * @author jldecoi
 */
public abstract class Check {
	
	protected Notification notification;
	
	public Notification getNotification(){
		return notification;
	}

}
