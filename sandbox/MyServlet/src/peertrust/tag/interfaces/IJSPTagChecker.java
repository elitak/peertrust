package peertrust.tag.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import peertrust.common.interfaces.ICredential;

/**
 * Helps with the handling of the PolicyConditionTag (JSP tag).
 * @see PolicyConditionTag 
 * @author Sebastian Wittler
 */
public interface IJSPTagChecker {
	/**
	 * Checks if the client fulfilled all requirements (policy) of the resource
	 * with the sending of this credential.
	 * @param req The http request.
	 * @param strCredName The credential that the client must show.
	 * @return True, if access granted, otherwise false.
	 */
//TODO: String? ICredential
	public boolean accept(HttpServletRequest req,String strCredName);
	
	/**
	 * Check if the jsp file requested contains the PolicyConditionTag.
	 * @param req The http request.
	 * @return PolicyConditionTag in jsp page?
	 */
	public boolean hasPolicyJSPTag(HttpServletRequest req);
	
	/**
	 * Checks if the handling of the PolicyConditionTag is finished.
	 * @param req The http request.
	 * @return Is handling finished?
	 */
	public boolean isPolicyJSPFinished(HttpServletRequest req);
	
	/**
	 * Stores the credentials that the client must show, so that he can get them
	 * and his response can be validated. Also sends the requirements,
	 * @param req The http request.
	 * @param listCredentials The credentials the client must show because of the
	 * 							PolicyConditionTag requirements. 
	 */
//TODO: IPolicy?
	public void deliverJSPTagRequirements(HttpServletRequest req,
		List<ICredential> listCredentials);
	
	/**
	 * Prepares for accessing the jsp page to collect the PolicyConditionTag's
	 * required credentials.
	 * @param req
	 */
	public void requestJSPTags(HttpServletRequest req);
	
	/**
	 * Gets the credentials that the PolicyConditionTags in the jsp page require.
	 * @param req The http request.
	 * @return The required credentials, null if none.
	 */
//	TODO: IPolicy?
	public List<ICredential> getJSPTagRequirements(HttpServletRequest req);
}
