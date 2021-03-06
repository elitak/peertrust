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

import g4mfs.impl.org.peertrust.net.Message;
import g4mfs.impl.org.peertrust.security.credentials.CryptTools;

import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;

import org.apache.log4j.Logger;
import g4mfs.impl.org.peertrust.net.NetServer;
//import javax.net.ServerSocketFactory;

/**
 * <p>
 * 
 * </p><p>
 * $Id: SecureServerSocket.java,v 1.1 2005/11/30 10:35:18 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:18 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla 
 */
public class SecureServerSocket implements NetServer {
	
	private static Logger log = Logger.getLogger(SecureServerSocket.class);
	
	//	The new constants that are used during setup.
   //public static final String KEYSTORE_FILE = "server_keystore";
   public static final String ALGORITHM = "sunx509";
   //public static final String KEY_PASSWORD = "serverpw";
   //public static final String STORE_PASSWORD = "serverstorepw";
   private final int TIMEOUT = 15000 ;


//   private int port ;
//   private String keystoreFile ;
//   private String keyPassword ;
//   private String storePassword ;
   private SSLServerSocket _ss = null;
   
	public SecureServerSocket(int port, String keystoreFile, String keyPassword, String storePassword) {
		log.debug("$Id: SecureServerSocket.java,v 1.1 2005/11/30 10:35:18 ionut_con Exp $");
		
//		this.port=port;
//		this.keystoreFile = keystoreFile ;
//		this.keyPassword = keyPassword ;
//		this.storePassword = storePassword ;
		//java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		try {

		 	// Local references used for clarity.
		 	KeyManagerFactory kmf;
		 	KeyManager[] km;
		 	KeyStore ks;
		 	TrustManagerFactory tmf;
		 	TrustManager[] tm;
		 	SSLContext sslc;
      
		 	// Create a keystore that will read the JKS (Java KeyStore)
		 	// file format which was created by the keytool utility.
		 	ks = KeyStore.getInstance("JKS");
      
		 	// Load the keystore object with the binary keystore file and
		 	// a byte array representing its password.
		 	ks.load(new FileInputStream(keystoreFile), storePassword.toCharArray());
			//ks.load(SecureServerSocket.class.getResourceAsStream(KEYSTORE_FILE),STORE_PASSWORD.toCharArray()) ;
			
			// Gives us a factory for key managers that will let
		 	// us handle the asymetric keys we created earlier.
			kmf = KeyManagerFactory.getInstance(ALGORITHM);

		 	// Initialize the key manager factory with the keystore object,
		 	// again using the same password for security since it is going to
		 	// access the private key.
		 	kmf.init(ks, keyPassword.toCharArray());
      
		 	// Now we can get the key managers from the factory, since it knows
		 	// what type we are using now.
		 	km = kmf.getKeyManagers();
      
		 	// Next, create a trust manager factory using the same algorithm.
		 	// This is to avoid using the certificates in cacerts that
		 	// represent an authentication security risk.
		 	tmf = TrustManagerFactory.getInstance(ALGORITHM);
      
		 	// ...then initialize it with the keystore object. This time we don't
		 	// need the keystore password. This is because trusted certificates
		 	// are not a sensitive element in the keystore, unlike the
		 	// private keys.
		 	tmf.init(ks);
      
		 	// Once that's initialized, get the trust managers from the factory.
			tm = tmf.getTrustManagers();
      
		 	// Almost done, we need a context object that will get our
		 	// server socket factory. We specify TLS to indicate that we will
		 	// need a server socket factory that supports SSL.
		 	sslc = SSLContext.getInstance("TLS");
      
		 	// Initialize the context object with the key managers and trust
		 	// managers we got earlier. The third parameter is an optional
		 	// SecureRandom object. By passing in null, we are letting the
		 	// context object create its own.
		 	sslc.init(km, tm, null);
      
		 	// Finally, we get the ordinary-looking server socket factory
		 	// from the context object.
		 	SSLServerSocketFactory ssf = sslc.getServerSocketFactory();
      
			// From the factory, we simply ask for an ordinary-looking
			// server socket on the port we wish.
			_ss = (SSLServerSocket) ssf.createServerSocket(port);
			
			// we establish that client must also be authenticated
			_ss.setNeedClientAuth(true) ;
			
			_ss.setSoTimeout(TIMEOUT) ;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.NetServer#listen()
	 */
	public Message listen() {

		try
		{	
			SSLSocket recSocket;
			try
			{
				//System.out.println ("Waiting for connections at port " + Integer.parseInt(config.getValue(SERVER_PORT_TAG))) ;
				recSocket = (SSLSocket) _ss.accept();
				log.debug("Socket connection received") ;

				try {
			 
					DataInputStream    in     = new DataInputStream(recSocket.getInputStream());
					ObjectInputStream  objIn  = new ObjectInputStream(in);

					Message message = (Message) objIn.readObject() ;
					
					log.debug("Message received from " + message.getSource().getAlias());
					
					String DN = CryptTools.getCertificateName (recSocket.getSession().getPeerCertificateChain()) ;
					if (validAuthentication(DN, message.getSource().getAlias()) == false)
					{
						log.error("Certificate DN (" + DN + ") and alias (" + message.getSource().getAlias() + ") mismatch") ;
					}
					else
					{
						//System.out.println("Received: from peer "+tree.getId()+" goal "+tree.getGoal());
						return message ;
					}
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
			log.error("Exception", e) ;
		}
		return null ;
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
