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

import org.apache.log4j.Logger;

/**
 * <p>
 * 
 * </p><p>
 * $Id: PrologCompoundTerm.java,v 1.1 2006/01/25 16:07:46 dolmedilla Exp $
 * <br/>
 * Date: 19-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/01/25 16:07:46 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public class PrologCompoundTerm extends PrologTerm
{
	private static Logger log = Logger.getLogger(PrologCompoundTerm.class);
	
	String _functor ;
	int _arity ;
	PrologTerm[] _args ;
	
	public PrologCompoundTerm (String functor, int arity, PrologTerm[] args, String text)
	{
		super (text) ;
		log.debug("$Id: PrologCompoundTerm.java,v 1.1 2006/01/25 16:07:46 dolmedilla Exp $");
		_functor = functor ;
		_arity = arity ;
		_args = args ;
	}

	public String toString()
	{
		StringBuffer s = new StringBuffer(_functor + "(") ;
		
		if (_arity > 0)
		{
			s.append(_args[0].toString()) ;
			for (int i = 1 ; i < _arity ; i++)
				s.append("," + _args[i].toString()) ;
		}
		s.append(")") ;
		
		return s.toString() ;
	}
	
	/**
	 * @return Returns the args.
	 */
	public PrologTerm[] getArgs() {
		return _args;
	}
	
	public PrologTerm getArg(int i) {
		if (i >= _arity)
			return null ;
		else
			return _args[i] ;
	}

	/**
	 * @return Returns the arity.
	 */
	public int getArity() {
		return _arity;
	}

	/**
	 * @return Returns the functor.
	 */
	public String getFunctor() {
		return _functor;
	}

}
