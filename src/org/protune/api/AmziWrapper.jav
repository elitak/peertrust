package org.protune.api;

import java.util.Vector;
import amzi.ls.*;

/**
 * Class <tt>AmziWrapper</tt> adapts an <tt>amzi.ls.LogicServer</tt> component to the interface
 * specified in {@link org.protune.api.PrologEngine}. For further information about the
 * <tt>amzi.ls.LogicServer</tt> component see the
 * <a href="http://www.amzi.com/products/prolog_products.htm">Amzi! Prolog</a> web site.
 * @author cjin
 */
public class AmziWrapper extends PrologEngine {

	LogicServer ls;
	
	public AmziWrapper() throws InitException {
		ls = new LogicServer();
		try {
			ls.Init("");
		}
		catch (LSException lse) {
			throw new InitException();
		}
	}
	
	public String[] getAllAnswers(String query) throws QueryException {
		long term;
		Vector<String> results = new Vector<String>();
		try {
			term = ls.CallStr(query);
	      	// Check for the empty list or an atom
	      	long type = ls.GetTermType(term);

	      	if (type != LogicServer.pLIST) return new String[0];

	      	while (term != 0) {
	      		results.add(ls.TermToStr(ls.GetHead(term), 256));
	         	term = ls.GetTail(term);
	      	}
	    	String[] sa = new String[0];
			return results.toArray(sa);
		} catch (LSException e) {
				throw new QueryException();
			}
		}

	public String getFirstAnswer(String query) throws QueryException {
		try {
			long term = ls.CallStr(query);
	      	return ls.TermToStr(ls.GetHead(term),256);
		   
		}
		catch (LSException e) {
			throw new QueryException();
		}
	}
	
	public boolean isSuccessful(String query) throws QueryException {
		try {
			long term = ls.CallStr(query);
			if(term==0) return false;
			else return true;
		}
		catch (LSException e) {
			throw new QueryException();
		}
	}

	public void loadTheory(String theory) throws LoadTheoryException {
		try {
			ls.Load(theory);
		}
		catch (LSException lse) {
			throw new LoadTheoryException();
		}
	}

}
