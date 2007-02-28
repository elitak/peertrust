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

package org.policy.engine;

import org.policy.communication.LocalPeerEngine;
import org.policy.communication.message.ServiceMessage;
import org.policy.communication.net.NetworkCommunicationFactory;
import org.policy.config.Configurable;
import org.policy.event.EventListener;

/**
 * <p>
 * 
 * </p><p>
 * $Id: PolicyEngine.java,v 1.5 2007/02/28 17:29:07 dolmedilla Exp $
 * <br/>
 * Date: Feb 12, 2007
 * <br/>
 * Last changed: $Date: 2007/02/28 17:29:07 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public interface PolicyEngine extends Configurable
{
	// Local reference to the application peer
	//       (to be used on the messages between the apolication client and the engine)
	LocalPeerEngine getLocalPeerServer() ;
	
	public void setCommunicationChannel (NetworkCommunicationFactory factory) ;
	
	// request
	// id local_query (question, givenInfo) --> id send_request (question, localPeer, givenInfo (forState) )
	// 			to be sent locally
	// id send_request (question, peer, givenInfo)
	// 			to be sent to peer

	// Info monitorQuery (id, opt step (MonitordskmOptions) )
	// 			asks for the status (ongoing, finished, waiting)

	// Available services:
	//   * Negotiation request
	//     - With LocalPeer: application request
	//     - With Peer != LocalPeer: network request
	//   * Monitoring information request
	//     - Current status
	//   * Policy Management
	//     - Add/modify/remove policies
	//   * Engine Management
	//     - Start/Stop
	//public RequestIdentifier sendRequest(ServiceMessage request) throws EngineInternalException;
	public void sendRequest(ServiceMessage request) throws EngineInternalException;
}
