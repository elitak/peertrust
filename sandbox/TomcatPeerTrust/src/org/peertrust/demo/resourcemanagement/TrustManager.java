/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.peertrust.TrustClient;
import org.peertrust.config.Configurable;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.credential_distribution.CredentialDistributionServer;
import org.peertrust.demo.credential_distribution.CredentialRequest;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.demo.session_registration.SessionRegisterer;
import org.peertrust.exception.ConfigurationException;
import org.xml.sax.SAXException;

/**
 * Trust manager is the central repository for objects 
 * participating in the trust negotiation in the http context.
 * 
 * @author Patrice Congo(token77)
 *
 */
public class TrustManager implements 	Configurable,
										ResourceRequestHandler
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
	 * next handler in the chain of resposibility for serving
	 * resource request. 
	 */
	ResourceRequestHandler nextHandler;
	
	/** 
	 * Path of the xml setup file
	 */
	private StringWrapper setupFilePath;
	
	/**
	 * The trust client used bei the evaluator. hold for compatibility
	 * reason. expected to be removed.
	 */
	TrustClient trustClient;
	
	/**
	 * Session registerer used to bind peer to communication session.
	 * This necessary since the communication channel is not statically 
	 * fix.
	 */
	SessionRegisterer sessionRegisterer;
	
	
	//TODO remove this since actualy the trust manager is becoming just a king of repository and fasade
	 
	/**
	 * Creates a virgin TrustManager object.
	 * The initialization of this object then take too steps:
	 * <lu>
	 * <li/>set the setupFilePath and trustClient using the
	 * 		appropriate setter.
	 * <li>call init
	 * </ul> 
	 * These 2 steps will 4 e.g. be done automaticaly if the
	 * Trustmanager object is build from a peertrust configuration 
	 * rdf file.
	 */
	public TrustManager(){
		//empty
	}
	

	private void setup(
				TrustClient trustClient,  
				String resourceMngXmlConfigPath) throws Exception
	{
		System.out.println("\n*************>resourceMngXmlConfigPath:"+resourceMngXmlConfigPath);
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
//		
//		this.requestServingMechanismPool=
//				makeRequestServingMechanismPool(resourceMngXmlConfigPath);//requestServingMechanismPoolSetupFile);
//		
		
	
		this.credentialDistributionServer=
				makeCredentialDistributionServer(
								resourceMngXmlConfigPath,
								trustClient);
		
	}
	
//	private ResourceClassifier makeResourceClassifier(
//								String classifierXMLSetupFilePath)
//	{
//		
//		try {
//			ResourceClassifierImpl classifier=
//				new ResourceClassifierImpl();
//			//classifier.setup(classifierXMLSetupFilePath);
//			classifier.setSetupFilePath(classifierXMLSetupFilePath);
//			classifier.init();
//			return classifier;
//		} catch (ConfigurationException  e) {
//			throw new Error("Could create and set up ResourceClassifier",e);
//		} 
//	}
	
//	private PolicySystem makePolicySystem(String policySystemXMLSetupFilePath){
//		try {
//			PolicySystemImpl polSystem= new PolicySystemImpl();
//			//polSystem.setup(policySystemXMLSetupFilePath);
//			StringWrapper path=
//				new StringWrapper();
//			path.setWrappedString(policySystemXMLSetupFilePath);
//			path.init();
//			polSystem.setSetupFilePath(path);
//			polSystem.init();
//			return polSystem;
//		} catch (ConfigurationException e) {
//			e.printStackTrace();
//			throw new Error("Could not create and init the PolicySystem");
//		} 
//	}
	
//	private PolicyEvaluator makePolicyEvaluator(String policyEvaluatorXMLSetupPath, 
//												TrustClient trustClient){
//		//this.trustClient=trustClient;
//		
//		///return new SimplePolicyEvaluator(trustClient);
//		try {
//			SimplePolicyEvaluator e=
//					new SimplePolicyEvaluator();
//			e.setTrustClient(trustClient);
//			e.init();
//			return e;
//		} catch (ConfigurationException e) {
//			throw new Error("Could not create the PolicyEvaluator",e);
//		}
//	}
	
//	private RequestServingMechanismPool makeRequestServingMechanismPool(
//									String requestServingMechanismPoolSetupFile) 
//									throws IOException, SetupException
//	{
//		try {
//			RequestServingMechanismPool pool=
//							new RequestServingMechanismPool();
//			//pool.setup(requestServingMechanismPoolSetupFile);
//			pool.setSetupFilePath(requestServingMechanismPoolSetupFile);
//			pool.init();
//			System.out.println("\n============================MECHANISM POOL READY===========================");
//			return pool;
//			
//		} catch (ConfigurationException e) {
//			throw new Error("Could not create the mechnism pool",e);
//		}
//		
//	}
	
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
	 * @return Returns the credentialDistributionServer.
	 */
	public CredentialDistributionServer getCredentialDistributionServer() {
		return credentialDistributionServer;
	}


	/**
	 * @param credentialDistributionServer The credentialDistributionServer to set.
	 */
	public void setCredentialDistributionServer(
			CredentialDistributionServer credentialDistributionServer) {
		this.credentialDistributionServer = credentialDistributionServer;
	}


	/**
	 * @return Returns the policyEvaluator.
	 */
	public PolicyEvaluator getPolicyEvaluator() {
		return policyEvaluator;
	}


	/**
	 * @param policyEvaluator The policyEvaluator to set.
	 */
	public void setPolicyEvaluator(PolicyEvaluator policyEvaluator) {
		this.policyEvaluator = policyEvaluator;
	}


	/**
	 * @return Returns the policySystem.
	 */
	public PolicySystem getPolicySystem() {
		return policySystem;
	}


	/**
	 * @param policySystem The policySystem to set.
	 */
	public void setPolicySystem(PolicySystem policySystem) {
		this.policySystem = policySystem;
	}


	/**
	 * @return Returns the requestServingMechanismPool.
	 */
	public RequestServingMechanismPool getRequestServingMechanismPool() {
		return requestServingMechanismPool;
	}


	/**
	 * @param requestServingMechanismPool The requestServingMechanismPool to set.
	 */
	public void setRequestServingMechanismPool(
			RequestServingMechanismPool requestServingMechanismPool) {
		this.requestServingMechanismPool = requestServingMechanismPool;
	}


	/**
	 * @return Returns the resourceClassifier.
	 */
	public ResourceClassifier getResourceClassifier() {
		return resourceClassifier;
	}


	/**
	 * @param resourceClassifier The resourceClassifier to set.
	 */
	public void setResourceClassifier(ResourceClassifier resourceClassifier) {
		this.resourceClassifier = resourceClassifier;
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
	
	

	/**
	 * @return Returns the sessionRegisterer.
	 */
	public SessionRegisterer getSessionRegisterer() {
		return sessionRegisterer;
	}


	/**
	 * @param sessionRegisterer The sessionRegisterer to set.
	 */
	public void setSessionRegisterer(SessionRegisterer sessionRegisterer) {
		this.sessionRegisterer = sessionRegisterer;
	}


	/* (non-Javadoc)
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
//		if(setupFilePath==null){
//			throw new ConfigurationException("setupFilePath must not be null");
//		}
		
		if(policyEvaluator==null){
			throw new ConfigurationException(
							"PolicyEvaluator not set at "+this.getClass());
		}
		
		if(policySystem==null){
			throw new ConfigurationException(
							"PolicySystem not set"+this.getClass());
		}
		
		if(trustClient==null){
			throw new ConfigurationException(
								"trustClient not set"+this.getClass());
		}
		
		if(requestServingMechanismPool==null){
			throw new ConfigurationException(
					"requestServingMechnism not be null"+this.getClass());
		}
		
		if(resourceClassifier==null){
			throw new ConfigurationException(
					"resourceClassifier net set"+this.getClass());
		}
		
		if(credentialDistributionServer==null){
			throw new ConfigurationException(
					"credentialDistributionServer not set"+this.getClass());
		}
		
		if(sessionRegisterer==null){
			throw new ConfigurationException(
					"sessionRegisterer not set"+this.getClass());
		}
		
		this.setNextHandle((ResourceRequestHandler)resourceClassifier);
		((ResourceRequestHandler)resourceClassifier).setNextHandle(
									(ResourceRequestHandler)policySystem);
		((ResourceRequestHandler)policySystem).setNextHandle(
								(ResourceRequestHandler)policyEvaluator);
		((ResourceRequestHandler)policyEvaluator).setNextHandle(
					(ResourceRequestHandler)requestServingMechanismPool);
//		try {
//			setup(trustClient,setupFilePath);
//		} catch (Exception e) {
//			throw new ConfigurationException("TrustManagerFails",e);
//		}
	}
	
	

	/**
	 * @see org.peertrust.demo.resourcemanagement.ResourceRequestHandler#handle(java.lang.Object)
	 */
	public void handle(Object requestSpec) throws ResourceManagementException {
		if(requestSpec == null){
			throw new ResourceManagementException("Cannot handle null requestSpec");
		}
		
		if(!(requestSpec instanceof ResourceRequestSpec)){
			throw new ResourceManagementException(
						"Cannot handle "+requestSpec+
						" can only handle a "+ResourceRequestSpec.class);
		}
		
		ResourceRequestSpec spec=(ResourceRequestSpec)requestSpec;
		
		if(nextHandler!=null){
			nextHandler.handle(spec);
		}else{
			throw new ResourceManagementException(
					"ResourceClassifier cannot act as the end of the chain");
		}
		
		return;
	}


	/**
	 * @see org.peertrust.demo.resourcemanagement.ResourceRequestHandler#setNextHandle(org.peertrust.demo.resourcemanagement.ResourceRequestHandler)
	 */
	public void setNextHandle(ResourceRequestHandler nextHandler) {
		this.nextHandler=nextHandler;
	}


	static public void main(String[] args){
		System.out.println("dadad Requester ggg Requester".replaceAll("Requester","alice"));
	}
}
