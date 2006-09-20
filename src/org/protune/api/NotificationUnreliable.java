package org.protune.api;

/**
 * A confirmation that what is asserted in a {@link org.protune.api.Notification} is not reliable.
 * @author jldecoi
 */
public class NotificationUnreliable extends Check {
	
	NotificationUnreliable(Notification n){
		notification = n;
	}
	
}
