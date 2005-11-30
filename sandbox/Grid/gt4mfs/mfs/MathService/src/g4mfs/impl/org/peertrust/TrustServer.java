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
package g4mfs.impl.org.peertrust;

import g4mfs.impl.org.peertrust.config.PTConfigurator;
import g4mfs.impl.org.peertrust.config.Vocabulary;
import g4mfs.impl.org.peertrust.exception.ConfigurationException;


/**
 * <p>
 * Simple server.
 * </p><p>
 * $Id: TrustServer.java,v 1.1 2005/11/30 10:35:17 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:17 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla
 */
public class TrustServer
{
	public static final String PREFIX = "Server app.: " ;

	public static void main(String[] args) throws ConfigurationException
	{
		final String PREFIX = TrustServer.PREFIX ;
		
		String defaultConfigFile = "file:peertrustConfig.rdf" ;
		String defaultComponent = Vocabulary.PeertrustEngine.toString() ;
		
		int TIMEOUT = 15000 ;
		int SLEEP_INTERVAL = 500 ;
		
		String newArgs[] = new String[1] ;
		
		if (args.length < 1)
		{
			System.out.println ("Args: <configFile.rdf>") ;
			//return ;
			newArgs[0] = defaultConfigFile ; 
		}
		else
			newArgs[0] = args[0] ;
	
		//	java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		
		PTConfigurator config = new PTConfigurator() ;
		
		String[] components = { defaultComponent } ;
		config.startApp(newArgs, components) ;

		System.out.println(PREFIX + "Started") ;
	}
}
