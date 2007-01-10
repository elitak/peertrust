package org.protune.core;

import org.protune.api.*;

/**
 * The interface <tt>Checker</tt> provides methods for checking whether a notification 
 * is (not) reliable, i.e. whether an action, which is supposed to be executed, was actually executed as
 * reported in its notification. Therefore a suitable method should be implemented for each kind of
 * notification available in the <i>Protune</i> system (i.e. for each known subclass of
 * {@link org.protune.core.Notification}).<br />
 * <b>NOTE:</b> The interface <tt>Checker</tt> should be aware of each subclass of <tt>Notification</tt>
 * available in the system, therefore whenever a new subclass is added, the interface <tt>Checker</tt> (and
 * all classes implementing it) should be modified.
 * @author jldecoi
 */
public interface Checker {

	public Check checkNotification(ActionWellPerformed awp) throws UnknownNotificationException;
	
	public Check checkNotification(ActionWrongPerformed awp) throws UnknownNotificationException;
	
	/**
	 * <b>OPEN ISSUE:</b> If I remember well the semantics of Java, this method should be invoked only
	 * for subclasses of {@link org.protune.core.Checker} different from the ones explicitly mentioned
	 * in the other methods. If it is so, this one is a facility method for dealing with kinds of
	 * notifications not explicitly considered in the Protune system (yet). 
	 * @param n
	 */
	public Check checkNotification(Notification n) throws UnknownNotificationException;
	
}
