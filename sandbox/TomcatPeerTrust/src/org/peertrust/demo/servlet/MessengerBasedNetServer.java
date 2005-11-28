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
 * MessengerBasedNetServer provides a implementation of
 * NetServer based on a messenger. I.e. It uses  the messenger to
 * vor the data.
 * 
 * @author Patrice Congo (token77)
 */
public class MessengerBasedNetServer implements NetServer {

	/**
	 * the messenger used for listening.
	 */
	Messenger messenger; 
	
	/**
	 * Message logger
	 */
	Logger logger;
	
	/**
	 * represents the local peer
	 */
	Peer localPeer;
	
	/**
	 *Constructs a new MessengerBasedNetServer
	 *  
	 * @param logger -- the logger to use
	 * @param localPeer -- represents the local negotiating peer
	 * @param messenger -- the messenger used to listen
	 */
	public MessengerBasedNetServer(
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
	
	/**
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
