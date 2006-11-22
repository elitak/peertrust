package org.protune.gui;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.protune.net.AddressPortPointer;
import org.protune.net.DispatcherMessage;
import org.protune.net.DispatcherPointer;
import org.protune.net.DispatcherStartNegotiationMessage;
import org.protune.net.EndNegotiationMessage;
import org.protune.net.OngoingNegotiationMessage;
import org.protune.net.ServiceAvailableMessage;
import org.protune.net.ServiceRequestMessage;
import org.protune.net.StartNegotiationMessage;
import org.protune.net.SuccessfulNegotiationResult;

/**
 * 
 * @author cjin, swittler
 */
public class DummyClient {

	private ClientGui clientgui;

	public DummyClient(ClientGui gui) {
		clientgui=gui;
	}

	public String startClient(
			String strAdresse,
			int nPort,
			String strService,
			String strRequest,
			String strServerAdress,
			int nServerPort
	) throws Exception {
		Socket s = new Socket(strServerAdress, nServerPort);
		//System.out.println("new Binding...");
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		ServiceRequestMessage srm = new ServiceRequestMessage(strService);
		//System.out.println("Client sent ServiceRequestMessage...");
		oos.writeObject(srm);
		oos.flush();
		//System.out.println("Sent ServiceRequestMessage: " + srm);
		ObjectInputStream ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		//System.out.println("Client received ServiceAvailableMessage...");
		ServiceAvailableMessage sam = (ServiceAvailableMessage)(ois.readObject());
		//System.out.println("Received ServiceAvailableMessage: " + sam);
		s.close();

		s = new Socket(strServerAdress, nServerPort);
		//System.out.println("new Binding...");
		oos = new ObjectOutputStream(s.getOutputStream());
		AddressPortPointer app = new AddressPortPointer(strAdresse, nPort);
		DispatcherStartNegotiationMessage dsnm = new DispatcherStartNegotiationMessage(app, strService);
		oos.writeObject(dsnm);
		//System.out.println("Client sent DispatcherStartNegotiationMessage..." );
		oos.flush();
		//System.out.println("Sent DispatcherStartNegotiationMessage: " + dsnm);
		ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
		//System.out.println("Client received StartNegotiationMessage...");
		StartNegotiationMessage snm = (StartNegotiationMessage)(ois.readObject());
		//System.out.println("Received StartNegotiationMessage: " + snm);
		s.close();

		boolean bStop = false;
		
		ServerSocket ss = null;
		DispatcherMessage dm;
		long serviceId = 0;
		
		while(!bStop){
			boolean bAccept = clientgui.showQuery("Send OngoingNegotiationMessage? Otherwise EndNegotiationMessage will be send.");
			
			if(bAccept){
				s = new Socket(strServerAdress, nServerPort);
				//System.out.println("new Binding...");
				oos = new ObjectOutputStream(s.getOutputStream());
				OngoingNegotiationMessage onm = new OngoingNegotiationMessage();
				serviceId = ((DispatcherPointer)(((StartNegotiationMessage)snm).getPeerPointer())).getServiceID() ;
				//System.out.println("currentServiceId : " + serviceId);
				dm = new DispatcherMessage(serviceId, onm);
				oos.writeObject(dm);
				//System.out.println("Client sent DispatcherMessage with OngoingNegotiationMessage");
				oos.flush();
				//System.out.println("Sent DispatcherMessage: " + dm);
				//System.out.println("Message in DispatcherMessage is OngoingNegotiationMessage : " + onm);
				s.close();

				if(ss==null) ss = new ServerSocket(nPort);
				s = ss.accept();
				//System.out.println("Client received OngoingNegotiationMessage...");
				ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
				onm = (OngoingNegotiationMessage)(ois.readObject());
				//System.out.println("Received OngoingNegotiationMessage: " + onm);
				s.close();
			}
			else {
				bStop = true;
				
				s = new Socket(strServerAdress, nServerPort);
				oos = new ObjectOutputStream(s.getOutputStream());
				SuccessfulNegotiationResult snr = new SuccessfulNegotiationResult();
				EndNegotiationMessage enm = new EndNegotiationMessage(snr);
				dm = new DispatcherMessage(serviceId, enm);
				oos.writeObject(dm);
				//System.out.println("Client sent DispatcherMessage with EndNegotiationMessage...");
				oos.flush();
				//System.out.println("Sent DispatcherMessage: " + dm);
				//System.out.println("Message in DispatcherMessage is EndNegotiationMessage: " + enm);
				s.close();
			}
		}

		return "";
	}
	
}
