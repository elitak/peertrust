/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.util.Vector;

import org.peertrust.TrustClient;
import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;

class SimplePolicyEvaluator implements 	PolicyEvaluator,
										Configurable{
	static final public String PEER_NAME_SPACE_HOLDER="Requester";
	private TrustClient trustClient;
	String message=null;
	public SimplePolicyEvaluator(){
		//nothing
	}
	
//	public SimplePolicyEvaluator(TrustClient trustClient){
//		this.trustClient=trustClient;
//		this.trustClient.setTimeout(10*1000);
//		this.trustClient.setSleepInterval(100);
//	}
	

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
		//this.trustClient=trustClient;
		if(trustClient==null){
			throw new ConfigurationException("trustClient must not be null");
		}
		
		this.trustClient.setTimeout(10*1000);
		this.trustClient.setSleepInterval(100);
	}
	
	
}