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
package org.peertrust.event;

/**
 * $Id: PTEvent.java,v 1.1 2004/11/18 12:50:46 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/11/18 12:50:46 $
 * by $Author: dolmedilla $
 * @description
 */
public class PTEvent {
	
	static int id = 0 ;
	int _identifier ;
	Object _source ;
	
	/**
	 * 
	 */
	public PTEvent(Object source) {
		super();
		_identifier = getNewId() ;
		_source = source ;
	}
	
	private synchronized int getNewId ()
	{
		id += 1 ;
		return id ;
	}
	
	public Object getSource ()
	{
		return  _source ;
	}
}
