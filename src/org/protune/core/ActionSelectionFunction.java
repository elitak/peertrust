package org.protune.core;

import org.protune.api.Action;

/**
 * <b>NOTE:</b> The signature of the method {@link #selectActions(Action[], Status)} should most
 * likely be changed.<br />
 * After having identified the unlocked actions, a subset is chosen and executed according to the
 * Negotiation Strategy. The class {@link org.protune.core.ActionSelectionFunction} represents the
 * function selecting this subset. Each concrete selection function should extend
 * {@link org.protune.core.ActionSelectionFunction}.
 * @author jldecoi
 */
public abstract class ActionSelectionFunction {

	/**
	 * <b>OPEN ISSUE:</b> Does this method get as parameter an array of Actions or something more
	 * structured (e.g. an AND-OR tree)? Discussion is needed. 
	 * @param aa
	 * @param s
	 */
	abstract Action[] selectActions(Action[] aa, Status s);
	
}
