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
package org.peertrust.net.socket;

import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.NetClient;
import org.peertrust.net.NetServer;
import org.peertrust.net.Peer;

/**
 * <p>
 * 
 * </p><p>
 * $Id: SimpleSocketFactory.java,v 1.2 2005/05/22 17:56:47 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:47 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class SimpleSocketFactory implements AbstractFactory, Configurable {

	private static Logger log = Logger.getLogger(SimpleSocketFactory.class);
	
	String _host ;
	int _port ;
	
	public SimpleSocketFactory ()
	{
		super() ;
		log.debug("$Id: SimpleSocketFactory.java,v 1.2 2005/05/22 17:56:47 dolmedilla Exp $");
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		
	}
	
	public Peer getServerPeer (String alias)
	{
		return new Peer(alias, _host, _port) ;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetClient(net.jxta.edutella.util.Configurator)
	 */
	public NetClient createNetClient() {
		
		log.debug("Creating new ClientSocket") ;
		
		return new SimpleClientSocket() ;
	}
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetServer(net.jxta.edutella.util.Configurator)
	 */
	public NetServer createNetServer() {
		log.debug("Creating new ServerSocket") ;
		log.debug("port: " + _port) ;
		
		return new SimpleServerSocket(_port) ;
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
