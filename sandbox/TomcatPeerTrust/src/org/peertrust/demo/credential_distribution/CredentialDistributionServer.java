package org.peertrust.demo.credential_distribution;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;

import org.peertrust.TrustClient;
import org.peertrust.config.Configurable;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.demo.resourcemanagement.StringWrapper;
import org.peertrust.exception.ConfigurationException;

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
public class CredentialDistributionServer 
						implements 	PTComASPMessageListener,
									Configurable
{

	/**
	 *a store of credentials 
	 */
	private CredentialStore credentialStore;
	
	/**
	 * Provides generic comunication on top of the peertrust comunication
	 */
	private PTCommunicationASP communicationASP;
	
	/** 
	 * Path of the xml setup file
	 */
	private StringWrapper setupFilePath;
		
	/** 
	 * Create a virgin CredentialDistributionServer.
	 * The creation process is to complete by:
	 * <ul>
	 * 	<li/>Setting the communicationASP and setupFilePath
	 * 	<lu/>and then Calling init(). 
	 * </ul>
	 */
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
			//System.out.println("Sending credential:\n"+credResp);
			//swap target and source since we have now the server view
			communicationASP.send(credResp,target,source);
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
			communicationASP=(PTCommunicationASP)eventL;
		}else{
			throw new Error("PTCommunicationASP expected as EventListener for pt but got "+eventL);
		}
		
	}
	
	public void setup() 
			throws	NullPointerException, 
					SAXException, 
					IOException, 
					ParserConfigurationException
	{
		String credentialStoreXmlFile=setupFilePath.getWrappedString();
		if(credentialStoreXmlFile==null){
			throw new NullPointerException(
					"Parameter credentialStoreXmlFile must not be null");
		}
	
		if(communicationASP==null){
			throw new Error("PTCommunicationASP must not be null");
		}
		
		credentialStore.setup(credentialStoreXmlFile);
		System.out.println("CredentialStore:"+credentialStore);
		communicationASP.registerPTComASPMessageListener(this,CredentialRequest.class);
	}
	
	/**
	 * @return Returns the credentialStore.
	 */
	public CredentialStore getCredentialStore() {
		return credentialStore;
	}
	
	
	
	/**
	 * @return Returns the communicationASP.
	 */
	public PTCommunicationASP getCommunicationASP() {
		return communicationASP;
	}
	/**
	 * @param communicationASP The communicationASP to set.
	 */
	public void setCommunicationASP(PTCommunicationASP communicationASP) {
		this.communicationASP = communicationASP;
	}
	
	
	
	
	
	/**
	 * @return Returns the setupFilePath.
	 */
	public StringWrapper getSetupFilePath() {
		return setupFilePath;
	}
	
	
	/**
	 * @param setupFilePath The setupFilePath to set.
	 */
	public void setSetupFilePath(StringWrapper setupFilePath) {
		this.setupFilePath = setupFilePath;
	}
	
	
	/**
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		if(communicationASP==null){
			throw  new ConfigurationException(
						"communicationASP not set at"+this.getClass());
		}
		
		if(setupFilePath==null){
			throw  new ConfigurationException(
					"setupFilePath not set at"+this.getClass());
		}
		
		try {
			setup();
		} catch (NullPointerException e) {
			throw new ConfigurationException(
					"could not setup the credential server internals",
					e);
		} catch (SAXException e) {
			throw new ConfigurationException(
					"could not setup the credential server internals",
					e);
		} catch (IOException e) {
			throw new ConfigurationException(
					"could not setup the credential server internals",
					e);
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException(
					"could not setup the credential server internals",
					e);
		}

	}
	
	
	
}
