package org.protune.api;

import java.io.Serializable;

/**
 * Represents a goal.
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
