package org.protune.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AddressPortPointer implements Pointer {
	
	static final long serialVersionUID = 12;
	String address;
	int port;
	
	public AddressPortPointer(String s, int i){
		address = s;
		port = i;
	}
	
	String getAddress(){
		return address;
	}
	
	int getPort(){
		return port;
	}
	
	public void sendMessage(NegotiationMessage nm) throws IOException{
		Socket s = new Socket(address, port);
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		
		oos.writeObject(nm);
		oos.flush();
		s.close();
	}
	
}
