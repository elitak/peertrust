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
 * $Id: PrologVariable.java,v 1.1 2006/01/25 16:07:46 dolmedilla Exp $
 * <br/>
 * Date: 19-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/01/25 16:07:46 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public class PrologVariable extends PrologTerm
{
	private static Logger log = Logger.getLogger(PrologVariable.class);
	
	public PrologVariable (String var)
	{
		super(var) ;
		log.debug("$Id: PrologVariable.java,v 1.1 2006/01/25 16:07:46 dolmedilla Exp $");
	}

}
