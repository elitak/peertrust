/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

import javax.servlet.jsp.PageContext;

import org.peertrust.TrustClient;
import org.peertrust.demo.servlet.NegotiationObjects;

/**
 * @author pat_dev
 *
 */
public class TagsPeerTrustEvaluator implements PTPropertyEvaluator {
	NegotiationObjects negoObjects;
	TrustClient trustClient;
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
	}

}
