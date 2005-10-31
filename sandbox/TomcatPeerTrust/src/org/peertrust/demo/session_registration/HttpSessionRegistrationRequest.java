/**
 * 
 */
package org.peertrust.demo.session_registration;

import java.io.Serializable;
//import org.peertrust.net.Peer;

/**
 * Protocol object to un/register an httpsesson. 
 * @author pat_dev
 *
 */
 
public class HttpSessionRegistrationRequest  implements Serializable{
	/** command for registration*/
	static public int MAKE_REGISTRATION=0;
	
	/** comand for dereistration*/
	static public int REMOVE_REGISTRATION=0;
	
	/** the session ke to register*/
	private String sessionKey;
	/** the key representing the action to take: MAKE_REGISTRATION to register or
	 * REMOVE_REGISTRATION to unregister th session.
	 */ 
	private int cmd=MAKE_REGISTRATION;
	
	/**
	 * Create a registration reuest object object with the cmd preset to MAKE_REGISTRATION.
	 * @param sessionKey ths session key to register
	 * @param source -- the sending peer(e.g. on th browsers seid )
	 * @parm target -- the destination peer(e.g. on the http server side)
	 */
	public HttpSessionRegistrationRequest(String sessionKey/*,Peer source, Peer target*/) {
		this(sessionKey,/*source,target,*/MAKE_REGISTRATION);
	}
	
	/**
	 * Create a registration request object.
	 * @param sessionKey ths session key to register
	 * @param source -- the sending peer(e.g. on th browsers seid )
	 * @parm target -- the destination peer(e.g. on the http server side)
	 * @param cmd the int key of the action to take
	 */
	public HttpSessionRegistrationRequest(	String sessionKey,
											/*Peer source, 
											Peer target,*/
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
	
	/**
	 * @return true if the registration is to be made
	 */
	public boolean doMakeRegistration(){
		return cmd==MAKE_REGISTRATION;
	}
	
	/**
	 * 
	 * @return trueif the registration is to be removed.
	 */
	public boolean doRemoveRegistration(){
		return cmd==REMOVE_REGISTRATION; 
	} 
}
