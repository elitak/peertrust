package org.protune.net;

import java.io.*;

/**
 * A {@link org.protune.net.DispatcherPeer} is able to handle many negotiations concurrently. This
 * requires of course that the system keeps track of each ongoing negotiation and is able to retrieve
 * the status of the previous negotiation as soon as a new negotiation message is received. This means
 * that, in order to identify the service taking care of a specific negotiation, an {@link
 * org.protune.net.AddressPortPointer} does not suffice, as an identifier of the specific negotiation
 * should be part of the address as well. Such an identifier is provided by a
 * <tt>DispatcherPointer</tt>.
 * @author jldecoi
 */
public class DispatcherPointer extends AddressPortPointer{
	
	static final long serialVersionUID = 811;
	private long serviceID;
	
	public DispatcherPointer(String s, int i, long l){
		super(s, i);
		serviceID = l;
	}
	
	public DispatcherPointer(AddressPortPointer app, long l){
		this(app.getAddress(), app.getPort(), l);
	}
/*
	public void sendMessage(NegotiationMessage nm) throws IOException{
		DispatcherMessage dm = new DispatcherMessage(serviceID, nm);
		super.sendMessage(dm);
	}
	
	/**
	 * A <tt>DispatcherPointer</tt> expects a <tt>NegotiationMessage</tt>. If it receives a
	 * <tt>Message</tt> belonging to some other class, it throws an <tt>IOException</tt>.
	 */
	public void sendMessage(Message m) throws IOException{
		if(m instanceof NegotiationMessage){
			super.sendMessage(new DispatcherMessage(serviceID, (NegotiationMessage)m));
		}
		else throw new IOException();
	}
	
	public long getServiceID() {
		return serviceID;
	}

}
