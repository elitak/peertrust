package org.protune.api;

/**
 * A confirmation that what is asserted in a {@link org.protune.api.Notification} is reliable.
 * @author jldecoi
 */
public class NotificationReliable extends Check {
	
	public NotificationReliable(Notification n){
		notification = n;
	}
	
}
