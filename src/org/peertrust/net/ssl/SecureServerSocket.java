package org.peertrust.net.ssl;

import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;
//import javax.net.ServerSocketFactory;

/**
 * @author Rita Gavriloaie, Daniel Olmedilla
 * @date 10-Dec-2003
 * @description
 */

public class SecureServerSocket {

	//	The new constants that are used during setup.
   //public static final String KEYSTORE_FILE = "server_keystore";
   public static final String ALGORITHM = "sunx509";
   //public static final String KEY_PASSWORD = "serverpw";
   //public static final String STORE_PASSWORD = "serverstorepw";

   private int port ;
   private String keystoreFile ;
   private String keyPassword ;
   private String storePassword ;
   private SSLServerSocket ss = null;
   
	public SecureServerSocket(int port, String keystoreFile, String keyPassword, String storePassword) {
		this.port=port;
		this.keystoreFile = keystoreFile ;
		this.keyPassword = keyPassword ;
		this.storePassword = storePassword ;
		//java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	}
	
	protected void finalize ()
	{
		if (ss != null)
		{
			try {
				ss.close() ;
				ss = null ;
			}
			catch (IOException e)
			{
				e.printStackTrace() ;
			}
		}
	}
		
	public SSLServerSocket getServerSocket() {
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
			ss = (SSLServerSocket) ssf.createServerSocket(port);
			
			// we establish that client must also be authenticated
			ss.setNeedClientAuth(true) ;
		}
		catch(Exception e){
			e.printStackTrace();
			return null ;
		}
		return ss ;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException
		{
			SecureServerSocket sss = new SecureServerSocket(35000, "testServer_keystore", "testkey", "testkey") ;
			
			ServerSocket ss = sss.getServerSocket() ;
			
			Socket s = ss.accept() ;
			
			DataInputStream    in     = new DataInputStream(s.getInputStream());
							
			ObjectInputStream  objIn  = new ObjectInputStream(in);

			String st = (String) objIn.readObject();
			
			System.out.println("Message received: " + st) ;
		}
}
