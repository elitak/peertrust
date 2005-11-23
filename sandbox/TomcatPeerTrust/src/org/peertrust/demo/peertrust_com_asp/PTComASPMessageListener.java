package org.peertrust.demo.peertrust_com_asp;

import java.io.Serializable;

import org.peertrust.net.Peer;

/**
 * Interface to implements on order to be able to register
 * at the PTCommunicationASP to receive message.
 * 
 * @author Patrice Congo (token77)
 */
public interface PTComASPMessageListener 
{
	/**
	 * Call if the message the listerner has register for
	 * arrives.
	 * @param message -- the receive message
	 * @param source -- the sending peer
	 * @param target -- the target peer
	 */
	public void PTMessageReceived(
								Serializable message, 
								Peer source, 
								Peer target); 
}
