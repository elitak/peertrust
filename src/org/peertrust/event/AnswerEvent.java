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

import org.peertrust.net.Answer;

/**
 * $Id: AnswerEvent.java,v 1.2 2004/11/18 12:50:46 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/11/18 12:50:46 $
 * by $Author: dolmedilla $
 * @description
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
