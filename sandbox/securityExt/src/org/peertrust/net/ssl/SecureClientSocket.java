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

import java.io.*;
import java.security.*;
//import javax.net.SocketFactory ;
import javax.net.ssl.*;

import org.apache.log4j.Logger;
import org.peertrust.net.Message;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;
//import javax.security.cert.X509Certificate;

/**
 * $Id: SecureClientSocket.java,v 1.1 2004/12/21 11:09:35 seb0815 Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/12/21 11:09:35 $
 * by $Author: seb0815 $
 * @description
 */
public class SecureClientSocket implements NetClient {
	
	private static Logger log = Logger.getLogger(SecureClientSocket.class);
	
	SSLSocket socket = null;
	private final int MAX_NUM_TRIES = 5 ;

	//public static final String KEYSTORE_FILE = "client_keystore";
  	public static final String ALGORITHM = "sunx509";
//  	public static final String KEY_PASSWORD = "clientpw";
//  	public static final String STORE_PASSWORD = "clientstorepw" ;

	//static private SecureRandom secureRandom;	

	private SSLSocketFactory sf ;
	private String keystoreFile ;
	private String keyPassword ;
	private String storePassword ;
	
	public SecureClientSocket(String keystoreFile, String keyPassword, String storePassword) {
		log.debug("init()") ;
		this.keystoreFile = keystoreFile ;
		this.keyPassword = keyPassword ;
		this.storePassword = storePassword ;
		
		//	Local references used for clarity
		KeyManagerFactory kmf;
		KeyStore ks;
		TrustManagerFactory tmf;
		SSLContext sslc;

		try {
			kmf = KeyManagerFactory.getInstance(ALGORITHM);
			ks = KeyStore.getInstance( "JKS" );
			ks.load(new FileInputStream(keystoreFile), storePassword.toCharArray());
			kmf.init(ks, keyPassword.toCharArray());
			tmf = TrustManagerFactory.getInstance(ALGORITHM);
			tmf.init(ks);
			sslc = SSLContext.getInstance("TLS");
			sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			// The process is different from here on the client. Instead of
			// getting a ServerSocketFactory, we ask for a SocketFactory from
			// the SSL context.
			sf = sslc.getSocketFactory();
		}
		catch (Exception e) {
			e.printStackTrace() ;	
		}
	}

	/* (non-Javadoc)
	 * @see org.peertrust.net.NetClient#send(org.peertrust.net.Message, org.peertrust.net.Peer)
	 */
	/**
	 * @param obj
	 */
	public void send(Message message, Peer peer)
	{
		log.debug("Send() " + message.toString() + " to " + peer.getAlias()) ;
		//int tries = 0 ;
		//boolean sent = false ;
		
		socket = null ;
		
			try {
				//	Then we get the socket from the factory and treat it
				// as if it were a standard (plain) socket.
				socket = (SSLSocket) sf.createSocket(peer.getAddress(), peer.getPort()) ;
			
//				((SSLSocket)socket).addHandshakeCompletedListener(new HandshakeCompletedListener()
//							{
//								public void	handshakeCompleted(HandshakeCompletedEvent event)
//								{
//									System.out.println("Handshake finished!");
//									System.out.println("\t CipherSuite:" +
//									event.getCipherSuite());
//									System.out.println("\t SessionId " +
//									event.getSession());
//									System.out.println("\t PeerHost " +
//									event.getSession().getPeerHost());
//									try
//									{
//										X509Certificate [] certs = event.getSession().getPeerCertificateChain() ;
//										for (int i = 0 ; i < certs.length ; i++)
//										{
//											String name = certs[i].getIssuerDN().getName() ;
//											int index = name.indexOf("CN=") + 3 ;
//											int index2 = name.indexOf(",", index) ;
//											String dn =name.substring(index, index2) ; 
//											System.out.println("Cert to " + certs[i].getSubjectDN().getName()
//													+ " by " + dn + 
//													" and it is valid till " + certs[i].getNotAfter()) ;
//											try
//											{
//												socket.close() ;
//												event.getSocket().close() ;
//											} catch (IOException e1)
//											{
//												e1.printStackTrace();
//											}
//										}
//									} catch (SSLPeerUnverifiedException e)
//									{
//										e.printStackTrace();
//									}
//									
//								}
//							});

				//System.out.println("Sending object: " + obj);
				ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream()); 

				objOut.writeObject(message);
				objOut.flush();
				
	//			sent = true ;
				//System.out.println("Object sent: " + obj);
			}
			catch(Exception e)
			{
				e.printStackTrace();
	//			sent = false ;
				  
	//			try
//				{
//					Thread.sleep(200) ;
//				} catch (InterruptedException e1)
//				{
//					e1.printStackTrace();
//				}
			}
			finally{
				if(socket!=null)
				{
					try
					{
						socket.close();
					}
					catch(IOException e)
					{
						e.printStackTrace() ;
					}
				}
			}
//		}
	}
	
//	public static void main(String[] args)
//	{
//		SecureClientSocket sc = new SecureClientSocket("testClient_keystore", "testkey", "testkey") ;
//		
//		sc.send(new Query (null, new Peer(null, null, 12), 12), new Peer ("alice", "localhost", 35000)) ;
//		
//		System.out.println("Message sent") ;
//	}
//

}
