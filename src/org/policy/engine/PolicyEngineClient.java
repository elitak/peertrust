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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
*/

package org.policy.engine;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.policy.GeneralPolicyEngineException;
import org.policy.communication.LocalPeerClient;
import org.policy.communication.LocalPeerEngine;
import org.policy.communication.Peer;
import org.policy.communication.Replyable;
import org.policy.communication.message.NegotiationRequest;
import org.policy.communication.message.NegotiationResponse;
import org.policy.communication.message.ServiceMessage;
import org.policy.communication.message.SingleStepNegotiationRequest;
import org.policy.communication.net.NetworkCommunicationFactory;
import org.policy.config.ConfigurationException;
import org.policy.config.Configurator;
import org.policy.engine.service.ServiceHandler;
import org.policy.model.ClientRequestId;
import org.policy.model.Explanation;
import org.policy.model.NegotiationInfo;
import org.policy.model.Notification;
import org.policy.model.Policy;
import org.policy.model.RequestIdentifier;
import org.policy.protune.model.Protune;
import org.policy.protune.model.ProtunePolicy;

/**
 * <p>
 * Wrapper for an application client, that is, the wrapper provides a simple API an application to communicate
 *     with the local policy engine. 
 * </p><p>
 * $Id: PolicyEngineClient.java,v 1.6 2007/02/28 08:40:14 dolmedilla Exp $
 * <br/>
 * Date: Feb 14, 2007
 * <br/>
 * Last changed: $Date: 2007/02/28 08:40:14 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class PolicyEngineClient implements Replyable
{
	private static Logger log = Logger.getLogger(PolicyEngineClient.class);

	static final String DEFAULT_ALIAS = "Client" ;
	static public int DEFAULT_TIMEOUT = 15000 ;
	static public int DEFAULT_SLEEP_INTERVAL = 100 ;
	static public long DEFAULT_FRESHNESS_TIME = 7200000 ;
	
/*	final String PREFIX_MESSAGE = "CLIENT: " ;
	final String INFO_MESSAGE = PREFIX_MESSAGE + "INFO: " ;
	final String ERROR_MESSAGE = PREFIX_MESSAGE + "ERROR: " ;
	final String WARN_MESSAGE = PREFIX_MESSAGE + "WARN: " ;
	final String DEBUG_MESSAGE = PREFIX_MESSAGE + "DEBUG: " ;*/

	Hashtable<ClientRequestId,CommunicationEntry> _requests = 
		new Hashtable<ClientRequestId,CommunicationEntry> () ;
	
	Configurator _config ;
	PolicyEngine _engine ;
	
	private LocalPeerClient _localPeer ;
	private LocalPeerEngine _enginePeer ;

	String _alias = DEFAULT_ALIAS ;
	int _timeout = DEFAULT_TIMEOUT ;
	int _sleepInterval = DEFAULT_SLEEP_INTERVAL ;
	
	// time for which previous requests are considered fresh (default 2 hours)
	long _freshnessTime = DEFAULT_FRESHNESS_TIME ;
	
	String [] _configurationArgs ;
	String [] _components ;
		
	public PolicyEngineClient (String [] configurationArgs, String [] components) throws ConfigurationException
	{
		super() ;
		
		_configurationArgs = configurationArgs ;
		_components = components ;
	}

	public void init() throws ConfigurationException
	{
		// TODO clean the HashTable from old requests
		
		_config = new Configurator() ;

		_config.startApp(_configurationArgs, _components) ;
		
		//_engine = (PolicyEngine) _config.getComponentByType(Vocabulary.PolicyEngine) ;
		_engine = (PolicyEngine) _config.getComponent(PolicyVocabulary.PolicyEngineObject.getLocalName()) ;
		
		checkInitializedEngine () ;
		
		_localPeer = new LocalPeerClient (this.getAlias(), this) ;
		_enginePeer = _engine.getLocalPeerServer() ;
		
		// TODO Whole client initialization: load policies, credentials, etc...
	}

	private void checkInitializedEngine () throws ConfigurationException
	{
		if (_engine == null)
		{
			String msg = "No policy engine has been specified." ;
			
			log.error (msg) ;
			throw new ConfigurationException(msg) ;
		}
	}
	
	public void stop() throws ConfigurationException
	{
		checkInitializedEngine () ;
				
		// TODO Create the EngineMgmtRequest message to stop the engine and send it  
	}

	public void setCommunicationChannel(NetworkCommunicationFactory factory)
	{
		_engine.setCommunicationChannel(factory) ;
	}

	// Querying for a quick answer (no negotiation but a single step)
	//
	// Policy : the query to be answered
	// ServiceHandler: specifies which framework should process the request
	// Peer : the peer to which the query is sent
	//     Peer = LocalPeerEngine : the query is local
	//     Peer = any other peer : the query is forwarded to the appropriate peer 
	public ClientRequestId sendSimpleNegotiationRequest (Policy query, ServiceHandler handler, Peer peer)
		throws CommunicationEntryNotFound, ConfigurationException, EngineInternalException
	{
		checkInitializedEngine () ;
		
		Policy [] policyArray = { query } ;
		NegotiationInfo negInfo = new NegotiationInfo (policyArray) ;
		
		ClientRequestId requestId = new ClientRequestId() ;
		
		// TODO check that peer is not localpeerclient
		
		SingleStepNegotiationRequest negRequest = 
			new SingleStepNegotiationRequest (requestId, _localPeer, peer, handler, negInfo) ;
		
		CommunicationEntry entry = new NegotiationCommunicationEntry (requestId, negRequest) ;
		sendAndWaitForAnswer(entry) ;
				
		return requestId ;
	}

	// Querying for the result of a negotiation (which the engine will perform with peer)
	//
	// Policy : the query to be answered
	// ServiceHandler: specifies which framework should process the request
	// Peer : the peer to which the query is sent. The negotiation is performed with this peer
	
	public ClientRequestId sendNegotiationRequest (Policy query, ServiceHandler handler, Peer peer) 
		throws CommunicationEntryNotFound, ConfigurationException, EngineInternalException
	{
		checkInitializedEngine () ;
		
		Policy [] policyArray = { query } ;
		NegotiationInfo negInfo = new NegotiationInfo (policyArray) ;
		
		ClientRequestId requestId = new ClientRequestId() ;
		
//		 TODO check that peer is not localpeerclient or localpeerengine
		
		NegotiationRequest negRequest = new NegotiationRequest (requestId, _localPeer, peer, handler,  negInfo) ;
		
		CommunicationEntry entry = new NegotiationCommunicationEntry (requestId, negRequest) ;
		
		sendAndWaitForAnswer(entry) ;
		
		return requestId ;		
	}

	public synchronized RequestIdentifier sendAndWaitForAnswer (CommunicationEntry entryRequest)
		throws CommunicationEntryNotFound, EngineInternalException
	{
		ClientRequestId clientReqId = entryRequest.getClientRequestIdentifier() ;
		
		_requests.put(clientReqId, entryRequest) ;
		
		RequestIdentifier id = _engine.sendRequest (entryRequest.getRequest()) ;
		
		long time = System.currentTimeMillis() ;
		long newTime ;
		
		while (entryRequest.isResponseReceived() == false)
		{
			newTime = System.currentTimeMillis() ;
			
			if (newTime - time < _timeout )
			{
				String msg = "Timeout for request " + id + " - " + entryRequest.getRequest().toString() ;
				log.error(msg) ;
				throw new EngineInternalException (msg) ;
			}
			
            try
			{
				wait(_sleepInterval);
			} catch (InterruptedException e)
			{}
		}		
			
		return id ;
	}
	
	public synchronized void reply (ClientRequestId identifier, ServiceMessage response)
	{
		CommunicationEntry entry;
		try
		{
			entry = checkCommunicationEntry (identifier);
		} catch (CommunicationEntryNotFound e)
		{
			log.error(e.getMessage()) ;
			return ;
		}
		
		entry.setResponse(response) ;
		
		notifyAll();
	}

	public CommunicationEntry checkCommunicationEntry(ClientRequestId identifier) 
		throws CommunicationEntryNotFound 
	{
		CommunicationEntry entry = _requests.get(identifier) ;
		
		if (entry == null)
			throw new CommunicationEntryNotFound ("The identifier " + 
					identifier + " is not found in the list of previous requests.") ;
		
		return entry ;
	}
	
	public void cleanOldRequests ()
	{
		// TODO remove old entries from the hashtable based on the freshness property
	}
	
	public boolean isSuccessful (ClientRequestId id) throws CommunicationEntryNotFound
	{
		CommunicationEntry entry = checkCommunicationEntry (id);
		
		return entry.isSuccessful() ;
	}
	
	public boolean isResponseReceived (ClientRequestId id) throws CommunicationEntryNotFound
	{
		CommunicationEntry entry = checkCommunicationEntry (id);
		
		return entry.isResponseReceived() ;	
	}
	
	public boolean isNegotiationFinished (ClientRequestId id) 
		throws FunctionNotSupported, CommunicationEntryNotFound
	{
		CommunicationEntry entry = checkCommunicationEntry (id);
		
		if ((entry instanceof NegotiationCommunicationEntry) == false )
			throw new FunctionNotSupported ("The function requested is only available for negotiations") ;
		
		return ((NegotiationCommunicationEntry) entry).isNegotiationFinished() ;
	}
	
	public Explanation getExplanation (ClientRequestId id, int type) 
		throws FunctionNotSupported, CommunicationEntryNotFound
	{
		CommunicationEntry entry = checkCommunicationEntry (id);
		
		if ((entry instanceof NegotiationCommunicationEntry) == false )
			throw new FunctionNotSupported ("The function requested is only available for negotiations") ;
		
		return ((NegotiationCommunicationEntry) entry).getExplanation(type) ;
	}
	
	public Notification[] getNotifications (ClientRequestId id) 
		throws FunctionNotSupported, CommunicationEntryNotFound
	{
		CommunicationEntry entry = checkCommunicationEntry (id);
		
		if ((entry instanceof NegotiationCommunicationEntry) == false )
			throw new FunctionNotSupported ("The function requested is only available for negotiations") ;
		
		return ((NegotiationCommunicationEntry) entry).getNotifications() ;
	}
	
	public static void main(String[] args) 
		throws ConfigurationException, CommunicationEntryNotFound, FunctionNotSupported, EngineInternalException
	{	
		String USAGE_MESSAGE = "Usage: program <configFile> <queryString>" ;
		int USAGE_RETURN_ERROR = 1 ; 
		
		//String [] defaultComponents = { Vocabulary.PolicyEngine.toString() } ;
		String [] defaultComponents = { PolicyVocabulary.PolicyEngineObject.toString() } ;
		
		String newArgs[] = new String[1] ;
		String query = null ;
		if (args.length < 2)
		{
			System.out.println (USAGE_MESSAGE) ;
			System.exit(USAGE_RETURN_ERROR) ;
		}
		else
		{
			if (args.length > 2)
			{
				System.out.println(USAGE_MESSAGE) ;
				System.exit(USAGE_RETURN_ERROR) ;
			}
			else
			{
				newArgs[0] = args[0] ;
				query = args[1] ;
			}
		}
	
		//	java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		
		PolicyEngineClient client = new PolicyEngineClient (newArgs, defaultComponents) ;
		client.init() ;

		ClientRequestId id = client.sendSimpleNegotiationRequest(new ProtunePolicy(query), 
				Protune.getServiceHandler(), client.getEnginePeer()) ;
		
		System.out.println("Results: ") ;
		System.out.println("\tRequest successful: " + client.isSuccessful(id)) ;
		System.out.println("\tResponse received: " + client.isResponseReceived(id)) ;
		System.out.println("\tNegotiation successful: " + client.isNegotiationFinished(id)) ;
		System.out.println("\tExplanation: " + client.getExplanation(id, Explanation.WHY_NOT_EXPLANATION)) ;
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
		this._alias = alias.toLowerCase();
		_localPeer = new LocalPeerClient (alias, this) ;
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
	
	/**
	 * @return Returns the _enginePeer.
	 */
	public LocalPeerEngine getEnginePeer()
	{
		return _enginePeer;
	}

	public abstract class CommunicationEntry
	{
		long _timestamp ;
		ClientRequestId _id ;
		ServiceMessage _request ;
		ServiceMessage _response ;
		
		public CommunicationEntry (ClientRequestId id)
		{
			_timestamp = System.currentTimeMillis() ;
			_id = id ;
		}
		
		public boolean isSuccessful ()
		{
			// TODO is negotiation successful?
			
			return true ;
		}

		public boolean isResponseReceived ()
		{	
			return _response != null ? true : false ;
		}
		
		/**
		 * @return Returns the request.
		 */
		public ServiceMessage getRequest()
		{
			return _request;
		}

		/**
		 * @return Returns the response.
		 */
		public ServiceMessage getResponse()
		{
			return _response;
		}
		
		public void setResponse(ServiceMessage response)
		{
			_response = response ;
		}

		/**
		 * @return Returns the _id.
		 */
		public ClientRequestId getClientRequestIdentifier()
		{
			return _id;
		}
	}
	
	public class NegotiationCommunicationEntry extends CommunicationEntry
	{
		NegotiationRequest _request ;
		NegotiationResponse _response ;
		
		public NegotiationCommunicationEntry (ClientRequestId id, NegotiationRequest request)
		{
			super(id) ;
			_request = request ;
		}
		
		public boolean isNegotiationFinished ()
		{
			// TODO is negotiation finished?
			
			return true ;
		}
		
		public Explanation getExplanation (int type)
		{
			// TODO add explanations
			
			return null ;
		}
		
		public Notification[] getNotifications ()
		{
			// TODO extract and show the notifications
			
			return null ;
		}
		
		/**
		 * @return Returns the request.
		 */
		public NegotiationRequest getRequest()
		{
			return _request;
		}

		/**
		 * @return Returns the response.
		 */
		public NegotiationResponse getResponse()
		{
			return _response;
		}
		
		public void setResponse(NegotiationResponse response)
		{
			_response = response ;
		}
	}
	
	public class CommunicationEntryNotFound extends GeneralPolicyEngineException {

		/**
		 * @param arg0
		 */
		public CommunicationEntryNotFound(String arg0) {
			super(arg0);
		}
	}
	
	public class FunctionNotSupported extends GeneralPolicyEngineException {

		/**
		 * @param arg0
		 */
		public FunctionNotSupported(String arg0) {
			super(arg0);
		}
	}
}
