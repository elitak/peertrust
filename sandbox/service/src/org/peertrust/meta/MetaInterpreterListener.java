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

import java.util.Hashtable;

import javax.net.ssl.SSLServerSocket;

import net.jxta.edutella.util.Configurator;

import org.apache.log4j.Logger;
import org.peertrust.inference.InferenceEngine;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.NetServer;
import org.peertrust.net.Query;
import org.peertrust.strategy.Queue;

/**
 * $Id: MetaInterpreterListener.java,v 1.1 2004/08/07 12:51:56 magik Exp $
 * 
 * @author olmedilla
 * @date 05-Dec-2003 Last changed $Date: 2004/08/07 12:51:56 $ by $Author:
 *       dolmedilla $
 * @description
 */
public class MetaInterpreterListener implements Runnable {
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    private Queue queue;

    private InferenceEngine engine;

    private Thread metaIThread = null;

    private SSLServerSocket serverSocket;

    private Hashtable entities;

    private Configurator config;

    private AbstractFactory factory;

    private static Logger log = Logger.getLogger(MetaInterpreterListener.class);

    public MetaInterpreterListener(Queue queue, InferenceEngine engine,
            Hashtable entities, Configurator config, AbstractFactory factory) {
        log.debug("Created");

        this.queue = queue;
        this.engine = engine;
        this.entities = entities;
        this.config = config;
        this.factory = factory;
        metaIThread = new Thread(this, "MetaInterpreterListener");
        metaIThread.start();
    }

    public void run() {
        Thread myThread = Thread.currentThread();

        NetServer netServer = factory.createNetServer(config);

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

        log.info("System ready");

        while (metaIThread == myThread) {
            Message message = netServer.listen();
            if (message != null) {
                //System.out.println("Received: from peer "+tree.getId()+" goal
                // "+tree.getGoal());
                processReceivedTree(message);
            }
        }

    }

    public void stop() {
        metaIThread = null;
        log.debug("Stopping");
    }

    private void processReceivedTree(Message message) {
        // check if the message is a query or an answer
        if (message instanceof Query) {
            Query query = (Query) message;

            entities.put(query.getOrigin().getAlias(), query.getOrigin());
            Tree tree = new Tree(query.getGoal(), query.getOrigin(), query
                    .getReqQueryId());

            log.debug("New query received from " + query.getOrigin().getAlias()
                    + ": " + query.getGoal());
            queue.add(tree);

        } else if (message instanceof Answer) {
            Answer answer = (Answer) message;
            // status might be answer or a failure
            switch (answer.getStatus()) {
            // new answer
            case Answer.ANSWER:
                // it is an answer, we unify the answer with the the
                // corresponding query
                Tree pattern = new Tree(answer.getReqQueryId());
                Tree match = queue.search(pattern);

                engine.unifyTree(match, answer.getGoal());

                log.debug("New answer received: " + answer.getGoal());

                // we duplicate the tree (one still wait for new answers and the
                // copy can continue being processed)
                Tree newTree = new Tree(match);
                newTree.setStatus(Tree.READY);

                // we add the proof from the answer
                newTree.appendProof(answer.getProof());

                queue.add(newTree);

                // we update the waiting query
                if (match.getStatus() == Tree.WAITING) {
                    match.setStatus(Tree.ANSWERED_AND_WAITING);
                    queue.update(pattern, match);
                }
                break;

            // last answer to a query
            case Answer.LAST_ANSWER:
                Tree pattern2 = new Tree(answer.getReqQueryId());

                // the query waiting is removed from the queue
                Tree match2 = queue.remove(pattern2);

                // unification of the query goal with the answer
                engine.unifyTree(match2, answer.getGoal());

                log.debug("Last answer received: " + answer.getGoal());

                // and we add the updated one
                Tree newTree2 = new Tree(match2);
                newTree2.setStatus(Tree.READY);

                // add the proof from the answer
                newTree2.appendProof(answer.getProof());
                //queue.remove(pattern2) ;
                queue.add(newTree2);

                break;

            // failure
            case Answer.FAILURE:
                Tree pattern3 = new Tree(answer.getReqQueryId());
                Tree match3 = queue.search(pattern3);

                // if no answers were received so far, the query is updated to
                // FAILED
                if (match3.getStatus() == Tree.WAITING) {
                    log.debug("Failure received: " + answer.getGoal());

                    match3.setStatus(Tree.FAILED);
                    queue.update(pattern3, match3);
                } else
                    // if at least one query was received, we just remove the
                    // pending query from the queue
                    queue.remove(match3);

                break;
            }
        } else
            log.error("Unknown message type");

    }

}