package org.peertrust.demo.servlet.jsptags;


import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.peertrust.TrustClient;
import org.peertrust.demo.servlet.NegotiationObjectRepository;
import org.peertrust.demo.servlet.NegotiationObjects;
import org.peertrust.net.Peer;

/**
 * TagsPeerTrustEvaluator is responsible for evaluating whether a
 * property is hold or not
 * 
 * @author Patrice Congo (token77)
 *
 */
public class TagsPeerTrustEvaluator implements PTPropertyEvaluator {
	static final public String PEER_NAME_PLACE_HOLDER="Requester"; 
	
	/**
	 * Provide a quick access to the trust objects
	 */
	NegotiationObjectRepository negoObjects;
	
	/**
	 * The trustclient used for negotiation
	 */
	TrustClient trustClient;
	
	/**
	 * The current http session
	 */
	HttpSession session;
	
	/**
	 *Default contsructor 
	 */
	public TagsPeerTrustEvaluator() {
		super();
	}

	/**
	 * Customizes the property with the remote negotiating peer alias
	 * and uses the trust client to evaluate it.
	 * @return true if the property is hold, or false if not
	 *  
	 * @see org.peertrust.demo.servlet.jsptags.PTPropertyEvaluator#eval(java.lang.String)
	 */
	public boolean eval(String propertySpec) {
		long id=trustClient.sendQuery(buildQuery(propertySpec));
		Boolean res= trustClient.waitForQuery(id);
		if(res==null){
			return false;
		}else{
			return res.booleanValue();
		}
	}

	/**
	 * Customizes the property spec with the alias of the remote negotiating
	 * peer.
	 *   
	 * @param propertySpec -- the uncustomized property specification.
	 * @return
	 */
	private String buildQuery(String propertySpec){
		if(session!=null){
			Peer peer= negoObjects.getSessionPeer(session.getId());
			String peerName= null;
			if(peer!=null){
				peerName=peer.getAlias();
				propertySpec= propertySpec.replaceAll(PEER_NAME_PLACE_HOLDER,peerName);
			}
			System.out.println("resulting query:"+propertySpec);
		}
		return propertySpec;
	}
	
	/**
	 * @see org.peertrust.demo.servlet.jsptags.PTPropertyEvaluator#init(javax.servlet.jsp.PageContext)
	 */
	public void init(PageContext context) {
		negoObjects=
			NegotiationObjects.createAndAddForAppContext(context.getServletConfig());
		trustClient=negoObjects.getTrustClient();
		//trustClient.setTimeout(1000*10);
		//trustClient.setSleepInterval(1000);
		session=context.getSession();
	}

}
