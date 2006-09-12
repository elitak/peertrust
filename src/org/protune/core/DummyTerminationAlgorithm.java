package org.protune.core;

import org.protune.api.FilteredPolicy;

/**
 * Dummy implementation of the interface {@link org.protune.core.TerminationAlgorithm}, which
 * terminates if the other Peer sent the same filtered policy as before and no further (reliable)
 * notifications.
 * @author jldecoi
 */
public class DummyTerminationAlgorithm implements TerminationAlgorithm {

	public boolean terminate(Status s) {
		int i = s.getCurrentNegotiationStepNumber();
		if(i<=2) return true;
		else return s.getReliableChecks(i-1).length == 0 &&
		   FilteredPolicy.equal(
			   s.getFilteredPolicies(i),
			   s.getFilteredPolicies(i-2)
		   );
	}

}
