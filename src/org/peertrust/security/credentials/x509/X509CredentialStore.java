/*
 * X509CredentialStore.java
 *
 * Version 1.0: Initial implementation
 *     Author: Eric Knauss
 *     Date:   31/03/2004
 */
package org.peertrust.security.credentials.x509;

import java.security.cert.*;
import java.security.*;
import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import org.peertrust.security.credentials.*;

/**
 * Implementation of the abstract credentials.CredentialStore class. This class
 * allows creation and persistant storage of credentials as well as a
 * convinient method for accessing the information of the stored Credentials.
 * 
 * @author Eric Knauss
 */
public class X509CredentialStore extends CredentialStore {

	private String file ;
	private String storePassword ;
	private KeyStore ks;

	private static Logger log = Logger.getLogger(CredentialStore.class);
	
	public X509CredentialStore(String file, String storePassword ) throws Exception {
		super() ;
		this.file = file ;
		this.storePassword = storePassword ; 
		ks = KeyStore.getInstance( "JKS" );
		ks.load(new FileInputStream(file), storePassword.toCharArray() );
		addAllCredentials (ks) ;
	}
	
	public X509CredentialStore(String storePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		super() ;
		this.storePassword = storePassword ; 
		createEmptyStore () ;
	}

	public void loadAllCredentialsFromFile(String file) throws KeyStoreException, Exception {
		KeyStore keystore = KeyStore.getInstance( "JKS" );
		keystore.load(new FileInputStream(file), storePassword.toCharArray() );
		
		addAllCredentials (keystore) ;
	}
	
	private void addAllCredentials (KeyStore ks) throws Exception
	{
		Enumeration tmpE = ks.aliases();
		String tmpS;
		while ( tmpE.hasMoreElements() ) {
			tmpS = (String)tmpE.nextElement();
			if ( ks.isCertificateEntry( tmpS ) ) {
				// log.debug("TMP: Is certificate") ;
				X509Certificate cert = (X509Certificate)ks.getCertificate( tmpS ) ;
				if (X509Credential.isX509Credential(cert))
				{
					// log.debug("TMP: Is credential") ;
					addCredential( new X509Credential(cert) );
				}
			}
		}
	}
	private void createEmptyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	{
		ks = KeyStore.getInstance( "JKS" );
		ks.load(null, storePassword.toCharArray() );
	}
	
	public void addCredential( Credential credential ) throws Exception {
		super.addCredential( credential );
		X509Certificate cert = (X509Certificate)credential.getEncoded();
		ks.setCertificateEntry( cert.getSubjectDN().getName(), cert );
	}

	public void saveAllCredentialsToFile( String file ) throws Exception {
		OutputStream os = new FileOutputStream( file );
		ks.store(os, storePassword.toCharArray());
	}
}
