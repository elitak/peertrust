/*
 * Created on 14.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
import org.peertrust.demo.common.ConfigurationOption;

//import org.peertrust.demo.common.StopCmd;
import org.peertrust.net.*;

//import java.util.Hashtable;
//import java.util.Iterator;
//import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PeerTrustCommunicationServlet 	extends HttpServlet 
											/*implements PeerTrustCommunicationListener*/{
	final static public  String PEER_NAME_KEY="finalDestination"; 
	//private ServletSideNetServer trustServer=null;
	//private ServletSideNetClient trustClient=null;
	private NegotiationObjects negoObjects=null;
	//private Hashtable messagePool= new Hashtable(20);
	private Logger logger;//=Logger.getLogger(this.getClass());
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init(ServletConfig config) throws ServletException {
		//super.init();
		//ServletContext context=config.getServletContext();
		negoObjects=
			NegotiationObjects.createAndAddForAppContext(config);
		
		//trustServer= negoObjects.getNetServer();
		
		//trustClient= negoObjects.getNetClient();
		//trustEngin=negoObjects.getPeerTrustEventListener();
		logger=ConfigurationOption.getLogger(this.getClass().getName());
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
					" action:"+action+ " isL:"+negoObjects.getIsListening()+
					" negoObjects:"+negoObjects);
		if(action==null){
			//resp.sendError(HttpServletResponse.SC_NO_CONTENT,"no action specify");
			sendObject(resp,"no action specify");
		}else if(action.equals("send")){
			if(negoObjects.getIsListening()){
				ObjectInputStream objIn = null;
			    //PrintWriter out = null;
			    //BufferedReader inTest = null;
			    Object rcvObj=null;    
			    try
			    {  
			        objIn = new ObjectInputStream(req.getInputStream());				    
			        // read and send message ti trust server        
			        rcvObj = objIn.readObject();
//			        if(rcvObj instanceof HttpSessionRegistrationRequest){
//			        	
//			        	Peer peer=((HttpSessionRegistrationRequest)rcvObj).getSource();
//			        	String sesId=((HttpSessionRegistrationRequest)rcvObj).getSessionKey();
//			        	System.out.println("Resgistering "+sesId+" peer:"+peer);
//			        	negoObjects.registerSession(sesId,peer);
//			        }else{
//			        	negoObjects.addMessage(rcvObj);
//			        }
			        negoObjects.addMessage(rcvObj);
			        //////////////////////////////////////////
			        sendObject(resp,"Object Send");										
			    }catch(Exception e){
			    	logger.error("send error:",e);
			        // handle exception
			    	resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			    					e.getMessage());
			    }
			}else{
				//resp.sendError(HttpServletResponse.SC_NO_CONTENT,"trust server not listening");
				sendObject(resp,"trust server not listening");
			}

		}else if(action.equals("receive")){
			try {
				ObjectInputStream objIn=
					new ObjectInputStream(req.getInputStream());
				Object obj= objIn.readObject();
				
				Object toSend=null;
				if(obj instanceof Peer){
					System.out.println("------------------------------Listening client peer:"+obj);
					logger.info("Listening client peer:"+obj);
					//negoObjects.addPeerTrustCommunicationListener((Peer)obj, this);
					HttpSession ses= req.getSession(true);
					
					if(ses.getAttribute(PEER_NAME_KEY)==null){
						//peer for final removal
						System.out.println("-------logging peer communication channel"+ses.getId());
						ses.setAttribute(PEER_NAME_KEY,(Peer)obj);				
					}
					
					//look and see if there is a message
					BlockingQueue messageFIFO=
						negoObjects.getMessageFIFO(((Peer)obj));
					toSend=messageFIFO.take();
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
//		ArrayBlockingQueue queue=null;
//		for(Iterator it =messagePool.values().iterator();it.hasNext();){
//			queue=(ArrayBlockingQueue)it.next();
//			queue.clear();
//			queue.offer(new StopCmd());
//		}
		super.destroy();
	}
}
