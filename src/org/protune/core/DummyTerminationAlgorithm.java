package org.protune.core;

import org.policy.filtering.JLogPrologApi;

/**
 * Dummy implementation of the interface {@link org.protune.core.TerminationAlgorithm}, which
 * terminates if the other Peer sent the same filtered policy as before and no further (reliable)
 * notifications.
 * @author jldecoi
 */
public class DummyTerminationAlgorithm extends TerminationAlgorithm {
	
	DummyTerminationAlgorithm(JLogPrologApi j){
		jlpa = j;
	}

}
