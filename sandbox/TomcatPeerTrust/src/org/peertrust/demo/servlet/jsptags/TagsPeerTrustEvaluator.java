/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;


import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.peertrust.TrustClient;
import org.peertrust.demo.servlet.NegotiationObjectRepository;
import org.peertrust.demo.servlet.NegotiationObjects;
import org.peertrust.net.Peer;

/**
 * @author pat_dev
 *
 */
public class TagsPeerTrustEvaluator implements PTPropertyEvaluator {
	static final public String PEER_NAME_PLACE_HOLDER="Requester"; 
	NegotiationObjectRepository negoObjects;
	TrustClient trustClient;
	HttpSession session;
	/**
	 * 
	 */
	public TagsPeerTrustEvaluator() {
		super();
	}

	/* (non-Javadoc)
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

	public String buildQuery(String propertySpec){
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
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.jsptags.PTPropertyEvaluator#init(javax.servlet.jsp.PageContext)
	 */
	public void init(PageContext context) {
		negoObjects=
			NegotiationObjects.createAndAddForAppContext(context.getServletConfig());
		trustClient=negoObjects.getTrustClient();
		trustClient.setTimeout(1000*60);
		session=context.getSession();
	}

}
