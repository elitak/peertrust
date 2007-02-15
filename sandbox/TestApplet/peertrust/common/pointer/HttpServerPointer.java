package peertrust.common.pointer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.protune.net.Message;
import org.protune.net.Pointer;

/**
 * Pointer (communication part) that the client uses to send his negotiation
 * messages to the server. Used in the HttpNegotiationService.
 * @see HttpNegotiationService
 * @author Sebastian Wittler
 */
public class HttpServerPointer implements Pointer {
	private static final long serialVersionUID = -5966478901476106014L;
	// The service ID.
	private long nServiceID;
	// The URL the messages should be sent to
	private String strURL;
	// The connection to the URL
	private HttpURLConnection urlcon=null;

	/**
	 * Constructor.
	 * @param service_id The service ID.
	 * @param url The URL the messages should be sent to.
	 */
	public HttpServerPointer(long service_id,String url) {
		nServiceID=service_id;
		strURL=url;
	}

	/**
	 * @see Pointer.sendMessage
	 */
	public void sendMessage(Message m) throws IOException {
		// Open and initialize post connecction to URL
		URL url=new URL(strURL);
		urlcon=(HttpURLConnection)url.openConnection();
		urlcon.setUseCaches(false);
		urlcon.setDefaultUseCaches(false);
		urlcon.setDoOutput(true);
		urlcon.setDoInput(true);
        urlcon.setRequestProperty("Content-type","application/octet-stream");
		urlcon.setAllowUserInteraction(false);
		HttpURLConnection.setDefaultAllowUserInteraction(false);
		urlcon.setRequestMethod("POST");
		// Post message
		ObjectOutputStream oos=new ObjectOutputStream(urlcon.getOutputStream());
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
	 * Returns the connction to the URL
	 * @return Connection to URL.
	 */
	public HttpURLConnection getURLConnection() {
		return urlcon;
	}
}
