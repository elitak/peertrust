package org.peertrust.demo.credential_distribution;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;

import org.peertrust.TrustClient;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASPObject;

import org.peertrust.net.AbstractFactory;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;
import org.xml.sax.SAXException;


public class CredentialDistributionServer implements PTComASPMessageListener{

	private CredentialStore credentialStore;
	private NetClient netClient;
	
	public CredentialDistributionServer(){
		credentialStore= new CredentialStore();
	}
	
	public void PTMessageReceived(Serializable message,Peer source, Peer target) {
		if(message instanceof CredentialRequest){
			String credName=((CredentialRequest)message).getName();
			String value=credentialStore.getCredential(credName);
			CredentialResponse credResp=
				new CredentialResponse(credName,value);
			//swap target and source since we have now the server view
			PTCommunicationASPObject.send(netClient,credResp,target,source);
		}
	}
	
	public void setup(String credentialStoreXmlFile, TrustClient trustClient) throws NullPointerException, SAXException, IOException, ParserConfigurationException{
		if(credentialStoreXmlFile==null){
			throw new NullPointerException("Parameter credentialStoreXmlFile must not be null");
		}
		
		if(trustClient==null){
			throw new NullPointerException("Parameter trustClient must not be null");
		}
		
		credentialStore.setup(credentialStoreXmlFile);
		Object eventL=
			trustClient.getComponent(Vocabulary.EventListener);
		if(eventL instanceof PTCommunicationASP){
			((PTCommunicationASP)eventL).registerPTComASPMessageListener(this,CredentialRequest.class);
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
		
	}

}
