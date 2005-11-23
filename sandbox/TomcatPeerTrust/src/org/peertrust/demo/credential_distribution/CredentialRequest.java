package org.peertrust.demo.credential_distribution;

import java.io.Serializable;



/**
 * Represent a protocol object send by a credential distribution client
 * to a server to get a credential.
 * 
 * @author Patrice Congo (token77)
 *
 */
public class CredentialRequest implements Serializable {
	/**
	 * The name of the credential to get.
	 */
	private String name;
	
	/**
	 * create a blank credential.
	 */
	public CredentialRequest(){
		this.name=null;
	}
	
	/**
	 * Creates a  Request for a named credential.
	 * @param name -- the name of the credntial to request.
	 */
	public CredentialRequest(String name){
		this.name=name;
	}

	/**
	 * @return Returns the name of the credential to request.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the credential to request.
	 * @param name -- the new name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
