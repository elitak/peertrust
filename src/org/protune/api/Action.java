package org.protune.api;

/**
 * Class {@link org.protune.api.Action} is the superclass of each state predicate. According to
 * deliverable <tt>I2-D2</tt>, this class is extended by two subclasses {@link
 * org.protune.api.ReadAction} (resp. {@link org.protune.api.SideEffectAction}) representing a provisional
 * predicate (resp. a state predicate not provisional).
 * @author jldecoi
 */
public abstract class Action {
	
	abstract Notification perform();
	
	public static Notification[] perform(Action[] aa){
		Notification[] na = new Notification[aa.length];
		for(int i=0; i<na.length; i++) na[i] = aa[i].perform();
		return na;
	}
	
	/**
	 * Returns a string representation of the policy as a Prolog goal.<br />
	 * <b>NOTE:</b> It is not allowed to terminate with a period (<tt>.</tt>). E.g. it can be
	 * <blockquote><tt>release(Credential)</tt></blockquote>
	 * but not
	 * <blockquote><tt>release(Credential).</tt></blockquote>
	 * @return Returns a string representation of the policy as a Prolog goal.<br />
	 * <b>NOTE:</b> It is not allowed to terminate with a period (<tt>.</tt>). E.g. it can be
	 * <blockquote><tt>release(Credential)</tt></blockquote>
	 * but not
	 * <blockquote><tt>release(Credential).</tt></blockquote>
	 */
	abstract String toGoal();

}
