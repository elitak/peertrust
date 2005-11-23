/*
 * Created on 14.04.2005
 */
package org.peertrust.demo.servlet;

import org.apache.log4j.Logger;
import org.peertrust.demo.common.Messenger;
import org.peertrust.net.*;

import java.io.IOException;

/**
 * @author Patrice Congo (token77)
 */
public class ServletSideNetClient implements NetClient{
	
//	private Hashtable listenerPool;
	
	private Messenger messenger;
	
	private Logger logger;
	
	PeerTrustCommunicationListener peerTrustCommunicationListener;
	
	public ServletSideNetClient(
							Logger logger,
							Messenger messenger)
	{
		this.logger=logger;
		this.messenger=messenger;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.NetClient#send(org.peertrust.net.Message, org.peertrust.net.Peer)
	 */
	public void send(Message mes, Peer finalDestination) {
		logger.info("\nServlet PT engine sending message to:"+
					finalDestination.getAlias()+ 
					" listener:"+
					peerTrustCommunicationListener);
		try {
			messenger.sendMsg(mes);
		} catch (IOException e) {
			logger.debug("Could not send:"+mes,e);
		}
		return;
	}
	

	/**
	 * @return Returns the messenger.
	 */
	public Messenger getMessenger() {
		return messenger;
	}

	/**
	 * @param messenger The messenger to set.
	 */
	public void setMessenger(Messenger messenger) {
		this.messenger = messenger;
	}
	
	

}
