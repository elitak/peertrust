package org.protune.core;

import java.io.Serializable;

import org.protune.api.LoadTheoryException;
import org.protune.api.Mapper;
import org.protune.api.PrologEngine;
import org.protune.api.TuPrologWrapper;

/**
 * Represents a goal.
 * @author jldecoi
 */
public class Goal implements Serializable{

	static final long serialVersionUID = 2;
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
	
	public String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}
