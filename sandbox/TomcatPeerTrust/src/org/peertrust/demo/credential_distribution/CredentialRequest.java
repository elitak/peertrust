package org.peertrust.demo.credential_distribution;

import java.io.Serializable;



/**
 * Represent a protocol object send from a credential distribution client
 * to a server to get a credential.
 * 
 * @author pat_dev
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
	 * Creates a  named credential.
	 * @param name
	 */
	public CredentialRequest(String name){
		this.name=name;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
