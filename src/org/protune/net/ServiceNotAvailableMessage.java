package org.protune.net;

/**
 * Classes {@link org.protune.net.ServiceRequestMessage}, {@link
 * org.protune.net.ServiceAvailableMessage}, <tt>ServiceNotAvailableMessage</tt> and {@link
 * org.protune.net.DispatcherStartNegotiationMessage} were developed in order to support negotiations
 * with a {@link org.protune.net.DispatcherPeer}.
 * A <tt>DispatcherPeer</tt> is able to provide support to a number of services that the node may
 * want to make available, nevertheless a client could ask the <tt>DispatcherPeer</tt> for providing a
 * service it is actually unable to provide. In this case the <tt>DispatcherPeer</tt> should communicate
 * the client back that the service it asked for is unavailable. This is the function of a
 * <tt>ServiceNotAvailableMessage</tt>.
 * @author jldecoi
 */
public class ServiceNotAvailableMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 311;
	
}
