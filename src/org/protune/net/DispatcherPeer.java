package org.protune.net;

import java.net.*;
import java.io.*;
import java.util.Hashtable;

/**
 * When a service is provided by a node and made available to the outer world throught the Internet,
 * most likely a number of clients will request the service concurrently. The node providing the service
 * should hence find a way to handle these concurrent requests. Two possible opposite solutions are
 * <ul>
 * <li>letting a client wait until the previous clients' requests are handled</li>
 * <li>handling the concurrent requests concurrently (i.e. creating a new thread for each new
 * request)</li>
 * </ul>
 * At present our choice tries to keep the things as simple as possible, according to the constraints
 * set by the characteristics of our application. In the <i>Protune</i> system we are mostly interested
 * in negotiations among (pairs of) peers. Since a negotiation may consist of many steps, it would not
 * be realistic letting a client wait until the previous negotiations are terminated. Therefore we chose
 * to handle the negotiations with clients in an interleaved way, i.e.
 * <ul>
 * <li>each client has to wait until the previous clients' <em>negotiation steps</em> (and not
 * <em>requests</em>) are handled</li>
 * <li>as soon as the previous clients' negotiation steps are handled, the current negotiation step of
 * the client is handled</li>
 * </ul>
 * Such an approach requires of course that the system keeps track of each ongoing negotiation and is
 * able to retrieve the status of the previous negotiation as soon as a new negotiation message is
 * received (see {@link org.protune.net.DispatcherPointer} for further details on this).<br />
 * As stated before, in the Protune system we are mostly interested in negotiations, but we do not
 * mind just supporting {@link org.protune.core.ProtuneService}: with a bit more effort, the
 * communication infrastructure can be made much more general by providing support to a number of
 * services the node may want to make available. To this goal it suffices that the
 * <tt>DispatcherPeer</tt> is aware of the services the node wants to make available and is able to
 * provide them (or at least is able to suggest which entity will be able to provide them).<br />
 * At this point we are able to sketch the main steps a negotiation involving a <tt>DispatcherPeer</tt>
 * consists of (see Fig. 1).
 * <img src="./DispatcherPeerNegotiation.gif" alt="Negotiation involving a DispatcherPeer" />
 * At the beginning of the negotiation a client asks the <tt>DispatcherPeer</tt> whether a service is
 * available. If it is so, the negotiation starts and goes on until one of the peers decides to
 * terminate it (by sending the other an {@link org.protune.net.EndNegotiationMessage}).
 * <dl>
 * <dt><b>NOTE:</b></dt>
 * <dd>At the beginning of the negotiation a <tt>DispatcherPeer</tt> requires to be sent not only a
 * {@link org.protune.net.StartNegotiationMessage} but a subclass of it (namely
 * {@link org.protune.net.DispatcherStartNegotiationMessage}).</dd>
 * </dl>
 * @author jldecoi
 */
public class DispatcherPeer implements Peer {
	
	AddressPortPointer selfPointer;
	ServerSocket ss;
	String[] availableServices;
	Hashtable<Long, Service> runningServices = new Hashtable<Long, Service>();
	Hashtable<Long, NegotiationResult> negotiationResults = new Hashtable<Long, NegotiationResult>();
	long currentRunningServiceID = 0;
	
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
			System.out.println(dsnm.getRequestedService());
			Service s =
				(Service) Class.forName(dsnm.getRequestedService()).getConstructor(ca).newInstance(oa);
			System.out.println("Created: " + s);
			runningServices.put(currentRunningServiceID, s);
			currentRunningServiceID++;
		}
		catch(IOException ioe){}
		catch(ClassNotFoundException cnfe){System.out.println(1);
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
			NegotiationMessage nm = s.perform(onm);
			if(nm instanceof EndNegotiationMessage){
				runningServices.remove(l);
				negotiationResults.put(l, ((EndNegotiationMessage)nm).getNegotiationResult());
			}
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
