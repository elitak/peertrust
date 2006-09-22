package org.protune.api;

import java.util.Hashtable;
import java.util.Vector;
import ubc.cs.JLog.Foundation.jPrologAPI;

/**
 * Class <tt>JLogWrapper</tt> adapts a <tt>ubc.cs.JLog.Foundation.jPrologAPI</tt> component to the
 * interface specified in {@link org.protune.api.PrologEngine}. For further information about the
 * <tt>ubc.cs.JLog.Foundation.jPrologAPI</tt> component see the
 * <a href="http://jlogic.sourceforge.net/">JLog</a> web site.
 * @author jldecoi
 */
public class JLogWrapper extends PrologEngine {

	jPrologAPI jpa;
	
	public JLogWrapper(){
		jpa = new jPrologAPI("");
		jpa.setFailUnknownPredicate(true);
	}
	
	public String[] getAllAnswers(String query) throws QueryException {
		try{
			Vector<String> vs = new Vector<String>();
			Hashtable h = jpa.query(query);
			
			while(h != null){
				vs.add(h.get(h.keys().nextElement()).toString());
				h = jpa.retry();
			}
			
			String[] sa = new String[0];
			return vs.toArray(sa);
		}
		catch(RuntimeException re){
			throw new QueryException();
		}
	}

	public String getFirstAnswer(String query) throws QueryException {
		try{
			Hashtable h = jpa.query(query);
			if(h==null) throw new QueryException();
			return h.get(h.keys().nextElement()).toString();
		}
		catch(RuntimeException re){
			throw new QueryException();
		}
	}
	
	public boolean isSuccessful(String query) throws QueryException{
		try{
			Hashtable h = jpa.query(query);
			if(h==null) return false;
			else return true;
		}
		catch(RuntimeException re){
			throw new QueryException();
		}
	}

	public void loadTheory(String theory) throws LoadTheoryException {
		try{
			jpa.consultSource(theory);
		}
		catch(RuntimeException re){
			throw new LoadTheoryException();
		}
	}

}
