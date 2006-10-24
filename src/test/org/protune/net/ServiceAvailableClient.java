package test.org.protune.net;

import java.io.*;
import java.net.Socket;
import org.protune.net.*;

public class ServiceAvailableClient {

	public static void main(String[] args) throws IOException, ClassNotFoundException{
		
		//System.out.println(0);
		Socket s = new Socket("localhost", 1234);
		//System.out.println(1);
		
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		//System.out.println(2);

		ServiceRequestMessage srm = new ServiceRequestMessage("DummyService");
		//System.out.println(3);
		oos.writeObject(srm);
		//System.out.println(4);
		oos.flush();
		//System.out.println("Sent ServiceRequestMessage: " + srm);
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		//System.out.println(5);
		ServiceAvailableMessage sam = (ServiceAvailableMessage)(ois.readObject());
		//System.out.println("Received ServiceAvailableMessage: " + sam);

		AddressPortPointer app = new AddressPortPointer("localhost", 1235);
		//System.out.println(6);
		DispatcherStartNegotiationMessage dsnm = new DispatcherStartNegotiationMessage(app, "DummyService");
		//System.out.println(7);
		oos.writeObject(dsnm);
		//System.out.println(8);
		oos.flush();
		System.out.println("Sent DispatcherStartNegotiationMessage: " + dsnm);
		StartNegotiationMessage snm = (StartNegotiationMessage)(ois.readObject());
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
