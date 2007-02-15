package peertrust.filter.service;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.protune.api.FilteredPolicy;
import org.protune.api.LoadTheoryException;
import org.protune.core.ProtuneMessage;
import org.protune.net.DispatcherMessage;
import org.protune.net.EndNegotiationMessage;
import org.protune.net.NegotiationMessage;
import org.protune.net.OngoingNegotiationMessage;
import org.protune.net.Pointer;
import org.protune.net.Service;
import org.protune.net.WrongMessageTypeException;

import peertrust.common.interfaces.ICredential;
import peertrust.common.pointer.HttpClientPointer;
import peertrust.common.pointer.HttpSuccessfulNegotiationResult;
import peertrust.common.pointer.HttpUnsuccessfulNegotiationResult;
import peertrust.filter.ProtectedResponseWrapper;
import peertrust.filter.interfaces.IProtectedResourceChecker;
import peertrust.filter.interfaces.ISessionStoreList;
import peertrust.tag.factory.JSPTagCheckerSingletonFactory;
import peertrust.tag.factory.SessionStoreItemFactory;
import peertrust.tag.interfaces.IJSPTagChecker;

//TODO: replace dummy things
//TODO: Refactoring eval

/**
* The Client negotiation service. Used by the ProtectedResourcePolicyFilter
* for trust negotiation. Dummy implementation.
* @see ProtectedResourcePolicyFilter
* @author Sebastian Wittler
*/
public class HttpNegotiationService extends Service {
	// Logger
	private static Logger log = Logger.getLogger(HttpNegotiationService.class);
	// The checker object which decides if a resource is protected by policies or
	// the client fullfills the protecting policies
	private IProtectedResourceChecker checker;
	// Hashtable that stores the negotiation status
	private Hashtable<String,Set> hashtableNegotiation=new Hashtable<String,Set>();
	// The http response
	private HttpServletResponse http_resp=null;
	// The http request
	private HttpServletRequest http_req=null;
	// The filter chain
	private FilterChain filterchain=null;
	// Credentials that were received for fulfillment of custom JSP tags
	// See PolicyCondition(Tag)
	private List<ICredential> listCredsJSPReceived=null;
	// This interface represents hashtable for the ProtectedResourcePolicyFilter
	// in which the requeested resource content is stored
	private ISessionStoreList sessionstorelist;

	/**
	 * Constructor.
	 * @param p Communication part (pointer) of the client.
	 * @param _checker The checker if a resource is protected or the client fullfills
	 * 					the policies protecting it.
	 * @param sessionlist The hashtable to store the requested resource content for
	 * 						the client
	 */
	public HttpNegotiationService(Pointer p,IProtectedResourceChecker _checker,ISessionStoreList sessionlist) {
		otherPeer=p;
		checker=_checker;
		sessionstorelist=sessionlist;
	}

	/**
	 * Initializes the negotiation service. Must be called before every call of
	 * perform, because the answer must be send as an http response in reaction to
	 * the clients request.
	 * @param resp The http response.
	 * @param req The http request.
	 * @param chain The filter chain.
	 */
	public void init(HttpServletResponse resp,HttpServletRequest req,FilterChain chain) {
		http_resp=resp;
		http_req=req;
		filterchain=chain;
	}

	/**
	 * @see Service.eval
	 */
	public NegotiationMessage eval(OngoingNegotiationMessage onm) throws WrongMessageTypeException {
		// If the message from the server is not correct or the communication part
		// of the client is wrong, the negotiation is not successful
		if((!(onm instanceof ProtuneMessage))||(!(otherPeer instanceof HttpClientPointer)))
//			TODO: tell the user
			return new EndNegotiationMessage(new HttpUnsuccessfulNegotiationResult(getSession(false,false)));
		// To answer the client, the http response is used, so it is forwarded to
		// the communication part of the client
		((HttpClientPointer)otherPeer).setResponse(http_resp);
		// Get FilteredPolicies out of message
		ProtuneMessage pm=(ProtuneMessage)onm;
		FilteredPolicy policies[]=pm.getFilteredPolicies();
		// Perhaps jsp file was requested, handling of PolicyConditionTag necessary
		if(policies.length==0)
			return handleJSPPage(false);
		// List of policies that protect a resource (send to client later)
		List<FilteredPolicy> listPolicies=new LinkedList<FilteredPolicy>();
		// Iterate through all FilteredPolicies that the client sent
		for(int i=0;i<policies.length;i++) {
			// Get the credential or resource the current FilteredPolicy requests
			// from client
			String strCred=policies[i].toString();
			log.debug("Credential: "+strCred);
			// Get policies that protect this resource
			Set setPols=checker.getPolicyForResource(strCred);
//TODO: Hack raus
			if(setPols==null)
				setPols=checker.getPolicyForResource("http://localhost:8081/MyServlet/"+strCred);
			//If Resource/credential is protected
			if(setPols!=null) {
				log.debug("protected");
				NegotiationMessage nm=handleResourceProtected(setPols,listPolicies,strCred);
				if(nm!=null)
					return nm;
			}
			//If Resource/credential is not protected
			else {
				log.debug("unprotected");
				handleResourceNotProtected(setPols,strCred);
			}
		}
		// If negotiation state is empty (client fulfilled all requirements for the
		// requested resource), the negotiation is successful
		if(hashtableNegotiation.isEmpty()) {
			log.debug("Negotiation successful");
			// Perhaps policies for PolicyConditionTags in jsp are fulfilled now
			return handleJSPPage(true);
		}
		// Send requirements (policies to access requested resource) to the client 
		log.debug("Send requirements to access resource "+listPolicies);
		return new ProtuneMessage((FilteredPolicy[])listPolicies.toArray(new FilteredPolicy[0]),null);
	}
	
	/**
	 * Handles the case when a resource or credential a client requests is protected.
	 * @param setPols The policies which protect the resource/credential.
	 * @param listPolicies The list of policies which should be sent to the client.
	 * @param strCred The requested resource/policy
	 * @return A response which should be sent to the client, null if not
	 */
	private NegotiationMessage handleResourceProtected(Set setPols,List<FilteredPolicy> listPolicies,String strCred) {
		// if not protected, send requested resource back
		if(setPols.isEmpty()) {
			try {
				listPolicies.add(new FilteredPolicy(strCred));
			}
			catch(LoadTheoryException e) {
				return new EndNegotiationMessage(new HttpUnsuccessfulNegotiationResult(getSession(false,false)));
			}
		}
		// Otherwise put policies to the negotiation state
		else
			hashtableNegotiation.put(strCred,setPols);
		log.debug("Resource/Credential protected");
		// Iterate through all policies that protect the resource/credential
		Iterator iter=setPols.iterator();
		while(iter.hasNext())
			try {
				// Transform policy into filtered policy and add it to the list that
				// should be sent to the client
				listPolicies.add(new FilteredPolicy(
					((ICredential)iter.next()).toStringRepresentation()));
			} catch (LoadTheoryException e) {
//TODO: tell
				log.debug("LoadTheoryException 1");
				// If filtered policy can't be created, end trust negotiation
				return new EndNegotiationMessage(new HttpUnsuccessfulNegotiationResult(getSession(false,false)));
			}
		// Send no message
		return null;
	}

	/**
	 * Handles the case when a resource or credential a client requests is not
	 * protected.
	 * @param setPols The policies which protect the resource/credential.
	 * @param strCred The requested resource/policy
	 */
	private void handleResourceNotProtected(Set setPols,String strCred) {
//TODO: Was ist mit set-Parameter?
		// Look if credential from client matches to one of the entries in the negotiaion
		// state, maybe policies are fulfilled now
		log.debug("Look to previous negotiations "+hashtableNegotiation.size());
		Enumeration _enum=hashtableNegotiation.keys();
		String key;
		// Iterate through negotiation state
		while(_enum.hasMoreElements()) {
			key=(String)_enum.nextElement();
			log.debug("Key "+key);
			setPols=(Set)hashtableNegotiation.get(key);
			Iterator iter=setPols.iterator();
			// Iterate through policies of current negotiation state entry
			while(iter.hasNext()) {
				ICredential credential=(ICredential)iter.next();
				log.debug("Value "+credential.toStringRepresentation());
//TODO: Credential/Policy?
				// Does credential from client match/fulfills current policy?
				if(strCred.equals(credential.toStringRepresentation())) {
					log.debug("One requirement fulfilled "+setPols.size());
					// If credentials were required to fulfill custom PolicyCondition
					// tag in jsp, store credential here
					if(listCredsJSPReceived!=null)
						listCredsJSPReceived.add(credential);
					// Remove policy from negotiation state, as it is fulfilled now
					iter.remove();
					log.debug("Item removed "+setPols.size());
					break;
				}
			}
			// If current entry in negotiation state is empty, remove it, the resource
			// it protected can now be accessed by the client
			if(setPols.isEmpty()) {
				log.debug("Set fulfilled");
				hashtableNegotiation.remove(key);
			}
		}
	}

	/**
	 * @see Service.perform
	 */
	public NegotiationMessage perform(OngoingNegotiationMessage onm) throws IOException, WrongMessageTypeException{
		// Perform negotiation
		NegotiationMessage toSend = eval(onm);
		// Send answer to the client
		long service_id=((HttpClientPointer)otherPeer).getServiceID();
		otherPeer.sendMessage(new DispatcherMessage(service_id,toSend));
		return toSend;
	}

	/**
	 * Returns the session ID for the resource content (as clients have to get it
	 * after trust negotiation) or error page. Also makes the corresponding entries
	 * in the hashtable of the ProtectedResourcePolicyFilter (resource content/error page).
	 * @param satisfied Was trust negotiation successful?
	 * @param jsp Was JSP file with PolicyConditionTag accessed?
	 * @return The session ID
	 */
	private String getSession(boolean satisfied,boolean jsp) {
		log.debug("getSession "+satisfied+" "+jsp);
		HttpSession session=http_req.getSession();
		String strSessionID=session.getId();
		String strURL=http_req.getRequestURL().toString();
		SessionStoreItemFactory factory=SessionStoreItemFactory.getInstance();
		// If trust negotiation was succesfull (client gets the resource)
		if(satisfied) {
			ProtectedResponseWrapper wrapper=new ProtectedResponseWrapper(http_resp);
			try {
				// Was JSP file with PolicyConditionTag accessed?
				if(jsp) {
					// Send the tags the PolicyConditionTag required to the tag
					// handler, so that the content of the page can be returned
					log.debug("Credentials sent: "+listCredsJSPReceived);
					JSPTagCheckerSingletonFactory.getInstance().getChecker().
						deliverJSPTagRequirements(http_req,listCredsJSPReceived);
				}
				// Get the resource content with the help of a wrapper
				filterchain.doFilter(http_req,wrapper);
			} catch (ServletException e) {
//TODO:tell
				sessionstorelist.addToList(strURL+strSessionID,
					factory.createSessionStoreItem(
					e.getMessage(),"text/html"));
			} catch (IOException e) {
//TODO:tell
				sessionstorelist.addToList(strURL+strSessionID,
					factory.createSessionStoreItem(
					e.getMessage(),"text/html"));
			}
			// Store resource content in the hashtable in ProtectedresourcePolicyFilter
			// along with the sessionID (and URL)
			sessionstorelist.addToList(strURL+strSessionID,
					factory.createSessionStoreItem(
					jsp ? wrapper.toString().getBytes() : wrapper.getBytes(),
					http_resp.getContentType()));
		}
		// If trust negotiation was not succesful (client gets the resource)
		else
//TODO:tell
			// Store error page in the hashtable in ProtectedresourcePolicyFilter
			// along with the sessionID (and URL)
			sessionstorelist.addToList(strURL+strSessionID,
				factory.createSessionStoreItem(
				"Sorry, this page is protected, you will have to provide credentials in order to access it","text/html"));
		return strSessionID;
	}
	
	/**
	 * Handles access to jsp pages (because of the custom PolicyConditionTag and its
	 * handling).
	 * @param satisfied Was trust negotiation successful?
	 * @return Message that should be sent back to the client.
	 */
	private NegotiationMessage handleJSPPage(boolean satisfied) {
		String strURL=http_req.getRequestURL().toString();
		// If requested resource is not an jsp file, it can maybe be accessed
		if(!strURL.toLowerCase().endsWith((".jsp")))
			return new EndNegotiationMessage(new HttpSuccessfulNegotiationResult(getSession(satisfied,false)));
		try {
			log.debug("start handleJSPPage");
			// If requested jsp page contains the PolicyConditionTag
			if(!JSPTagCheckerSingletonFactory.getInstance().
				getChecker().hasPolicyJSPTag(http_req)) {
				// Send client the policies that these tags in the page requires
				log.debug("request JSP");
				return whatCredsDoJSPFileRequire();
			}
			// If requested jsp page doesn't contain the PolicyConditionTag
			else {
				// The page can maybe be accessed
				log.debug("response JSP");
				return new EndNegotiationMessage(new HttpSuccessfulNegotiationResult(getSession(true,true)));
			}
		} catch (IOException e) {
//TODO: tell
			e.printStackTrace();
		} catch (ServletException e) {
//TODO: tell
			e.printStackTrace();
		}
		// Resource can be accessed
//TODO: really?
		return new EndNegotiationMessage(new HttpSuccessfulNegotiationResult(getSession(true,false)));
	}
	
	/**
	 * Send client the policies that the PolicyConditionTags in the requested
	 * jsp page require.
	 * @return Message to client that contains all the requested policies. 
	 * @throws IOException
	 * @throws ServletException
	 */
	private NegotiationMessage whatCredsDoJSPFileRequire() throws IOException, ServletException {
		String strURL=http_req.getRequestURL().toString();
		IJSPTagChecker checker=JSPTagCheckerSingletonFactory.getInstance().getChecker();
		// Prepare the request for the policies
		checker.requestJSPTags(http_req);
		// Access the jsp page with the help of a wrapper
		ProtectedResponseWrapper wrapper=new ProtectedResponseWrapper(http_resp);
		filterchain.doFilter(http_req,wrapper);
		// If jsp file doesn't contain the PolicyConditionTag, no policies are required
		// and the client can get access (trust negotiation successful)
		if(!checker.hasPolicyJSPTag(http_req)) {
			log.debug("No Credential attribute");
			return new EndNegotiationMessage(new HttpSuccessfulNegotiationResult(getSession(true,false)));
		}
		// Get policies the PolicyConditionTags request
		List listPolicies=checker.getJSPTagRequirements(http_req);
		log.debug("List policies "+listPolicies);
		// If no policies are requested from these tags, the trust negotiation is
		// successful
		if(listPolicies.isEmpty()) {
			log.debug("empty list");
			return new EndNegotiationMessage(new HttpSuccessfulNegotiationResult(getSession(true,false)));
		}
		// If client hasn't fulfilled the policies yet
		if(!checker.isPolicyJSPFinished(http_req)) {
			log.debug("Required credentials");
			try {
				// Put requested policies into the negotiation state
				FilteredPolicy policies[]=new FilteredPolicy[listPolicies.size()];
				Set<ICredential> setPols=new HashSet<ICredential>();
				ICredential cred;
				for(int i=0;i<listPolicies.size();i++) {
					cred=(ICredential)listPolicies.get(i);
					policies[i]=new FilteredPolicy(cred.toStringRepresentation());
					setPols.add(cred);
				}
				hashtableNegotiation.put(strURL,setPols);
				// This list stores all credentials that a client sends to fulfill
				// above policies for the jsp page
				listCredsJSPReceived=new LinkedList<ICredential>();
				log.debug("Required credentials finished "+setPols);
				// Message with policies for client
				return new ProtuneMessage(policies,null);
			} catch (LoadTheoryException e) {
//TODO: tell
				log.debug("LoadTheoryException 2");
				return new EndNegotiationMessage(new HttpSuccessfulNegotiationResult(getSession(true,false)));
			}
		}
		// Otherwise negotiation is successful
//TODO: really?
		return new EndNegotiationMessage(new HttpSuccessfulNegotiationResult(getSession(true,false)));
	}
}
