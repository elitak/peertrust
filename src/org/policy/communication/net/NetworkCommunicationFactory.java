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

package org.policy.communication.net;

import org.apache.log4j.Logger;
import org.policy.communication.NetworkPeer;
import org.policy.communication.Peer;
import org.policy.communication.message.ServiceMessage;
import org.policy.config.Configurable;
import org.policy.config.ConfigurationException;
import org.policy.event.CommunicationEventListener;
import org.policy.event.Event;
import org.policy.event.EventDispatcher;
import org.policy.event.EventListener;
import org.policy.event.ReceivedMessageEvent;
import org.policy.event.SendMessageEvent;

/**
 * <p>
 * 
 * </p><p>
 * $Id: NetworkCommunicationFactory.java,v 1.2 2007/02/18 00:38:12 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/18 00:38:12 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public abstract class NetworkCommunicationFactory implements Configurable, CommunicationEventListener {
	
	private static Logger log = Logger.getLogger(NetworkCommunicationFactory.class);
	
	String _alias ;
	EventDispatcher _dispatcher ;
	
	public abstract NetworkPeer getLocalServerPeerInfo () ;
	
	public abstract NetworkClient createNetClient() ;
	
	public abstract NetworkServer createNetServer() ;

	public NetworkCommunicationFactory ()
	{

	}
	
	public void init() throws ConfigurationException
	{	
		if (_dispatcher == null)
		{
			String msg = "No event dispatcher has been specified." ;

			log.error (msg) ;
			throw new ConfigurationException(msg) ;
		}
		
		NetworkServer netServer = createNetServer () ;
		NetworkServerThread serverThread = new NetworkServerThread (_dispatcher, netServer) ;
		Thread thread = new Thread(serverThread) ;
		thread.start() ;
		
		NetworkClient netClient = createNetClient () ;
		NetworkClientWrapper networkClientWrapper = new NetworkClientWrapper (netClient) ;
		_dispatcher.register(networkClientWrapper, SendMessageEvent.class) ;
	}
	
	/**
	 * @return Returns the alias.
	 */
	public String getAlias()
	{
		return _alias;
	}

	/**
	 * @param alias The alias to set.
	 */
	public void setAlias(String alias)
	{
		this._alias = alias;
	}

	/**
	 * @return Returns the dispatcher.
	 */
	public EventDispatcher getDispatcher()
	{
		return _dispatcher;
	}

	/**
	 * @param dispatcher The dispatcher to set.
	 */
	public void setDispatcher(EventDispatcher dispatcher)
	{
		this._dispatcher = dispatcher;
	}
	
	private class NetworkServerThread implements Runnable
	{
		NetworkServer _netServer ;
		EventDispatcher _dispatcher ;
		
		NetworkServerThread (EventDispatcher dispatcher, NetworkServer netServer)
		{
			_dispatcher = dispatcher ;
			_netServer = netServer ;
		}
		
		public void run()
		{
			log.info("Listening communication channel up") ;
			
			// TODO Condition for stopping the listening server thread
			
			while (true) {  
				ServiceMessage message = _netServer.listen() ;
				
				if (message != null)
				{
					log.debug ("New message received from " + message.getSource() + " to " + message.getTarget()) ;

					_dispatcher.event(new ReceivedMessageEvent(message)) ;
	 			}
			}
			
		}
		
	}
	
	private class NetworkClientWrapper implements EventListener
	{
		NetworkClient _netClient ;
		
		NetworkClientWrapper (NetworkClient netClient)
		{
			_netClient = netClient ;
		}

		public void event(Event event)
		{
			if (event instanceof SendMessageEvent)
			{
				ServiceMessage message = ( (SendMessageEvent) event).getMessage() ;
				Peer peer = message.getTarget() ;
				
				// if the peer is a NetworkPeer then the message is sent, otherwise it is ignored
				//    (it is not a network event but probably an application event)
				if (peer instanceof NetworkPeer)
					_netClient.send(message, (NetworkPeer) message.getTarget()) ;
			}
			else
				// The event should not have been received by this class
				log.error("Event of class " + event.getClass().getName() + " received.") ;
		}
	}
	
}