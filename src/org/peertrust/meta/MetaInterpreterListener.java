package org.peertrust.meta;

import javax.net.ssl.*;
import javax.security.cert.* ;

import org.apache.log4j.Logger;
import org.peertrust.inference.InferenceEngine;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.Query;
import org.peertrust.net.ssl.SecureServerSocket;
import org.peertrust.strategy.*;

import java.io.*;
import java.net.* ;
import java.util.Hashtable ;
import net.jxta.edutella.util.Configurator ;

/**
 * $Id: MetaInterpreterListener.java,v 1.1 2004/07/01 23:35:58 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:35:58 $
 * @description
 */
public class MetaInterpreterListener implements Runnable
{
	private final int TIMEOUT = 1000 ;

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	private Queue queue ;
	private InferenceEngine engine ;
	private Thread metaIThread = null ;
	private SSLServerSocket serverSocket;
	private Hashtable entities ;
	private Configurator config ;
	
	private static Logger log = Logger.getLogger(MetaInterpreterListener.class);
	
	public MetaInterpreterListener (Queue queue, InferenceEngine engine, Hashtable entities, Configurator config)
	{
		log.debug("Created") ;
		
		this.queue = queue ;
		this.engine = engine ;
		this.entities = entities ;
		this.config = config ;
		metaIThread = new Thread(this, "MetaInterpreterListener") ;
		metaIThread.start() ;
	}
	 
	public void run()
	{
		Thread myThread = Thread.currentThread();
		
		SecureServerSocket sss;

		sss = new SecureServerSocket(Integer.parseInt(config.getValue(MetaInterpreter.SERVER_PORT_TAG)),
															config.getValue(MetaInterpreter.BASE_FOLDER_TAG)  + config.getValue(MetaInterpreter.KEYSTORE_FILE_TAG),
															config.getValue(MetaInterpreter.KEY_PASSWORD_TAG),
															config.getValue(MetaInterpreter.STORE_PASSWORD_TAG));
		serverSocket = sss.getServerSocket();

		try
		{
			serverSocket.setSoTimeout(TIMEOUT) ;
		} catch (SocketException e1)
		{
			e1.printStackTrace();
		}
		
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
		
		while (metaIThread == myThread) {  
			try
			{	
				SSLSocket recSocket;
				try
				{
					//System.out.println ("Waiting for connections at port " + Integer.parseInt(config.getValue(SERVER_PORT_TAG))) ;
					recSocket = (SSLSocket) serverSocket.accept();
					log.debug("Socket connection received") ;

					try {
				 
						DataInputStream    in     = new DataInputStream(recSocket.getInputStream());
						ObjectInputStream  objIn  = new ObjectInputStream(in);

						Message message = (Message) objIn.readObject() ;
						
						log.debug("Message received from " + message.getOrigin().getAlias());
						
						String DN = getCertificateName (recSocket.getSession().getPeerCertificateChain()) ;
						if (validAuthentication(DN, message.getOrigin().getAlias()) == false)
						{
							log.error("Certificate DN (" + DN + ") and alias (" + message.getOrigin().getAlias() + ") mismatch") ;
						}
						else
						{
							//System.out.println("Received: from peer "+tree.getId()+" goal "+tree.getGoal());
							processReceivedTree(message);
						}
					}
					catch (ClassNotFoundException cnfe) {
						log.error( "Class Not Found", cnfe);
					}
					catch(IOException ie ) {
						log.error( "IOException2", ie);
					}
				}
				catch (SocketTimeoutException te)
				{
					// ignore
				} catch (IOException e)	{
					log.error ("IOException", e) ;
				}
			}
 			catch(Exception e)
 			{
 				log.error("Exception", e) ;
 			}
		}
		log.info("Finalizing") ;
		if (serverSocket != null)
		{
			try
			{
				serverSocket.close() ;
			} catch (IOException e)
			{
				log.error("Closing the server socket") ;
			}
			serverSocket = null ;
		}
	}

	private boolean validAuthentication(String name1, String name2)
	{
		return name1.toLowerCase().equals(name2.toLowerCase()) ;
	}
	
	private String getCertificateName (Certificate [] chain)
	{
		X509Certificate [] certs = (X509Certificate []) chain ;
		String name ;

//		for (int i = 0 ; i < certs.length ; i++)
//			{
		name = certs[0].getSubjectDN().getName() ;
		int index = name.indexOf("CN=") + 3 ;
		int index2 = name.indexOf(",", index) ;
		String dn =name.substring(index, index2) ; 
		System.err.println("Cert to " + dn + " valid till " + certs[0].getNotAfter()) ;
//			}
		return dn ;
	}
	
	protected void finalize ()
	{
		if (serverSocket != null)
		{
			try {
				serverSocket.close() ;
				serverSocket = null ;
			}
			catch (IOException e)
			{
				e.printStackTrace() ;
			}
		}
	}
	
	public void stop()
	{
		metaIThread = null ;
		log.debug("Stopping") ;
	}

	private void processReceivedTree (Message message)
	{	
		// check if the message is a query or an answer
		if (message instanceof Query)
		{
			Query query = (Query) message ;

			entities.put(query.getOrigin().getAlias(), query.getOrigin()) ;
			Tree tree = new Tree (query.getGoal(), query.getOrigin(), query.getReqQueryId()) ;
			
			log.debug ("New query received from " + query.getOrigin().getAlias() + ": " + query.getGoal()) ;
			queue.add(tree) ;

		}
		else if (message instanceof Answer)
		{
			Answer answer = (Answer) message ;
			// status might be answer or a failure
			switch (answer.getStatus())
			{
				// new answer
				case Answer.ANSWER:
					// it is an answer, we unify the answer with the the corresponding query
					Tree pattern = new Tree (answer.getReqQueryId()) ;
					Tree match = queue.search(pattern) ;
					
					engine.unifyTree(match,answer.getGoal()) ;
					
					log.debug ("New answer received: " + answer.getGoal()) ;
					
					// we duplicate the tree (one still wait for new answers and the copy can continue being processed)
					Tree newTree = new Tree (match) ;
					newTree.setStatus(Tree.READY) ;
					
					// we add the proof from the answer
					newTree.appendProof(answer.getProof()) ;
					
					queue.add(newTree) ;
					
					// we update the waiting query
					if (match.getStatus() == Tree.WAITING)
					{
						match.setStatus(Tree.ANSWERED_AND_WAITING) ;
						queue.update(pattern, match) ;
					}
					break ;
					
				// last answer to a query
				case Answer.LAST_ANSWER:
					Tree pattern2 = new Tree (answer.getReqQueryId()) ;

					// the query waiting is removed from the queue
					Tree match2 = queue.remove(pattern2) ;
					
					// unification of the query goal with the answer
					engine.unifyTree(match2,answer.getGoal()) ;
					
					log.debug("Last answer received: " + answer.getGoal()) ;

					// and we add the updated one
					Tree newTree2 = new Tree (match2) ;
					newTree2.setStatus(Tree.READY) ;
					
					// add the proof from the answer
					newTree2.appendProof(answer.getProof()) ;
					//queue.remove(pattern2) ;
					queue.add(newTree2) ;
				
					break ;

				// failure
				case Answer.FAILURE:
					Tree pattern3 = new Tree (answer.getReqQueryId()) ;
					Tree match3 = queue.search(pattern3) ;
					
					// if no answers were received so far, the query is updated to FAILED
					if (match3.getStatus() == Tree.WAITING)
					{
						log.debug("Failure received: " + answer.getGoal()) ;
						
						match3.setStatus(Tree.FAILED) ;
						queue.update(pattern3, match3) ;
					}
					else // if at least one query was received, we just remove the pending query from the queue
						queue.remove(match3) ;

					break ;				
			}
		}
		else
			log.error("Unknown message type") ;
			
	}
	
}
