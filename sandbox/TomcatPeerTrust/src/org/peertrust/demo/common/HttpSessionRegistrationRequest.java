/**
 * 
 */
package org.peertrust.demo.common;

import java.io.Serializable;

//import org.peertrust.net.Message;
import org.peertrust.net.Peer;

/**
 * @author pat_dev
 *
 */
public class HttpSessionRegistrationRequest  implements Serializable{
	
	private String sessionKey;
	
	
	/**
	 * 
	 */
	public HttpSessionRegistrationRequest(String sessionKey,Peer source, Peer target) {
		//super(source,target);
		this.sessionKey=sessionKey;
	}

	/**
	 * @return Returns the sessionKey.
	 */
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * @param sessionKey The sessionKey to set.
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
}
