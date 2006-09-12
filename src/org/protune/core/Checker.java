package org.protune.core;

import org.protune.api.*;

/**
 * The interface {@link org.protune.core.Checker} provides the methods for checking whether a
 * notification is (not) reliable, i.e. whether an action, which is supposed to be executed, was
 * actually executed as reported in its notification. Therefore a suitable method should be implemented
 * for each kind of notification available in the Protune system (i.e. for each known subclass of
 * {@link org.protune.api.Notification}).<br />
 * <b>OPEN ISSUE:</b> At present the system contains only two subclasses of {@link
 * org.protune.api.Notification} (namely {@link org.protune.api.ActionWellPerformed} and {@link
 * org.protune.api.ActionWrongPerformed}). Whenever in the future new subclasses are developed, this
 * interface and all classes implementing it will need to be changed. Think at a smart solution
 * avoiding this problem. 
 * @author jldecoi
 */
public interface Checker {

	Check checkNotification(ActionWellPerformed awp) throws UnknownNotificationException;
	
	Check checkNotification(ActionWrongPerformed awp) throws UnknownNotificationException;
	
	/**
	 * <b>OPEN ISSUE:</b> If I remember well the semantics of Java, this method should be invoked only
	 * for subclasses of {@link org.protune.core.Checker} different from the ones explicitly mentioned
	 * in the other methods. If it is so, this one is a facility method for dealing with kinds of
	 * notifications not explicitly considered in the Protune system (yet). 
	 * @param n
	 * @return
	 */
	Check checkNotification(Notification n) throws UnknownNotificationException;
	
}
