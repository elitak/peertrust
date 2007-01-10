package org.protune.core;

import java.io.Serializable;

import org.protune.api.LoadTheoryException;
import org.protune.api.Mapper;
import org.protune.api.TuPrologWrapper;

/**
 * Represents a filtered policy.<br />
 * <b>OPEN ISSUE:</b> If I understood well, each filtered policy is a Prolog theory, but not each
 * Prolog theory is a filtered policy, therefore the check in {@link #FilteredPolicy(String)} is not
 * enough.
 * @author jldecoi
 */
public class FilteredPolicy implements Serializable{

	static final long serialVersionUID = 3;
	private String policy;
	
	public FilteredPolicy(String s) throws LoadTheoryException{
		TuPrologWrapper tpw = new TuPrologWrapper();
		tpw.loadTheory("dummy(" + s + ").");
		policy = s;
	}
	
	public String toString(){
		return policy;
	}
	
	public String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}
