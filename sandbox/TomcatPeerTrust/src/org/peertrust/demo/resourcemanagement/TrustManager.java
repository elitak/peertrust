/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.peertrust.TrustClient;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.credential_distribution.CredentialDistributionServer;
import org.peertrust.demo.credential_distribution.CredentialRequest;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.xml.sax.SAXException;

/**
 * @author pat_dev
 *
 */
public class TrustManager {

	class SimplePolicyEvaluator implements PolicyEvaluator{
		static final public String PEER_NAME_SPACE_HOLDER="Requester";
		private TrustClient trustClient;
		private String message=null;
		public SimplePolicyEvaluator(TrustClient trustClient){
			this.trustClient=trustClient;
			this.trustClient.setTimeout(60000);
			this.trustClient.setSleepInterval(100);
		}
		

		/**
		 * @return Returns the message.
		 */
		public String getMessage() {
			return message;
		}


		/**
		 * @param message The message to set.
		 */
		public void setMessage(String message) {
			this.message = message;
		}


		/* (non-Javadoc)
		 * @see org.peertrust.demo.resourcemanagement.PolicyEvaluator#eval(java.util.Vector)
		 */
		synchronized public int eval(Vector policyVector, String negotiatingPeerName) {
			final int SIZE=policyVector.size();
			Policy pol=null;
			long id;
			Boolean result;
			String query;
			for(int i=0; i<SIZE;i++){
				pol=(Policy)policyVector.elementAt(i);
				query=buildQuery(pol.getPolicyValue(),negotiatingPeerName);
				if(query!=null){
					id=trustClient.sendQuery(query);
					trustClient.waitForQuery(id);
					result=trustClient.isQuerySuccessful(id);
					System.out.println("-------------------------Nego"+i+"\n"+
								"id: "+id+" result:"+result+" query:"+query+ 
								" finished:"+trustClient.isQueryFinished(id));
					
					if(result==null){
						return i;
					}else if(!result.booleanValue()){
						return i;
					}
				}else{
					message="Automatic peer online registration not done!";
					return 0;
				}
			}
			return PolicyEvaluator.SUCCESS_FLAG;
		}
		
		private String buildQuery(String polValue, String negotiatinPeerName){
			try{
				polValue=polValue.replaceAll(PEER_NAME_SPACE_HOLDER,negotiatinPeerName);
				System.out.println("parsed query:"+polValue);
				return polValue;
			}catch(NullPointerException e){
				e.printStackTrace();
				return null;
			}
			
		}
	}
	
	private ResourceClassifier resourceClassifier;
	private PolicySystem policySystem;
	private PolicyEvaluator policyEvaluator;
	//private TrustClient trustClient;
	private RequestServingMechanismPool requestServingMechanismPool;
	private CredentialDistributionServer credentialDistributionServer;
	
	/**
	 * 
	 */
	public TrustManager(TrustClient trustClient,  
						String resourceMngXmlConfigPath
//						String classifierXMLSetupFilePath,
//						String policySystemXMLSetupFilePath,
//						String policyEvaluatorXMLSetupPath,
//						String requestServingMechanismPoolSetupFile
						) {
		System.out.println("*************>resourceMngXmlConfigPath:"+resourceMngXmlConfigPath);
		System.out.println();
		this.resourceClassifier= 
			makeResourceClassifier(resourceMngXmlConfigPath);//classifierXMLSetupFilePath);
		this.policySystem=
			makePolicySystem(resourceMngXmlConfigPath);//policySystemXMLSetupFilePath);
		//this.trustClient=trustClient;
		this.policyEvaluator=
			makePolicyEvaluator(
							resourceMngXmlConfigPath,//policyEvaluatorXMLSetupPath,
							trustClient);
		try {
			this.requestServingMechanismPool=
				makeRequestServingMechanismPool(resourceMngXmlConfigPath);//requestServingMechanismPoolSetupFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SetupException e) {
			e.printStackTrace();
		}
		
	
		try {
			this.credentialDistributionServer=
				makeCredentialDistributionServer(resourceMngXmlConfigPath,trustClient);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}

	private ResourceClassifier makeResourceClassifier(String classifierXMLSetupFilePath){
		
		try {
			ResourceClassifierImpl classifier=
				new ResourceClassifierImpl();
			classifier.setup(classifierXMLSetupFilePath);
			return classifier;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedFormatException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private PolicySystem makePolicySystem(String policySystemXMLSetupFilePath){
		try {
			PolicySystemImpl polSystem=
				new PolicySystemImpl();
			polSystem.setup(policySystemXMLSetupFilePath);
			return polSystem;
		} catch (UnsupportedFormatException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private PolicyEvaluator makePolicyEvaluator(String policyEvaluatorXMLSetupPath, 
												TrustClient trustClient){
		//this.trustClient=trustClient;
		
		return new SimplePolicyEvaluator(trustClient);
	}
	
	private RequestServingMechanismPool makeRequestServingMechanismPool(String requestServingMechanismPoolSetupFile) throws IOException, SetupException{
		RequestServingMechanismPool pool=
						new RequestServingMechanismPool();
		pool.setup(requestServingMechanismPoolSetupFile);
		System.out.println("\n============================MECHANISM POOL READY===========================");
		return pool;
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
	
	static public void main(String[] args){
		System.out.println("dadad Requester ggg Requester".replaceAll("Requester","alice"));
	}
}
