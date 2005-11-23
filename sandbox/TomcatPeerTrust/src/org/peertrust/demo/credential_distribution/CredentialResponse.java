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
 * @author Patrice Congo (token77)
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
	 * Constructs a blank credential response.
	 */
	public CredentialResponse(){
		this.name=null;
		this.value=null;
	}
	
	/**
	 * Constructs a credential response from provided credential value and name.
	 * @param name -- the name of the credential
	 * @param value -- the value of the credential
	 */
	public CredentialResponse(String name, String value){
		this.name=name;
		this.value=value;
	}

	/**
	 * @return Returns the name of the credential.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the credential
	 * @param name -- the new name the credential.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value of the credentual contains in this response.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the credential distributed by the response.
	 * @param value -- the new value of the credential.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {		
		return "CredentialResponse[name:"+name+",value:"+value+"]";
	}
	
	

}
