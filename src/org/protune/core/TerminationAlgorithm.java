package org.protune.core;

import org.protune.api.Mapper;
import org.policy.filtering.*;

/**
 * At each negotiation step the current peer should decide whether going on or stopping the negotiation,
 * based on the current value of the negotiation state. This decision is devolved to a distinct
 * component, namely a temination algorithm. A termination algorithm is implemented by (an object of)
 * the class <tt>TerminationAlgorithm</tt>. Each different termination algorithm should be implemented
 * by a subclass of <tt>TerminationAlgorithm</tt>.<br />
 * <b>NOTE:</b> The actual algorithm is meant to be implemented Prolog-side, therefore neither this
 * abstract class nor any of its subclasses need to specify Java-side the computational steps the
 * algorithm consists of. The {@link org.protune.api.Mapper} is responsible for translating (each
 * subclass of) <tt>TerminationAlgorithm</tt> to a suitable Prolog goal, whose proof will provide
 * the answer to the question whether going on or stopping the negotiation.
 * @author jldecoi
 */
public abstract class TerminationAlgorithm {

	JLogPrologApi jlpa;
	
	boolean terminate(Mapper m, Status s){
		try{
			return jlpa.execute(accept(m));
		}
		catch(PrologEngineException pee){
			return false;
		}
	}
	
	String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}
