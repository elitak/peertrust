package test.org.protune;

import java.io.*;
import java.net.Socket;
import org.protune.net.*;

public class ServiceNotAvailableClientTest {

	public static final String MY_ADDRESS = "localhost";
	public static final int MY_PORT = 1235;
	public static final String PROTUNE_ADDRESS = "localhost";
	public static final int PROTUNE_PORT = 1234;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		
		Runnable runnable = new Runnable() {
			public void run() {
				// Local variables initialisation.
				String[] sa = {"DummyService"};
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
		

		System.out.println(2);
		Socket s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);

		System.out.println(3);
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		
		System.out.println(4);
		// Handshaking phase.
		oos.writeObject(new ServiceRequestMessage("ProtuneService"));
		oos.flush();
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		if(!(ois.readObject() instanceof ServiceAvailableMessage))
		{// Service "ProtuneService" is not available on the Server, or error occurred.
			System.out.println("Service not available");
			return;
		}
		s.close();
		
		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		
		oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(new DispatcherStartNegotiationMessage(
				new AddressPortPointer(MY_ADDRESS, MY_PORT),
				"ProtuneService"));
		oos.flush();
		
		if(!(ois.readObject() instanceof StartNegotiationMessage)) // Error occurred
			{	
			System.out.println("not StartNegotiationMessage");
			return;
			}
		
		OngoingNegotiationMessage onm = new OngoingNegotiationMessage();
		oos.writeObject(onm);
		oos.flush();
		onm = (OngoingNegotiationMessage)(ois.readObject());
		
		EndNegotiationMessage enm = new EndNegotiationMessage(new SuccessfulNegotiationResult());
		oos.writeObject(enm);
		oos.flush();
		System.out.println(5);
		s.close();
		
		thread.interrupt();
		thread=null;
		System.gc();		
	}
}
