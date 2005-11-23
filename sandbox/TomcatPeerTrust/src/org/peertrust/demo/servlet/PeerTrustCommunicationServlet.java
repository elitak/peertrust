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
 * @author Patrice Congo (token77)
 */
public class PeerTrustCommunicationServlet 	extends HttpServlet 
{
	final static public  String PEER_NAME_KEY="finalDestination"; 
	private NegotiationObjects negoObjects=null;
	private Logger logger;	
	private Messenger messenger;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init(ServletConfig config) throws ServletException {
		negoObjects=
			NegotiationObjects.createAndAddForAppContext(config);
		messenger=negoObjects.getMessenger();
		logger=Utils.getLogger(this.getClass().getName());
		return;		
	}
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		sendObject(resp,"GET NOT SUPPORTED");
	}
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//logger.info("com request URI:"+req.getRequestURI());
		String action = req.getParameter("action");
		logger.info("com request URI:"+req.getRequestURI()+
					" action:"+action+ //" isL:"+negoObjects.getIsListening()+
					" negoObjects:"+negoObjects);
		if(action==null){
			//resp.sendError(HttpServletResponse.SC_NO_CONTENT,"no action specify");
			sendObject(resp,"no action specify");
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
						System.out.println("-------logging peer communication channel"+ses.getId());
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
			//resp.getWriter().print("NO");
			sendObject(resp,"NO");
		}else{
			sendObject(resp,"action not supported:"+action);
		}
	}
	
	private void sendObject(HttpServletResponse resp, Object toSend){
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
	

	

	
	
	public void destroy() {
		//TODO close all channels
		super.destroy();
	}
}
