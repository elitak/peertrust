/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

import org.peertrust.TrustClient;
import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;

/**
 * This class provides a simplem policy evaluator implementation.
 * 
 * @author Patrice Congo(token77)
 *
 */
class SimplePolicyEvaluator implements 	PolicyEvaluator,
										Configurable{
	static final public String PEER_NAME_SPACE_HOLDER="Requester";
	
	/** 
	 * The trust client use during trust negotiation.
	 */
	private TrustClient trustClient;
	
	/**
	 * string use to cache the excexution fails messages
	 */
	String message=null;
	
	/**
	 * Creates a virgin SimplePolicyEvaluator Object.
	 * The full setup of this object has 2 steps:
	 * <ul>
	 * 	</li>sets trustClient using the appropriate setter
	 * 	</li>call init to finalize the setup process
	 * </ul> 
	 * This prcess is e.g. done automacally when the
	 * SimplePlicyEvaluator object is build from a  rdf config
	 * file. 
	 */
	public SimplePolicyEvaluator(){
		//nothing
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


	/** 
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
	
	/**
	 * Replacesthe peertrust name place holder with the real negotiating
	 * peer alias.
	 * @param polValue -- the policy to customize.
	 * @param negotiatinPeerName -- the real alias of the negotiating peer
	 * @return the customized policy
	 */
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
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		if(trustClient==null){
			throw new ConfigurationException("trustClient must not be null");
		}
		
		this.trustClient.setTimeout(10*1000);
		this.trustClient.setSleepInterval(100);
	}
	
	
}