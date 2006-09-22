package org.protune.api;

import java.util.*;

import alice.tuprolog.*;

/**
 * Class <tt>TuPrologWrapper</tt> adapts an <tt>alice.tuprolog.Prolog</tt> component to the interface
 * specified in {@link org.protune.api.PrologEngine}. For further information about the
 * <tt>alice.tuprolog.Prolog</tt> component see the
 * <a href="http://www.alice.unibo.it:8080/tuProlog/">tuProlog</a> web site.
 * @author jldecoi
 */
public class TuPrologWrapper extends PrologEngine {

	Prolog p;
	
	public TuPrologWrapper(){
		p = new Prolog();
	}
	
	public String[] getAllAnswers(String query) throws QueryException {
		try{
			Vector<String> vs = new Vector<String>();
			SolveInfo si = p.solve(query);
			
			try{
				while(true){
					vs.add(si.getTerm(si.toVarArray()[0].getName()).toString());
					si = p.solveNext();
				}
			}
			catch(UnknownVarException uve){
				// It cannot happen.
			}
			catch(NoSolutionException nse){}
			catch(NoMoreSolutionException nmse){}
			
			String[] sa = new String[0];
			return vs.toArray(sa);
		}
		catch(MalformedGoalException mge){
			throw new QueryException();
		}
		catch(ArrayIndexOutOfBoundsException aioobe){
			throw new QueryException();
		}
	}

	public String getFirstAnswer(String query) throws QueryException {
		try{
			SolveInfo si = p.solve(query);
			return si.getTerm(si.toVarArray()[0].getName()).toString();
		}
		catch(Exception e){
			throw new QueryException();
		}
	}
	
	public boolean isSuccessful(String query) throws QueryException{
		try{
			return p.solve(query).isSuccess();
		}
		catch(MalformedGoalException mge){
			throw new QueryException();
		}
	}

	public void loadTheory(String theory) throws LoadTheoryException {
		try{
			p.addTheory(new Theory(theory));
		}
		catch(InvalidTheoryException ite){
			throw new LoadTheoryException();
		}
	}

}
