package org.protune.net;

import java.net.*;
import java.io.*;
import java.util.Hashtable;

public class DispatcherPeer implements Peer {
	
	AddressPortPointer selfPointer;
	ServerSocket ss;
	String[] availableServices;
	Hashtable<Long, Service> runningServices = new Hashtable<Long, Service>();
	Hashtable<Long, NegotiationResult> negotiationResults = new Hashtable<Long, NegotiationResult>();
	long currentRunningServiceID = -1;
	
	public DispatcherPeer(AddressPortPointer app, String[] sa) throws IOException{
		selfPointer = app;
		ss = new ServerSocket(app.getPort());
		availableServices = sa;
	}
	
	public DispatcherPeer(String s, int i, String[] sa) throws IOException{
		this(new AddressPortPointer(s, i), sa);
	}
	
	public Pointer getPointer(){
		return selfPointer;
	}
	
	public void init() throws IOException, ClassNotFoundException{
		for(;;){
			Socket s = ss.accept();
			ObjectInputStream ois = new ObjectInputStream(new DataInputStream(s.getInputStream()));
			Object o = ois.readObject();
			
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			if(o instanceof ServiceRequestMessage) handle((ServiceRequestMessage)o, oos);
			else if(o instanceof DispatcherStartNegotiationMessage)
				handle((DispatcherStartNegotiationMessage)o, oos);
			else if(o instanceof DispatcherMessage){
				DispatcherMessage dm = (DispatcherMessage)o;
				NegotiationMessage nm = dm.getNegotiationMessage();
				if(nm instanceof OngoingNegotiationMessage)
					handle(dm.getServiceID(), (OngoingNegotiationMessage)nm);
				if(nm instanceof EndNegotiationMessage)
					handle(dm.getServiceID(), (EndNegotiationMessage)nm);
			}
			// If o does not belong to any of the previously listed classes, it is simply discarded.

			s.close();
		}
	}

	/**
	 * If an error occurs, the negotiation is aborted.
	 * @param srm
	 * @param oos
	 * @throws IOException
	 */
	void handle(ServiceRequestMessage srm, ObjectOutputStream oos){
		int i;
		for(i=0; i<availableServices.length; i++)
			if(srm.getRequestedService().compareTo(availableServices[i])==0) break;
		try{
			if(i==availableServices.length) oos.writeObject(new ServiceNotAvailableMessage());
			else oos.writeObject(new ServiceAvailableMessage());
			
			oos.flush();
		}
		catch(IOException ioe){}
	}
	
	/**
	 * If an error occurs, the negotiation is aborted.
	 * @param dsnm
	 * @param oos
	 * @throws IOException
	 */
	void handle(DispatcherStartNegotiationMessage dsnm, ObjectOutputStream oos){
		try{
			oos.writeObject(
				new StartNegotiationMessage(
					new DispatcherPointer(selfPointer, currentRunningServiceID)
				)
			);
			oos.flush();

			Class[] ca = { Pointer.class };
			Object[] oa = { dsnm.getPeerPointer() };
			Service s =
				(Service) Class.forName(dsnm.getRequestedService()).getConstructor(ca).newInstance(oa);
			currentRunningServiceID++;
			runningServices.put(currentRunningServiceID, s);
		}
		catch(IOException ioe){}
		catch(ClassNotFoundException cnfe){
			// If the communication protocol is respected this exception should be never thrown.
		}catch(Exception e){
			// If each subclass of {@link org.protune.net.Service} implements the constructor
			// &lt;subclassName&gt;({@link org.protune.net.Pointer}) this exception should be never
			// thrown.
		}
	}
	
	/**
	 * If an error occurs, the negotiation is aborted.
	 * @param l
	 * @param onm
	 */
	void handle(long l, OngoingNegotiationMessage onm){
		Service s = runningServices.get(l);
		if(s != null) try{
			if(!s.perform(onm)) runningServices.remove(l);
		} catch(IOException ioe){
			runningServices.remove(l);
		} catch(WrongMessageTypeException wmte){
			runningServices.remove(l);
		}
	}
	
	void handle(long l, EndNegotiationMessage enm){
		runningServices.remove(l);
		negotiationResults.put(l, enm.getNegotiationResult());
	}
	
}
