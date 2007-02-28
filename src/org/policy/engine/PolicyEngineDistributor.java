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
import org.policy.communication.LocalPeer;
import org.policy.communication.LocalPeerClient;
import org.policy.communication.LocalPeerEngine;
import org.policy.communication.NetworkPeer;
import org.policy.communication.Peer;
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
import org.policy.event.ReceivedCommMessageEvent;
import org.policy.event.ReceivedEngineRequestEvent;
import org.policy.event.SendCommMessageEvent;
import org.policy.event.SendEngineResponseEvent;
import org.policy.model.ClientRequestId;

/**
 * <p>
 * 
 * </p><p>
 * Two ways of receiving requests: via Java interface sendRequest or via the network 
 * interface (transmitted as an internal event). If the former is used, an event is simulated 
 * in order to have an homogeneus control (and also inform listeners properly of the new request). 
 * </p><p>
 * $Id: PolicyEngineDistributor.java,v 1.1 2007/02/28 17:29:07 dolmedilla Exp $
 * <br/>
 * Date: Feb 14, 2007
 * <br/>
 * Last changed: $Date: 2007/02/28 17:29:07 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class PolicyEngineDistributor implements PolicyEngine, EventListener
{
	private static Logger log = Logger.getLogger(PolicyEngineDistributor.class);

	static final String DEFAULT_ALIAS = "PolicyEngine" ;
	static final int DEFAULT_QUEUE_SIZE = 100 ;
	
	String _alias = DEFAULT_ALIAS ;
	LocalPeerEngine _enginePeer ;
	ServiceHandlerRegistry _handlerRegistry ;
	EventDispatcher _dispatcher ;
	NetworkCommunicationFactory _commChannel ;
	
	public PolicyEngineDistributor ()
	{
		log.debug("$Id: PolicyEngineDistributor.java,v 1.1 2007/02/28 17:29:07 dolmedilla Exp $");
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
		
		_dispatcher.register(this, ReceivedCommMessageEvent.class) ;
	}

	// a request is received from the communication channel
	public void event(Event event)
	{
		if (event instanceof ReceivedCommMessageEvent)
		{
			log.debug("New request received from communication channel") ;
			
			ReceivedCommMessageEvent rcvEvent = (ReceivedCommMessageEvent) event ;
			
			try
			{
				handleRequest(rcvEvent.getMessage()) ;	
			} 
			catch (EngineInternalException e)
			{
				log.error("Internal policy error: " + e.getMessage() + "\n\tAction taken: IGNORE", e) ;
			}
		}
		else if (event instanceof SendEngineResponseEvent)
		{
			ServiceMessage response = ((SendEngineResponseEvent) event).getMessage() ;
			
			log.debug("New reply for client request id " + response.getClientRequestIdentifier() + " received: " + response.toString()) ;
			
			Peer peer = response.getSource() ;
			
			if (peer instanceof LocalPeerClient)
			{
				log.debug("Reply to application") ;
				((LocalPeerClient) peer).getAttachedClass().reply(response.getClientRequestIdentifier(), response) ;
			}
			else if (peer instanceof NetworkPeer)
			{
				log.debug("Dispatch network send event") ;
				_dispatcher.event(new SendCommMessageEvent(this, response)) ;
			}
			else
				log.error("Unknown peer class " + peer.getClass().getName()) ;				
		}
		else
		{
			log.error("Unknown event " + event.getClass().getName()) ;
		}
	}
		
	// A request is received from the application
	/* (non-Javadoc)
	 * @see org.policy.engine.PolicyEngine#sendRequest(org.policy.communication.message.ServiceRequest, org.policy.communication.Peer)
	 */
	public void sendRequest(ServiceMessage request) throws EngineInternalException
	{	
		log.debug("New request received from application: " + request.toString()) ;
		
		_dispatcher.event(new ReceivedCommMessageEvent(this, request)) ;
		
		handleRequest(request) ;
	}
	
	private PolicyEngine retrieveServiceHandler (ServiceHandler handler) throws UnavailableServiceHandlerException
	{
		return _handlerRegistry.retrieveServiceHandler(handler) ;
	}
	
	public void handleRequest (ServiceMessage request) throws EngineInternalException
	{
		PolicyEngine engine;
		try
		{
			engine = retrieveServiceHandler (request.getHandler()) ;
		} catch (UnavailableServiceHandlerException e)
		{
			log.error("No policy engine is associated to service handler " + request.getHandler().getClass().getName()) ;
			throw new EngineInternalException (e) ;
		}

		log.debug("Sending the request to " + engine.getClass().getName()) ;
		
		// TODO use the direct communication and handler registry like it is or better using the dispatcher?
		// The dispatcher would remove the need for the handler registry. However, current way is probably clearer?
		engine.sendRequest(request) ;
		
		log.debug("Request sent") ;
		
		_dispatcher.event(new ReceivedEngineRequestEvent (this, request)) ;
		
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
