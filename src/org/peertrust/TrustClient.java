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
package org.peertrust;

import java.util.Hashtable;
import java.util.Vector;

import org.peertrust.config.PTConfigurator;
import org.peertrust.config.Vocabulary;
import org.peertrust.event.AnswerEvent;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.Answer;
import org.peertrust.net.Peer;
import org.peertrust.net.Query ;

/**
 * $Id: TrustClient.java,v 1.6 2005/04/17 20:44:42 dolmedilla Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/04/17 20:44:42 $
 * by $Author: dolmedilla $
 * @description
 */
public class TrustClient implements PTEventListener
{
	public static final String PREFIX = "Client app.: " ;

	public final String ALIAS = "Client" ;
	EventDispatcher _ed ;
	private String _query ;
	private int _id ;
	
	private Hashtable _queries ;
	
	private Peer _peer ;
	
//	private String localAlias ;
	
	TrustClient (EventDispatcher ed)
	{
		_ed = ed ;
		_id = 0 ;
		_queries = new Hashtable() ;
		_peer = new Peer (ALIAS.toLowerCase(), null, -1) ;
	}

	public void sendQuery (String query)
	{
		_id++ ;
		Query newQuery = new Query(query, _peer, null, _id, null) ;
		QueryEvent qe = new QueryEvent(this, newQuery) ;
		
		_ed.event(qe) ;
		
		_queries.put(query + _id, new Vector()) ;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.event.PTEventListener#event(org.peertrust.event.PTEvent)
	 */
	public void event(PTEvent event) {
		if (event instanceof QueryEvent)
		{
			QueryEvent qe = (QueryEvent) event ;
			Query q = qe.getQuery() ;
			System.out.println (PREFIX + "Query sent " + q.getReqQueryId() + ": " + q.getGoal()) ;
		}
		else if (event instanceof AnswerEvent)
		{
			AnswerEvent ae = (AnswerEvent) event ;
			Answer a = ae.getAnswer() ;
			System.out.println (PREFIX + "Answer to query " + a.getReqQueryId() + ": " + a.getGoal()) ;
		}
		else
			System.out.println (PREFIX + "Unknown event of class " + event.getClass().getName()) ;
	}

	public static void main(String[] args) throws ConfigurationException
	{
		final String PREFIX = TrustClient.PREFIX ;
		
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
		
		PTEngine engine = (PTEngine) config.getComponent(Vocabulary.PeertrustEngine) ;
		EventDispatcher dispatcher = engine.getEventDispatcher() ;
		if (dispatcher == null)
		{
			System.out.println ("No event dispatcher has been found") ;
			System.out.println (PREFIX + "No event dispatcher has been found") ;
		}
		
		TrustClient tc = new TrustClient(dispatcher) ;
		
		tc.sendQuery("request(spanishCourse,Session) @ elearn") ;
		
		long time = System.currentTimeMillis() ;
		
		while (System.currentTimeMillis() - time < TIMEOUT )
			try {
				Thread.sleep(SLEEP_INTERVAL) ;
			} catch (InterruptedException e) {
				// ignore
			}

		System.out.println (PREFIX + "Stopping") ;
		engine.stop() ;

//		tc.sendQuery("employee(alice, microsoft) @ microsoft") ;
//		tc.sendQuery("access(result1)") ;
//		tc.sendQuery("document(project7,V15595312, ibm)") ;
//		tc.sendQuery("document(project7, _) @ company7") ;
//		tc.sendQuery("document(project7,V15595312)") ;
//		tc.sendQuery("employee(alice7,V32048085)@V32048085@alice7") ;
//		tc.sendQuery("constraint(peerName(alice7))") ;
//		tc.sendQuery("policeOfficer(alice) @ caStatePolice") ;
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
	
//	public void init()
//	{
//		AbstractFactory factory = new SecureSocketFactory() ;
//		NetClient netClient = factory.createNetClient(config) ;
//		
////		SecureClientSocket ssc = new SecureClientSocket(destination.getAddress(),
////				destination.getPort(),
////				config.getValue(Vocabulary.BASE_FOLDER_TAG)  + config.getValue(Vocabulary.KEYSTORE_FILE_TAG),
////				config.getValue(Vocabulary.KEY_PASSWORD_TAG),
////				config.getValue(Vocabulary.STORE_PASSWORD_TAG));
//		
//		// sending an object using serialization
//		log.debug("Starting a new request: " + query) ;
//		
//		//ssc.send(new Query(query, new Peer(LOCAL_ALIAS,config.getValue(Vocabulary.LOCAL_ADDRESS_TAG), LOCAL_PORT), -1));
//		
//		netClient.send(new Query(query, new Peer(config.getValue(Vocabulary.PEERNAME),config.getValue(Vocabulary.LOCAL_ADDRESS_TAG), Integer.parseInt(config.getValue(Vocabulary.SERVER_PORT_TAG))), -1),
//				destination);
//		//ssc.send(new Tree(query, "[query(" + query + ",no)]", "[]", Tree.READY, new Peer(localAlias,config.getValue(LOCAL_ADDRESS_TAG), LOCAL_PORT), -1));
//		
//		NetServer netServer = factory.createNetServer(config) ; 
//		
//		Message message = netServer.listen() ;
//		log.debug("Connection accepted.");
//		
//			if ( (message != null) && (message instanceof Answer) )
//			{
//				Answer answer = (Answer) message ;
//				
//				if (answer.getStatus() == Tree.FAILED)
//				{
//					log.debug("Negotiation of " + query + " failed") ;
//					
//					solution = "" ;
//				}
//				else
//				{
//					log.debug ("Negotiation of " + query + " succeed") ;
//
//					log.info("Answer: " + answer.getGoal()) ;
//					
//					log.info("Proof: " + answer.getProof()) ;
//
//					solution = answer.getGoal() ;
//				}
//			}
//			else
//			{
//				log.error("Invalid answer") ;
//			}
//	}
}
