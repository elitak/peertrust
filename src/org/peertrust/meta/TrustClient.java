package org.peertrust.meta;

/**
 * $Id: TrustClient.java,v 1.1 2004/07/01 23:35:58 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:35:58 $
 * @description
 */
import java.net.*;
import java.net.Socket ;
import java.io.* ;

import org.apache.log4j.Logger;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.Query;
import org.peertrust.net.ssl.*;

import net.jxta.edutella.util.Configurator ;
import net.jxta.edutella.util.Option;
import net.jxta.edutella.util.Configurable ;
//import java.util.Random;

public class TrustClient implements Configurable
{
	private final String LOCAL_ALIAS = "alice" ;
	private final int LOCAL_PORT = 31000 ;
	private final int TIMEOUT = 15000 ;
	//private final String LOCAL_ADDRESS = "130.75.152.153" ;
		
	private String keystoreFile ;
	private String keyPassword ;
	private String storePassword ;
	
	private String baseFolder = System.getProperty("user.home") + "/trust/" ;
	private String solution = "" ;
	
	private Peer destiny ;
	private Configurator config ;
	private String query ;
	
	private static Logger log = Logger.getLogger(TrustClient.class);
	
//	private String localAlias ;
	
	TrustClient (String query, Peer destiny, Configurator config)
	{
		//Random rand = new Random(System.currentTimeMillis()) ;
		//localAlias = new String (LOCAL_ALIAS +  rand.nextLong()) ;

		this.config = config ;
		this.destiny = destiny ;
		this.query = query ;
	}

	void run()
	{
		SecureClientSocket ssc = new SecureClientSocket(destiny.getAddress(),
				destiny.getPort(),
				config.getValue(MetaInterpreter.BASE_FOLDER_TAG)  + config.getValue(MetaInterpreter.KEYSTORE_FILE_TAG),
				config.getValue(MetaInterpreter.KEY_PASSWORD_TAG),
				config.getValue(MetaInterpreter.STORE_PASSWORD_TAG));
		
		// sending an object using serialization
		log.debug("Starting a new request: " + query) ;
		
		ssc.send(new Query(query, new Peer(LOCAL_ALIAS,config.getValue(MetaInterpreter.LOCAL_ADDRESS_TAG), LOCAL_PORT), -1)); 
		//ssc.send(new Tree(query, "[query(" + query + ",no)]", "[]", Tree.READY, new Peer(localAlias,config.getValue(LOCAL_ADDRESS_TAG), LOCAL_PORT), -1));
		
		ssc = null ;
		
		SecureServerSocket sss;
		ServerSocket serverSocket;

		sss = new SecureServerSocket(LOCAL_PORT,
				config.getValue(MetaInterpreter.BASE_FOLDER_TAG)  + config.getValue(MetaInterpreter.KEYSTORE_FILE_TAG),
				config.getValue(MetaInterpreter.KEY_PASSWORD_TAG),
				config.getValue(MetaInterpreter.STORE_PASSWORD_TAG));
															
		serverSocket = sss.getServerSocket();
		
		try
		{
			serverSocket.setSoTimeout(TIMEOUT) ;
		} catch (SocketException e)
		{
			log.error ("SocketException", e) ;
		}
		
		Socket recSocket;
		
		try {
			log.debug("Listening at port " + LOCAL_PORT) ;
			recSocket = serverSocket.accept();
			log.debug("Connection accepted.");
			
			try {
				 
				DataInputStream    in     = new DataInputStream(recSocket.getInputStream());
				ObjectInputStream  objIn  = new ObjectInputStream(in);

				Message message = (Message) objIn.readObject();
				
				if (message instanceof Answer)
				{
					Answer answer = (Answer) message ;
					
					if (answer.getStatus() == Tree.FAILED)
					{
						log.debug("Negotiation of " + query + " failed") ;
						
						solution = "" ;
					}
					else
					{
						log.debug ("Negotiation of " + query + " succeed") ;

						log.info("Answer: " + answer.getGoal()) ;
						
						log.info("Proof: " + answer.getProof()) ;
	
						solution = answer.getGoal() ;
					}
				}
				else
					log.error("Invalid answer") ;
			}
			catch (ClassNotFoundException cnfe) {
				log.error("Class Not Found", cnfe);
			}
			catch( IOException ie ) {
				log.error("IOException", ie);
			}
		} catch (SocketTimeoutException te)
		{
			log.error("The request timed out the " + TIMEOUT/1000 + "seconds.") ;
		} catch (IOException e)	{
			log.error("Accept IOException", e) ;
		}
		
		try {
			serverSocket.close() ;
		}
		catch (IOException e)
		{
			log.error("IOException", e) ;
		}
	}
	
	public String getSolution () {
		return solution ; 
	}

	public static void main(String[] args)
	{
		//java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		
		try {
					Configurator config = new Configurator("trust.properties",new String[0]);
					config.setAppInfo("Automated Trust Negotiation Requester");
					
					//TrustClient client = new TrustClient("employee(alice, microsoft) @ microsoft", new Peer("alice","localhost", 32000), config) ;
					//TrustClient client = new TrustClient("access(result1)", new Peer("alice","localhost", 32000), config) ;
					TrustClient client = new TrustClient("request(spanishCourse,Session) @ eLearn", new Peer("alice","localhost", 32000), config) ;
					//TrustClient client = new TrustClient("document(project7,V15595312, ibm)", new Peer("company","localhost", 37000), config) ;
				// register objects which need configuration
				config.register(client) ;

					// configure objects
					config.finishConfig();

				client.run() ;
				//TrustClient client = new TrustClient("document(project7, _) @ company7", new Peer("alice7","localhost", 32000),config) ;
				//TrustClient client = new TrustClient("document(project7,V15595312)", new Peer("company","webbase.learninglab.uni-hannover.de", 37000), config) ;
				//TrustClient client = new TrustClient("employee(alice7,V32048085)@V32048085@alice7", new Peer("company7","localhost", 37000)) ;
				//TrustClient client = new TrustClient("constraint(peerName(alice7))", new Peer("alice7","localhost", 32000),config) ;
			
			//TrustClient client = new TrustClient("policeOfficer(alice) @ caStatePolice", new Peer("alice","localhost", 32000), config) ;
			
		}
		catch (Exception e)
		{
			System.err.println("ERROR: " + e.getMessage()) ;
			e.printStackTrace() ;	
		}
//		java.net.InetAddress i = null ;
//		try
//		{
//			i = java.net.InetAddress.getLocalHost();
//		} catch (UnknownHostException e)
//		{
//			e.printStackTrace();
//		}
//		System.out.println(i);

//		try {
//				 java.util.Enumeration e = NetworkInterface.getNetworkInterfaces();
//
//				 while(e.hasMoreElements()) {
//					NetworkInterface netface = (NetworkInterface)
//					e.nextElement();
//					System.out.println("Net interface: "+netface.getName());
//
//					java.util.Enumeration e2 = netface.getInetAddresses();
//
//					while (e2.hasMoreElements()){
//					   InetAddress ip = (InetAddress) e2.nextElement();
//					   System.out.println("IP address: "+ip.toString());
//					}
//				 }
//			  }
//			  catch (Exception e) {
//				 System.out.println ("e: " + e);
//			  }
//		String hostName = getCodeBase();
//		Socket s = new Socket( hostName, portNum );
//		InetAddress address = s.getLocalAddress();
//		String hostName = address.getHostName();

// 		 String hostName = request.getRemoteAddr();
	}

	/**
	 * @see net.jxta.edutella.util.Configurable#getOptions()
	 */
	public Option[] getOptions()
	{
		Option baseOpt =	new Option(
			'b',
			"metaI.baseFolder",
			"Base Folder",
			"Folder with local files",
			true) ;

		Option addOpt =	new Option(
			'a',
			"metaI.address",
			"Address",
			"Local Address",
			true) ;

		Option nameOpt = new Option(
			'n',
			"metaI.peerName",
			"Name",
			"Peer name",
			true);
		Option portOpt =	new Option(
			'p',
			"metaI.serverPort",
			"Port",
			"Server Port",
			true) ;
		
		Option entOpt =	new Option(
			'e',
			"metaI.entitiesFile",
			"Entities File",
			"Entities File",
			true) ;
		
		Option keystoreOpt =	new Option(
			'f',
			"metaI.keystoreFile",
			"Keystore File",
			"Keystore File",
			true) ;
		
		Option keypwdOpt =	new Option(
			'k',
			"metaI.keyPassword",
			"Key Password",
			"Key Password",
			true) ;
		keypwdOpt.setIsPassword(true) ;			
		
		Option storepwdOpt =	new Option(
			's',
			"metaI.storePassword",
			"Keystore Password",
			"Keystore Password",
			true) ;
		storepwdOpt.setIsPassword(true) ;
		
		return new Option[] {baseOpt,addOpt, nameOpt, portOpt, entOpt, keystoreOpt, keypwdOpt, storepwdOpt};
	}

	/**
	 * @see net.jxta.edutella.util.Configurable#getPropertyPrefix()
	 */
	public String getPropertyPrefix() {
		return "metaI";
	}

	public void setBaseFolder (String folder) {
		baseFolder = folder ;
	}
	
	public String getBaseFolder () {
		return baseFolder ;
	}

}
