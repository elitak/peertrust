package org.protune.net;

/**
 * Classes <tt>ServiceRequestMessage</tt>, {@link org.protune.net.ServiceAvailableMessage}, {@link
 * org.protune.net.ServiceNotAvailableMessage} and {@link
 * org.protune.net.DispatcherStartNegotiationMessage} were developed in order to support negotiations
 * with a {@link org.protune.net.DispatcherPeer}.<br />
 * A <tt>DispatcherPeer</tt> is able to provide support to a number of services that the node may
 * want to make available, therefore during the first step of the communication with a
 * <tt>DispatcherPeer</tt> the client should ask whether the service it would like to get can be
 * actually provided by the <tt>DispatcherPeer</tt>. This is the function of a
 * <tt>ServiceRequestMessage</tt>.
 * @author jldecoi
 */
public class ServiceRequestMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 111;
	private String requestedService;
	
	public ServiceRequestMessage(String s){
		requestedService = s;
	}
	
	public String getRequestedService(){
		return requestedService;
	}
	
}
