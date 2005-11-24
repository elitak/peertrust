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
			
			System.out.println("=============>removing:"+leavingPeer);
			
			try {
				ServletContext context=se.getSession().getServletContext();
				NegotiationObjects negoObjects= 
						(NegotiationObjects)context.getAttribute(
											NegotiationObjects.class.getName());
				negoObjects.removePeertrustSessionEntries(leavingPeer);	
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
}
