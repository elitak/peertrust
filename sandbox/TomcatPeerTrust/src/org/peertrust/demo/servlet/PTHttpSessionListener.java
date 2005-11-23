/*
 * Created on 14.07.2005
 */
package org.peertrust.demo.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.peertrust.net.Peer;

/**
 * @author Patrice Congo (token77)
 */
public class PTHttpSessionListener implements HttpSessionListener {

	/**
	 * 
	 */
	public PTHttpSessionListener() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		
		Peer finalDestination =
			(Peer)se.getSession().getAttribute(PeerTrustCommunicationServlet.PEER_NAME_KEY);
		System.out.println("removing:"+finalDestination);
		if(finalDestination!=null){
			try {
				ServletContext context=se.getSession().getServletContext();
//				NegotiationObjectRepository negoObjects= 
//					(NegotiationObjectRepository)context.getAttribute(NegotiationObjects.class.getName());

				NegotiationObjects negoObjects= 
						(NegotiationObjects)context.getAttribute(
											NegotiationObjects.class.getName());

				//BlockingQueue queue;//=negoObjects.removeMessageFIFO(finalDestination);
				negoObjects.getMessenger().closeChannel(finalDestination);
				//queue.offer(new StopCmd());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
