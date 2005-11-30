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
package g4mfs.impl.org.peertrust.event;

import g4mfs.impl.org.peertrust.net.Answer;;

/**
 * <p>
 * Event that represents an answer message.
 * </p><p>
 * $Id: AnswerEvent.java,v 1.1 2005/11/30 10:35:13 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:13 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla 
 */
public class AnswerEvent extends PTEvent {
	/**
	 * 
	 */
	Answer _answer ;
	
	public AnswerEvent(Object source, Answer answer) {
		super(source);
		_answer = answer ;
		// TODO Auto-generated constructor stub
	}
	
	public Answer getAnswer ()
	{
		return _answer ;
	}
}
