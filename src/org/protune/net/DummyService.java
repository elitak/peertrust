package org.protune.net;

public class DummyService extends Service {
	
	DummyService(Pointer p){
		otherPeer = p;
	}

	public NegotiationMessage eval(OngoingNegotiationMessage onm) {
		return onm;
	}

}
