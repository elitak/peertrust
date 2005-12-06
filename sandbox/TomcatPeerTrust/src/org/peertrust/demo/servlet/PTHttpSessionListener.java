/*
 * Created on 14.07.2005
 */
package org.peertrust.demo.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.peertrust.net.Peer;

/**
 * PTHttpSessionListener dooes listen for session end; 
 * and removes session peer.
 * @author Patrice Congo (token77)
 */
public class PTHttpSessionListener implements HttpSessionListener 
{

	/**
	 * Creates a new PTHttpSessionListener
	 */
	public PTHttpSessionListener() {
		super();
	}

	/**
	 * Receives session creation event, does nothing
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent arg0) 
	{
		//nothing
	}

	/**
	 * Receives session Destroyed events and removes all references 
	 * the registered peer.
	 * 
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se) 
	{
		Peer leavingPeer =
			(Peer)se.getSession().getAttribute(PeerTrustCommunicationServlet.PEER_NAME_KEY);
				
		if(leavingPeer!=null){
			ServletContext context=se.getSession().getServletContext();
			context.log("=============>removing:"+leavingPeer);
			
			try {
				
//				NegotiationObjects negoObjects= 
//						(NegotiationObjects)context.getAttribute(
//											NegotiationObjects.class.getName());
				NegotiationObjects negoObjects=
					NegotiationObjects.createAndAddForAppContext(context);
				negoObjects.removePeertrustSessionEntries(leavingPeer);	
				
			} catch (Exception e) {
				context.log(
					"Error while removing registration for this peer:"+
						leavingPeer,
					e);
			}
		}
	}

	
}
