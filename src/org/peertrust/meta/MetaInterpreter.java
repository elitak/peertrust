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

import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.event.AnswerEvent;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.exception.InferenceEngineException;
import org.peertrust.inference.*;
import org.peertrust.net.*;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.Query;
import org.peertrust.security.credentials.Credential;
import org.peertrust.security.credentials.CredentialStore;
import org.peertrust.strategy.*;

/**
 * $Id: MetaInterpreter.java,v 1.8 2005/02/15 17:36:34 dolmedilla Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/02/15 17:36:34 $
 * by $Author: dolmedilla $
 * @description
 */
public class MetaInterpreter implements Configurable, Runnable, PTEventListener
{
	private static Logger log = Logger.getLogger(MetaInterpreter.class);
	
//	private PTConfigurator _configurator ;

	private final int SLEEP_TIME = 200 ;
	private final String PEERNAME_PREDICATE = "peerName" ;
	
	private Queue _queue ;
	private InferenceEngine _inferenceEngine ;
	//private MetaInterpreterListener _metaInterpreterListener ;
	private CredentialStore _credStore ;
	private NetClient _netClient ;
	
	private EventDispatcher _dispatcher ;
	
	AbstractFactory _commChannelFactory ;

	private Thread _metaIThread = null ;
	
	private EntitiesTable _entities ;
	private String _alias ;
	
	private Peer _localPeer ;

	
	public MetaInterpreter ()
	{
		super() ;
		log.debug("$Id: MetaInterpreter.java,v 1.8 2005/02/15 17:36:34 dolmedilla Exp $");
	}
	
	public void init () throws ConfigurationException
	{
		String msg = null ;
		if (_dispatcher == null)
			msg = "There not exist an event dispatcher" ;
		else if (_queue == null)
			msg = "There not exist a queue" ;
		else if (_inferenceEngine == null)
			msg = "There not exist an inference engine" ;
		else if (_credStore == null)
			msg = "There not exist a credential store" ;
		else if (_commChannelFactory == null)
			msg = "There not exist a communication channel factory" ;
		else if (_alias == null)
			msg = "No alias has been defined for the peer" ;
		else if (_entities == null)
			msg = "There not exist a table of entities" ;
		
		if (msg != null)
		{
			log.error (msg) ;
			throw new ConfigurationException(msg) ;
		}
		
		log.info("PeerName = " + _alias) ;
		log.debug("(Init) PeerName = " + _alias) ;
		
//		_queue = (Queue) _configurator.createComponent(Vocabulary.Queue, true) ;

//		_inferenceEngine = (InferenceEngine) _configurator.createComponent(Vocabulary.InferenceEngine, true) ;
//		_inferenceEngine.init() ;
		try {
			_inferenceEngine.insert(PEERNAME_PREDICATE + "(" + _alias + ")") ;
		} catch (InferenceEngineException e) {
			log.error("Error:", e) ;
		}
		
//		_credStore = (CredentialStore) _configurator.createComponent(Vocabulary.CredentialStore, true) ;
		Vector credentials = _credStore.getCredentials() ;
		//log.debug("TMP number of elements " + credentials.size()) ;
		for (int i = 0 ; i < credentials.size() ; i++)
		{
			String stringCredential = ( (Credential) credentials.elementAt(i)).getStringRepresentation() ;
			log.debug("Adding credential string '" + stringCredential + "'") ;
			try {
				_inferenceEngine.insert(stringCredential) ;
			} catch (InferenceEngineException e1) {
				log.error("Error inserting credentials in the inference engine", e1) ;
			}
		}

//		AbstractFactory _commChannelFactory = (AbstractFactory) _configurator.createComponent(Vocabulary.CommunicationChannel, true) ;
		_netClient = _commChannelFactory.createNetClient() ;
		_localPeer = _commChannelFactory.getServerPeer(_alias) ;
		
		log.debug ("Local Peer: alias = " + _localPeer.getAlias() + " - host = " + _localPeer.getAddress() + " - port = " + _localPeer.getPort()) ;
		
//		_metaInterpreterListener = (MetaInterpreterListener) _configurator.createComponent(Vocabulary.MetaInterpreterListener, true) ; 
//		_metaInterpreterListener.setConfigurator(_configurator) ;
//		_metaInterpreterListener.setEngine(_inferenceEngine) ;
//		_metaInterpreterListener.setEntities(_entities) ;
//		_metaInterpreterListener.setQueue(_queue) ;
//		_metaInterpreterListener.setCommChannelFactory(_commChannelFactory) ;
//		_metaInterpreterListener.init() ;
//		
//		PTEngine.getDispatcher().register(this, QueryEvent.class) ;

		_dispatcher.register(this, QueryEvent.class) ;
		
		_metaIThread = new Thread(this, "MetaInterpreter") ;

		_metaIThread.start() ;
	}
	
	public void run()
	{
		log.debug("start") ;
		Thread myThread = Thread.currentThread();
		
		while (_metaIThread == myThread)
		{
			processQueue() ;	
		}	
	}
	
	public void stop()
	{
		_metaIThread = null ;
//		_metaInterpreterListener.stop() ;
	}
		
	/* (non-Javadoc)
	 * @see org.peertrust.event.PeerTrustEventListener#event(org.peertrust.event.PeerTrustEvent)
	 */
	public void event(PTEvent event) {
		if (event instanceof QueryEvent)
		{
			Query query = ( (QueryEvent) event).getQuery() ;
			//_entities.put(query.getOrigin().getAlias(), query.getOrigin()) ;
			Tree tree = new Tree (query.getGoal(), query.getSource(), query.getReqQueryId()) ;

			log.debug ("New query received from " + ( (query.getSource() == null) ? "null" : query.getSource().getAlias()) + ": " + query.getGoal()) ;
			_queue.add(tree) ;
		}
		else
			log.debug("Unknown event " + event.getClass().getName()) ;
	}
	
	void processQueue()
	{
		Tree selectedTree = _queue.pop() ;
		
		if (selectedTree == null)
		{
			// if there is no pending queries, we send the thread to sleep for some time and then we start again
			try
			{
				Thread.sleep(SLEEP_TIME) ;
			} catch (InterruptedException e) {
				log.error(_localPeer.getAlias() + ": interpreter waken up", e) ;
			}
			return ;
		}
					
		// if the query is a failure, we forward the failure to the requester
		if (selectedTree.getStatus() == Tree.FAILED)
		{
			Answer failure = new Answer(selectedTree.getGoal(), null, Answer.FAILURE, selectedTree.getReqQueryId(), _localPeer, selectedTree.getRequester()) ; 
			
			log.debug("Sending answer of " + failure.getGoal() + " to " + selectedTree.getRequester().getAlias()) ;
			sendMessage (failure) ;
			
			return ;
		} // if the query is already answered, then we send the answer to the requester
		else if ( ( (selectedTree.getStatus() == Tree.READY) && (selectedTree.getSubqueries().equals("[]")) ) ||
				  ( selectedTree.getStatus() == Tree.ANSWERED) )
		{
			// query completely answered: send answer remotely
			Tree pattern = new Tree (selectedTree.getRequester(), selectedTree.getReqQueryId()) ;
			
			int status ;
			
			if (_queue.search(pattern) == null)
				status = Answer.LAST_ANSWER ;
			else
				status = Answer.ANSWER ;
			
			Answer answer = new Answer (selectedTree.getGoal(), selectedTree.getProof(), status, selectedTree.getReqQueryId(), _localPeer, selectedTree.getRequester()) ;
			
			log.debug("Sending answer " + answer.getGoal() + " to " + selectedTree.getRequester().getAlias() + " with proof " + answer.getProof()) ;
			sendMessage(answer) ;
			return ;
		}

		// we create the appropriate logic query for the selected tree 
		LogicQuery logicQuery = new LogicQuery(selectedTree.getGoal(), selectedTree.getSubqueries(), selectedTree.getRequester().getAlias()) ;

		//processing the tree
	 	LogicAnswer [] results = null ;
		try {
			results = _inferenceEngine.processTree(logicQuery);
		} catch (InferenceEngineException e) {
			log.error ("Error processing tree", e) ;
		}
		// if there is no answers to the query we set the tree's status to FAILED and add it to the queue
	 	// 		so it will be send a failure to the requester in the next time it is selected
		if (results == null)
		{
			log.debug ("results == null") ;
			
			// query failed: send failure remotely if there is no other query pending
			Tree pattern = new Tree (selectedTree.getRequester(), selectedTree.getReqQueryId()) ;
			if (_queue.search(pattern) == null)
			{
				Tree failedTree = new Tree (selectedTree) ;
				failedTree.setStatus(Tree.FAILED) ;
				log.debug("Tree " + failedTree.getGoal() + " failed") ;
				_queue.add(failedTree) ;
			}
		}
		else
		{
			log.debug("results == " + results.length) ;
			
			// we check all the different answers received from the inference engine
			for (int i = 0 ; i < results.length ; i++)
			{
				String delegator = results[i].getDelegator() ; 
				// if we don't delegate
				if (delegator == null)
				{
					log.debug ("Delegator == null") ;
					
					// if the query is already finished we send an answer to the requester
					if (results[i].getSubgoals().equals("[]"))
					{
						log.debug ("subqueries == []") ;

						Tree answeredTree = new Tree(selectedTree) ;
						answeredTree.setGoal(results[i].getGoal()) ;
						answeredTree.appendProof(results[i].getProof()) ;
						answeredTree.setStatus(Tree.ANSWERED) ;
						
						log.debug("Tree " + answeredTree.getGoal() + " answered") ;
						_queue.add(answeredTree) ;
					}
					else // otherwise, we add the query to the queue
					{
						// continue processing the queue
						Tree newTree = new Tree(results[i].getGoal(), results[i].getSubgoals(), selectedTree.getProof(), Tree.READY, selectedTree.getRequester(), selectedTree.getReqQueryId()) ;
						newTree.appendProof(results[i].getProof()) ;
						_queue.add(newTree) ;
					}
				}
				else // we delegate
				{
					log.debug("Searching for delegator '" + delegator + "' in the entities table") ;
					Peer peerDelegator = (Peer) _entities.get(delegator) ;
					if (peerDelegator == null)
					{
						log.warn ("Delegator '" + delegator + "' is unknown") ;
						log.warn ("Ignoring query " + results[i].getGoalExpanded()) ;
					}
					else
					{
						log.debug ("Delegator == " + delegator) ;
						
						// look up the real address and port corresponding to the alias while creating the new tree

						Tree delegatedTree = new Tree (results[i].getGoal(), results[i].getSubgoals(),
								selectedTree.getProof(), Tree.WAITING, selectedTree.getRequester(),
								selectedTree.getReqQueryId(), peerDelegator, results[i].getGoalExpanded()) ;
						delegatedTree.appendProof(results[i].getProof()) ;
						
						_queue.add(delegatedTree) ;
						
						// and send query to remote delegator
						Query query = new Query(delegatedTree.getLastExpandedGoal(), _localPeer, delegatedTree.getDelegator(), delegatedTree.getId()) ;
											
						log.debug("Sending request " + query.getGoal() + " to " + delegatedTree.getDelegator().getAlias() + " from " + query.getSource().getAlias()) ;
						sendMessage(query) ;
					}
				}
			}
		}
	}
	
	private void sendMessage(Message message)
	{
		PTEvent ptevent = null ;
		Peer destination = message.getTarget() ;
		if (message instanceof Query)
		{
			log.debug("Send query to " + destination.getAddress() + ":" + destination.getPort() + " from " + message.getSource().getAlias()) ;
			ptevent = new QueryEvent (this, (Query)message) ;
		}
		else if (message instanceof Answer)
		{
			log.debug("Send answer to " + destination.getAddress() + ":" + destination.getPort() + " from " + message.getSource().getAlias()) ;
			ptevent = new AnswerEvent (this, (Answer)message) ;
		}
		else
			log.error("Unknown message type") ;
		
		if (destination.getAddress() != null)
			// sending an object
			_netClient.send(message, destination) ;

		if (ptevent != null)
			_dispatcher.event(ptevent) ;
	}
	
	public static void main(String[] args)
	{
		//java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		
//		try {
//			Configurator cf = new Configurator("trust.properties", args);
//			cf.setAppInfo("Atomated Trust Negotiation Peer");
//
//			MinervaProlog engine = new MinervaProlog(cf) ;
//			cf.register(engine) ;
//			
//			// register objects which need configuration
//			MetaInterpreter metaI = new MetaInterpreter (new FIFOQueue(), engine, cf) ;
//			cf.register(metaI) ;
//			
//			// configure objects
//			cf.finishConfig();
//			
//			metaI.run() ;
//
//		} catch (Exception e) {
//			e.printStackTrace() ;
//		}
	}
	
//	/**
//	 * @return Returns the _configurator.
//	 */
//	public PTConfigurator getConfigurator() {
//		return _configurator;
//	}
//	
//	/**
//	 * @param _configurator The _configurator to set.
//	 */
//	public void setConfigurator(PTConfigurator _configurator) {
//		this._configurator = _configurator;
//	}
	/**
	 * @return Returns the _alias.
	 */
	public String getPeerName() {
		return _alias;
	}
	/**
	 * @param _alias The _alias to set.
	 */
	public void setPeerName(String _alias) {
		this._alias = _alias.toLowerCase() ;
	}

	/**
	 * @param engine The _inferenceEngine to set.
	 */
	public void setInferenceEngine(InferenceEngine engine) {
		_inferenceEngine = engine;
	}
//	/**
//	 * @param interpreterListener The _metaInterpreterListener to set.
//	 */
//	public void setMetaInterpreterListener(MetaInterpreterListener interpreterListener) {
//		_metaInterpreterListener = interpreterListener;
//	}
	/**
	 * @param channelFactory The _commChannelFactory to set.
	 */
	public void setCommunicationChannelFactory(AbstractFactory channelFactory) {
		_commChannelFactory = channelFactory;
	}
	/**
	 * @param store The _credStore to set.
	 */
	public void setCredentialStore(CredentialStore store) {
		_credStore = store;
	}
	/**
	 * @param _queue The _queue to set.
	 */
	public void setQueue(Queue _queue) {
		this._queue = _queue;
	}
	/**
	 * @param _dispatcher The _dispatcher to set.
	 */
	public void setEventDispatcher(EventDispatcher _dispatcher) {
		this._dispatcher = _dispatcher;
	}
	/**
	 * @return Returns the _entities.
	 */
	public EntitiesTable getEntitiesTable() {
		return _entities;
	}
	/**
	 * @param _entities The _entities to set.
	 */
	public void setEntitiesTable(EntitiesTable _entities) {
		this._entities = _entities;
	}
}
