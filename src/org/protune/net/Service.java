package org.protune.net;

import java.io.IOException;

/**
 * <b>NOTE:</b> Each subclass of {@link org.protune.net.Service} should implement the constructor
 * <tt>&lt;subclassName&gt;({@link org.protune.net.Pointer})</tt>.
 * @author jldecoi
 */
public abstract class Service {
	
	Pointer otherPeer;
	
	boolean perform(OngoingNegotiationMessage onm) throws IOException, WrongMessageTypeException{
		NegotiationMessage toSend = eval(onm); 
		otherPeer.sendMessage(toSend);
		
		if(toSend instanceof OngoingNegotiationMessage) return true;
		// else toSend is instance of EndNegotiationMessage, cf. DispatcherPeer
		else return false;
	}
	
	public abstract NegotiationMessage eval(OngoingNegotiationMessage onm) 
		throws WrongMessageTypeException;

}
