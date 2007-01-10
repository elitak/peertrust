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
/*	
	public Goal toGoal(Mapper m) throws QueryException{
		return new Goal(accept(m));
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
	 *
	public String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
*/
}
