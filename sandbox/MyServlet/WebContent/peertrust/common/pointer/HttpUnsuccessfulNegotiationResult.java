package peertrust.common.pointer;

import org.protune.net.UnsuccessfulNegotiationResult;

/**
 * Class to indicate that the negotiation between client and server was
 * unsuccessful.
 * @author Sebastian Wittler
 */
public class HttpUnsuccessfulNegotiationResult extends
	UnsuccessfulNegotiationResult {

	private static final long serialVersionUID = 1175861000994459905L;
	// Session ID that the client can use to access the resource requested
//TODO: Evtl. nicht benötigt
	private String strSessionID;
	
	/**
	 * Constructor
	 * @param session_id The session ID.
	 */
	public HttpUnsuccessfulNegotiationResult(String session_id) {
		strSessionID=session_id;
	}
	
	/**
	 * Returns the session ID.
	 * @return Session ID.
	 */
	public String getSessionID() {
		return strSessionID;
	}
}
