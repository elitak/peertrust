/**
 * 
 */
package org.peertrust.demo.session_registration;

import java.io.Serializable;

//import org.peertrust.net.Message;
import org.peertrust.net.Peer;

/**
 * @author pat_dev
 *
 */
public class HttpSessionRegistrationRequest  implements Serializable{
	static public int MAKE_REGISTRATION=0;
	static public int REMOVE_REGISTRATION=0;
	
	private String sessionKey;
	private int cmd=MAKE_REGISTRATION;
	
	/**
	 * 
	 */
	public HttpSessionRegistrationRequest(String sessionKey,Peer source, Peer target) {
		this(sessionKey,source,target,MAKE_REGISTRATION);
	}

	public HttpSessionRegistrationRequest(	String sessionKey,
											Peer source, 
											Peer target,
											int cmd) {
		this.cmd=cmd;
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
	
	public boolean doMakeRegistration(){
		return cmd==MAKE_REGISTRATION;
	}
	
	public boolean doRemoveRegistration(){
		return cmd==REMOVE_REGISTRATION; 
	}
}
