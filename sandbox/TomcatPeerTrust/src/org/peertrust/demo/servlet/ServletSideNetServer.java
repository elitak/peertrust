/*
 * Created on 14.04.2005
 */
package org.peertrust.demo.servlet;

import org.apache.log4j.Logger;
import org.peertrust.demo.common.Messenger;
import org.peertrust.net.Message;
import org.peertrust.net.NetServer;
import org.peertrust.net.Peer;

import java.io.IOException;

/**
 * @author Patrice Congo (token77)
 */
public class ServletSideNetServer implements NetServer {

	
	Messenger messenger; 
	Logger logger;
	Peer localPeer;
	
	public ServletSideNetServer(
							Logger logger, 
							Peer localPeer,
							Messenger messenger)
	{
		this.messenger=messenger;
		this.logger=logger;
		this.localPeer=localPeer;
		try {
			messenger.openChannel(localPeer);
		} catch (IOException e) {
			throw new RuntimeException("Could not create channel for server",e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.NetServer#listen()
	 */
	public Message listen() {
		logger.info("\n----------------------------------Servlet PT server listening");
		try {
			return messenger.receiveMsg(localPeer);
		} catch (IOException e) {
			logger.debug("Erro while receiving message",e);
			return null;
		}
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
