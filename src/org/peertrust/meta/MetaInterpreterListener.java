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
import org.peertrust.config.Configurable;
import org.peertrust.config.RunTimeOptions;
import org.peertrust.event.AnswerEvent;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.QueryEvent;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.exception.InferenceEngineException;
import org.peertrust.inference.InferenceEngine;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.Answer;
import org.peertrust.net.EntitiesTable;
import org.peertrust.net.Message;
import org.peertrust.net.NetServer;
import org.peertrust.net.Query;
import org.peertrust.strategy.Queue;

/**
 * $Id: MetaInterpreterListener.java,v 1.8 2005/02/22 08:47:23 dolmedilla Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/02/22 08:47:23 $
 * by $Author: dolmedilla $
 * @description
 */
public class MetaInterpreterListener implements Runnable, Configurable
{
	private static Logger log = Logger.getLogger(MetaInterpreterListener.class);
	
	//private PTConfigurator _configurator ;
	
	private Queue _queue ;
	private InferenceEngine _inferenceEngine ;
	private EntitiesTable _entities ;
	private NetServer _netServer ;
	private AbstractFactory _commChannelFactory ;
	private EventDispatcher _dispatcher ;
	private RunTimeOptions _runTimeOptions ;
	
	private Thread _metaIThread = null ;

	public MetaInterpreterListener ()
	{
		log.debug("$Id: MetaInterpreterListener.java,v 1.8 2005/02/22 08:47:23 dolmedilla Exp $");
	}
	
	public void init() throws ConfigurationException
	{
		String msg = null ;
		if (_dispatcher == null)
			msg = "There not exist an event dispatcher" ;
		else if (_queue == null)
			msg = "There not exist a queue" ;
		else if (_inferenceEngine == null)
			msg = "There not exist an inference engine" ;
		else if (_commChannelFactory == null)
			msg = "There not exist a communication channel factory" ;
		else if (_entities == null)
			msg = "There not exist a table of entities" ;
		else if (_runTimeOptions == null)
			msg = "There are no runtime options specified" ;
		
		if (msg != null)
		{
			msg += ". Please, check your configuration file" ;
			log.error (msg) ;
			throw new ConfigurationException(msg) ;
		}
		
		//AbstractFactory _commChannelFactory = (AbstractFactory) _configurator.createComponent(Vocabulary.CommunicationChannel, true) ;
		_netServer = _commChannelFactory.createNetServer() ;
	
		_metaIThread = new Thread(this, "MetaInterpreterListener") ;
		
		_metaIThread.start() ;
	}
	
	public void run()
	{
		log.debug("start") ;
		Thread myThread = Thread.currentThread();
		
//		System.runFinalizersOnExit(true) ;
		
		// closing the socket
//		Runtime.getRuntime().addShutdownHook (new Thread() {
//					public void run() {
//						try {  
//							System.out.println("closing opened socket") ;
//							if (serverSocket != null)
//							{							
//									serverSocket.close() ;
//									serverSocket = null ;
//							}
//						} catch (IOException e)
//						{
//							System.out.println ("Error closing the server socket") ;
//							e.printStackTrace();
//						}
//					}
//				});
		
		log.info("System ready") ;
		
		while (_metaIThread == myThread) {  
			Message message = _netServer.listen() ;
			if (message != null)
			{
				//System.out.println("Received: from peer "+tree.getId()+" goal "+tree.getGoal());
				processReceivedTree(message);
 			}
		}

	}
		
	public void stop()
	{
		_metaIThread = null ;
		log.debug("Stopping") ;
	}

	private void processReceivedTree (Message message)
	{	
		// check if the message is a query or an answer
		if (message instanceof Query)
		{
			Query query = (Query) message ;

			String alias = query.getSource().getAlias() ;
			if (_entities.get(alias) == null)
			{
				_entities.put(query.getSource().getAlias(), query.getSource()) ;
				log.debug("Added peer '" + alias + "' to the entities table") ;
			}
			
			_dispatcher.event(new QueryEvent(this, query)) ;
			
			//Tree tree = new Tree (query.getGoal(), query.getOrigin(), query.getReqQueryId()) ;
			
			log.debug ("New query received from " + query.getSource().getAlias() + ": " + query.getGoal()) ;
			//_queue.add(tree) ;

		}
		else if (message instanceof Answer)
		{
			Answer answer = (Answer) message ;
			
			_dispatcher.event(new AnswerEvent(this, answer)) ;
			
			// status might be answer or a failure
			switch (answer.getStatus())
			{
				// new answer
				case Answer.ANSWER:
					// it is an answer, we unify the answer with the the corresponding query
					Tree pattern = new Tree (answer.getReqQueryId()) ;
					Tree match = _queue.search(pattern) ;
					
					try {
						_inferenceEngine.unifyTree(match,answer.getGoal()) ;
					} catch (InferenceEngineException e) {
						log.error("Error unifying " + match.getLastExpandedGoal() + " and " + answer.getGoal(), e) ;
					}
					
					log.debug ("New answer received: " + answer.getGoal()) ;
					
					// we duplicate the tree (one still wait for new answers and the copy can continue being processed)
					Tree newTree = new Tree (match) ;
					newTree.setStatus(Tree.READY) ;
					
					// we add the proof from the answer
					newTree.appendProof(answer.getProof()) ;
					
					_queue.add(newTree) ;
					
					// we update the waiting query
					if (match.getStatus() == Tree.WAITING)
					{
						match.setStatus(Tree.ANSWERED_AND_WAITING) ;
						_queue.update(pattern, match) ;
					}
					break ;
					
				// last answer to a query
				case Answer.LAST_ANSWER:
					// to-do
					// CHECK IF THERE IS AN ERROR BECAUSE IT IS NOT IN THE QUEUE 
					Tree pattern2 = new Tree (answer.getReqQueryId()) ;

					// the query waiting is removed from the queue
					Tree match2 = _queue.remove(pattern2) ;
					
					// unification of the query goal with the answer
					try {
						_inferenceEngine.unifyTree(match2,answer.getGoal()) ;
					} catch (InferenceEngineException e) {
						log.error("Error unifying " + match2.getLastExpandedGoal() + " and " + answer.getGoal(), e) ;
					}
					
					log.debug("Last answer received: " + answer.getGoal()) ;

					// and we add the updated one
					Tree newTree2 = new Tree (match2) ;
					newTree2.setStatus(Tree.READY) ;
					
					// add the proof from the answer
					newTree2.appendProof(answer.getProof()) ;
					//queue.remove(pattern2) ;
					_queue.add(newTree2) ;
				
					break ;

				// failure
				case Answer.FAILURE:
					Tree pattern3 = new Tree (answer.getReqQueryId()) ;
					Tree match3 = _queue.search(pattern3) ;
					
					// if no answers were received so far, the query is updated to FAILED
					if (match3.getStatus() == Tree.WAITING)
					{
						log.debug("Failure received: " + answer.getGoal()) ;
						
						match3.setStatus(Tree.FAILED) ;
						_queue.update(pattern3, match3) ;
					}
					else // if at least one query was received, we just remove the pending query from the queue
						_queue.remove(match3) ;

					break ;				
			}
		}
		else
			log.error("Unknown message type") ;
			
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
	 * @return Returns the _engine.
	 */
	public InferenceEngine getEngine() {
		return _inferenceEngine;
	}
	/**
	 * @param _engine The _engine to set.
	 */
	public void setInferenceEngine(InferenceEngine _engine) {
		this._inferenceEngine = _engine;
	}
//	/**
//	 * @return Returns the _queue.
//	 */
//	public Queue getQueue() {
//		return _queue;
//	}
	/**
	 * @param _queue The _queue to set.
	 */
	public void setQueue(Queue _queue) {
		this._queue = _queue;
	}

//	/**
//	 * @return Returns the _commChannelFactory.
//	 */
//	public AbstractFactory getCommChannelFactory() {
//		return _commChannelFactory;
//	}
	/**
	 * @param channelFactory The _commChannelFactory to set.
	 */
	public void setCommunicationChannelFactory(AbstractFactory channelFactory) {
		_commChannelFactory = channelFactory;
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
	
	/**
	 * @return Returns the runTimeOptions.
	 */
	public RunTimeOptions getRunTimeOptions() {
		return _runTimeOptions;
	}
	/**
	 * @param runTimeOptions The runTimeOptions to set.
	 */
	public void setRunTimeOptions(RunTimeOptions runTimeOptions) {
		this._runTimeOptions = runTimeOptions;
	}
}
