package org.peertrust.demo.credential_distribution;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;

import org.peertrust.TrustClient;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;

import org.peertrust.net.Peer;
import org.xml.sax.SAXException;

/**
 * Provide The server side mechanisms to to distribute credentials.
 * A credential store is used to hold the credntials.
 * A PTCommunicationASP is used to receive requests and to send the credential to the requester.
 *  
 * @author Patrice Congo (token77)
 *
 */
public class CredentialDistributionServer implements PTComASPMessageListener{

	/**
	 *a store of credentials 
	 */
	private CredentialStore credentialStore;
	
	/**
	 * Provides generic comunication on top of the peertrust comunication
	 */
	private PTCommunicationASP comASP;
	
//	/** used to send the credential*/
//	private NetClient netClient;
//	
	/** create a blank CredentialDistributionServer*/
	public CredentialDistributionServer(){
		credentialStore= new CredentialStore();
	}
	/**
	 * Implemented to received the CredentialRequest and 
	 * send back the requested credential.
	 */
	public void PTMessageReceived(
							Serializable message,
							Peer source, 
							Peer target) {
		if(message instanceof CredentialRequest){
			String credName=((CredentialRequest)message).getName();
			String value=credentialStore.getCredentialValue(credName,source);
			CredentialResponse credResp=
				new CredentialResponse(credName,value);
			System.out.println("Sending credential:\n"+credResp);
			//swap target and source since we have now the server view
			//PTCommunicationASPObject.send(netClient,credResp,target,source);
			comASP.send(credResp,target,source);
		}
	}
	
	/**
	 * Setups this credential distribution server.
	 * 
	 * @param 	credentialStoreXmlFile -- the path of the xml file containing the
	 * 			credentials specifications
	 * 
	 * @param 	trustClient -- the trust client which net client will be used 
	 * 			to send back the answer and which EventListener whill be used 
	 * 			to receive the requests.
	 *  
	 * @throws NullPointerException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public void setup(
				String credentialStoreXmlFile, 
				TrustClient trustClient) throws NullPointerException, SAXException, IOException, ParserConfigurationException{
		if(credentialStoreXmlFile==null){
			throw new NullPointerException("Parameter credentialStoreXmlFile must not be null");
		}
		
		if(trustClient==null){
			throw new NullPointerException("Parameter trustClient must not be null");
		}
		
		credentialStore.setup(credentialStoreXmlFile);
		System.out.println("CredentialStore:"+credentialStore);
		Object eventL=
			trustClient.getComponent(Vocabulary.EventListener);
		if(eventL instanceof PTCommunicationASP){
			((PTCommunicationASP)eventL).registerPTComASPMessageListener(this,CredentialRequest.class);
			comASP=(PTCommunicationASP)eventL;
		}else{
			throw new Error("PTCommunicationASP expected as EventListener for pt but got "+eventL);
		}
//		
//		netClient=
//			((AbstractFactory)
//					trustClient.getComponent(Vocabulary.CommunicationChannelFactory)).createNetClient();
		
	}
	
	
	
	/**
	 * @return Returns the credentialStore.
	 */
	public CredentialStore getCredentialStore() {
		return credentialStore;
	}
	
}
