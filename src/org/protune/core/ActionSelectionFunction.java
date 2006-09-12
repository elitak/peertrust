package org.protune.core;

import org.protune.api.Action;

/**
 * After having identified the unlocked actions, a subset is chosen and executed according to the
 * Negotiation Strategy. The class {@link org.protune.core.ActionSelectionFunction} represents the
 * function selecting this subset. Each concrete selection function should extend
 * {@link org.protune.core.ActionSelectionFunction}.<br />
 * <b>OPEN ISSUE:</b> The signature of the method {@link #selectActions(Action[], Status)} should most
 * likely be changed.
 * @author jldecoi
 */
public abstract class ActionSelectionFunction {

	/**
	 * <b>OPEN ISSUE:</b> Does this method get as parameter an array of Actions or something more
	 * structured (e.g. an AND-OR tree)? Discussion is needed. 
	 * @param aa
	 * @param s
	 * @return
	 */
	abstract Action[] selectActions(Action[] aa, Status s);
	
}
