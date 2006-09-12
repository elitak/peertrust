package org.protune.net;

public class DispatcherMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 711;
	long serviceID;
	NegotiationMessage negotiationMessage;
	
	DispatcherMessage(long l, NegotiationMessage nm){
		serviceID = l;
		negotiationMessage = nm;
	}
	
	long getServiceID(){
		return serviceID;
	}
	
	NegotiationMessage getNegotiationMessage(){
		return negotiationMessage;
	}
	
}
