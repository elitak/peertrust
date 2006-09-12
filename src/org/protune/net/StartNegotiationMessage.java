package org.protune.net;

public class StartNegotiationMessage implements NegotiationMessage {
	
	static final long serialVersionUID = 411;
	Pointer peerPointer;
	
	public StartNegotiationMessage(Pointer p){
		peerPointer = p;
	}
	
	Pointer getPeerPointer(){
		return peerPointer;
	}

}
