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

package org.policy.communication.message;

import org.policy.communication.Peer;
import org.policy.engine.service.ServiceHandler;
import org.policy.model.ClientRequestId;
import org.policy.model.NegotiationInfo;

/**
 * <p>
 * This class avoids having a negotiation and requests having a single step only. Speciall useful when
 *     it is wanted to know only the policy applying to a resource or to take a simple boolean decision
 *     based on given parameters 
 * </p><p>
 * $Id: SingleStepNegotiationRequest.java,v 1.2 2007/02/25 23:00:29 dolmedilla Exp $
 * <br/>
 * Date: Feb 14, 2007
 * <br/>
 * Last changed: $Date: 2007/02/25 23:00:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class SingleStepNegotiationRequest extends NegotiationRequest
{
	public SingleStepNegotiationRequest(ClientRequestId clientReqId, Peer origin, Peer target, ServiceHandler handler, NegotiationInfo negInfo)
	{
		super (clientReqId, origin, target, handler, negInfo) ;
	}
}
