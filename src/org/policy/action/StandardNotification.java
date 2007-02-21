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

package org.policy.action;

import org.policy.model.Action;
import org.policy.model.Notification;

/**
 * <p>
 * 
 * </p><p>
 * $Id: StandardNotification.java,v 1.1 2007/02/21 06:52:48 dolmedilla Exp $
 * <br/>
 * Date: Feb 18, 2007
 * <br/>
 * Last changed: $Date: 2007/02/21 06:52:48 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class StandardNotification extends Notification
{
	
	// TODO to write completely
	public StandardNotification (Action action, String notification)
	{
		this(action, new String[] {notification} ) ;
	}
	
	public StandardNotification (Action action, String [] notifications)
	{
		super(action, notifications) ;
	}
}
