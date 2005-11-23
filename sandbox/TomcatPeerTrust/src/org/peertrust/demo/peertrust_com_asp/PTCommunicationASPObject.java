package org.peertrust.demo.peertrust_com_asp;

import java.io.Serializable;

import org.peertrust.net.Message;
import org.peertrust.net.Peer;

/**
 * PTCommunicationASPObject is a Message which kan peggy back any serializable.
 * Thus this serializable can be send using the peertrust communication channel
 * without providing its own message extension.
 * 
 * @author Patrice Congo (token77)
 */
public class PTCommunicationASPObject extends Message {
	/**
	 * The peggy backed message 
	 */
	private Serializable peggyBackedMessage;
	
	/**
	 * Constructs a new PTCommunicationASPObject
	 * @param source -- source of the new message 
	 * @param dest -- the destination o the new message
	 * @param peggyBackedMessage -- the transported message
	 */
	public PTCommunicationASPObject(
								Peer source, 
								Peer dest,
								Serializable peggyBackedMessage) 
	{
		super(source, dest);
		this.peggyBackedMessage=peggyBackedMessage;
	}

	
	/**
	 * @return Returns the peggyBackedMessage.
	 */
	public Serializable getPeggyBackedMessage() {
		return peggyBackedMessage;
	}


	/**
	 * Sets the peggy backed message.
	 * @param peggyBackedMessage -- The peggyBackedMessage to set.
	 */
	public void setPeggyBackedMessage(Serializable peggyBackedMessage) {
		this.peggyBackedMessage = peggyBackedMessage;
	}

//	static public void send(NetClient netClient, 
//							Serializable payload,
//							Peer source, Peer target){
//		PTCommunicationASPObject mes=
//			new PTCommunicationASPObject(source,target,payload);
//		netClient.send(mes,target);
//	}
	
	
	
	/**
	 * @see org.peertrust.net.Message#toString()
	 */
	public String toString() {
		return 	super.toString()+
				"\n\tMessage:"+peggyBackedMessage;
	}

}
