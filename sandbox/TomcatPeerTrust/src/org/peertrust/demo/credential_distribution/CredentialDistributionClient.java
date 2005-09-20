package org.peertrust.demo.credential_distribution;

import java.io.Serializable;

import org.peertrust.TrustClient;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.common.Executable;
import org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASPObject;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;

public class CredentialDistributionClient implements PTComASPMessageListener {

	private NetClient netClient;
	
	private Executable executeOnCredentialResponse;
	
	
	public void PTMessageReceived(Serializable message, Peer source, Peer target) {
		if(message instanceof CredentialResponse){
			executeOnCredentialResponse.execute();
		}
	}

	public void requestCredential(String name, Peer source, Peer target){
		CredentialRequest req= 
			new CredentialRequest(name);
		PTCommunicationASPObject.send(netClient,req,source,target);
	}
	
	public void setup(	TrustClient trustClient, 
						Executable executeOnCredentialResponse){
		
		if(executeOnCredentialResponse==null){
			throw new NullPointerException("Parameter executeOnCredentialResponse must not be null");
		}
		
		if(trustClient==null){
			throw new NullPointerException("Parameter trustClient must not be null");
		}
		//
		this.executeOnCredentialResponse=executeOnCredentialResponse;
		
		//register to get response
		Object eventL=
			trustClient.getComponent(Vocabulary.EventListener);
		if(eventL instanceof PTCommunicationASP){
			((PTCommunicationASP)eventL).registerPTComASPMessageListener(this,CredentialResponse.class);
		}else{
			throw new Error("PTCommunicationASP expected as EventListener for pt but got "+eventL);
		}
		
		netClient=
			((AbstractFactory)
					trustClient.getComponent(Vocabulary.CommunicationChannelFactory)).createNetClient();
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
