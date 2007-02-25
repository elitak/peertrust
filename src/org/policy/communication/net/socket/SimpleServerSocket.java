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
import java.net.*;

import org.apache.log4j.Logger;
import org.policy.communication.message.ServiceMessage;
import org.policy.communication.net.NetworkCommunicationException;
import org.policy.communication.net.NetworkServer;

/**
 * <p>
 * 
 * </p><p>
 * $Id: SimpleServerSocket.java,v 1.2 2007/02/25 23:00:29 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/25 23:00:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class SimpleServerSocket implements NetworkServer {
	
	private static Logger log = Logger.getLogger(SimpleServerSocket.class);
	
	//	The new constants that are used during setup.
   private final int TIMEOUT = 15000 ;

   private ServerSocket _ss = null;
   
	public SimpleServerSocket(int port) throws IOException
	{
		log.debug("$Id: SimpleServerSocket.java,v 1.2 2007/02/25 23:00:29 dolmedilla Exp $");
		
		_ss = new ServerSocket (port) ;			
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.NetServer#listen()
	 */
	public ServiceMessage listen() throws NetworkCommunicationException
	{

		ServiceMessage message = null ;
		try
		{	
			Socket recSocket;
			try
			{
				//System.out.println ("Waiting for connections at port " + Integer.parseInt(config.getValue(SERVER_PORT_TAG))) ;
				recSocket = (Socket) _ss.accept();
				log.debug("Socket connection received") ;

				try {
			 
					DataInputStream    in     = new DataInputStream(recSocket.getInputStream());
					ObjectInputStream  objIn  = new ObjectInputStream(in);

					message = (ServiceMessage) objIn.readObject() ;
					
					log.debug("listen(): " + message);
				}
				catch (ClassNotFoundException cnfe) {
					log.error( "Class Not Found", cnfe);
				}
				catch(IOException ie ) {
					log.error( "IOException2", ie);
				}
			}
			catch (SocketTimeoutException te)
			{
				// ignore
			} catch (IOException e)	{
				log.error ("IOException", e) ;
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage()) ;
			throw new NetworkCommunicationException(e.getMessage(),e) ;
		}
		return message ;
	}

	private boolean validAuthentication(String name1, String name2)
	{
		return name1.toLowerCase().equals(name2.toLowerCase()) ;
	}

	protected void finalize ()
	{
		log.info("Finalizing") ;
		
		if (_ss != null)
		{
			try {
				_ss.close() ;
			}
			catch (IOException e)
			{
				log.error("Closing the server socket", e) ;
			}
			_ss = null ;
		}
	}

//	public static void main(String[] args) throws IOException, ClassNotFoundException
//		{
//			SecureServerSocket sss = new SecureServerSocket(35000, "testServer_keystore", "testkey", "testkey") ;
//			
//			ServerSocket ss = sss.getServerSocket() ;
//			
//			Socket s = ss.accept() ;
//			
//			DataInputStream    in     = new DataInputStream(s.getInputStream());
//							
//			ObjectInputStream  objIn  = new ObjectInputStream(in);
//
//			String st = (String) objIn.readObject();
//			
//			System.out.println("Message received: " + st) ;
//		}


}
