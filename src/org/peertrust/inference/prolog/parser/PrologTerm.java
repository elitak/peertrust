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
 * $Id: PrologTerm.java,v 1.2 2006/03/06 12:47:57 dolmedilla Exp $
 * <br/>
 * Date: 19-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/03/06 12:47:57 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public abstract class PrologTerm {

	private static Logger log = Logger.getLogger(PrologTerm.class);
	
	String _text ;
	
	public PrologTerm (String text)
	{
		super() ;
		log.debug("$Id: PrologTerm.java,v 1.2 2006/03/06 12:47:57 dolmedilla Exp $");
		_text = text ;
	}
	
	public String getText()
	{
		return _text ;
	}
	
	public String toString()
	{
		return _text ;
	}
}
