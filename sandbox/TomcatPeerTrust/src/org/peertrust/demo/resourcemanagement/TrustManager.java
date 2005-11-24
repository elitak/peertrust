/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.peertrust.TrustClient;
import org.peertrust.config.Configurable;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.credential_distribution.CredentialDistributionServer;
import org.peertrust.demo.credential_distribution.CredentialRequest;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.exception.ConfigurationException;
import org.xml.sax.SAXException;

/**
 * Trust manager is the central repository for objects 
 * participating in the trust negotiation in the http context.
 * 
 * @author Patrice Congo(token77)
 *
 */
public class TrustManager implements Configurable
{
	/**
	 * a resource classifier
	 */
	private ResourceClassifier resourceClassifier;
	
	/**
	 * the policy system
	 */
	private PolicySystem policySystem;
	
	/**
	 * the policy evaluator
	 */
	private PolicyEvaluator policyEvaluator;
	
	/**
	 * a pool of requestServingMechanism
	 */
	private RequestServingMechanismPool requestServingMechanismPool;
	
	/**
	 * The credential distribution server 
	 */
	private CredentialDistributionServer credentialDistributionServer;
	
	/** 
	 * Path of the xml setup file
	 */
	private String setupFilePath;
	
	/**
	 * The trust client used bei the evaluator. hold for compatibility
	 * reason. expected to be removed.
	 */
	TrustClient trustClient;
	
	public TrustManager(){
		//empty
	}
	
	/**
	 * Creates a trust manager.
	 */
	private TrustManager(TrustClient trustClient,  
						String resourceMngXmlConfigPath
						) {
//		System.out.println("\n*************>resourceMngXmlConfigPath:"+resourceMngXmlConfigPath);
//		this.trustClient=trustClient;
//		this.resourceClassifier= 
//			makeResourceClassifier(resourceMngXmlConfigPath);//classifierXMLSetupFilePath);
//		this.policySystem=
//			makePolicySystem(resourceMngXmlConfigPath);//policySystemXMLSetupFilePath);
//		//this.trustClient=trustClient;
//		this.policyEvaluator=
//			makePolicyEvaluator(
//							resourceMngXmlConfigPath,//policyEvaluatorXMLSetupPath,
//							trustClient);
//		try {
//			this.requestServingMechanismPool=
//				makeRequestServingMechanismPool(resourceMngXmlConfigPath);//requestServingMechanismPoolSetupFile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SetupException e) {
//			e.printStackTrace();
//		}
//		
//	
//		try {
//			this.credentialDistributionServer=
//				makeCredentialDistributionServer(resourceMngXmlConfigPath,trustClient);
//		} catch (NullPointerException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}
		try {
			setup(trustClient,resourceMngXmlConfigPath);
		} catch (Exception e) {
			throw new Error("TrustManager Setupt fails");
		}
	}

	private void setup(
				TrustClient trustClient,  
				String resourceMngXmlConfigPath) throws Exception
	{
		System.out.println("\n*************>resourceMngXmlConfigPath:"+resourceMngXmlConfigPath);
		this.trustClient=trustClient;
		this.resourceClassifier= 
			makeResourceClassifier(resourceMngXmlConfigPath);//classifierXMLSetupFilePath);
		this.policySystem=
			makePolicySystem(resourceMngXmlConfigPath);//policySystemXMLSetupFilePath);
		//this.trustClient=trustClient;
		this.policyEvaluator=
			makePolicyEvaluator(
							resourceMngXmlConfigPath,//policyEvaluatorXMLSetupPath,
							trustClient);
		
		this.requestServingMechanismPool=
				makeRequestServingMechanismPool(resourceMngXmlConfigPath);//requestServingMechanismPoolSetupFile);
		
		
	
		this.credentialDistributionServer=
				makeCredentialDistributionServer(resourceMngXmlConfigPath,trustClient);
		
	}
	
	private ResourceClassifier makeResourceClassifier(
								String classifierXMLSetupFilePath)
	{
		
		try {
			ResourceClassifierImpl classifier=
				new ResourceClassifierImpl();
			//classifier.setup(classifierXMLSetupFilePath);
			classifier.setSetupFilePath(classifierXMLSetupFilePath);
			classifier.init();
			return classifier;
		} catch (ConfigurationException  e) {
			throw new Error("Could create and set up ResourceClassifier",e);
		} 
	}
	
	private PolicySystem makePolicySystem(String policySystemXMLSetupFilePath){
		try {
			PolicySystemImpl polSystem= new PolicySystemImpl();
			//polSystem.setup(policySystemXMLSetupFilePath);
			polSystem.setSetupFilePath(policySystemXMLSetupFilePath);
			polSystem.init();
			return polSystem;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new Error("Could not create and init the PolicySystem");
		} 
	}
	
	private PolicyEvaluator makePolicyEvaluator(String policyEvaluatorXMLSetupPath, 
												TrustClient trustClient){
		//this.trustClient=trustClient;
		
		///return new SimplePolicyEvaluator(trustClient);
		try {
			SimplePolicyEvaluator e=
					new SimplePolicyEvaluator();
			e.setTrustClient(trustClient);
			e.init();
			return e;
		} catch (ConfigurationException e) {
			throw new Error("Could not create the PolicyEvaluator",e);
		}
	}
	
	private RequestServingMechanismPool makeRequestServingMechanismPool(
									String requestServingMechanismPoolSetupFile) 
									throws IOException, SetupException
	{
		try {
			RequestServingMechanismPool pool=
							new RequestServingMechanismPool();
			//pool.setup(requestServingMechanismPoolSetupFile);
			pool.setSetupFilePath(requestServingMechanismPoolSetupFile);
			pool.init();
			System.out.println("\n============================MECHANISM POOL READY===========================");
			return pool;
			
		} catch (ConfigurationException e) {
			throw new Error("Could not create the mechnism pool",e);
		}
		
	}
	
	public Resource classifyResource(String url){
		Resource res=resourceClassifier.getResource(url);
		return res;
	}
	
	public Resource guardResource(Resource res, String negotiatingPeerName) throws IllegalAccessPolicyAssociation{
		
		//pt negotiation if external protected resource
		if(res instanceof ProtectedResource){
			String policyName=((ProtectedResource)res).getPolicyName();
			if(policyName==null){
				throw 
					new IllegalAccessPolicyAssociation(
							"Policy name associated with "+res.getUrl()+
							" is null");
			}
			if(policyName.trim().length()==0) {
				throw 
					new IllegalAccessPolicyAssociation(
						"Policy name associated with "+res.getUrl()+
						" is empty");
			}
			
			Vector associatePolicies=
				policySystem.getPolicies(policyName);
			if(associatePolicies.size()==0){
				throw 
				new IllegalAccessPolicyAssociation(
						"Policy with the name "+policyName+ "is empty");
			}
			
			//evaluate
			int result=policyEvaluator.eval(associatePolicies, negotiatingPeerName);
			if(result==PolicyEvaluator.SUCCESS_FLAG){
				((ProtectedResource)res).setCanAccess(true);
			}else{
				((ProtectedResource)res).setCanAccess(false);
				String message=((SimplePolicyEvaluator)policyEvaluator).getMessage();
				if(message==null){
					((ProtectedResource)res).setReason(
							"Fail to evaluate "+
							((Policy)associatePolicies.elementAt(result)).getPolicyValue());
				}else{
					((ProtectedResource)res).setReason(message);
				}
			}
		}
		
		return res;
	}
	
	public RequestServingMechanism getRequestServingMechanismByURL(String url){
		RequestServingMechanism m=requestServingMechanismPool.getMechanism(url);
		
		System.out.println("\n=============LOOKUP MECHANISM=========================");
		System.out.println("\nurl:"+url+"\nmechanism:"+m);
		System.out.println("\n======================================================\n");
		
		return m;
	}
	
	public RequestServingMechanism getRequestServingMechanismByName(String name){
		RequestServingMechanism m=requestServingMechanismPool.getMechanismByName(name);
		
		System.out.println("\n=============LOOKUP MECHANISM=========================");
		System.out.println("\nname:"+name+"\nmechanism:"+m);
		System.out.println("\n======================================================\n");
		
		return m;
	}
	
	private CredentialDistributionServer makeCredentialDistributionServer(
												String classifierXMLSetupFilePath,
												TrustClient trustClient) throws NullPointerException, SAXException, IOException, ParserConfigurationException{
		CredentialDistributionServer cds=
			new CredentialDistributionServer();
		cds.setup(
								classifierXMLSetupFilePath,
								trustClient);
		PTCommunicationASP comASP=
			(PTCommunicationASP)trustClient.getComponent(
											Vocabulary.EventListener);
		comASP.registerPTComASPMessageListener(cds,CredentialRequest.class);
		return cds;
	}
	
	public String getCredentialDescription(String credName){
		return credentialDistributionServer.getCredentialStore().getCredentialDescription(credName);
	}
	
	
	
	
	/**
	 * @return Returns the setupFilePath.
	 */
	public String getSetupFilePath() {
		return setupFilePath;
	}

	/**
	 * @param setupFilePath The setupFilePath to set.
	 */
	public void setSetupFilePath(String setupFilePath) {
		this.setupFilePath = setupFilePath;
	}

	
	
	
	/**
	 * @return Returns the trustClient.
	 */
	public TrustClient getTrustClient() {
		return trustClient;
	}

	/**
	 * @param trustClient The trustClient to set.
	 */
	public void setTrustClient(TrustClient trustClient) {
		this.trustClient = trustClient;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		if(setupFilePath==null){
			throw new ConfigurationException("setupFilePath must not be null");
		}
		
		if(trustClient==null){
			throw new ConfigurationException("trustClient must not be null");
		}
		
		try {
			setup(trustClient,setupFilePath);
		} catch (Exception e) {
			throw new ConfigurationException("TrustManagerFails",e);
		}
	}
	
	

	static public void main(String[] args){
		System.out.println("dadad Requester ggg Requester".replaceAll("Requester","alice"));
	}
}
