/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package org.peertrust.inference.prolog.parser;

import java.util.Vector;

import org.apache.log4j.Logger;

import yprolog.*;

/**
 * <p>
 * 
 * </p><p>
 * $Id: PrologTools.java,v 1.3 2006/04/24 12:01:29 dolmedilla Exp $
 * <br/>
 * Date: 19-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/04/24 12:01:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public class PrologTools {

	private static Logger log = Logger.getLogger(PrologTools.class);
	
	static Parser prologParser = prologParser = new Parser(System.in) ;

	static public PrologTerm getTerm(String query) throws ParseException
	{
		log.debug("Parsing: " + query) ;
		Term term = prologParser.getTerm(query) ;
		
		log.debug("Transforming: " + term.toString()) ;
		
		PrologTerm prologTerm = term2prologTerm(term) ;
		
		log.debug("Result: " + prologTerm.toString()) ;
		
		return prologTerm ;
	}
	
	
	// Structure from yprolog Term.toString()
	static PrologTerm term2prologTerm(Term term)
	{
		//String s ;
		
		Term deref = term.getReference() ;
		String functor = term.getFunctor() ;
		int arity = term.getArity() ;
	
        if (term.isBound())
        {
        	// if (deref) return "v" + varid + "->" + ref.toString();
        	if (deref != null)
        	{
        		System.out.println("================deref: " + deref.toString()) ;
        		// prologTerm = new PrologTerm(0, null, 0, null, deref.toString()) ;
        		return new PrologVariable(term.toString()) ;
        	}
        	else
        	{
	            if (functor.equals("null") & arity==0)
	                //prologTerm = new PrologTerm(PrologTerm.LIST, "[]", 0, null) ;
	            	return new PrologList(0, null, term.toString()) ;
	            if (functor.equals("cons") & arity==2)
	            {
	            	Vector<PrologTerm> args = new Vector<PrologTerm> () ;
	
	            	args.addElement(term2prologTerm(term.getarg(0))) ;
	
	                Term t = term.getarg(1); 
	                while (t.getfunctor().equals("cons") & t.getarity() == 2)
	                {
	                	args.addElement(term2prologTerm(t.getarg(0))) ;
	                	//s = s + "," + t.getarg(0);
	                	t = t.getarg(1);
	                }
	
	                //if (t.getfunctor().equals("null") & t.getarity() == 0) 
	                    //s = s + "]";
	                //else
	                if ( ! (t.getfunctor().equals("null") & t.getarity() == 0) )
	                {
	                	args.addElement(term2prologTerm(t)) ;
	                	//s = s + "|" + t + "]";
	                }
	
	                //prologTerm = new PrologTerm (PrologTerm.LIST, s, args.size(), (PrologTerm[])args.toArray()) ;
	                return new PrologList (args.size(), args.toArray(new PrologTerm[0]), term.toString()) ;
	            }
	            else
	            {
	                if (arity == 0)
	                {
	                    return new PrologAtom (YProlog.addQuotes(functor,true)) ;
	                }
	                else
	                {
	                	Vector<PrologTerm> args = new Vector<PrologTerm>() ;
	                	
	                    //s = YProlog.addQuotes(functor,false) + "(";
	                    for (int i=0; i < (arity - 1); i++)
	                    {
	                    	args.addElement(term2prologTerm(term.getarg(i))) ;
	                        //s =s + term.getarg(i).toString() + ",";
	                    }
	                    args.addElement(term2prologTerm(term.getarg(arity-1))) ;
	                    //s = s + term.getarg(arity-1).toString() + ")";
	                    
	                    //prologTerm = new PrologTerm (PrologTerm.COMPOUND_TERM, functor, args.size(), (PrologTerm[])args.toArray()) ;
	                    log.debug ("Functor: " + functor + " - size: " + args.size()) ;
	                    
	                    return new PrologCompoundTerm (YProlog.addQuotes(functor,false), args.size(), args.toArray(new PrologTerm[0]), term.toString()) ;
	                }
	            }
        	}
        }
        else
        	return new PrologVariable (term.toString()) ;
	}
}
