/*
 * Created on 14.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import org.apache.log4j.Logger;
import org.peertrust.net.*;
import java.util.Hashtable;
/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServletSideNetClient implements NetClient{
	
	Hashtable listenerPool;
	
	Hashtable messageTable;
	
	Logger logger;
	
	public ServletSideNetClient(Logger logger){
		listenerPool= new Hashtable();
		messageTable= new Hashtable();
		this.logger=logger;
		
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.NetClient#send(org.peertrust.net.Message, org.peertrust.net.Peer)
	 */
	public void send(Message mes, Peer finalDestination) {
		
		Object obj= listenerPool.get(finalDestination.getAlias());
		logger.info("Servlet PT engine sending message to:"+finalDestination.getAlias()+ " listener:"+obj);
		if(obj!=null){
			((PeerTrustCommunicationListener)obj).send(mes,finalDestination.getAlias());
		}else{
			logger.info(
					"Servlet PT engine; no listener assoziated with the code:"+
						finalDestination.getAlias()+ " listener:"+obj);
		}
		return;
	}
	
	public void addPeerTrustCommunicationListener(
										String finalDestination,
										PeerTrustCommunicationListener comHelper		){
		listenerPool.put(finalDestination,comHelper);
		
		return;
	}
	
	public void removePeerTrustCommunicationListener(String finalDestination){
		listenerPool.remove(finalDestination);
		return;
	}
	
	public void removeAllPeerTrustCommunicationListener(){
		listenerPool.clear();
		return;
	}
	
	private PeerTrustCommunicationListener getCommunicationHelper(String finalDestination){
		return (PeerTrustCommunicationListener)listenerPool.get(finalDestination);
	}
}
