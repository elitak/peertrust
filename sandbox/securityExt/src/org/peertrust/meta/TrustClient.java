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
package org.peertrust.meta;

import org.apache.log4j.Logger;
import org.peertrust.Vocabulary;
import org.peertrust.net.*;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.Query;
import org.peertrust.net.ssl.*;
import org.peertrust.security.*;

import net.jxta.edutella.util.Configurator ;
import net.jxta.edutella.util.Option;
import net.jxta.edutella.util.Configurable ;
import java.util.*;

/**
 * $Id: TrustClient.java,v 1.1 2004/12/21 11:09:34 seb0815 Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/12/21 11:09:34 $
 * by $Author: seb0815 $
 * @description
 */
public class TrustClient implements Configurable
{
	private final int TIMEOUT = 15000 ;
	//private final String LOCAL_ADDRESS = "130.75.152.153" ;
		
	private String keystoreFile ;
	private String keyPassword ;
	private String storePassword ;
	
	private String baseFolder = System.getProperty("user.home") + "/trust/" ;
	private String solution = "" ;
	
	private Peer destination ;
	private Configurator config ;
	private String query ;
	
	private static Logger log = Logger.getLogger(TrustClient.class);
	
//	private String localAlias ;
	public static final String LOCAL_ALIAS = "alice" ;
	public static final int LOCAL_PORT = 31000 ;
	
	public TrustClient (String query, Peer destination, Configurator config)
	{
		//Random rand = new Random(System.currentTimeMillis()) ;
		//localAlias = new String (LOCAL_ALIAS +  rand.nextLong()) ;

		this.config = config ;
		this.destination = destination ;
		this.query = query ;
	}

	public void run()
	{
		AbstractFactory factory = new SecureSocketFactory() ;
		NetClient netClient = factory.createNetClient(config) ;
		
//		SecureClientSocket ssc = new SecureClientSocket(destination.getAddress(),
//				destination.getPort(),
//				config.getValue(Vocabulary.BASE_FOLDER_TAG)  + config.getValue(Vocabulary.KEYSTORE_FILE_TAG),
//				config.getValue(Vocabulary.KEY_PASSWORD_TAG),
//				config.getValue(Vocabulary.STORE_PASSWORD_TAG));
		
		// sending an object using serialization
		log.debug("Starting a new request: " + query) ;
		
		//ssc.send(new Query(query, new Peer(LOCAL_ALIAS,config.getValue(Vocabulary.LOCAL_ADDRESS_TAG), LOCAL_PORT), -1));

		config.updateValue(Vocabulary.SERVER_PORT_TAG,""+LOCAL_PORT);
		config.updateValue(Vocabulary.PEERNAME,LOCAL_ALIAS);

		netClient.send(new Query(query, new Peer(config.getValue(Vocabulary.PEERNAME),config.getValue(Vocabulary.LOCAL_ADDRESS_TAG), Integer.parseInt(config.getValue(Vocabulary.SERVER_PORT_TAG))), -1),
				destination);
		
		//ssc.send(new Tree(query, "[query(" + query + ",no)]", "[]", Tree.READY, new Peer(localAlias,config.getValue(LOCAL_ADDRESS_TAG), LOCAL_PORT), -1));
		

		NetServer netServer = factory.createNetServer(config) ; 
		
		Message message = netServer.listen() ;
		log.debug("Connection accepted.");
		
			if ( (message != null) && (message instanceof Answer) )
			{
				Answer answer = (Answer) message ;
				
				if (answer.getStatus() == Answer.FAILURE)
				{
					log.debug("Negotiation of " + query + " failed") ;
					
					solution = "" ;
				}
				else
				{
					log.debug ("Negotiation of " + query + " succeed") ;

					log.info("Answer: " + answer.getGoal()) ;
					String str="Proof: [";
					Vector vector=answer.getProofRuleVector();
					for(int i=0;i<vector.size();i++) {
						str+=((ProofRule)vector.elementAt(i)).
						getRule();
						if(i<vector.size()-1)
							str+=",";
					}
					str+="]";
					log.info(str) ;

					solution = answer.getGoal() ;
				}
			}
			else
			{
				log.error("Invalid answer") ;
			}
			netServer=null;
			System.gc();
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
//TrustClient client = new TrustClient("request(spanishCourse,Session) @ eLearn", new Peer("alice","localhost", 32000), config) ;

TrustClient client=new TrustClient("authenticatesTo(Alice,alice,alice)@elearn",new Peer("alice","localhost", 32000),config);
					//TrustClient client = new TrustClient("document(project7,V15595312, ibm)", new Peer("company","localhost", 37000), config) ;
				// register objects which need configuration
				config.register(client) ;

					// configure objects
					config.finishConfig();
			
				//config.updateValue(Vocabulary.SERVER_PORT_TAG, new Integer(LOCAL_PORT).toString()) ;
				//config.updateValue(Vocabulary.PEERNAME, LOCAL_ALIAS) ;
					
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
