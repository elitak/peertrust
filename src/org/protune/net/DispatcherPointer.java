package org.protune.net;

import java.io.*;

public class DispatcherPointer extends AddressPortPointer{
	
	static final long serialVersionUID = 22;
	long serviceID;
	
	DispatcherPointer(String s, int i, long l){
		super(s, i);
		serviceID = l;
	}
	
	DispatcherPointer(AddressPortPointer app, long l){
		this(app.getAddress(), app.getPort(), l);
	}

	public void sendMessage(NegotiationMessage nm) throws IOException{
		DispatcherMessage dm = new DispatcherMessage(serviceID, nm);
		super.sendMessage(dm);
	}

}
