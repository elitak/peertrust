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
 * $Id: PrologList.java,v 1.1 2006/01/25 16:07:45 dolmedilla Exp $
 * <br/>
 * Date: 19-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/01/25 16:07:45 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public class PrologList extends PrologTerm
{
	private static Logger log = Logger.getLogger(PrologList.class);
	
	int _size ;
	PrologTerm _args[] ;
	
	public PrologList (int size, PrologTerm[] args, String text)
	{
		super(text) ;
		_size = size ;
		if (args == null)
			_args = new PrologTerm[0] ;
		else
			_args = args ;
	}
	
	public String toString()
	{
		StringBuffer s = new StringBuffer("[") ;
		
		if (_size > 0)
		{
			s.append(_args[0].toString()) ;
			for (int i = 1 ; i < _size ; i++)
				s.append("," + _args[i].toString()) ;
		}
		s.append("]") ;
		
		return s.toString() ;
	}

	public int getSize()
	{
		return _size ;
	}
	
	public PrologTerm[] getArgs ()
	{
		return _args ;
	}
}
