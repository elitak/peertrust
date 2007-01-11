package test.org.protune;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.protune.api.LoadTheoryException;
import org.protune.core.FilteredPolicy;
import org.protune.core.Goal;
import org.protune.core.Notification;
import org.protune.core.ProtuneMessage;
import org.protune.net.AddressPortPointer;
import org.protune.net.DispatcherMessage;
import org.protune.net.DispatcherPeer;
import org.protune.net.DispatcherPointer;
import org.protune.net.DispatcherStartNegotiationMessage;
import org.protune.net.EndNegotiationMessage;
import org.protune.net.OngoingNegotiationMessage;
import org.protune.net.Pointer;
import org.protune.net.ServiceAvailableMessage;
import org.protune.net.ServiceRequestMessage;
import org.protune.net.StartNegotiationMessage;
import org.protune.net.WrongMessageTypeException;

public class ProtuneTest{
	
	public static final String CLIENT_ADDRESS = "localhost";
	public static final int CLIENT_PORT = 1235;
	public static final String SERVER_ADDRESS = "localhost";
	public static final int SERVER_PORT = 1234;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
			
		Runnable runnable = new Runnable() {
			public void run() {
				// Local variables initialisation.
				String[] sa = {"test.org.protune.TestServer"};
				
				try {
					new DispatcherPeer(SERVER_ADDRESS, SERVER_PORT, sa).init();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		};
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
		
		ServerSocket ss = new ServerSocket(CLIENT_PORT);
		
		
		// First step.
		Socket s = new Socket(SERVER_ADDRESS, SERVER_PORT);
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		ServiceRequestMessage srm = new ServiceRequestMessage("test.org.protune.TestServer");
		oos.writeObject(srm);
		oos.flush();

		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		Object o = ois.readObject();
		if(!(o instanceof ServiceAvailableMessage)){
			System.out.println("An unexpected message was received.");
			s.close();
			ss.close();
			return;
		}
		s.close();
		
		
		// Second step.
		s = new Socket(SERVER_ADDRESS, SERVER_PORT);
		oos = new ObjectOutputStream(s.getOutputStream());
		DispatcherStartNegotiationMessage dsnm = new DispatcherStartNegotiationMessage(
				new AddressPortPointer(CLIENT_ADDRESS, CLIENT_PORT),
				"test.org.protune.TestServer"
		);
		oos.writeObject(dsnm);
		oos.flush();
		
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		o = ois.readObject(); 
		if(!(o instanceof StartNegotiationMessage)){
			System.out.println("An unexpected message was received.");
			s.close();
			ss.close();
			return;
		}
		Pointer p = ((StartNegotiationMessage)o).getPeerPointer();
		// Error: a client cannot know that the Pointer is a DispatcherPointer.
		s.close();
		
		
		// Following steps.
		s = new Socket(SERVER_ADDRESS, SERVER_PORT);
		oos = new ObjectOutputStream(s.getOutputStream());
		Notification[] na = new Notification[0];
		DispatcherMessage dm = null;
		try {
			dm = new DispatcherMessage(
					((DispatcherPointer)p).getServiceID(),
					new ProtuneMessage(
							new Goal("[execute(access(Resource))]"),
							new FilteredPolicy("[]"),
							na
					)
			);
		}
		catch (LoadTheoryException e) {
			// It should not happen.
		}
		oos.writeObject(dm);
		oos.flush();
		s.close();

		TestClient tc = new TestClient(p);
		
		s=ss.accept();
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		o = ois.readObject();
		
		while (o instanceof ProtuneMessage) {
			try {
				tc.perform((OngoingNegotiationMessage) o);
			} catch (IOException ioe) {
			} catch (WrongMessageTypeException wmte) {
			}
			s.close();
			s = ss.accept();
			ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
			o = ois.readObject();
		}
		
		
		// Conclusion.
		if(o instanceof EndNegotiationMessage)
			System.out.println(((EndNegotiationMessage)o).getNegotiationResult());
		else System.out.println("An unexpected message was received.");
		s.close();
		ss.close();
		
	}

}
