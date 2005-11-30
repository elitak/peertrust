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
package g4mfs.impl.test.org.peertrust.event;

import g4mfs.impl.org.peertrust.event.EventDispatcher;
import g4mfs.impl.org.peertrust.event.PTEvent;
import g4mfs.impl.org.peertrust.event.PTEventListener;


/**
 * $Id: TestClass.java,v 1.1 2005/11/30 10:35:11 ionut_con Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/11/30 10:35:11 $
 * by $Author: ionut_con $
 * @description
 */
public class TestClass implements PTEventListener {
	
	PTEvent _message ;
	EventDispatcher _dispatcher ;
	/**
	 * 
	 */
	public TestClass(EventDispatcher ed) {
		super();
		_dispatcher = ed ;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.event.PTEventListener#event(org.peertrust.event.PTEvent)
	 */
	public void event(PTEvent event) {
		_message = event ;
	}
	
	public void generateEvent (PTEvent event)
	{
		_dispatcher.event(event) ;
	}
	/**
	 * @return Returns the _message.
	 */
	public PTEvent getMessage() {
		return _message;
	}
}
