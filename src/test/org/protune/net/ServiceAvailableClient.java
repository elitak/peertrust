package test.org.protune.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.protune.net.*;

public class ServiceAvailableClient {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		
		Socket s = new Socket("localhost", 1234);
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));

		ServiceRequestMessage srm = new ServiceRequestMessage("DummyService");
		oos.writeObject(srm);
		oos.flush();
		System.out.println("Sent ServiceRequestMessage: " + srm);
		ServiceAvailableMessage sam = (ServiceAvailableMessage)(ois.readObject());
		System.out.println("Received ServiceAvailableMessage: " + sam);

		AddressPortPointer app = new AddressPortPointer("localhost", 1234);
		StartNegotiationMessage snm = new StartNegotiationMessage(app);
		oos.writeObject(snm);
		oos.flush();
		System.out.println("Sent StartNegotiationMessage: " + snm);
		snm = (StartNegotiationMessage)(ois.readObject());
		System.out.println("Received StartNegotiationMessage: " + snm);

		OngoingNegotiationMessage onm = new OngoingNegotiationMessage();
		oos.writeObject(onm);
		oos.flush();
		System.out.println("Sent OngoingNegotiationMessage: " + onm);
		onm = (OngoingNegotiationMessage)(ois.readObject());
		System.out.println("Received OngoingNegotiationMessage: " + onm);

		EndNegotiationMessage enm = new EndNegotiationMessage(new SuccessfulNegotiationResult());
		oos.writeObject(enm);
		oos.flush();
		System.out.println("Sent EndNegotiationMessage: " + enm);

		s.close();
		
	}

}
