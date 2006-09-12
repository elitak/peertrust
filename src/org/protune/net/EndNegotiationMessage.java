package org.protune.net;

public class EndNegotiationMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 611;
	NegotiationResult negotiationResult;
	
	public EndNegotiationMessage(NegotiationResult nr){
		negotiationResult = nr;
	}
	
	NegotiationResult getNegotiationResult(){
		return negotiationResult;
	}
	
}
