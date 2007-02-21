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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
*/

package org.policy.model;

/**
 * <p>
 * 
 * </p><p>
 * $Id: Notification.java,v 1.2 2007/02/21 06:52:48 dolmedilla Exp $
 * <br/>
 * Date: Feb 14, 2007
 * <br/>
 * Last changed: $Date: 2007/02/21 06:52:48 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class Notification
{
	Action _action ;
	String [] _notificationStrings ;
	
	public Notification (Action action, String [] notificationStrings)
	{
		_action = action ;
		_notificationStrings = notificationStrings ;
	}
	
	/**
	 * @return Returns the action.
	 */
	public Action getAction()
	{
		return _action;
	}

	/**
	 * @return Returns the notificationStrings.
	 */
	public String[] getNotificationStrings()
	{
		return _notificationStrings;
	}
}
