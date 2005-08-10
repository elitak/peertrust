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
package org.peertrust.net;

import java.io.Serializable;

import org.peertrust.meta.Trace;

/**
 * <p>
 * 
 * </p><p>
 * $Id: NegotiationMessage.java,v 1.1 2005/08/10 12:02:43 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/10 12:02:43 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class NegotiationMessage extends Message implements Serializable {
	
 	Trace _trace ;
 	long _negotiationId ;

	public NegotiationMessage(Peer origin, Peer target, long negotiationId, Trace trace)
	{
		super(origin, target) ;
 		
		_negotiationId = negotiationId ;
 		if (trace == null)
 			this._trace = new Trace() ;
 		else
 			this._trace = trace ;
	}
	
	public Trace getTrace()
	{
		return _trace ;
	}

	public String toString()
	{
		String message = super.toString() ;
		return " NEGOTIATION "+ message +
			"\n\t| - NegotiationId: " + _negotiationId +
			"\n\t| - Trace: " + _trace ;
		
	}
	/**
	 * @return Returns the negotiationId.
	 */
	public long getNegotiationId() {
		return _negotiationId;
	}
	/**
	 * @param negotiationId The negotiationId to set.
	 */
	public void setNegotiationId(long negotiationId) {
		this._negotiationId = negotiationId;
	}
}
