package org.protune.net;

import java.io.IOException;

/**
 * A <tt>Service</tt> object represents a service provided on some node of the network, whose result
 * should be sent back to the client asking for it. The abstract class <tt>Service</tt> takes care of
 * all issues related to the communication with the client, hopefully hiding the complexity
 * requested by the network communication and allowing the programmer to focus on the application by
 * simply incapsulating the application logic in a subclass of <tt>Service</tt>.<br />
 * <b>NOTE:</b> Each subclass of {@link org.protune.net.Service} should implement the constructor
 * <tt>&lt;subclassName&gt;({@link org.protune.net.Pointer})</tt>.
 * @author jldecoi
 */
public abstract class Service {
	
	Pointer otherPeer;
	
	NegotiationMessage perform(OngoingNegotiationMessage onm) throws IOException, WrongMessageTypeException{
		NegotiationMessage toSend = eval(onm);
		otherPeer.sendMessage(toSend);
		
		return toSend;
	}
	
	public abstract NegotiationMessage eval(OngoingNegotiationMessage onm) 
		throws WrongMessageTypeException;

}
