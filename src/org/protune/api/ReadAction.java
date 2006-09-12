package org.protune.api;

/**
 * Class {@link org.protune.api.ReadAction} represents a state (not provisional) predicate. Each state
 * (not provisional) predicate should extend {@link org.protune.api.ReadAction}. Deliverable
 * <tt>I2-D2</tt> lists the following state predicates
 * <ol>
 * <li><tt>request(n, R)</tt></li>
 * <li><tt>in(X, &lt;package_name&gt;: &lt;function&gt;(&lt;arg_list&gt;))</tt></li>
 * <li><tt>time(T)</tt></li>
 * </ol>
 * The intended meaning is as follows: the predicate holds if 
 * <ol>
 * <li><tt>R</tt> is the <tt>n</tt>-th request in the negotiation</li>
 * <li>the variable <tt>X</tt> ranges over the set of objects returned by calling the function
 * <tt>function</tt> of the external package <tt>package_name</tt> with arguments
 * <tt>arg_list</tt></li>
 * <li><tt>T</tt> is the current time of the system</li>
 * </ol>
 * These state predicates read some resource (resp. the state of the negotiation, an external package
 * or a system variable) and provide a result (resp. <tt>true</tt> or <tt>false</tt>, the result of
 * the query and the current time).<br />
 * <b>OPEN ISSUE:</b> So far only {@link org.protune.api.TimeReadAction} was implemented: for the other
 * subclasses of {@link org.protune.api.ReadAction} a discussion is needed.
 * @author jldecoi
 */
public abstract class ReadAction<X> extends Action {
	
	X result;
	
	X getResult(){
		return result;
	}
	
}
