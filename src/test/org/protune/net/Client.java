package test.org.protune.net;

import java.io.*;
import java.net.Socket;
import org.protune.net.*;

public class Client {

	public static final String MY_ADDRESS = "";
	public static final int MY_PORT = 1234;
	public static final String PROTUNE_ADDRESS = "";
	public static final int PROTUNE_PORT = 1234;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		
		// Local variables initialisation.
		Socket s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

		// Handshaking phase.
		oos.writeObject(new ServiceRequestMessage("ProtuneService"));
		oos.flush();
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		if(!(ois.readObject() instanceof ServiceAvailableMessage))
			// Service "ProtuneService" is not available on the Server, or error occurred.
			return;
		
		oos.writeObject(new DispatcherStartNegotiationMessage(
				new AddressPortPointer(MY_ADDRESS, MY_PORT),
				"ProtuneService"));
		oos.flush();
		if(!(ois.readObject() instanceof StartNegotiationMessage)) // Error occurred
			return;
		
		OngoingNegotiationMessage onm = new OngoingNegotiationMessage();
		oos.writeObject(onm);
		oos.flush();
		onm = (OngoingNegotiationMessage)(ois.readObject());
		
		EndNegotiationMessage enm = new EndNegotiationMessage(new SuccessfulNegotiationResult());
		oos.writeObject(enm);
		oos.flush();
		
		s.close();
		
	}

}
