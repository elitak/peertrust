/*
 * Created on 14.01.2004
 */
package org.peertrust.security.credentials;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * This class aims to ease the validation, management and distribution of
 * Security Credentials for automated trust-negotiation.
 * 
 * @author Eric Knauss
 */
public abstract class Credential implements Serializable {

	public abstract String getStringRepresentation();

	public abstract String getSignerName();

	private static Logger log = Logger.getLogger(Credential.class);
	
	/**
	 * Returns the credential in its special format. This might be a Certificate 
	 * or a signed xml document or what ever.
	 * @return An encoded credential
	 */
	public abstract Object getEncoded();
}
