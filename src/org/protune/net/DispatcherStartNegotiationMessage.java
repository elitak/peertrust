package org.protune.net;

/**
 * Classes {@link org.protune.net.ServiceRequestMessage}, {@link
 * org.protune.net.ServiceAvailableMessage}, {@link org.protune.net.ServiceNotAvailableMessage} and
 * <tt>DispatcherStartNegotiationMessage</tt> were developed in order to support negotiations with a
 * {@link org.protune.net.DispatcherPeer}.
 * A <tt>DispatcherPeer</tt> is able to provide support to a number of services that the node may
 * want to make available, therefore at the beginning of the negotiation a client should specify which
 * service it is asking for. To this aim a {@link org.protune.net.StartNegotiationMessage} does not
 * suffice, <tt>DispatcherStartNegotiationMessage</tt> extends it with the required information.
 * @author jldecoi
 */
public class DispatcherStartNegotiationMessage extends StartNegotiationMessage {
	
	static final long serialVersionUID = 4111;
	String requestedService;
	
	public DispatcherStartNegotiationMessage(Pointer p, String s){
		super(p);
		requestedService = s;
	}
	
	public Pointer getPeerPointer(){
		return peerPointer;
	}
	
	public String getRequestedService(){
		return requestedService;
	}

}
