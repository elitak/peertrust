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

package org.policy.communication.message;

import org.policy.communication.Peer;
import org.policy.model.ClientRequestId;
import org.policy.model.NegotiationInfo;
import org.policy.model.ServiceHandler;

/**
 * <p>
 * 
 * </p><p>
 * $Id: NegotiationMessage.java,v 1.1 2007/02/17 16:59:27 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:27 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public abstract class NegotiationMessage extends ServiceMessage {

	static final long NO_NEGOTIATION_ID = -1 ;

 	NegotiationInfo _negInfo ;

	long _negId ;
	
	public NegotiationMessage(ClientRequestId clientReqId, Peer origin, Peer target, ServiceHandler handler, NegotiationInfo negInfo)
	{
		this (clientReqId, origin, target, handler , NO_NEGOTIATION_ID, negInfo) ;
	}
	
	public NegotiationMessage(ClientRequestId clientReqId, Peer origin, Peer target, ServiceHandler handler, long negId, NegotiationInfo negInfo)
	{
		super(clientReqId, origin, target, handler) ;
 		
		_negInfo = negInfo ;
		_negId = negId ;
	}

	/**
	 * @return Returns the negInfo.
	 */
	public NegotiationInfo getNegInfo()
	{
		return _negInfo;
	}

	/**
	 * @param negInfo The negInfo to set.
	 */
	public void setNegInfo(NegotiationInfo negInfo)
	{
		this._negInfo = negInfo;
	}

	/**
	 * @return Returns the negId.
	 */
	public long getNegId()
	{
		return _negId;
	}
	
	public String toString()
	{
		String message = super.toString() ;
		return " NEGOTIATION "+ message +
			"\n\t| - Id: " + _negId + " - Negotiation Info: " + _negInfo.toString() ;
	}
}
