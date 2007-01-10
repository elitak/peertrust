package test.org.protune;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.protune.net.*;

public class ServiceAvailableClientTest{
	
	public static final String MY_ADDRESS = "localhost";
	public static final int MY_PORT = 1235;
	public static final String PROTUNE_ADDRESS = "localhost";
	public static final int PROTUNE_PORT = 1234;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
			
		Runnable runnable = new Runnable() {
			public void run() {
				// Local variables initialisation.
				String[] sa = {"org.protune.net.DummyService"};
				DispatcherPeer dp;
				
				try {
					dp = new DispatcherPeer("localhost", PROTUNE_PORT, sa);
					dp.init();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		};
		Thread thread=new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
		
		ServerSocket ss=new ServerSocket(MY_PORT);
		
		//AddressPortPointer app = new AddressPortPointer(PROTUNE_ADDRESS, PROTUNE_PORT);
		//app.sendMessage(new ServiceRequestMessage("org.protune.net.DummyService"));
		
		Socket s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Binding...");
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		ServiceRequestMessage srm = new ServiceRequestMessage("org.protune.net.DummyService");
		//System.out.println("Client sent ServiceRequestMessage...");
		oos.writeObject(srm);
		oos.flush();
		System.out.println("Sent ServiceRequestMessage: " + srm);
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		//System.out.println("Client received ServiceAvailableMessage...");
		ServiceAvailableMessage sam = (ServiceAvailableMessage)(ois.readObject());
		System.out.println("Received ServiceAvailableMessage: " + sam);
		s.close();
		
		
		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		DispatcherStartNegotiationMessage dsnm = new DispatcherStartNegotiationMessage(
				new AddressPortPointer(MY_ADDRESS, MY_PORT),
				"org.protune.net.DummyService"
		);
		oos.writeObject(dsnm);
		//System.out.println("Client sent DispatcherStartNegotiationMessage..." );
		oos.flush();
		System.out.println("Sent DispatcherStartNegotiationMessage: " + dsnm);
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		//System.out.println("Client received StartNegotiationMessage...");
		StartNegotiationMessage snm = (StartNegotiationMessage)(ois.readObject());
		System.out.println("Received StartNegotiationMessage: " + snm);
		s.close();
		
		
		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		//System.out.println("currentServiceId : "+serviceId);
		DispatcherMessage dm = new DispatcherMessage(
				((DispatcherPointer)(((StartNegotiationMessage)snm).getPeerPointer())).getServiceID(),
				new OngoingNegotiationMessage()
		);
		oos.writeObject(dm);
		System.out.println("Client sent DispatcherMessage with OngoingNegotiationMessage");
		oos.flush();
		System.out.println("Sent DispatcherMessage: " + dm);
		//System.out.println("Message in DispatcherMessage is OngoingNegotiationMessage : ");
		s.close();
		s=ss.accept();
		//System.out.println("Client received OngoingNegotiationMessage...");
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		OngoingNegotiationMessage onm = (OngoingNegotiationMessage)(ois.readObject());
		System.out.println("Received OngoingNegotiationMessage: " + onm);
		s.close();
		
		
		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		long serviceId =
			((DispatcherPointer)(((StartNegotiationMessage)snm).getPeerPointer())).getServiceID();
		//System.out.println("currentServiceId : "+serviceId);
		dm = new DispatcherMessage(
				serviceId,
				new OngoingNegotiationMessage()
		);
		oos.writeObject(dm);
		System.out.println("Client sent 2.DispatcherMessage with OngoingNegotiationMessage");
		oos.flush();
		System.out.println("Sent DispatcherMessage: " + dm);
		//System.out.println("Message in DispatcherMessage is OngoingNegotiationMessage : ");
		s.close();
		s=ss.accept();
		//System.out.println("Client received 2.OngoingNegotiationMessage...");
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		onm = (OngoingNegotiationMessage)(ois.readObject());
		System.out.println("Received OngoingNegotiationMessage: " + onm);
		s.close();
		
		
		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		oos = new ObjectOutputStream(s.getOutputStream());
		dm = new DispatcherMessage(
				serviceId,
				new EndNegotiationMessage(new SuccessfulNegotiationResult())
		);
		oos.writeObject(dm);
		System.out.println("Client sent DispatcherMessage with EndNegotiationMessage...");
		oos.flush();
		System.out.println("Sent DispatcherMessage: " + dm);
		//System.out.println("Message in DispatcherMessage is EndNegotiationMessage : ");
		s.close();
		
		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		
		ss.close();
	}

}
