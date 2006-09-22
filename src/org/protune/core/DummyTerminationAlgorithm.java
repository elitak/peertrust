package org.protune.core;

import org.protune.api.PrologEngine;

/**
 * Dummy implementation of the interface {@link org.protune.core.TerminationAlgorithm}, which
 * terminates if the other Peer sent the same filtered policy as before and no further (reliable)
 * notifications.
 * @author jldecoi
 */
public class DummyTerminationAlgorithm extends TerminationAlgorithm {
	
	DummyTerminationAlgorithm(PrologEngine pe){
		prologEngine = pe;
	}

}
