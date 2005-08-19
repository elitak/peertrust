/*
 * Created on 14.07.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import java.util.concurrent.BlockingQueue;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.peertrust.demo.common.StopCmd;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PTHttpSessionListener implements HttpSessionListener {

	/**
	 * 
	 */
	public PTHttpSessionListener() {
		super();
		// TODO Auto-generated constructor stub
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
		
		String finalDestination =
			(String)se.getSession().getAttribute(PeerTrustCommunicationServlet.PEER_NAME_KEY);
		System.out.println("removing:"+finalDestination);
		if(finalDestination!=null){
			try {
				ServletContext context=se.getSession().getServletContext();
				NegotiationObjects negoObjects= 
					(NegotiationObjects)context.getAttribute(NegotiationObjects.class.getName());
				BlockingQueue queue=negoObjects.removeMessageFIFO(finalDestination);
				queue.offer(new StopCmd());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
