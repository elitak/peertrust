package org.protune.api;

/**
 * Represents a filtered policy.<br />
 * <b>OPEN ISSUE:</b> If I understood well, each filtered policy is a Prolog theory, but not each
 * Prolog theory is a filtered policy, therefore the check in {@link #FilteredPolicy(String)} is not
 * enough.
 * @author jldecoi
 */
public class Goal {

	PrologEngine prologEngine;
	String goal;
	
	Goal(String s) throws LoadTheoryException{
		prologEngine = new TuPrologWrapper();
		prologEngine.loadTheory("dummy(" + s + ").");
		goal = s;
	}
	
	public String toString(){
		return goal;
	}
	
	String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}
