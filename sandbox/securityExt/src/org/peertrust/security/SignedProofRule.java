package org.peertrust.security;

import java.io.*;
import java.security.cert.*;
import org.peertrust.security.credentials.*;
import org.peertrust.security.credentials.x509.*;
import net.jxta.edutella.util.*;

/**
 * Represents a signed rule in a proof tree. Contains also the corresponding credential and
 * certificate chain.
 * @author Sebastian Wittler
 */
public class SignedProofRule extends ProofRule implements Serializable {
	/** The credential that corresponds to the signed rule. */
	private Credential credential;
	/** The CertificateChain that corresponds to the signed rule. */
	private CertificateChain certificatechain;

	/**
	 * Constructor.
	 * Constructs the certificate chain.
	 * @param rule The signed rule in text representation.
	 * @param credential The corresponding credential.
	 * @param Configurator The configuration object.
	 */
	public SignedProofRule(String rule,Credential credential,Configurator config) {
		super(rule);
		this.credential=credential;
		if(credential instanceof X509Credential) {
			//Contruct the certificate chain with the certificate which contains
			//the credential as starting point
			X509Certificate x509cert=(X509Certificate)credential.getEncoded();
			certificatechain=new CertificateChain(config,CertificateChain.
				getSubjectAlias(x509cert),CertificateChain.getIssuerAlias(
				x509cert),null);
		}
		else
			certificatechain=null;
	}

	/**
	 * Return the corresponding credential.
	 * @return Credential The corresponding credential.
	 */
	public Credential getCredential() {
		return credential;
	}

	/**
	 * Return the corresponding certificate chain.
	 * @return CertificateChain The corresponding certificate chain.
	 */
	public CertificateChain getCertificateChain() {
		return certificatechain;
	}
}