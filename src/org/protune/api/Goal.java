package org.protune.api;

import java.io.Serializable;

/**
 * Represents a filtered policy.<br />
 * <b>OPEN ISSUE:</b> If I understood well, each filtered policy is a Prolog theory, but not each
 * Prolog theory is a filtered policy, therefore the check in {@link #FilteredPolicy(String)} is not
 * enough.
 * @author jldecoi
 */
public class Goal implements Serializable{

	PrologEngine prologEngine;
	String goal;
	
	public Goal(String s) throws LoadTheoryException{
		TuPrologWrapper tpw = new TuPrologWrapper();
		tpw.loadTheory("dummy(" + s + ").");
		goal = s;
	}
	
	public String toString(){
		return goal;
	}
	
	String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}
