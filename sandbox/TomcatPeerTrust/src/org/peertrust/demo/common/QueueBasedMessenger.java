
package org.peertrust.demo.common;

import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.Message;
import org.peertrust.net.Peer;

/**
 * QueueBasedMessenger provides a Messenger implementation based 
 * on queues. The queues models the communication channels.
 *  
 * @author Patrice Congo
 */
public class QueueBasedMessenger implements Messenger,
											Configurable
{

	/** 
	 * Contains a channel 
	 */
	private Hashtable channelPool= new Hashtable(8);
	
	/**
	 * Gets the message fifo belonging to the specified peer.
	 * @param 	channelPeer -- the peer which channel is to be
	 * 			returned
	 * @return the message fifo belonging to the specified peer 
	 */
	synchronized private BlockingQueue getMessageFIFO(Peer channelPeer)
	{
		if(channelPeer==null){
			return null;
		}
		
		String key=channelPeer.getAlias();
		ArrayBlockingQueue messageFIFO=
			(ArrayBlockingQueue)channelPool.get(key);
		return messageFIFO;
	}
	
	/**
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getMessageFIFO(org.peertrust.net.Peer)
	 */
	synchronized private BlockingQueue createMessageFIFO(Peer channelPeer)
	{
		if(channelPeer==null){
			return null;
		}
		
		String key=channelPeer.getAlias();
		ArrayBlockingQueue messageFIFO=
			(ArrayBlockingQueue)channelPool.get(key);
		
		if(messageFIFO==null){
			    messageFIFO= new ArrayBlockingQueue(8);
				channelPool.put(key,messageFIFO);
		}
		
		return messageFIFO;
	}
	
	
	/**
	 * removed a channel queue from the channel pool
	 * @param channelPeer -- the peer used to create this channel
	 * @return returns the removed queue 
	 */
	synchronized private BlockingQueue removeMessageFIFO(
												Peer channelPeer)
	{
		if(channelPeer==null){
			return null;
		}
		String key=channelPeer.getAlias();
		return (BlockingQueue)channelPool.remove(key);
	}
	
	/**
	 * adds a message to the appropriate queue.
	 * @param mes -- the message to add
	 */
	public void addMsgToQueue(	Message mes)
								throws 	IOException 
	{
		if(mes==null){
			return;
		}
		
		try {
			String dest=mes.getTarget().getAlias();
			ArrayBlockingQueue queue=
				(ArrayBlockingQueue)channelPool.get(dest);
			if(queue==null){
				throw new IOException("No channel for "+dest+" exists");
			}
			queue.offer(mes);
		} catch (Exception e) {
			throw 
				new IOException("Could not deliver message to channel:"+
								e.getLocalizedMessage());
		}
		return;
	}
	
	
	
	/**
	 * Sends a messenge.
	 * @see org.peertrust.demo.common.Messenger#sendMsg(org.peertrust.net.Message)
	 */
	public void sendMsg(Message msg) throws IOException {
		addMsgToQueue(msg);
	}

	/**
	 * @see org.peertrust.demo.common.Messenger#openChannel(org.peertrust.net.Peer)
	 */
	public void openChannel(Peer receiver) throws IOException {
		createMessageFIFO(receiver);
	}

	/**
	 * @see org.peertrust.demo.common.Messenger#closeChannel(org.peertrust.net.Peer)
	 */
	public void closeChannel(Peer receiver) throws IOException {
		BlockingQueue queue=removeMessageFIFO(receiver);
		if(queue!=null){
			queue.clear();
		}
	}

	/**
	 * @see org.peertrust.demo.common.Messenger#receiveMsg(org.peertrust.net.Peer)
	 */
	public Message receiveMsg(	
							Peer channelPeer) 
							throws 	IOException, 
									NullPointerException 
	{
		ArrayBlockingQueue messageFIFO=
				(ArrayBlockingQueue)getMessageFIFO(channelPeer);
		if(messageFIFO==null){
			throw 
				new IOException("No channel previouly open for this peer:"+
								channelPeer);
		}
		try {
			return (Message)messageFIFO.take();
		} catch (InterruptedException e) {
			throw new IOException(	"Listening process interrupted:"+
									e.getLocalizedMessage());
		}
	}

	/**
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		//empty
		
	}

}
