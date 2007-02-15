package peertrust.common.pointer;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.protune.net.Message;
import org.protune.net.Pointer;

/**
 * Pointer (communication part) that the server uses to send his negotiation
 * messages to the client (as a response). Used in the HttpNegotiationService. 
 * @see HttpNegotiationService
 * @author Sebastian Wittler
 */

public class HttpClientPointer implements Pointer {
	private static final long serialVersionUID = -4377642414478327049L;
	// The service ID.
	private long nServiceID;
	// The http resonse
	private HttpServletResponse httpResponse;

	/**
	 * Constructor.
	 * @param service_id The service ID.
	 * @param resp The http response.
	 */
	public HttpClientPointer(long service_id,HttpServletResponse resp) {
		nServiceID=service_id;
		httpResponse=resp;
	}

	/**
	 * @see Pointer.sendMessage
	 */
	public void sendMessage(Message m) throws IOException {
		ObjectOutputStream oos=new ObjectOutputStream(httpResponse.getOutputStream());
		oos.writeObject(m);
		oos.flush();
		oos.close();
	}
	
	/**
	 * Returns the service ID.
	 * @return Service ID.
	 */
	public long getServiceID() {
		return nServiceID;
	}

	/**
	 * Returns the http response;
	 * @param resp Http response.
	 */
	public void setResponse(HttpServletResponse resp) {
		httpResponse=resp;
	}
}
