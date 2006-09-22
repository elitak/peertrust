package org.protune.net;

/**
 * Classes {@link org.protune.net.ServiceRequestMessage}, <tt>ServiceAvailableMessage</tt>, {@link
 * org.protune.net.ServiceNotAvailableMessage} and {@link
 * org.protune.net.DispatcherStartNegotiationMessage} were developed in order to support negotiations
 * with a {@link org.protune.net.DispatcherPeer}.
 * A <tt>DispatcherPeer</tt> is able to provide support to a number of services that the node may
 * want to make available, nevertheless a client could ask the <tt>DispatcherPeer</tt> for providing a
 * service it is actually unable to provide. If this is not the case, the <tt>DispatcherPeer</tt> should
 * confirm to the client that the service it asked for is available. This is the function of a
 * <tt>ServiceAvailableMessage</tt>.
 * @author jldecoi
 */
public class ServiceAvailableMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 211;
	
}
