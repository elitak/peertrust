package org.protune.core;

/**
 * A confirmation that what is asserted in a {@link org.protune.core.Notification} is not reliable.
 * @author jldecoi
 */
public class NotificationUnreliable extends Check {
	
	public NotificationUnreliable(Notification n){
		notification = n;
	}
	
}
