package org.protune.net;

public class DispatcherStartNegotiationMessage extends StartNegotiationMessage {
	
	static final long serialVersionUID = 4111;
	String requestedService;
	
	public DispatcherStartNegotiationMessage(Pointer p, String s){
		super(p);
		requestedService = s;
	}
	
	Pointer getPeerPointer(){
		return peerPointer;
	}
	
	String getRequestedService(){
		return requestedService;
	}

}
