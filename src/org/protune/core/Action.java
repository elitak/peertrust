package org.protune.core;

/**
 * Class <tt>Action</tt> is the superclass of each state predicate. According to
 * deliverable <tt>I2-D2</tt>, this class is extended by two subclasses {@link
 * org.protune.api.ReadAction} (resp. {@link org.protune.api.SideEffectAction}) representing a provisional
 * predicate (resp. a state predicate not provisional).
 * @author jldecoi
 */
public abstract class Action {
	
	public abstract Notification perform();

}
