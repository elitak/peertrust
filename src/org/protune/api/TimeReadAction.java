package org.protune.api;

import java.util.Date;

/**
 * Class {@link org.protune.api.TimeReadAction} represents the state (not provisional) predicate
 * <tt>time(T)</tt> which holds if <tt>T</tt> is the current time of the system (cf. deliverable
 * <tt>I2-D2</tt>).
 * @author jldecoi
 */
public class TimeReadAction extends ReadAction<Date> {

	public Notification perform() {
		result = new Date();
		return new ActionWellPerformed(this);
	}
	
	/*
	 * According to deliverable <tt>I2-D2</tt> the Prolog representation of this action is
	 * <blockquote><tt>time(T)</tt></blockquote>
	 * <b>OPEN ISSUE:</b> Should it maybe have been <tt>do(time(T))</tt>?
	 *
	String toGoal(){
		// return "time(T)";
		return "allow(access(books))";
	}*/

}
