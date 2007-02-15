package org.protune.api;

import java.io.Serializable;

/**
 * Represents a filtered policy.<br />
 * <b>OPEN ISSUE:</b> If I understood well, each filtered policy is a Prolog theory, but not each
 * Prolog theory is a filtered policy, therefore the check in {@link #FilteredPolicy(String)} is not
 * enough.
 * @author jldecoi
 */
public class FilteredPolicy implements Serializable {

	private static final long serialVersionUID = -8236195035865369180L;
	PrologEngine prologEngine;
	String policy;
	
	public FilteredPolicy(String s) throws LoadTheoryException{
//		prologEngine = new TuPrologWrapper();
//		prologEngine.loadTheory(s);
		policy = s;
	}
	
	public FilteredPolicy(String[] sa) throws LoadTheoryException{
		String s = "";
		for(int i=0; i<sa.length; i++) s += sa[i] + "\n";

//		prologEngine = new TuPrologWrapper();
//		prologEngine.loadTheory(s);
		policy = s;
	}
	
	/*
	 * Concatenates <tt>n</tt> filtered policies.
	 * @param fp
	 * @return
	 * @throws PrologEngineException Never (if I am not wrong the union of correct theory should be a
	 * correct theory).
	 *
	static FilteredPolicy concat(FilteredPolicy[] fp){
		try{
			String s = "";
			for(int i=0; i<fp.length; i++) s += fp[i].toString();
			return new FilteredPolicy(s);
		}
		catch(PrologEngineException pee){
			// Theoretically it should never happen.
			return null;
		}
	}
	
	static FilteredPolicy concat(FilteredPolicy fp1, FilteredPolicy fp2){
		FilteredPolicy[] fpa = {fp1, fp2};
		return concat(fpa);
	}
	
	/**
	 * This method compares two filtered policies.<br />
	 * <b>OPEN ISSUE:</b> At present it simply compares their string representation (which of course is
	 * not the case).
	 * @param fp
	 * @return
	 *
	boolean equals(FilteredPolicy fp){
		return policy.equals(fp.toString());
	}
	
	public static boolean equal(FilteredPolicy[] fpa1, FilteredPolicy[] fpa2){
		return concat(fpa1).equals(concat(fpa2));
	}*/
	
	public String toString(){
		return policy;
	}
	
	String accept(Mapper m){
		return m.toPrologRepresentation(this);
	}
	
}
