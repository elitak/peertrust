/**
 * 
 */
package org.peertrust.demo.common;

import java.io.IOException;

import org.peertrust.net.Message;
import org.peertrust.net.Peer;

/**
 * Implementation of this interface provide a mean to send
 * and 
 * @author Patrice Congo (token)
 */
public interface Messenger{

	/**
	 * Sends a message.
	 * Note that the message contains the source and
	 * destination Peer.
	 * 
	 * @param msg -- the message to send.
	 * @throws IOException
	 */
	void sendMsg(Message msg) throws IOException;
	
	/**
	 * Opens a communication channel.
	 * This is a required step for the receiveMsg to work
	 * 
	 * @param 	receiver -- the future listener, peer for which 
	 * 			the channel will be openned
	 * @throws IOException
	 */
	void openChannel(Peer receiver)throws IOException;
	
	/**
	 * Closes an open channel.
	 * After the channel is closed, receiveMsg will fail  
	 * @param 	receiver -- the receiver peer which was used to open
	 * 			a channel
	 * @throws IOException
	 */
	void closeChannel(Peer receiver)throws IOException;
	
	/**
	 * Receives messages destinated to the specified receiver.
	 * Note that a channel must beviously have been open.
	 * @param receiver -- the receiver peer which was used to open
	 * a channel;
	 * @throws IOException
	 */
	Message receiveMsg(Peer receiver) throws IOException;

}
