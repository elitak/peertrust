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
package g4mfs.impl.org.peertrust.net.ssl;

import g4mfs.impl.org.peertrust.config.Configurable;
import g4mfs.impl.org.peertrust.exception.ConfigurationException;
import g4mfs.impl.org.peertrust.net.AbstractFactory;
import g4mfs.impl.org.peertrust.net.Peer;

import org.apache.log4j.Logger;
import g4mfs.impl.org.peertrust.net.NetClient;
import g4mfs.impl.org.peertrust.net.NetServer;

/**
 * <p>
 * 
 * </p><p>
 * $Id: SecureSocketFactory.java,v 1.1 2005/11/30 10:35:18 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:18 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla 
 */
public class SecureSocketFactory implements AbstractFactory, Configurable {

	private static Logger log = Logger.getLogger(SecureSocketFactory.class);
	
	String _keystoreFile ;
	String _keyPassword ;
	String _storePassword ;
	String _host ;
	int _port ;
	
	public SecureSocketFactory ()
	{
		super() ;
		log.debug("$Id: SecureSocketFactory.java,v 1.1 2005/11/30 10:35:18 ionut_con Exp $");
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
		
		log.debug("Creating new NetClient") ;
		log.debug("keystoreFile: " + _keystoreFile + " - " + "keyPassword: " + _keyPassword + " - " + "storePassword: " + _storePassword) ;
		
		return new SecureClientSocket(_keystoreFile, _keyPassword, _storePassword) ;
	}
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetServer(net.jxta.edutella.util.Configurator)
	 */
	public NetServer createNetServer() {
		log.debug("Creating new NetServer") ;
		log.debug("port: " + _port + " - keystoreFile: " + _keystoreFile + " - keyPassword: " + _keyPassword + " - storePassword: " + _storePassword) ;
		
		return new SecureServerSocket(_port, _keystoreFile, _keyPassword, _storePassword) ;
	}
	/**
	 * @return Returns the _keyPassword.
	 */
	public String getKeyPassword() {
		return _keyPassword;
	}
	/**
	 * @param password The _keyPassword to set.
	 */
	public void setKeyPassword(String password) {
		_keyPassword = password;
	}
	/**
	 * @return Returns the _keystoreFile.
	 */
	public String getKeystoreFile() {
		return _keystoreFile;
	}
	/**
	 * @param file The _keystoreFile to set.
	 */
	public void setKeystoreFile(String file) {
		_keystoreFile = file;
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
	/**
	 * @return Returns the _storePassword.
	 */
	public String getStorePassword() {
		return _storePassword;
	}
	/**
	 * @param password The _storePassword to set.
	 */
	public void setStorePassword(String password) {
		_storePassword = password;
	}
}
