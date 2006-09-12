package org.protune.net;

public class ServiceRequestMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 111;
	String requestedService;
	
	public ServiceRequestMessage(String s){
		requestedService = s;
	}
	
	String getRequestedService(){
		return requestedService;
	}
	
}
