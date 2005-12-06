/*
 * Created on 14.04.2005
 */
package org.peertrust.demo.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.peertrust.demo.common.Messenger;
import org.peertrust.demo.common.Utils;
import org.peertrust.net.*;

/**
 * This servlet is reponsible for the peertrust communication.
 * Its provides the mechanism to send and listen to the peertrust messages.
 * A call of a specific functionality (send or receive) is done using 
 * a parameter, named "action" in the http request:
 * <ul>
 * 	<li/> 	if action="send", the post java object will be send throw the messenger
 * 			to the targeted peer.
 *  
 * 	<li/>  	if action="receive", the post data must be an object of type Peer;
 * 			and it will be use to listen at the messenger for messages.
 * 			if no message is available at that time the call will block. 			
 * </ul>  
 * @author Patrice Congo (token77)
 */
public class PeerTrustCommunicationServlet 	extends HttpServlet 
{
	/** 
	 * key used to logged a listening peer in the session context
	 */
	final static public  String PEER_NAME_KEY="finalDestination"; 
	
	/**
	 * message logger.
	 */
	private Logger logger;	
	
	/**
	 * The messenger used for sending and receiving the message.
	 */
	private Messenger messenger;
	
	/**
	 * Inits the servlet. 
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init(ServletConfig config) throws ServletException {
		NegotiationObjects negoObjects=
			NegotiationObjects.createAndAddForAppContext(config);
		messenger=negoObjects.getMessenger();
		logger=Utils.getLogger(this.getClass().getName());
		return;		
	}
	
	
	/**
	 * Responds a not supported to http get response.
	 *  
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(
					HttpServletRequest req, 
					HttpServletResponse resp)
							throws 	ServletException, 
									IOException 
	{
		sendObject(resp,"GET NOT SUPPORTED");
	}
	
	
	/**
	 * Serves the peertrust communication request.
	 * It gets the first the action parameter of the request and
	 * does the requested action:
	 * <ul>
	 * 	<li/> 	if action="send", the post java object will be send throw the messenger
	 * 			to the targeted peer.
	 *  
	 * 	<li/>  	if action="receive", the post data must be an object of type Peer;
	 * 			and it will be use to listen at the messenger for messages.
	 * 			if no message is available at that time the call will block.
	 *  <li/>   if actio=null, a "no action specified" message ist returned as
	 *  		response
	 *  <li/> 	if action="ask" not implemented 
	 *  
	 *  <li/>	else  a "action not supported" message is return as response.		
	 * </ul>  
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(
						HttpServletRequest req, 
						HttpServletResponse resp)
						throws ServletException, IOException 
	{
		String action = req.getParameter("action");
		logger.info("com request URI:"+req.getRequestURI()+
					" action:"+action);
		if(action==null){
			sendObject(resp,"no action specified");
		}else if(action.equals("send")){
				ObjectInputStream objIn = null;
			    Object rcvObj=null;    
			    try
			    {  
			        objIn = new ObjectInputStream(req.getInputStream());				    
			        rcvObj = objIn.readObject();
			        messenger.sendMsg((Message)rcvObj);
			        //////////////////////////////////////////
			        sendObject(resp,"Object Send");										
			    }catch(Exception e){
			    	logger.error("send error:",e);
			        // handle exception
			    	resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			    					e.getMessage());
			    }

		}else if(action.equals("receive")){
			try {
				ObjectInputStream objIn=
					new ObjectInputStream(req.getInputStream());
				Object obj= objIn.readObject();
				
				Object toSend=null;
				if(obj instanceof Peer){
					logger.info("Listening client peer:"+obj);
					HttpSession ses= req.getSession(true);
					
					if(ses.getAttribute(PEER_NAME_KEY)==null){
						//peer for final removal
						logger.info("-------logging peer communication channel"+ses.getId());
						ses.setAttribute(PEER_NAME_KEY,(Peer)obj);
					}
					messenger.openChannel((Peer)obj);	
					toSend=messenger.receiveMsg((Peer)obj);
				}else{
					toSend="send a peer object to receive anything";
				}
				logger.info("To send:"+toSend);
				sendObject(resp,toSend);
			} catch (IOException e) {
				logger.error("Error while listening:",e);sendObject(resp,e);
			} catch (ClassNotFoundException e) {
				logger.error("Error while listening:",e);sendObject(resp,e);
			}catch(Throwable th){
				logger.error("Error while listening:",th);sendObject(resp,th);
			}
		}else if(action.equals("ask")){
			sendObject(resp,"NO");
		}else{
			sendObject(resp,"action not supported:"+action);
		}
	}
	
	/**
	 * Send a java object as hhtp post response.
	 * 
	 * @param resp -- the http servlet response
	 * @param toSend -- the object to send.
	 */
	private void sendObject(HttpServletResponse resp, Object toSend)
	{
		try {
			ByteArrayOutputStream byteStream=
				new ByteArrayOutputStream();
			ObjectOutputStream objOut = 
				new ObjectOutputStream(byteStream);
			objOut.writeObject(toSend);
			objOut.flush();		
			resp.setContentType("application/octetstream");
			resp.setContentLength(byteStream.size());
			byteStream.writeTo(resp.getOutputStream());	
			resp.getOutputStream().flush();
		} catch (IOException e) {
			logger.debug("cannot send object",e);
		}		
	}
	

	 

	
	/**
	 * Overriden to close all the open channel.
	 * (not implemented jet)
	 */
	public void destroy() 
	{
		//TODO close all channels
		super.destroy();
	}
}
