package org.peertrust.net.ssl;

/**
 * @author Rita Gavriloaie, Daniel Olmedilla
 * @date 10-Dec-2003
 * @description
 */


import java.io.*;
import java.security.*;
//import javax.net.SocketFactory ;
import javax.net.ssl.*;
//import javax.security.cert.X509Certificate;

public class SecureClientSocket {
		
	SSLSocket socket = null;
	private final int MAX_NUM_TRIES = 5 ;

	//public static final String KEYSTORE_FILE = "client_keystore";
  	public static final String ALGORITHM = "sunx509";
//  	public static final String KEY_PASSWORD = "clientpw";
//  	public static final String STORE_PASSWORD = "clientstorepw" ;

	//static private SecureRandom secureRandom;	

	private String host = null;
	private int port ;
	private SSLSocketFactory sf ;
	private String keystoreFile ;
	private String keyPassword ;
	private String storePassword ;
	
	public SecureClientSocket(String host, int port, String keystoreFile, String keyPassword, String storePassword) {
		this.host = host;
		this.port = port;
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
		
	/**
	 * @param obj
	 */
	public void send(Object obj)
	{
		//int tries = 0 ;
		//boolean sent = false ;
		
		socket = null ;
		
			try {
				//	Then we get the socket from the factory and treat it
				// as if it were a standard (plain) socket.
				socket = (SSLSocket) sf.createSocket(host, port) ;
			
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

				objOut.writeObject(obj);
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
				if(socket!=null) {
					try{
						socket.close();
					}
					catch(IOException e){
						e.printStackTrace() ;
					}
				}
			}
//		}
	}
	
	public static void main(String[] args)
	{
		SecureClientSocket sc = new SecureClientSocket("localhost", 35000, "testClient_keystore", "testkey", "testkey") ;
		
		sc.send("prueba") ;
		
		System.out.println("Message sent") ;
	}
}
