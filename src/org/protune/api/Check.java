package org.protune.api;

/**
 * A check whether an action was really performed. The checking operation could have different
 * results, each one should be represented as a subclass of {@link org.protune.api.Check}.<br />
 * <b>OPEN ISSUE:</b> At present just two subclasses are developed:
 * {@link org.protune.api.NotificationReliable} and {@link org.protune.api.NotificationUnreliable}.  
 * @author jldecoi
 */
public abstract class Check {
	
	protected Notification notification;
	
}
