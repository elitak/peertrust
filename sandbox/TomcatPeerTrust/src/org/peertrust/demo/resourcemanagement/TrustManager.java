/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.peertrust.TrustClient;
import org.xml.sax.SAXException;

/**
 * @author pat_dev
 *
 */
public class TrustManager {

	class SimplePolicyEvaluator implements PolicyEvaluator{
		static final public String PEER_NAME_SPACE_HOLDER="Requester";
		private TrustClient trustClient;
		
		public SimplePolicyEvaluator(TrustClient trustClient){
			this.trustClient=trustClient;
			this.trustClient.setTimeout(60000);
			this.trustClient.setSleepInterval(100);
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
			}
			return PolicyEvaluator.SUCCESS_FLAG;
		}
		
		private String buildQuery(String polValue, String negotiatinPeerName){
			polValue=polValue.replaceAll(PEER_NAME_SPACE_HOLDER,negotiatinPeerName);
			System.out.println("parsed query:"+polValue);
			return polValue;
		}
	}
	
	private ResourceClassifier resourceClassifier;
	private PolicySystem policySystem;
	private PolicyEvaluator policyEvaluator;
	private TrustClient trustClient;
	
	/**
	 * 
	 */
	public TrustManager(TrustClient trustClient,  
						String classifierXMLSetupFilePath,
						String policySystemXMLSetupFilePath,
						String policyEvaluatorXMLSetupPath) {
		super();
		this.resourceClassifier= makeResourceClassifier(classifierXMLSetupFilePath);
		this.policySystem=makePolicySystem(policySystemXMLSetupFilePath);
		this.trustClient=trustClient;
		this.policyEvaluator=makePolicyEvaluator(policyEvaluatorXMLSetupPath,trustClient);
	}

	private ResourceClassifier makeResourceClassifier(String classifierXMLSetupFilePath){
		
		try {
			OntologyBasedResourceClassifier classifier=
				new OntologyBasedResourceClassifier();
			classifier.setup(classifierXMLSetupFilePath);
			return classifier;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private PolicySystem makePolicySystem(String policySystemXMLSetupFilePath){
		try {
			OntologyBasedPolicySystem polSystem=
				new OntologyBasedPolicySystem();
			polSystem.setup(policySystemXMLSetupFilePath);
			return polSystem;
		} catch (UnsupportedFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private PolicyEvaluator makePolicyEvaluator(String policyEvaluatorXMLSetupPath, 
												TrustClient trustClient){
		this.trustClient=trustClient;
		
		return new SimplePolicyEvaluator(trustClient);
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
				
				((ProtectedResource)res).setReason(
						"Fail to evaluate "+
						((Policy)associatePolicies.elementAt(result)).getPolicyValue());
			}
		}
		
		return res;
	}
	
	static public void main(String[] args){
		System.out.println("dadad Requester ggg Requester".replaceAll("Requester","alice"));
	}
}
