package peertrust.tag.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import peertrust.common.CredentialFactory;
import peertrust.common.interfaces.ICredential;
import peertrust.filter.ProtectedResourcePolicyFilter;
import peertrust.tag.interfaces.IJSPTagChecker;

/**
 * Implementation of the IJSPTagChecker interface. Makes use of session objects.
 * Handling of the PolicyConditionTag jsp tag is done mainly here. Used by
 * PolicyTagJSPFilter class.
 * @see PolicyConditionTag
 * @see PolicyTagJSPFilter
 * @see IJSPTagChecker
 * @author Sebastian Wittler
 */
public class JSPTagChecker implements IJSPTagChecker {
	// The session key for the state the handling of the PolicyConditionTag is in.
	public static final String SESSION_ACCESS_STATE_KEY="state";
	// State of handling of tag in which the client accesses jsp first time to
	// find out which credentials the tags require
	public static final String SESSION_ACCESS_STATE_REQUEST="request";
	// State of handling of tag in which the client accesses the jsp file a second
	// time to send the required credentials for the PolicyConditionTags
	public static final String SESSION_ACCESS_STATE_RESPONSE="response";
	// Session key for storing the list of required credentials
	public static final String SESSION_CRED_SET_ACTIVE="credential_state";
	// Session key for marking if the handling of the PolicyConditionTags in the jsp
	// file is over.
	public static final String SESSION_FINISHED_KEY="finished";
	// Marks the handling of the PolicyConditionTags in the jsp file as over
	public static final String SESSION_FINISHED_VALUE="true";

	// Logger
	private static Logger log = Logger.getLogger(JSPTagChecker.class);

	/**
	 * @see IJspTagChecker.accept
	 */
	public boolean accept(HttpServletRequest req,String strCredName) {
		log.debug("doStart");
		HttpSession session=req.getSession();
		session.setMaxInactiveInterval(ProtectedResourcePolicyFilter.SESSION_TIMEOUT);
		Object state=session.getAttribute(SESSION_ACCESS_STATE_KEY);
		Object obj=session.getAttribute(SESSION_CRED_SET_ACTIVE);
		List list;
		// If no list for required credentials exists in the session, store a new
		// list in there
		if(obj==null) {
			list=Collections.synchronizedList(new LinkedList<ICredential>());
			session.setAttribute(SESSION_CRED_SET_ACTIVE,list);
		}
		// Otherwise take old list
		else
			list=(List)obj;
		log.debug("setted");
		if((state==null)||(!(state instanceof String)))
			return false;
		String strState=(String)state;
		// If client requests the jsp to find out the requirements for the
		// PolicyConditionTags
		if(strState.equals(SESSION_ACCESS_STATE_REQUEST)) {
			log.debug(SESSION_ACCESS_STATE_REQUEST);
			// Add the required credential to the above list in the session
			list.add(CredentialFactory.getInstance().createCredential(strCredName));
			log.debug(SESSION_ACCESS_STATE_REQUEST+" "+list);
		}
		// If the client sends the requested credentials to fullfill the
		// PolicyConditionTags requirements.
		else if(strState.equals(SESSION_ACCESS_STATE_RESPONSE))
			// Check if PolicyConditionTags requirments are satisfied
			return isTagFulfilled(list,strCredName,session);
		return false;
	}

	/**
	 * Check if the credentials sent by t he client fullfill the requirement of a
	 * PolicyConditionTag.
	 * @param listCreds List of credentials the client sent.
	 * @param strCredName Credential that tag requires.
	 * @param session Session object.
	 * @return If tag's required credential is fulfilles, true, otherwise false
	 */
	private boolean isTagFulfilled(List listCreds,String strCredName,HttpSession session) {
		log.debug(SESSION_ACCESS_STATE_RESPONSE);
		// If client sent no credentials, the tag is not satisfied
		if(listCreds.isEmpty())
			return false;
		
		// Check if the list from the client contains the required credential
		boolean bFulfilled=false;
		ICredential wanted_cred=CredentialFactory.getInstance().createCredential(strCredName);
		for(int i=0;i<listCreds.size();i++)
			// If yes, tag requirements are fulfilled
			if((listCreds.get(i) instanceof ICredential)
				&&((ICredential)listCreds.get(i)).equalsCredential(wanted_cred)) {
				listCreds.remove(i);
//TODO: really remove?
				bFulfilled=true;
				break;
			}
		log.debug("tag fulfilled: "+bFulfilled+" "+listCreds.size()+" "+strCredName);

		// Handling of PolicyConditionTag is finished
		session.setAttribute(SESSION_FINISHED_KEY,SESSION_FINISHED_VALUE);
		return bFulfilled;
	}

	/**
	 * @see IJSPTagChecker.hasPolicyJSPTag
	 */
	public boolean hasPolicyJSPTag(HttpServletRequest req) {
		return req.getSession().getAttribute(SESSION_CRED_SET_ACTIVE)!=null;
	}

	/**
	 * @see IJSPTagChecker.isPolicyJSPFinished
	 */
	public boolean isPolicyJSPFinished(HttpServletRequest req) {
		return req.getSession().getAttribute(SESSION_FINISHED_KEY)!=null;
	}

	/**
	 * @see IJSPTagChecker.deliverJSPTagRequirements
	 */
	public void deliverJSPTagRequirements(HttpServletRequest req, List<ICredential> listCredentials) {
		HttpSession session=req.getSession();
		session.setAttribute(SESSION_ACCESS_STATE_KEY,SESSION_ACCESS_STATE_RESPONSE);
		session.setAttribute(SESSION_CRED_SET_ACTIVE,listCredentials);
	}

	/**
	 * @see IJSPTagChecker.requestJSPTags
	 */
	public void requestJSPTags(HttpServletRequest req) {
		req.getSession().setAttribute(SESSION_ACCESS_STATE_KEY,SESSION_ACCESS_STATE_REQUEST);
	}

	/**
	 * @see IJSPTagChecker.getJSPTagRequirements
	 */
	public List<ICredential> getJSPTagRequirements(HttpServletRequest req) {
		Object obj=req.getSession().getAttribute(SESSION_CRED_SET_ACTIVE);
		if((obj==null)||(!(obj instanceof List)))
			return null;
		return (List<ICredential>)obj;
	}
}