package org.protune.core;

import org.protune.api.Action;
import org.protune.api.FilteredPolicy;
import org.protune.api.Policy;

/**
 * The objects of the classes implementing the interface <tt>FilterEngine</tt> carry out every activity
 * related to the filtering process of a policy.
 * @author jldecoi
 */
public interface FilterEngine {

	/**
	 * Checks whether the overall goal of the negotiation is satisfied (i.e. whether it can be proved
	 * according to the current state of the peer).
	 * @param status The current state of the peer.
	 * @return <tt>true</tt> if the overall goal of the negotiation is satisfied (i.e. if it can be
	 * proved according to the current state of the peer), <tt>false</tt> otherwise.
	 */
	public boolean isNegotiationSatisfied(Status status);
	
	/**
	 * Extracts the actions contained in the filtered policy sent by the other peer.
	 * @param fp The filtered policy sent by the other peer.
	 * @return The set (array) of actions contained in the filtered policy sent by the other peer.
	 */
	public Action[] extractActions(FilteredPolicy fp);
	
	/**
	 * Returns the filtered policy protecting the action, whose execution was asked for by the other
	 * peer. The filtering is performed according to the policy available on the peer and according to
	 * its current state.
	 * @param policy The policy available on the peer.
	 * @param status The current state of the peer.
	 * @param request An action, whose execution was asked for by the other peer.
	 * @return The filtered policy protecting the action, whose execution was asked for by the other
	 * peer. The filtered policy is computed according to the current state of the peer and according
	 * to its local policy.
	 * @throws Exception
	 */
	public FilteredPolicy filter(Policy policy, Status status, Action request) throws Exception;
	
	/**
	 * Checks whether the policy protecting an action is satisfied (i.e. whether the action is
	 * <i>unlocked</i>).
	 * @param action The action.
	 * @return <tt>true</tt> if the policy protecting the action is satisfied (i.e. if the action is
	 * <i>unlocked</i>), <tt>false</tt> otherwise (i.e. if the action is <i>locked</i>).
	 * @throws Exception
	 */
	public boolean isUnlocked(Action action) throws Exception;
	
}
