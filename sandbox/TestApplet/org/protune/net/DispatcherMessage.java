package org.protune.net;

/**
 * A {@link org.protune.net.DispatcherPeer} is able to handle many negotiations concurrently. This
 * requires of course that the system keeps track of each ongoing negotiation and is able to retrieve
 * the status of the previous negotiation as soon as a new negotiation message is received. This means
 * that, in order to identify the service taking care of a specific negotiation, an {@link
 * org.protune.net.OngoingNegotiationMessage} or an {@link org.protune.net.EndNegotiationMessage} do
 * not suffice, as an identifier of the specific negotiation should be part of the message as well, in
 * order to allow the dispatching of the message to the correct negotiation. Such an identifier is
 * provided by a <tt>DispatcherMessage</tt> object, wrapping an {@link
 * org.protune.net.OngoingNegotiationMessage} or an {@link org.protune.net.EndNegotiationMessage} and
 * enclosing an identifier of the current negotiation as well.
 * @author jldecoi
 */
public class DispatcherMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 711;
	long serviceID;
	NegotiationMessage negotiationMessage;
	
	public DispatcherMessage(long l, NegotiationMessage nm){
		serviceID = l;
		negotiationMessage = nm;
	}
	
	public long getServiceID(){
		return serviceID;
	}
	
	public NegotiationMessage getNegotiationMessage(){
		return negotiationMessage;
	}
	
}
