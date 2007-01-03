package org.protune.api;

/**
 * Represents a filtered policy.<br />
 * <b>OPEN ISSUE:</b> If I understood well, each filtered policy is a Prolog theory, but not each
 * Prolog theory is a filtered policy, therefore the check in {@link #FilteredPolicy(String)} is not
 * enough.
 * @author jldecoi
 */
public class FilteredPolicy {

	PrologEngine prologEngine;
	String policy;
	
	FilteredPolicy(String s) throws LoadTheoryException{
		prologEngine = new TuPrologWrapper();
		prologEngine.loadTheory("dummy(" + s + ").");
		policy = s;
	}
	
	FilteredPolicy(String[] sa) throws LoadTheoryException{
		String s = "";
		for(int i=0; i<sa.length; i++) s += sa[i] + "\n";

		prologEngine = new TuPrologWrapper();
		prologEngine.loadTheory("dummy(" + s + ").");
		policy = s;
	}
	
	public String toString(){
		return policy;
	}
	
	String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}
