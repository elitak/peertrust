/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

import org.peertrust.TrustClient;

/**
 * @author pat_dev
 *
 */
public class TrustManager {

	class SimplePolicyEvaluator implements PolicyEvaluator{
		private TrustClient trustClient;
		
		public SimplePolicyEvaluator(TrustClient trustClient){
			this.trustClient=trustClient;
		}

		/* (non-Javadoc)
		 * @see org.peertrust.demo.resourcemanagement.PolicyEvaluator#eval(java.util.Vector)
		 */
		synchronized public int eval(Vector policyVector, String negotiatingPeerName) {
			final int SIZE=policyVector.size();
			Policy pol=null;
			long id;
			Boolean result;
			for(int i=0; i<SIZE;i++){
				pol=(Policy)policyVector.elementAt(i);
				id=trustClient.sendQuery(buildQuery(pol.getPolicyValue(),negotiatingPeerName));
				result=trustClient.waitForQuery(id);
				if(result==null){
					return i;
				}else if(!result.booleanValue()){
					return i;
				}
			}
			return PolicyEvaluator.SUCCESS_FLAG;
		}
		
		private String buildQuery(String polValue, String negotiatinPeerName){
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
		return new OntologyBasedResourceClassifier(classifierXMLSetupFilePath);
	}
	
	private PolicySystem makePolicySystem(String policySystemXMLSetupFilePath){
		return new OntologyBasedPolicySystem(policySystemXMLSetupFilePath);
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
							"Policy name associated with "+res.getVirtualURL()+
							" is null");
			}
			if(policyName.trim().length()==0) {
				throw 
					new IllegalAccessPolicyAssociation(
						"Policy name associated with "+res.getVirtualURL()+
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
			int result=policyEvaluator.eval(associatePolicies, null);
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
}
