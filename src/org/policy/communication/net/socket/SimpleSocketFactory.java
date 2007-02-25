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
package org.policy.communication.net.socket;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.policy.communication.NetworkPeer;
import org.policy.communication.net.NetworkClient;
import org.policy.communication.net.NetworkCommunicationException;
import org.policy.communication.net.NetworkCommunicationFactory;
import org.policy.communication.net.NetworkServer;
import org.policy.config.ConfigurationException;


/**
 * <p>
 * 
 * </p><p>
 * $Id: SimpleSocketFactory.java,v 1.2 2007/02/25 23:00:29 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/25 23:00:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class SimpleSocketFactory extends NetworkCommunicationFactory {

	private static Logger log = Logger.getLogger(SimpleSocketFactory.class);
	
	String _host ;
	int _port ;
	
	public SimpleSocketFactory ()
	{
		super() ;
		log.debug("$Id: SimpleSocketFactory.java,v 1.2 2007/02/25 23:00:29 dolmedilla Exp $");
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		super.init() ;
	}
	
	public NetworkPeer getLocalServerPeerInfo ()
	{
		return new SocketPeer(this.getAlias(), _host, _port) ;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetClient(net.jxta.edutella.util.Configurator)
	 */
	public NetworkClient createNetClient() {
		
		log.debug("Creating new ClientSocket") ;
		
		return new SimpleClientSocket() ;
	}
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetServer(net.jxta.edutella.util.Configurator)
	 */
	public NetworkServer createNetServer() throws NetworkCommunicationException {
		log.debug("Creating new ServerSocket") ;
		log.debug("port: " + _port) ;
		
		SimpleServerSocket socket;
		try {
			socket = new SimpleServerSocket(_port);
		} catch (IOException e) {
			log.error(e.getMessage()) ;
			throw new NetworkCommunicationException(e.getMessage(), e) ;
		}
		
		return socket  ;
	}
	/**
	 * @return Returns the _host
	 */
	public String getHost() {
		return _host;
	}
	/**
	 * @param _host The _host to set.
	 */
	public void setHost(String _host) {
		this._host = _host;
	}
	/**
	 * @return Returns the _port.
	 */
	public int getPort() {
		return _port;
	}
	/**
	 * @param _port The _port to set.
	 */
	public void setPort(int _port) {
		this._port = _port;
	}
}
