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
package org.policy.event;

import org.apache.log4j.Logger;
import org.policy.util.NumberGenerator;

/**
 * <p>
 * General Event
 * </p><p>
 * $Id: Event.java,v 1.1 2007/02/17 16:59:27 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed:  $Date: 2007/02/17 16:59:27 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public abstract class Event implements Cloneable {
	
	private static Logger log = Logger.getLogger(Event.class);
	
	static NumberGenerator generator = new NumberGenerator () ;
	
	long _identifier ;
	Object _source ;
	
	/**
	 * 
	 */
	public Event(Object source) {
		super();
		_identifier = generator.getNewId() ;
		_source = source ;
	}
		
	public Object getSource ()
	{
		return  _source ;
	}
}
