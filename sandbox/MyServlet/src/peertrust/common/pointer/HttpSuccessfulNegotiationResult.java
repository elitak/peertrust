package peertrust.common.pointer;

import org.protune.net.SuccessfulNegotiationResult;

/**
 * Class to indicate that the negotiation between client and server was
 * successful.
 * @author Sebastian Wittler
 */
public class HttpSuccessfulNegotiationResult extends
	SuccessfulNegotiationResult {

	private static final long serialVersionUID = 6222759965007137270L;
	// Session ID that the client can use to access the resource requested
	private String strSessionID;

	/**
	 * Constructor
	 * @param session_id The session ID.
	 */
	public HttpSuccessfulNegotiationResult(String session_id) {
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
