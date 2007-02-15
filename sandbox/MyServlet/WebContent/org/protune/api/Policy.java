package org.protune.api;

/**
 * Represents a policy.<br />
 * <b>OPEN ISSUE:</b> If I understood well, each policy is a Prolog theory, but not each Prolog theory
 * is a policy, therefore the check in {@link #Policy(String)} is not enough.
 * @author jldecoi
 */
public class Policy {

	PrologEngine prologEngine;
	String policy;
	
	Policy(String s) throws LoadTheoryException{
//		prologEngine = new TuPrologWrapper();
//		prologEngine.loadTheory(s);
		policy = s;
	}
	
	/*
	 * Returns a string representation of the policy as a Prolog theory.
	 * @return A string representation of the policy as a Prolog theory.
	 *
	String toTheory(){
		return policy;
	}*/
	
}
