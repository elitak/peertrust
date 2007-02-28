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

import org.apache.log4j.Logger;
import org.policy.communication.LocalPeerEngine;
import org.policy.communication.Replyable;
import org.policy.communication.message.ServiceMessage;
import org.policy.communication.net.NetworkCommunicationFactory;
import org.policy.config.ConfigurationException;
import org.policy.engine.service.ServiceHandler;
import org.policy.engine.service.ServiceHandlerRegistry;
import org.policy.engine.service.UnavailableServiceHandlerException;
import org.policy.event.Event;
import org.policy.event.EventDispatcher;
import org.policy.event.EventListener;
import org.policy.event.ReceivedMessageEvent;
import org.policy.model.ClientRequestId;
import org.policy.model.RequestIdentifier;

/**
 * <p>
 * 
 * </p><p>
 * $Id: PolicyEngineImpl.java,v 1.5 2007/02/28 08:40:14 dolmedilla Exp $
 * <br/>
 * Date: Feb 14, 2007
 * <br/>
 * Last changed: $Date: 2007/02/28 08:40:14 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class PolicyEngineImpl implements PolicyEngine, Replyable, EventListener
{
	private static Logger log = Logger.getLogger(PolicyEngineImpl.class);

	static final String DEFAULT_ALIAS = "PolicyEngine" ;
	
	String _alias = DEFAULT_ALIAS ;
	LocalPeerEngine _enginePeer ;
	ServiceHandlerRegistry _handlerRegistry ;
	EventDispatcher _dispatcher ;
	NetworkCommunicationFactory _commChannel ;

	public PolicyEngineImpl ()
	{
		log.debug("$Id: PolicyEngineImpl.java,v 1.5 2007/02/28 08:40:14 dolmedilla Exp $");
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
		
		_dispatcher.register(this, ReceivedMessageEvent.class) ;
	}

	public void event(Event event)
	{
		if (event instanceof ReceivedMessageEvent)
		{
			ReceivedMessageEvent rcvEvent = (ReceivedMessageEvent) event ;
			
			try {
				RequestIdentifier id = sendRequest(rcvEvent.getMessage()) ;
				
				// TODO not yet clear how to handle this
				
			} catch (EngineInternalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			log.error("Unknown event " + event.getClass().getName()) ;
		}
	}

	public void reply(ClientRequestId identifier, ServiceMessage response) {
		// TODO Auto-generated method stub
		
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
		log.debug("New request received: " + request.toString()) ;
		
		// We dispatch a new event in order to notify other components about the new request

		// TODO problem with circular notifications: TOCHECK
		// _dispatcher.event(new ReceivedMessageEvent(request)) ;
		
		PolicyEngine engine;
		try
		{
			engine = retrieveServiceHandler (request.getHandler());
		} catch (UnavailableServiceHandlerException e)
		{
			log.error("No policy engine is associated to service handler " + request.getHandler().getClass().getName()) ;
			throw new EngineInternalException (e) ;
		}

		log.debug("Sending the request to " + engine.getClass().getName()) ;
		
		RequestIdentifier id = engine.sendRequest(request) ;
		
		log.debug("Received answer id: " + id) ;
		
		return  id ;
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
	public void setEventDispatcher(EventDispatcher dispatcher)
	{
		this._dispatcher = dispatcher;
	}

}
