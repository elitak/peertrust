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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable ;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.Configurable;
import org.peertrust.PeertrustConfigurator;
import org.peertrust.PeertrustEngine;
import org.peertrust.Vocabulary;
import org.peertrust.event.PeerTrustEvent;
import org.peertrust.event.PeerTrustEventListener;
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
 * $Id: MetaInterpreter.java,v 1.3 2004/10/20 19:26:40 dolmedilla Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/10/20 19:26:40 $
 * by $Author: dolmedilla $
 * @description
 */
public class MetaInterpreter implements Configurable, Runnable, PeerTrustEventListener
{
	private static Logger log = Logger.getLogger(MetaInterpreter.class);
	
	private PeertrustConfigurator _configurator ;

	private final int SLEEP_TIME = 200 ;
	private final String PEERNAME_PREDICATE = "peerName" ;
	
	private Queue _queue ;
	private InferenceEngine _engine ;
	private MetaInterpreterListener _metaIListener ;
	private CredentialStore _credStore ;
	private NetClient _netClient ;
	
	private String _entitiesFile ;

	private Thread _metaIThread = null ;
	private Hashtable _entities = new Hashtable() ;
	private String _alias ;
	
	private Peer _localPeer ;

	
	public MetaInterpreter ()
	{
		super() ;
		log.debug("$Id: MetaInterpreter.java,v 1.3 2004/10/20 19:26:40 dolmedilla Exp $");
	}
	
	public void init () throws ConfigurationException
	{
		log.info("PeerName = " + _alias) ;
		log.debug("(Init) PeerName = " + _alias + " - EntitiesFile = " + _entitiesFile) ;
		
		_queue = (Queue) _configurator.createComponent(Vocabulary.Queue, true) ;

		_engine = (InferenceEngine) _configurator.createComponent(Vocabulary.InferenceEngine, true) ;
		_engine.init() ;
		try {
			_engine.setDebugMode(true) ;
			_engine.insert(PEERNAME_PREDICATE + "(" + _alias + ")") ;
		} catch (InferenceEngineException e) {
			log.error("Error:", e) ;
		}

		_credStore = (CredentialStore) _configurator.createComponent(Vocabulary.CredentialStore, true) ;
		Vector credentials = _credStore.getCredentials() ;
		//log.debug("TMP number of elements " + credentials.size()) ;
		for (int i = 0 ; i < credentials.size() ; i++)
		{
			String stringCredential = ( (Credential) credentials.elementAt(i)).getStringRepresentation() ;
			log.debug("Adding credential string '" + stringCredential + "'") ;
			try {
				_engine.insert(stringCredential) ;
			} catch (InferenceEngineException e1) {
				log.error("Error inserting credentials in the inference engine", e1) ;
			}
		}

		AbstractFactory _commChannelFactory = (AbstractFactory) _configurator.createComponent(Vocabulary.CommunicationChannel, true) ;
		_netClient = _commChannelFactory.createNetClient() ;
		_localPeer = _commChannelFactory.getServerPeer(_alias) ;
		
		log.debug ("Local Peer: alias = " + _localPeer.getAlias() + " - host = " + _localPeer.getAddress() + " - port = " + _localPeer.getPort()) ;
		
		_entities = readEntities(_entitiesFile) ;
		
		_metaIListener = (MetaInterpreterListener) _configurator.createComponent(Vocabulary.MetaInterpreterListener, true) ; 
		_metaIListener.setConfigurator(_configurator) ;
		_metaIListener.setEngine(_engine) ;
		_metaIListener.setEntities(_entities) ;
		_metaIListener.setQueue(_queue) ;
		_metaIListener.setCommChannelFactory(_commChannelFactory) ;
		_metaIListener.init() ;
		
		PeertrustEngine.getDispatcher().register(this, QueryEvent.class) ;

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
		_metaIListener.stop() ;
	}
		
	/* (non-Javadoc)
	 * @see org.peertrust.event.PeerTrustEventListener#event(org.peertrust.event.PeerTrustEvent)
	 */
	public void event(PeerTrustEvent event) {
		if (event instanceof QueryEvent)
		{
			Query query = ( (QueryEvent) event).getQuery() ;
			//_entities.put(query.getOrigin().getAlias(), query.getOrigin()) ;
			Tree tree = new Tree (query.getGoal(), query.getOrigin(), query.getReqQueryId()) ;
			
			log.debug ("New query received from " + ( (query.getOrigin() == null) ? "null" : query.getOrigin().getAlias()) + ": " + query.getGoal()) ;
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
			Answer failure = new Answer(selectedTree.getGoal(), null, Answer.FAILURE, selectedTree.getReqQueryId(), _localPeer) ; 
			
			log.debug("Sending answer of " + failure.getGoal() + " to " + selectedTree.getRequester().getAlias()) ;
			sendMessage (failure, selectedTree.getRequester()) ;
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
			
			Answer answer = new Answer (selectedTree.getGoal(), selectedTree.getProof(), status, selectedTree.getReqQueryId(), _localPeer) ;
			
			log.debug("Sending answer " + answer.getGoal() + " to " + selectedTree.getRequester().getAlias() + " with proof " + answer.getProof()) ;
			sendMessage(answer, selectedTree.getRequester()) ;
			return ;
		}

		// we create the appropriate logic query for the selected tree 
		LogicQuery logicQuery = new LogicQuery(selectedTree.getGoal(), selectedTree.getSubqueries(), selectedTree.getRequester().getAlias()) ;

		//processing the tree
	 	LogicAnswer [] results = null ;
		try {
			results = _engine.processTree(logicQuery);
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
					// look up the real address and port corresponding to the alias while creating the new tree
					Tree delegatedTree = new Tree (results[i].getGoal(), results[i].getSubgoals(),
							selectedTree.getProof(), Tree.WAITING, selectedTree.getRequester(),
							selectedTree.getReqQueryId(), (Peer) _entities.get(delegator), results[i].getGoalExpanded()) ;
					delegatedTree.appendProof(results[i].getProof()) ;

					log.debug ("Delegator == " + delegator) ;
					
					_queue.add(delegatedTree) ;
					
					// and send query to remote delegator
					Query query = new Query(delegatedTree.getLastExpandedGoal(), _localPeer, delegatedTree.getId()) ;
										
					log.debug("Sending request " + query.getGoal() + " to " + delegatedTree.getDelegator().getAlias()) ;
					sendMessage(query, delegatedTree.getDelegator()) ;
				}
			}
		}
	}
	
	private void sendMessage(Message message, Peer destination)
	{
		if (message instanceof Query)
			log.debug("Send query to " + destination.getAddress() + ":" + destination.getPort() + " from " + message.getOrigin().getAlias()) ;
		else if (message instanceof Answer)
			log.debug("Send answer to " + destination.getAddress() + ":" + destination.getPort() + " from " + message.getOrigin().getAlias()) ;
		else
			log.error("Unknown message type") ;
		
		// sending an object
		_netClient.send(message, destination) ;
	}

	private Hashtable readEntities (String fileName) throws ConfigurationException
	{
		Hashtable entities = new Hashtable() ;
		
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(fileName)); // Open the file.
		}
		catch(FileNotFoundException e) { // The file may not exist.
			log.error("Entities file not found: " + fileName, e) ;
			throw new ConfigurationException (e) ;
		}
		// Now we read the file, line by line, echoing each line to
		// the terminal.
		try {
			String line;
			String [] attributes ;
			while( (line = input.readLine()) != null ) {
				if (line.charAt(0) != '%')
				{
					attributes = line.split("\t") ;
					entities.put(attributes[0], new Peer (attributes[0], attributes[1], Integer.parseInt(attributes[2]))) ;
				}
			 }
		}
		catch(IOException x) {
			x.printStackTrace();
			throw new ConfigurationException (x) ;
		}
		return entities ;
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
	
	/**
	 * @return Returns the _configurator.
	 */
	public PeertrustConfigurator getConfigurator() {
		return _configurator;
	}
	
	/**
	 * @param _configurator The _configurator to set.
	 */
	public void setConfigurator(PeertrustConfigurator _configurator) {
		this._configurator = _configurator;
	}
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
	 * @return Returns the _entitiesFile.
	 */
	public String getEntitiesFile() {
		return _entitiesFile;
	}
	/**
	 * @param file The _entitiesFile to set.
	 */
	public void setEntitiesFile(String file) {
		_entitiesFile = file;
	}
}
