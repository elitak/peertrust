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
package org.peertrust.net.ssl;

import net.jxta.edutella.util.Configurator;

import org.apache.log4j.Logger;
import org.peertrust.Vocabulary;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.NetClient;
import org.peertrust.net.NetServer;

/**
 * $Id: SecureSocketFactory.java,v 1.1 2004/12/21 11:09:35 seb0815 Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/12/21 11:09:35 $
 * by $Author: seb0815 $
 * @description
 */
public class SecureSocketFactory implements AbstractFactory {

	private static Logger log = Logger.getLogger(SecureSocketFactory.class);
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetClient(net.jxta.edutella.util.Configurator)
	 */
	public NetClient createNetClient(Configurator config) {
		
		log.debug("Creating new NetClient") ;
		String keystoreFile = config.getValue(Vocabulary.BASE_FOLDER_TAG)  + config.getValue(Vocabulary.KEYSTORE_FILE_TAG) ;
		String keyPassword = config.getValue(Vocabulary.KEY_PASSWORD_TAG) ;
		String storePassword = config.getValue(Vocabulary.STORE_PASSWORD_TAG) ;
		log.debug("keystoreFile: " + keystoreFile + " - " + "keyPassword: " + keyPassword + " - " + "storePassword: " + storePassword) ;
		
		return new SecureClientSocket(keystoreFile, keyPassword, storePassword) ;
	}
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetServer(net.jxta.edutella.util.Configurator)
	 */
	public NetServer createNetServer(Configurator config) {
		log.debug("Creating new NetServer") ;
		int port = Integer.parseInt(config.getValue(Vocabulary.SERVER_PORT_TAG)) ;
		String keystoreFile = config.getValue(Vocabulary.BASE_FOLDER_TAG)  + config.getValue(Vocabulary.KEYSTORE_FILE_TAG) ;
		String keyPassword = config.getValue(Vocabulary.KEY_PASSWORD_TAG) ;
		String storePassword = config.getValue(Vocabulary.STORE_PASSWORD_TAG) ;
		log.debug("port: " + port + " - keystoreFile: " + keystoreFile + " - keyPassword: " + keyPassword + " - storePassword: " + storePassword) ;
		
		return new SecureServerSocket(port, keystoreFile, keyPassword, storePassword) ;
	}
}
