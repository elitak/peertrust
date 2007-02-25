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

import java.io.*;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.policy.communication.NetworkPeer;
import org.policy.communication.message.ServiceMessage;
import org.policy.communication.net.NetworkClient;
//import javax.security.cert.X509Certificate;
import org.policy.communication.net.NetworkCommunicationException;

/**
 * <p>
 * 
 * </p><p> 
 * $Id: SimpleClientSocket.java,v 1.2 2007/02/25 23:00:29 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/25 23:00:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class SimpleClientSocket implements NetworkClient {
	
	private static Logger log = Logger.getLogger(SimpleClientSocket.class);
	
	Socket _socket = null;
	private final int MAX_NUM_TRIES = 5 ;

	public SimpleClientSocket() {
		log.debug("$Id: SimpleClientSocket.java,v 1.2 2007/02/25 23:00:29 dolmedilla Exp $");
	}

	/* (non-Javadoc)
	 * @see org.peertrust.net.NetClient#send(org.peertrust.net.Message, org.peertrust.net.Peer)
	 */
	/**
	 * @param obj
	 */
	public void send(ServiceMessage message, NetworkPeer peer) throws NetworkCommunicationException
	{
		log.debug("Send() " + message) ;
		//int tries = 0 ;
		//boolean sent = false ;
		
		SocketPeer socketPeer = (SocketPeer) peer ;
		
		_socket = null ;
		
		try {
			//	Then we get the socket from the factory and treat it
			// as if it were a standard (plain) socket.
			_socket = new Socket (socketPeer.getAddress(), socketPeer.getPort()) ;
		
			//System.out.println("Sending object: " + obj);
			ObjectOutputStream objOut = new ObjectOutputStream(_socket.getOutputStream()); 

			objOut.writeObject(message);
			objOut.flush();				
		}
		catch(Exception e)
		{
			log.error(e.getMessage()) ;
			throw new NetworkCommunicationException(e.getMessage(), e) ;
		}
		finally{
			if(_socket!=null)
			{
				try
				{
					_socket.close();
				}
				catch(IOException e)
				{
					e.printStackTrace() ;
				}
			}
		}
	}
}
