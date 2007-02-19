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

package org.policy.peertrust.engine;

import org.apache.log4j.Logger;
import org.policy.communication.LocalPeerEngine;
import org.policy.communication.message.ServiceMessage;
import org.policy.communication.net.NetworkCommunicationFactory;
import org.policy.config.ConfigurationException;
import org.policy.engine.EngineInternalException;
import org.policy.engine.PolicyEngine;
import org.policy.engine.ServiceHandlerRegistry;
import org.policy.engine.UnavailableServiceHandlerException;
import org.policy.event.EventDispatcher;
import org.policy.event.ReceivedMessageEvent;
import org.policy.model.RequestIdentifier;
import org.policy.model.ServiceHandler;

/**
 * <p>
 * 
 * </p><p>
 * $Id: PeerTrustPolicyEngine.java,v 1.1 2007/02/19 09:01:28 dolmedilla Exp $
 * <br/>
 * Date: Feb 14, 2007
 * <br/>
 * Last changed: $Date: 2007/02/19 09:01:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class PeerTrustPolicyEngine implements PolicyEngine
{
	// TODO completely
	
	private static Logger log = Logger.getLogger(PeerTrustPolicyEngine.class);

	static final String DEFAULT_ALIAS = "PolicyEngine" ;
	
	String _alias = DEFAULT_ALIAS ;
	LocalPeerEngine _enginePeer ;
	ServiceHandlerRegistry _handlerRegistry ;
	EventDispatcher _dispatcher ;
	NetworkCommunicationFactory _commChannel ;

	public PeerTrustPolicyEngine ()
	{
	}
	
	public void init() throws ConfigurationException
	{
		_enginePeer = new LocalPeerEngine (_alias) ;
		
		String msg = null ;
		
		if (_handlerRegistry == null)
			msg = "No service handler registry has been specified." ;
		else if (_dispatcher == null)
			msg = "No event dispatcher has been specified." ;
		else if (_commChannel == null)
			msg = "No communication channel has been specified." ;
		
		if (msg != null)
		{
			log.error (msg) ;
			throw new ConfigurationException(msg) ;
		}
	}

	private PolicyEngine retrieveServiceHandler (ServiceHandler handler) throws UnavailableServiceHandlerException
	{
		return _handlerRegistry.retrieveServiceHandler(handler) ;
	}
	
	/* (non-Javadoc)
	 * @see org.policy.engine.PolicyEngine#sendRequest(org.policy.communication.message.ServiceRequest, org.policy.communication.Peer)
	 */
	public RequestIdentifier sendRequest(ServiceMessage request) throws EngineInternalException
	{	
		// We dispatch a new event in order to notify other components about the new request
		_dispatcher.event(new ReceivedMessageEvent(request)) ;
		
		PolicyEngine engine;
		try
		{
			engine = retrieveServiceHandler (request.getHandler());
		} catch (UnavailableServiceHandlerException e)
		{
			log.error("No policy engine is associated to service handler " + request.getHandler().getClass().getName()) ;
			throw new EngineInternalException (e) ;
		}

		return engine.sendRequest(request) ;
	}
	
	public void setCommunicationChannel(NetworkCommunicationFactory factory)
	{
		_commChannel = factory ;
	}

	public LocalPeerEngine getLocalPeerServer()
	{
		return _enginePeer ;
	}

	/**
	 * @param handlerRegistry The handlerRegistry to set.
	 */
	public void setHandlerRegistry(ServiceHandlerRegistry handlerRegistry)
	{
		this._handlerRegistry = handlerRegistry;
	}

	/**
	 * @param dispatcher The dispatcher to set.
	 */
	public void setDispatcher(EventDispatcher dispatcher)
	{
		this._dispatcher = dispatcher;
	}

}
