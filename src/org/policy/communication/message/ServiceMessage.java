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

import java.io.Serializable;

import org.policy.communication.LocalPeerClient;
import org.policy.communication.Peer;
import org.policy.engine.service.ServiceHandler;
import org.policy.model.ClientRequestId;

/**
 * <p>
 * 
 * </p><p>
 * $Id: ServiceMessage.java,v 1.2 2007/02/25 23:00:29 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/25 23:00:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public abstract class ServiceMessage extends Message implements Serializable {

	// source is the peer issuing the message and target the destination peer
	Peer _source, _target ;
	// this field is basic for asynchronous communication. The client sets it to a number in order
	//     to distinguish which answer from the server corresponsds with which request.
	//     The server ignores it and only copies it to the response sent to the client
	ClientRequestId _clientRequestIdentifier ;
	// the handler specifies which framework (engine) should process this message (e.g., PeerTrust or Protune).
	//     The class ServiceHandler is used only as an identifier and does not contain any logic.
	ServiceHandler _handler ;
	
	public ServiceMessage(ClientRequestId clientReqId, Peer source, Peer target, ServiceHandler handler)
	{
		super() ;
 		_source = source ;
 		_target = target ;
 		_clientRequestIdentifier = clientReqId ;
 		_handler = handler ;
	}
	
	/**
	 * @return Returns the source.
	 */
	public Peer getSource() {
		return _source;
	}
		
	/**
	 * @return Returns the target.
	 */
	public Peer getTarget() {
		return _target;
	}
	
	/**
	 * @return Returns the _clientRequestIdentifier.
	 */
	public ClientRequestId get_clientRequestIdentifier()
	{
		return _clientRequestIdentifier;
	}
	
	public String toString()
	{
		return "MESSAGE: ClientReqId: " + _clientRequestIdentifier +
		" - Source: " + _source + " - Target: " + _target ;
	}

	/**
	 * @return Returns the handler.
	 */
	public ServiceHandler getHandler()
	{
		return _handler;
	}

}
