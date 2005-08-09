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
 * <p>
 * Simple client
 * </p><p>
 * $Id: TrustClient.java,v 1.9 2005/08/09 13:47:55 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/09 13:47:55 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */
public class TrustClient implements PTEventListener
{	
	static final String DEFAULT_ALIAS = "Client" ;

	static public int DEFAULT_TIMEOUT = 15000 ;
	static public int DEFAULT_SLEEP_INTERVAL = 200 ;

	final String PREFIX_MESSAGE = "CLIENT: " ;
	final String INFO_MESSAGE = PREFIX_MESSAGE + "INFO: " ;
	final String ERROR_MESSAGE = PREFIX_MESSAGE + "ERROR: " ;
	final String WARN_MESSAGE = PREFIX_MESSAGE + "WARN: " ;
	final String DEBUG_MESSAGE = PREFIX_MESSAGE + "DEBUG: " ;
	
	EventDispatcher _ed ;
	private String _query ;
	private long _id ;
	
	private Hashtable _queries ;
	
	private Peer _peer ;

	String _alias ;
	int _timeout ;
	int _sleepInterval ;
	
//	private String localAlias ;
	
	public TrustClient (String [] configurationArgs, String [] components) throws ConfigurationException
	{
		this(configurationArgs, components, true) ;
	}
	
	public TrustClient (String [] configurationArgs, String [] components, boolean localClient) throws ConfigurationException
	{	
		PTConfigurator config = new PTConfigurator() ;

		config.startApp(configurationArgs, components) ;
		
		PTEngine engine = (PTEngine) config.getComponent(Vocabulary.PeertrustEngine) ;
		_ed = engine.getEventDispatcher() ;
		
		_ed.register(this, PTEvent.class) ;
		
		if (_ed == null)
		{
			System.out.println(ERROR_MESSAGE + "No event dispatcher has been found") ;
		}
		
		_id = 0 ;
		_queries = new Hashtable() ;
		
		setAlias(DEFAULT_ALIAS) ;
		setTimeout(DEFAULT_TIMEOUT) ;
		setSleepInterval(DEFAULT_SLEEP_INTERVAL) ;
	}

	public long sendQuery (String query)
	{
		_id++ ;
		
		// Peer target and Trace are ignored and set to null
		Query newQuery = new Query(query, _peer, null, _id, null) ;
		QueryEvent qe = new QueryEvent(this, newQuery) ;
		
		_ed.event(qe) ;
		
		_queries.put(new Long(_id), new QueryEntry(_id)) ;
		
		return _id ;
	}
	
	public void removeQuery(long id)
	{
		QueryEntry tmp = (QueryEntry) _queries.remove(new Long(id)) ;
		if (tmp == null)
			System.out.println(ERROR_MESSAGE + "Query with id " + id + " does not exist") ;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.event.PTEventListener#event(org.peertrust.event.PTEvent)
	 */
	public void event(PTEvent event) {
		if (event instanceof QueryEvent)
		{
			QueryEvent qe = (QueryEvent) event ;
			Query q = qe.getQuery() ;
			System.out.println(INFO_MESSAGE + "Query sent " + q.getReqQueryId() + ": " + q.getGoal()) ;
		}
		else if (event instanceof AnswerEvent)
		{
			AnswerEvent ae = (AnswerEvent) event ;
			Answer a = ae.getAnswer() ;
			System.out.println(INFO_MESSAGE + "Answer to query " + a.getReqQueryId() + ": " + a.getGoal()) ;
			QueryEntry queryEntry = (QueryEntry) _queries.get(new Long(a.getReqQueryId())) ;
			if (queryEntry != null)
				queryEntry.addAnswer(a) ;
			else
				if (a.getTarget().equals(_peer))
					System.out.println(ERROR_MESSAGE + "Received an answer with a wrong id: " + a.getReqQueryId()) ;
				else
					System.out.println(DEBUG_MESSAGE + "Received answer to a query which was originally posted by: " + a.getTarget().getAlias()) ;
		}
		else
			System.out.println(WARN_MESSAGE + "Unknown event of class " + event.getClass().getName()) ;
	}

	private QueryEntry checkQuery (long id) throws IllegalArgumentException
	{
		QueryEntry qe = (QueryEntry) _queries.get(new Long(id)) ;
		
		if (qe == null)
		{
			String message = ERROR_MESSAGE + "Wrong id: " + id ;
			System.out.println(message) ;
			throw new IllegalArgumentException(message) ;
		}
		return qe ;
	}
	
	public String[] getAnswers (long queryId)
	{
		QueryEntry qe = checkQuery(queryId) ;
		
		Vector v = qe.getAnswers() ;
		
		String [] answers = new String[v.size()] ;
		
		for (int i = 0 ; i < v.size() ; i++)
		{
			answers[i] = ((Answer) v.elementAt(i)).getGoal() ;
		}
		return answers ;
	}
		
	public boolean isQueryFinished (long queryId)
	{
		QueryEntry qe = checkQuery(queryId) ;
		
		return qe.isFinished() ;
	}
	
	public Boolean isQuerySuccessful(long queryId)
	{
		QueryEntry qe = checkQuery(queryId) ;
		
		return qe.isSuccessful() ;
	}

	public Boolean waitForQuery(long id)
	{
		long time = System.currentTimeMillis() ;

		while (System.currentTimeMillis() - time < _timeout )
		{
			//System.out.println ("CURRENT QUERY IS " + isQueryFinished(id) + " FINISHED") ;
			if (isQueryFinished(id) == true)
				break ;
			else
				try {
					Thread.sleep(_sleepInterval) ;
				} catch (InterruptedException e) {
					// ignore
				}
		}
		
		return isQuerySuccessful(id) ;
	}
	
	public static void main(String[] args) throws ConfigurationException
	{	
		
		//tc.sendQuery("orderDrugs(Drug,Requester) @ elearn") ;
		//		tc.sendQuery("employee(alice, microsoft) @ microsoft") ;
//		tc.sendQuery("access(result1)") ;
//		tc.sendQuery("document(project7,V15595312, ibm)") ;
//		tc.sendQuery("document(project7, _) @ company7") ;
//		tc.sendQuery("document(project7,V15595312)") ;
//		tc.sendQuery("employee(alice7,V32048085)@V32048085@alice7") ;
//		tc.sendQuery("constraint(peerName(alice7))") ;
//		tc.sendQuery("policeOfficer(alice) @ caStatePolice") ;
		
		String DEFAULT_QUERY = "request(spanishCourse,Session) @ elearn" ;
		
		String defaultConfigFile = "file:peertrustConfig.rdf" ;
		String [] defaultComponents = { Vocabulary.PeertrustEngine.toString() } ;
			
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
		
		TrustClient tc = new TrustClient (newArgs, defaultComponents) ;

		long id = tc.sendQuery(DEFAULT_QUERY) ;
		
		tc.waitForQuery(id) ;

		System.out.println("Query finished: " + tc.isQueryFinished(id)) ;
		
		Boolean querySuccessful = tc.isQuerySuccessful(id) ;
		String message = "Query successful: " ;
		if (querySuccessful == null)
			message += "undefined" ;
		else
		{
			message += querySuccessful.booleanValue() ;
			
			if (querySuccessful.booleanValue() == true)
			{
				String [] answers = tc.getAnswers(id) ;
		
				System.out.println("Answers: ") ;
				for (int i = 0 ; i < answers.length ; i++)
					System.out.println("\t" + answers[i]) ;
			}
		}

		tc.removeQuery (id) ;
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
	
	class QueryEntry
	{
		long id ;
		Vector answers ;
		boolean finished = false ;
		Boolean successful = null ;
		
		QueryEntry (long id)
		{
			this.id = id ;
			answers = new Vector() ;
		}
		
		
		void addAnswer (Answer answer)
		{
			switch(answer.getStatus())
			{
				case Answer.ANSWER:
					setSuccessful(new Boolean(true)) ;
					answers.add(answer) ;
					break ;
				case Answer.LAST_ANSWER:
					setSuccessful(new Boolean(true)) ;
					setFinished(true) ;
					answers.add(answer) ;
					break ;
				case Answer.FAILURE:
					Boolean succeeded = isSuccessful() ;
				
					// if no previous answer was received, then the query has failed
					if (succeeded == null)
						setSuccessful (new Boolean(false)) ;
					setFinished(true) ;
					break ;
				default:
					System.out.println("ERROR: Wrong status in answer " + answer) ;
			}
			
		}
		
		/**
		 * @return Returns the answers.
		 */
		Vector getAnswers() {
			return answers;
		}
		/**
		 * @param answers The answers to set.
		 */
//		void setAnswers(Vector answers) {
//			this.answers = answers;
//		}
		/**
		 * @return Returns the finished.
		 */
		boolean isFinished() {
			return finished;
		}
		/**
		 * @param finished The finished to set.
		 */
		void setFinished(boolean finished) {
			this.finished = finished;
		}
		/**
		 * @return Returns the id.
		 */
		long getId() {
			return id;
		}
		/**
		 * @param id The id to set.
		 */
		void setId(long id) {
			this.id = id;
		}
		/**
		 * @return Returns the successful.
		 */
		Boolean isSuccessful() {
			return successful;
		}
		/**
		 * @param successful The successful to set.
		 */
		void setSuccessful(Boolean successful) {
			this.successful = successful;
		}
	}
	
	/**
	 * @return Returns the alias.
	 */
	public String getAlias() {
		return _alias;
	}
	/**
	 * @param alias The alias to set.
	 */
	public void setAlias(String alias) {
		this._alias = alias;
		_peer = new Peer (_alias, Peer.UNSPECIFIED_ADDRESS, Peer.UNSPECIFIED_PORT) ;
	}
	/**
	 * @return Returns the sleepInterval.
	 */
	public int getSleepInterval() {
		return _sleepInterval;
	}
	/**
	 * @param sleepInterval The sleepInterval to set.
	 */
	public void setSleepInterval(int sleepInterval) {
		this._sleepInterval = sleepInterval;
	}
	/**
	 * @return Returns the timeout.
	 */
	public int getTimeout() {
		return _timeout;
	}
	/**
	 * @param timeout The timeout to set.
	 */
	public void setTimeout(int timeout) {
		this._timeout = timeout;
	}
}
