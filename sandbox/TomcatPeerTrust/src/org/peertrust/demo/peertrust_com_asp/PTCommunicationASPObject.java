package org.peertrust.demo.peertrust_com_asp;

import java.io.Serializable;

import org.peertrust.net.Message;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;


public class PTCommunicationASPObject extends Message {
	private Serializable peggyBackedMessage;
	
	public PTCommunicationASPObject(Peer source, Peer dest,Serializable peggyBackedMessage) {
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
	 * @param peggyBackedMessage The peggyBackedMessage to set.
	 */
	public void setPeggyBackedMessage(Serializable peggyBackedMessage) {
		this.peggyBackedMessage = peggyBackedMessage;
	}

	static public void send(NetClient netClient, Serializable payload,Peer source, Peer target){
		PTCommunicationASPObject mes=
			new PTCommunicationASPObject(source,target,payload);
		netClient.send(mes,target);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
