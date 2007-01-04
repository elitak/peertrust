package test.org.protune.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.protune.api.ActionWellPerformed;
import org.protune.api.DummyAction;
import org.protune.api.FilteredPolicy;
import org.protune.api.Goal;
import org.protune.api.LoadTheoryException;
import org.protune.api.Notification;
import org.protune.core.ProtuneMessage;
import org.protune.net.AddressPortPointer;
import org.protune.net.DispatcherMessage;
import org.protune.net.DispatcherPeer;
import org.protune.net.DispatcherPointer;
import org.protune.net.DispatcherStartNegotiationMessage;
import org.protune.net.EndNegotiationMessage;
import org.protune.net.ServiceAvailableMessage;
import org.protune.net.ServiceRequestMessage;
import org.protune.net.StartNegotiationMessage;

public class ProtuneTest{
	
	public static final String MY_ADDRESS = "localhost";
	public static final int MY_PORT = 1235;
	public static final String PROTUNE_ADDRESS = "localhost";
	public static final int PROTUNE_PORT = 1234;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
			
		Runnable runnable = new Runnable() {
			public void run() {
				// Local variables initialisation.
				String[] sa = {"org.protune.net.TestProtuneService"};
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
		//app.sendMessage(new ServiceRequestMessage("org.protune.net.TestProtuneService"));
		
		Socket s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Binding...");
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		ServiceRequestMessage srm = new ServiceRequestMessage("org.protune.net.TestProtuneService");
		//System.out.println("Client sent ServiceRequestMessage...");
		oos.writeObject(srm);
		oos.flush();
		//System.out.println("Sent ServiceRequestMessage: " + srm);
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		//System.out.println("Client received ServiceAvailableMessage...");
		ServiceAvailableMessage sam = (ServiceAvailableMessage)(ois.readObject());
		//System.out.println("Received ServiceAvailableMessage: " + sam);
		s.close();
		
		
		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		DispatcherStartNegotiationMessage dsnm = new DispatcherStartNegotiationMessage(
				new AddressPortPointer(MY_ADDRESS, MY_PORT),
				"org.protune.net.TestProtuneService"
		);
		oos.writeObject(dsnm);
		//System.out.println("Client sent DispatcherStartNegotiationMessage..." );
		oos.flush();
		//System.out.println("Sent DispatcherStartNegotiationMessage: " + dsnm);
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		//System.out.println("Client received StartNegotiationMessage...");
		StartNegotiationMessage snm = (StartNegotiationMessage)(ois.readObject());
		//System.out.println("Received StartNegotiationMessage: " + snm);
		s.close();
		
		
		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		//System.out.println("currentServiceId : "+serviceId);
		Notification[] na = new Notification[0];
		DispatcherMessage dm = null;
		try {
			dm = new DispatcherMessage(
					((DispatcherPointer)(((StartNegotiationMessage)snm).getPeerPointer())).getServiceID(),
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
		//System.out.println("Client sent DispatcherMessage with ProtuneMessage");
		oos.flush();
		//System.out.println("Sent DispatcherMessage: " + dm);
		//System.out.println("Message in DispatcherMessage is OngoingNegotiationMessage : ");
		s.close();
		s=ss.accept();
		//System.out.println("Client received OngoingNegotiationMessage...");
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		ProtuneMessage pm = (ProtuneMessage)(ois.readObject());
		//System.out.println("Received ProtuneMessage: " + pm);
		s.close();
		

		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		//System.out.println("currentServiceId : "+serviceId);
		na = new Notification[]{new ActionWellPerformed(new DummyAction("clientCredential2"))};
		dm = null;
		try {
			dm = new DispatcherMessage(
					((DispatcherPointer)(((StartNegotiationMessage)snm).getPeerPointer())).getServiceID(),
					new ProtuneMessage(
							new Goal("[execute(access(Resource))]"),
							new FilteredPolicy("[rule(execute(clientCredential2),[]),rule(metapolicy(execute(clientCredential2),type,abbreviation),[]),rule(execute(clientCredential1),[abbreviationPublicClient(result2),abbreviationPrivateClient(result3),stateQueryPublicClient1,stateQueryPrivateClient1,provisionalSelfClient1(result2,result3),serverCredential1]),rule(metapolicy(execute(clientCredential1),type,abbreviation),[]),rule(abbreviationPublicClient(result2),[stateQueryPublicClient2,stateQueryPrivateClient2,provisionalSelfClient2(result2),serverCredential2]),rule(metapolicy(abbreviationPublicClient(result2),type,abbreviation),[]),rule(stateQueryPublicClient2,[]),rule(metapolicy(stateQueryPublicClient2,type,stateQuery),[]),rule(stateQueryPrivateClient2,[]),rule(metapolicy(stateQueryPrivateClient2,type,stateQuery),[]),rule(metapolicy(provisionalSelfClient2(result2),type,provisional),[]),rule(metapolicy(provisionalSelfClient2(result2),actor,self),[]),rule(metapolicy(provisionalSelfClient2(result2),executed,true),[]),rule(metapolicy(serverCredential2,type,provisional),[]),rule(metapolicy(serverCredential2,actor,peer),[]),rule(metapolicy(abbreviationPrivateClient(result3),type,abbreviation),[]),rule(metapolicy(abbreviationPrivateClient(result3),blurred,true),[]),rule(stateQueryPublicClient1,[]),rule(metapolicy(stateQueryPublicClient1,type,stateQuery),[]),rule(stateQueryPrivateClient1,[]),rule(metapolicy(stateQueryPrivateClient1,type,stateQuery),[]),rule(metapolicy(provisionalSelfClient1(result2,result3),type,provisional),[]),rule(metapolicy(provisionalSelfClient1(result2,result3),actor,self),[]),rule(metapolicy(provisionalSelfClient1(result2,result3),executed,true),[]),rule(metapolicy(serverCredential1,type,provisional),[]),rule(metapolicy(serverCredential1,actor,peer),[])]"),
							na
					)
			);
		}
		catch (LoadTheoryException e) {
			// It should not happen.
		}
		oos.writeObject(dm);
		//System.out.println("Client sent DispatcherMessage with ProtuneMessage");
		oos.flush();
		//System.out.println("Sent DispatcherMessage: " + dm);
		//System.out.println("Message in DispatcherMessage is OngoingNegotiationMessage : ");
		s.close();
		s=ss.accept();
		//System.out.println("Client received OngoingNegotiationMessage...");
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		pm = (ProtuneMessage)(ois.readObject());
		//System.out.println("Received ProtuneMessage: " + pm);
		s.close();
		

		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		//System.out.println("currentServiceId : "+serviceId);
		na = new Notification[]{new ActionWellPerformed(new DummyAction("clientCredential3"))};
		dm = null;
		try {
			dm = new DispatcherMessage(
					((DispatcherPointer)(((StartNegotiationMessage)snm).getPeerPointer())).getServiceID(),
					new ProtuneMessage(
							new Goal("[execute(access(Resource))]"),
							new FilteredPolicy("[rule(execute(clientCredential2),[]),rule(metapolicy(execute(clientCredential2),type,abbreviation),[]),rule(execute(clientCredential1),[abbreviationPublicClient(result2),abbreviationPrivateClient(result3),stateQueryPublicClient1,stateQueryPrivateClient1,provisionalSelfClient1(result2,result3),serverCredential1]),rule(metapolicy(execute(clientCredential1),type,abbreviation),[]),rule(abbreviationPublicClient(result2),[stateQueryPublicClient2,stateQueryPrivateClient2,provisionalSelfClient2(result2),serverCredential2]),rule(metapolicy(abbreviationPublicClient(result2),type,abbreviation),[]),rule(stateQueryPublicClient2,[]),rule(metapolicy(stateQueryPublicClient2,type,stateQuery),[]),rule(stateQueryPrivateClient2,[]),rule(metapolicy(stateQueryPrivateClient2,type,stateQuery),[]),rule(metapolicy(provisionalSelfClient2(result2),type,provisional),[]),rule(metapolicy(provisionalSelfClient2(result2),actor,self),[]),rule(metapolicy(provisionalSelfClient2(result2),executed,true),[]),rule(metapolicy(serverCredential2,type,provisional),[]),rule(metapolicy(serverCredential2,actor,peer),[]),rule(metapolicy(abbreviationPrivateClient(result3),type,abbreviation),[]),rule(metapolicy(abbreviationPrivateClient(result3),blurred,true),[]),rule(stateQueryPublicClient1,[]),rule(metapolicy(stateQueryPublicClient1,type,stateQuery),[]),rule(stateQueryPrivateClient1,[]),rule(metapolicy(stateQueryPrivateClient1,type,stateQuery),[]),rule(metapolicy(provisionalSelfClient1(result2,result3),type,provisional),[]),rule(metapolicy(provisionalSelfClient1(result2,result3),actor,self),[]),rule(metapolicy(provisionalSelfClient1(result2,result3),executed,true),[]),rule(metapolicy(serverCredential1,type,provisional),[]),rule(metapolicy(serverCredential1,actor,peer),[]),rule(execute(clientCredential3),[]),rule(metapolicy(execute(clientCredential3),type,abbreviation),[])]"),
							na
					)
			);
		}
		catch (LoadTheoryException e) {
			// It should not happen.
		}
		oos.writeObject(dm);
		//System.out.println("Client sent DispatcherMessage with ProtuneMessage");
		oos.flush();
		//System.out.println("Sent DispatcherMessage: " + dm);
		//System.out.println("Message in DispatcherMessage is OngoingNegotiationMessage : ");
		s.close();
		s=ss.accept();
		//System.out.println("Client received OngoingNegotiationMessage...");
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		pm = (ProtuneMessage)(ois.readObject());
		//System.out.println("Received ProtuneMessage: " + pm);
		s.close();
		

		s = new Socket(PROTUNE_ADDRESS, PROTUNE_PORT);
		//System.out.println("new Socket Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		//System.out.println("currentServiceId : "+serviceId);
		na = new Notification[]{new ActionWellPerformed(new DummyAction("clientCredential1"))};
		dm = null;
		try {
			dm = new DispatcherMessage(
					((DispatcherPointer)(((StartNegotiationMessage)snm).getPeerPointer())).getServiceID(),
					new ProtuneMessage(
							new Goal("[execute(access(Resource))]"),
							new FilteredPolicy("[rule(execute(clientCredential2),[]),rule(metapolicy(execute(clientCredential2),type,abbreviation),[]),rule(execute(clientCredential1),[abbreviationPublicClient(result2),abbreviationPrivateClient(result3),stateQueryPublicClient1,stateQueryPrivateClient1,provisionalSelfClient1(result2,result3),serverCredential1]),rule(metapolicy(execute(clientCredential1),type,abbreviation),[]),rule(abbreviationPublicClient(result2),[stateQueryPublicClient2,stateQueryPrivateClient2,provisionalSelfClient2(result2),serverCredential2]),rule(metapolicy(abbreviationPublicClient(result2),type,abbreviation),[]),rule(stateQueryPublicClient2,[]),rule(metapolicy(stateQueryPublicClient2,type,stateQuery),[]),rule(stateQueryPrivateClient2,[]),rule(metapolicy(stateQueryPrivateClient2,type,stateQuery),[]),rule(metapolicy(provisionalSelfClient2(result2),type,provisional),[]),rule(metapolicy(provisionalSelfClient2(result2),actor,self),[]),rule(metapolicy(provisionalSelfClient2(result2),executed,true),[]),rule(metapolicy(serverCredential2,type,provisional),[]),rule(metapolicy(serverCredential2,actor,peer),[]),rule(metapolicy(abbreviationPrivateClient(result3),type,abbreviation),[]),rule(metapolicy(abbreviationPrivateClient(result3),blurred,true),[]),rule(stateQueryPublicClient1,[]),rule(metapolicy(stateQueryPublicClient1,type,stateQuery),[]),rule(stateQueryPrivateClient1,[]),rule(metapolicy(stateQueryPrivateClient1,type,stateQuery),[]),rule(metapolicy(provisionalSelfClient1(result2,result3),type,provisional),[]),rule(metapolicy(provisionalSelfClient1(result2,result3),actor,self),[]),rule(metapolicy(provisionalSelfClient1(result2,result3),executed,true),[]),rule(metapolicy(serverCredential1,type,provisional),[]),rule(metapolicy(serverCredential1,actor,peer),[]),rule(execute(clientCredential3),[]),rule(metapolicy(execute(clientCredential3),type,abbreviation),[])]"),
							na
					)
			);
		}
		catch (LoadTheoryException e) {
			// It should not happen.
		}
		oos.writeObject(dm);
		//System.out.println("Client sent DispatcherMessage with ProtuneMessage");
		oos.flush();
		//System.out.println("Sent DispatcherMessage: " + dm);
		//System.out.println("Message in DispatcherMessage is OngoingNegotiationMessage : ");
		s.close();
		s=ss.accept();
		//System.out.println("Client received OngoingNegotiationMessage...");
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		System.out.println(((EndNegotiationMessage)ois.readObject()).getNegotiationResult().getClass());
		
		s.close();
		ss.close();
	}

}
