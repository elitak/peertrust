/**
 * 
 */
package org.peertrust.demo.credential_distribution;

import java.io.Serializable;

/**
 * Represents a protocol object send in response to a credential
 * request.
 * 
 * @see org.peertrust.demo.credential_distribution.CredentialRequest
 * @author pat_dev
 *
 */
public class CredentialResponse implements Serializable {
	/**
	 * the name or key associated with a credential
	 */
	private String name;
	
	/**
	 * the value of the credential
	 */
	private String value;
	
	/**
	 * Constructs a blank credential.
	 *
	 */
	public CredentialResponse(){
		this.name=null;
		this.value=null;
	}
	
	/**
	 * construct a credential from providing value and name.
	 * @param name -- the name of the credential
	 * @param value -- the value of the credential
	 */
	public CredentialResponse(String name, String value){
		this.name=name;
		this.value=value;
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

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	

}
