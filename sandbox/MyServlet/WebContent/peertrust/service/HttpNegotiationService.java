package peertrust.service;

import java.awt.TextField;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.protune.api.FilteredPolicy;
import org.protune.api.LoadTheoryException;
import org.protune.core.ProtuneMessage;
import org.protune.net.DispatcherMessage;
import org.protune.net.EndNegotiationMessage;
import org.protune.net.NegotiationMessage;
import org.protune.net.OngoingNegotiationMessage;
import org.protune.net.Pointer;
import org.protune.net.Service;
import org.protune.net.UnsuccessfulNegotiationResult;
import org.protune.net.WrongMessageTypeException;

import peertrust.applet.IAddToLog;
import peertrust.common.CredentialFactory;
import peertrust.common.interfaces.ICredential;
import peertrust.common.pointer.HttpServerPointer;

//TODO: replace dummy things
//TODO: Refactoring eval

/**
 * The Client negotiation service. Used by the applet for trust negotiation.
 * Dummy implementation.
 * @see PeerTrustApplet
 * @author Sebastian Wittler
 */
public class HttpNegotiationService extends Service {
	// Hashtable that stores the credentials and the policies protecting them
	private Hashtable<String,String> hashtableCredStore=new Hashtable<String,String>();
	// Hashtable that stores the negotiation status
	private Hashtable<String,Set> hashtableNegotiation=new Hashtable<String,Set>();
	private IAddToLog log;
private List<String> listSent=new LinkedList<String>();

	/**
	 * Constructor.
	 * @param strKeys The credentials the client has.
	 * @param strValue The policies that protect these credentials.
	 * @param p The pointer (communication part) of the server.
	 */
	public HttpNegotiationService(String strKeys[],String strValue[],Pointer p,
		IAddToLog log) {
		for(int i=0;i<Math.min(strKeys.length,strValue.length);i++)
			hashtableCredStore.put(strKeys[i],strValue[i]);
		otherPeer=p;
		this.log=log;
	}

	/**
	 * @see Service.eval
	 */
	public NegotiationMessage eval(OngoingNegotiationMessage onm) throws WrongMessageTypeException {
		// If the message from the server is not correct, the negotiation is not
		// successful
//TODO: pointer checken
		if(!(onm instanceof ProtuneMessage)) {
//TODO: tell the user
			return new EndNegotiationMessage(new UnsuccessfulNegotiationResult());
		}
		// Get FilteredPolicies out of message
		ProtuneMessage pm=(ProtuneMessage)onm;
		FilteredPolicy policies[]=pm.getFilteredPolicies();
		// List of credentials the client discloses to the server
		List<FilteredPolicy> listPolicies=new LinkedList<FilteredPolicy>();
		// Iterate through all FilteredPolicies
		for(int i=0;i<policies.length;i++) {
			// Get the credential the current FilteredPolicy requests from client
			String strCred=policies[i].toString();
if(listSent.contains(strCred)) {
log.addToLog("Received Credential: "+strCred);
listSent.remove(strCred);
}
else
log.addToLog("Received request for credential: "+strCred);
			// Get the clients policies that protect this credential
			String strPolicies=hashtableCredStore.get(strCred);
//TODO: Hat Client wirklich Credential?
			// If these do not exist
			if(strPolicies!=null) {
				// If no policies protect this credential
				if(strPolicies.length()==0) {
					try {
						// Add credential to the ones that should be disclosed to the
						// server
						listPolicies.add(new FilteredPolicy(strCred));
log.addToLog("Send Credential: "+strCred);
					} catch (LoadTheoryException e) {
//TODO: tell the user
						return new EndNegotiationMessage(new UnsuccessfulNegotiationResult());
					}
				}
				// If there are policies that protect the credential
				else {
					// Extract these Policies
					String strProtectingPolicies[]=strPolicies.split(",");
					// Set that contains the policies that protect the credential
					Set<ICredential> setCred=new HashSet<ICredential>();
					// Iterate through policies
					for(int j=0;j<strProtectingPolicies.length;j++) {
						// Add policy to set
						setCred.add(CredentialFactory.getInstance().createCredential(strProtectingPolicies[i]));
						try {
							// Add the credentials that fulfill the policy to the
							// ones that should be disclosed tp the server (request)
							listPolicies.add(new FilteredPolicy(strProtectingPolicies[i]));
log.addToLog("Send request for credential: "+strProtectingPolicies[i]);
listSent.add(strProtectingPolicies[i]);
						} catch (LoadTheoryException e) {
//TODO: tell the user
							return new EndNegotiationMessage(new UnsuccessfulNegotiationResult());
						}
					}
					// Store the requested credentials in the negotiation state
					hashtableNegotiation.put(strCred,setCred);
				}
			}
			// If policies exist that protect the credential
			else {
				// Iterate through the negotiation state hashtable
				Enumeration _enum=hashtableNegotiation.keys();
				while(_enum.hasMoreElements()) {
					String key=(String)_enum.nextElement();
					// Get the policies for the current key
					Set setPolicies=(Set)hashtableNegotiation.get(key);
					Iterator iter=setPolicies.iterator();
					// Iterate through these policies
					while(iter.hasNext())
						// If credential shown by server matches one of the policies
						// protecting anything, remove that policy
						if(strCred.equals(((ICredential)iter.next()).toStringRepresentation())) {
							iter.remove();
							break;
						}
					// If no policies protect a credential any more (server fullfilled
					// all policies)
					if(setPolicies.isEmpty()) {
						// Remove entry from negotiation state
						hashtableNegotiation.remove(key);
						try {
							// credential is added to the others that
							// should be disclosed to the server
							listPolicies.add(new FilteredPolicy(key));
log.addToLog("Send Credential: "+key);
						} catch (LoadTheoryException e) {
//TODO: tell the user
							return new EndNegotiationMessage(new UnsuccessfulNegotiationResult());
						}
					}
				}
			}
		}
		// Returns the message that should be sent to the server (contains all
		// credentials that should be disclosed
		return new ProtuneMessage((FilteredPolicy[])listPolicies.toArray(new FilteredPolicy[0]),null);
	}

	/**
	 * @see Service.perform
	 */
	public NegotiationMessage perform(OngoingNegotiationMessage onm) throws IOException, WrongMessageTypeException{
		// Perform negotiation
		NegotiationMessage toSend = eval(onm);
		
		// Send answer to the server
		long service_id=((HttpServerPointer)otherPeer).getServiceID();
		otherPeer.sendMessage(new DispatcherMessage(service_id,toSend));
		
		return toSend;
	}
}