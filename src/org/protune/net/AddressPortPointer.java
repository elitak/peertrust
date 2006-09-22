package org.protune.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Simple implementation of the {@link org.protune.net.Pointer} interface, suitable for the cases in
 * which a pair <tt>(address, port)</tt> as itself identifies an instance of a service.
 * @author jldecoi
 */
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
	
	public void sendMessage(Message m) throws IOException{
		Socket s = new Socket(address, port);
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		
		oos.writeObject(m);
		oos.flush();
		s.close();
	}
	
}
