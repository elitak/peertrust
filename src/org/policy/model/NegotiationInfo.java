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
 * $Id: NegotiationInfo.java,v 1.1 2007/02/17 16:59:26 dolmedilla Exp $
 * <br/>
 * Date: Feb 13, 2007
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:26 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class NegotiationInfo
{
	Policy [] _policyRules ;
	Notification [] _notifications ;
	
	public NegotiationInfo (Policy [] policyRules)
	{
		this (policyRules, null) ;
	}

	public NegotiationInfo (Notification [] notifications)
	{
		this (null, notifications) ;
	}

	public NegotiationInfo (Policy [] policyRules, Notification [] notifications)
	{
		super () ;
	}

	public boolean hasPolicies ()
	{
		return (_policyRules != null) ? true : false ;
	}
	
	public boolean hasNotifications ()
	{
		return (_notifications != null) ? true : false ;
	}
	
	/**
	 * @return Returns the notifications.
	 */
	public Notification[] getNotifications()
	{
		return _notifications;
	}

	/**
	 * @param notifications The notifications to set.
	 * /
	public void setNotifications(Notification[] notifications)
	{
		this._notifications = notifications;
	}*/

	/**
	 * @return Returns the policyRules.
	 */
	public Policy[] getPolicyRules()
	{
		return _policyRules;
	}

	/**
	 * @param policyRules The policyRules to set.
	 * /
	public void setPolicyRules(Policy[] policyRules)
	{
		this._policyRules = policyRules;
	}*/
}
