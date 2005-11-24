/*
 * Created on 14.04.2005
 */
package org.peertrust.demo.servlet;

import org.apache.log4j.Logger;
import org.peertrust.demo.common.Messenger;
import org.peertrust.net.*;

import java.io.IOException;

/**
 * ServletSideNetClient implements NetClient to
 * Provides an interface to send messages in an http 
 * communication context.
 * 
 * @author Patrice Congo (token77)
 */
public class ServletSideNetClient implements NetClient{
	
	/** 
	 * The messeneger to which the sends are delegated 
	 */
	private Messenger messenger;
	
	/**
	 * Message logger
	 */
	private Logger logger;
	
	
	/**
	 * Creates a new ServerSideNetClient.
	 * This client will delegates to send to the provided 
	 * client.
	 * 
	 * @param logger -- message logger
	 * @param messenger -- messenger use to send message
	 */
	public ServletSideNetClient(
							Logger logger,
							Messenger messenger)
	{
		this.logger=logger;
		this.messenger=messenger;
	}
	
	/**
	 * Sends a message.
	 * Note that the provided destination peer is ignored
	 * since it is already in the message object.
	 * The actual sending is delegated to the messenger.
	 * @param mes -- the message to send
	 * @param finalDestination -- the destination of the message
	 * 
	 * @see org.peertrust.net.NetClient#send(org.peertrust.net.Message, org.peertrust.net.Peer)
	 */
	public void send(Message mes, Peer finalDestination) {
		logger.info("\nServlet PT engine sending message to:"+
					finalDestination.getAlias()+ 
					" listener:"/*+
					peerTrustCommunicationListener*/);
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
