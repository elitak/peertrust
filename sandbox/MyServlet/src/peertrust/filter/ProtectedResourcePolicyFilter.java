package peertrust.filter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.protune.core.ProtuneMessage;
import org.protune.net.DispatcherMessage;
import org.protune.net.DispatcherStartNegotiationMessage;
import org.protune.net.EndNegotiationMessage;
import org.protune.net.NegotiationMessage;
import org.protune.net.NegotiationResult;
import org.protune.net.Pointer;
import org.protune.net.Service;
import org.protune.net.ServiceAvailableMessage;
import org.protune.net.ServiceNotAvailableMessage;
import org.protune.net.ServiceRequestMessage;
import org.protune.net.StartNegotiationMessage;
import org.protune.net.WrongMessageTypeException;

import com.hp.hpl.jena.shared.JenaException;

import peertrust.common.pointer.HttpClientPointer;
import peertrust.common.pointer.HttpServerPointer;
import peertrust.filter.factory.ProtectedResourceCheckerFactory;
import peertrust.filter.interfaces.IProtectedResourceChecker;
import peertrust.filter.interfaces.ISessionStoreItem;
import peertrust.filter.interfaces.ISessionStoreList;
import peertrust.filter.service.HttpNegotiationService;

/**
 * A filter that checks if the clients wants to access a protected resource. If
 * yes, the client must fulfill the policies protecting it. Otherwise an error
 * page is shown. The trust negotiation on the server side is done in this filter. 
 * @author Sebastian Wittler
 */
public class ProtectedResourcePolicyFilter implements Filter,ISessionStoreList {

	// Timeout for sessions
	public static final int SESSION_TIMEOUT=20;
	// Name of the negotiation service
	public static final String SERVICE="SuchAService";
	// Filter config
	private FilterConfig filterconfig;
	// Hashmap that stores the content for the session IDs and URL (if the trust negotiation
	// succeeds, the client is given a session ID, the content of the resource is
	// stored here, the client can access the resource then)
	private HashMap<String,ISessionStoreItem> hashmapContent;
	// Checker if the requested resource is protected and if yes, client can satisfy
	// the policies
	private static IProtectedResourceChecker resourceChecker=null;
	// Logger
	private static Logger log = Logger.getLogger(ProtectedResourcePolicyFilter.class);
	// Hashtable with running negotiation services
	Hashtable<Long,Service> hashtableRunningServices = new Hashtable<Long,Service>();
	// Hashtable with negotiation results
	Hashtable<Long,NegotiationResult> hashtableNegotiationResults = new Hashtable<Long,NegotiationResult>();
	// Next service ID that should be used
	long nCurrentRunningServiceID = -1;

	/**
	 * @see Filter.init
	 */
	public void init(FilterConfig config) throws ServletException {
		filterconfig=config;
		try {
			if(resourceChecker==null) {
				// Create resource checker and load its content (resources and the
				// policies protecting them) from rdf file
//TODO: RDF-Datei von auﬂen angeben
				String strBase=config.getServletContext().getRealPath("/");
				resourceChecker=ProtectedResourceCheckerFactory.getInstance().createChecker();
				resourceChecker.loadPolicies(strBase+"schema.rdf",strBase+"model.rdf");
				hashmapContent=new HashMap<String,ISessionStoreItem>();
			}
		} catch (JenaException e) {
///TODO: Handling
			throw new ServletException(e.getMessage());
		}
		log.debug("init ended");
	}

	/**
	 * @see Filter.destroy
	 */
	public void destroy() {
		filterconfig=null;
//TODO: auch andere Objekte
	}
	
	/**
	 * Sends an error page back to the client that a credential is required.
	 * @param resp The http response.
	 * @param credential The Credential that is required
	 * @throws IOException
	 */
	private void sendErrorPage(HttpServletResponse resp,String credential) throws IOException {
//TODO: In Util-Klasse?
		log.debug("Send error page, "+credential);
		BufferedWriter bw=new BufferedWriter(resp.getWriter());
		bw.write("<html><head><title>Error</title></head><body>Sorry, this page is protected, you will have to provide credentials in order to access it</body></html>");
		bw.flush();
		bw.close();
	}

	/**
	 * Sends binary data back to the client. 
	 * @param resp The http response
	 * @param data The binary data
	 * @param contenttype The content type of the object represnted by the binary data
	 * @throws IOException
	 */
	private void sendBytes(HttpServletResponse resp,byte data[],String contenttype) throws IOException {
//		TODO: In Util-Klasse?
		//log.debug("Send bytes to page: "+data.length+" Content-Type: "+resp.getContentType());
		resp.setContentType(contenttype);
		ServletOutputStream sos=resp.getOutputStream();
		sos.write(data);
		sos.flush();
		sos.close();
	}

	/**
	 * @see Filter.doFilter
	 */
	public void doFilter(ServletRequest req, ServletResponse resp,
		FilterChain chain) throws IOException, ServletException {
		if(filterconfig==null)
			return;
		HttpServletRequest http_req=(HttpServletRequest)req;
		HttpServletResponse http_resp=(HttpServletResponse)resp;
		String strURL=http_req.getRequestURL().toString();
		//log.debug("Resource "+strURL);
		Enumeration<String> _enum=http_req.getParameterNames();
		String str="";
		while(_enum.hasMoreElements())
			str+=_enum.nextElement()+",";
		//log.debug("Paramters: "+str);
		// Create session object, if not available
		HttpSession session=http_req.getSession();
		session.setMaxInactiveInterval(3);
		// Look for the applet_access-parameter was specified by the client 
		String strAppletAccess=http_req.getParameter("applet_access");
		// ID of the session
		String strSessionID=session.getId();
		// Look for the session-parameter was specified by the client 
		String strSessionIDParameter=http_req.getParameter("session");

		// Get resource content for session ID given by the client
//TODO: Security, refactoring, in if rein?
		Object o=hashmapContent.get(strURL+strSessionID);
		if((o==null)&&(strSessionIDParameter!=null))
			o=hashmapContent.get(strURL+strSessionIDParameter);

		// If page was accessed by the browser or the applet after trust negotiation,
		// either show resource if it is unprotected or an error page if not
		if(strAppletAccess==null) {
			handlePureBrowserAccess(o,http_resp,http_req,chain);
			return;
		}

		// Read Object that client posted
		Object obj=null;
		try {
			obj=readObject(http_req);
		} catch (ClassNotFoundException e) {
//TODO: Zentraler?
			sendErrorPage(http_resp,"No object");
			return;
		}
		log.debug("Receive Object from class "+obj.getClass());

		// Handle message (trust negotiation)
		if(obj instanceof ServiceRequestMessage)
			handle((ServiceRequestMessage)obj,http_resp);
		else if(obj instanceof DispatcherStartNegotiationMessage)
			handle((DispatcherStartNegotiationMessage)obj, http_resp,strURL);
		else if(obj instanceof DispatcherMessage)
			handle((DispatcherMessage)obj,http_resp,http_req,chain);
		else {
//TODO: was tun?
		}
	}

	/**
	 * Handles a DispatcherMessage from client. The main trust negotiation takes
	 * place here.
	 * @param dm The DispatcherMessage.
	 * @param resp The http response
	 * @param req The http request
	 * @param chain The filter chain
	 */
	private void handle(DispatcherMessage dm,HttpServletResponse resp,
		HttpServletRequest req,FilterChain chain) {
		log.debug("Receive DispatcherMessage");
		// Get negotiation message and it's service ID
		NegotiationMessage nm = dm.getNegotiationMessage();
		long nServiceID=dm.getServiceID();
		// If the client wants to continue with trust negotiation
		if(nm instanceof ProtuneMessage) {
			log.debug("ProtuneMessage in DispatcherMessage");
			ProtuneMessage onm=(ProtuneMessage)nm;
			// Get negotiation service for ID, initialize it with http request and
			// response so that he can send his response back to the client
			Service s=hashtableRunningServices.get(nServiceID);
			if(s instanceof HttpNegotiationService)
				((HttpNegotiationService)s).init(resp,req,chain);
//TODO: Error, falls nicht HttpNegotiationService
			if(s != null) {
				try {
					// Perform next trust negotiatio step with negotiation service
					// and send result to the client
					log.debug("Perform Service start");
					NegotiationMessage nmsg=s.perform(onm);
					// If the negotiation service is finished with the negotiation,
					// remove the service
					if(nmsg instanceof EndNegotiationMessage){
						log.debug("Service sends EndNegotiationMessage");
						hashtableRunningServices.remove(nServiceID);
						hashtableNegotiationResults.put(nServiceID,
							((EndNegotiationMessage)nmsg).getNegotiationResult());
//TODO: NegotiationResult wird wirklich gebraucht?
					}
				} catch(IOException ioe){
					hashtableRunningServices.remove(nServiceID);
//TODO: Fehlermeldung
				} catch(WrongMessageTypeException wmte){
					hashtableRunningServices.remove(nServiceID);
//TODO: Fehlermeldung
				}
			}
		}
		// If th client is finished with the negotiation, remove negotiation service
		else if(nm instanceof EndNegotiationMessage) {
			log.debug("Receive EndNegotiationMessage");
			hashtableRunningServices.remove(nServiceID);
			hashtableNegotiationResults.put(nServiceID,((EndNegotiationMessage)nm).getNegotiationResult());
//TODO: NegotiationResult wird wirklich gebraucht?
		}
	}
	
	/**
	 * Handle DispatcherStartNegotiationMessage from client, by which he indicates
	 * that he wants to start a trust negotiation for accessing a servers resource. 
	 * @param dsnm The DispatcherStartNegotiationMessage from the client.
	 * @param resp The http response.
	 * @param strURL The URL of the resource the client wants to access.
	 * @throws IOException
	 */
	private void handle(DispatcherStartNegotiationMessage dsnm,HttpServletResponse resp,String strURL) throws IOException {
		log.debug("Receive DispatcherStartMessage");
		// If service requested by the client is not supported by the server, tell
		// user and leave
		if(!SERVICE.equals(dsnm.getRequestedService())) {
// TODO: Error, andre Message zur¸cksenden?
			sendErrorPage(resp,"wrong service");
			return;
		}
		// Create pointer (communication part) for the communication with the client
		Pointer pointer=new HttpClientPointer(nCurrentRunningServiceID,resp);
		log.debug("Create Service");
		// Create negotiation service
		Service service=new HttpNegotiationService(pointer,resourceChecker,this);
		hashtableRunningServices.put(nCurrentRunningServiceID,service);
		// Tell client that the negotiation can be started
		sendObject(resp,new StartNegotiationMessage(new HttpServerPointer(nCurrentRunningServiceID,strURL+"?applet_access=true")));
		nCurrentRunningServiceID++;
	}
	
	/**
	 * Handle ServiceRequestMessage in which the client ask the server if he offers
	 * a special service, in this case a trust negotiation service.
	 * @param srm The ServiceRequestMessage.
	 * @param resp The http response.
	 * @throws IOException
	 */
	private void handle(ServiceRequestMessage srm,HttpServletResponse resp) throws IOException {
		// Tell the client if the requested service is supported or not
		log.debug("Receive ServiceRequestMessage");
		if(SERVICE.equals(srm.getRequestedService()))
			sendObject(resp,new ServiceAvailableMessage());
		else
			sendObject(resp,new ServiceNotAvailableMessage());
	}

	/**
	 * If the client wants to get the real resource (either the browser directly or
	 * the applet after trust negotiation). This message sends it to the client.
	 * @param o
	 * @param http_resp
	 * @param http_req
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	private void handlePureBrowserAccess(Object o,HttpServletResponse http_resp,
		HttpServletRequest http_req,FilterChain chain) throws IOException, ServletException {
		String strURL=http_req.getRequestURL().toString();
//TODO: Hier in Hashmap schauen?
		// If resource content was stored by the server for the session ID the client
		// specified
		if(o!=null) {
			// Send content to client and remove stored content from server
			log.debug("Non-Browser-Access started");
			ISessionStoreItem item=(ISessionStoreItem)o;
//TODO: evtl. in Helper class
			sendBytes(http_resp,item.getContentBytes(),item.getContentType());
			hashmapContent.remove(o);
			log.debug("Non-Browser-Access ended "+strURL+" "+item.getContentType());
		}
		// If no resource content is known for this session ID
		else {
			// If resource is protected, tell the user by an error page (access denied)
			Set setCredentials=resourceChecker.getPolicyForResource(strURL);
			log.debug(strURL+" "+setCredentials);
			if((setCredentials!=null)&&(setCredentials.size()>0))
				sendErrorPage(http_resp,"");
//TODO: besser
			// If resource is unprotected, send content to the user (access granted)
			else {
				ProtectedResponseWrapper wrapper=new ProtectedResponseWrapper(http_resp);
				chain.doFilter(http_req,wrapper);
//TODO: Helper class!
				if(strURL.toLowerCase().endsWith(".jsp"))
					sendBytes(http_resp,wrapper.toString().getBytes(),http_resp.getContentType());
				else
					sendBytes(http_resp,wrapper.getBytes(),http_resp.getContentType());
			}
		}
		// Invalidate session, as it is not used any more
		http_req.getSession().invalidate();
	}

	/**
	 * Read an object that the client posted to the server.
	 * @param req The http request
	 * @return The object posted by the client.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Object readObject(HttpServletRequest req) throws IOException, ClassNotFoundException {
//TODO: helper class
		ObjectInputStream ois=new ObjectInputStream(req.getInputStream());
		Object obj=ois.readObject();
		ois.close();
		return obj;
	}

	/**
	 * Send an object back to the client.
	 * @param resp The http response.
	 * @param obj The object that should be sent.
	 * @throws IOException
	 */
	private void sendObject(HttpServletResponse resp,Object obj) throws IOException {
//TODO: helper class
		log.debug("Send object from class "+obj.getClass());
		ObjectOutputStream oos=new ObjectOutputStream(resp.getOutputStream());
		oos.writeObject(obj);
		oos.flush();
		oos.close();
	}

	/**
	 * Adds a resource content to the list, so that the client can access it after
	 * trust negotiation.
	 * @param key The session ID for the content.
	 * @param item The resource content.
	 */
	public void addToList(String key, ISessionStoreItem item) {
		hashmapContent.put(key,item);
	}
}
