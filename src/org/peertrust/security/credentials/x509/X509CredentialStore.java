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
import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.security.credentials.*;

/**
 * Implementation of the abstract credentials.CredentialStore class. This class
 * allows creation and persistant storage of credentials as well as a
 * convinient method for accessing the information of the stored Credentials.
 * 
 * @author Eric Knauss
 */
public class X509CredentialStore extends CredentialStore implements Configurable {

	private String _file ;
	private String _storePassword ;
	private KeyStore _ks;

	private static Logger log = Logger.getLogger(CredentialStore.class);
	
	public X509CredentialStore() {
		super() ;
	}
	
	public void init() throws ConfigurationException
	{
		try {
			_ks = KeyStore.getInstance( "JKS" );
			_ks.load(new FileInputStream(_file), _storePassword.toCharArray() );
			addAllCredentials (_ks) ;
		} catch (KeyStoreException e) {
			log.error(e.getMessage(), e) ;
			throw new ConfigurationException(e) ;
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e) ;
			throw new ConfigurationException(e) ;
		} catch (CertificateException e) {
			log.error(e.getMessage(), e) ;
			throw new ConfigurationException(e) ;
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e) ;
			throw new ConfigurationException(e) ;
		} catch (IOException e) {
			log.error(e.getMessage(), e) ;
			throw new ConfigurationException(e) ;
		} catch (Exception e) {
			log.error(e.getMessage(), e) ;
			throw new ConfigurationException(e) ;
		}
		
	}
//	public X509CredentialStore(String storePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
//		super() ;
//		this.storePassword = storePassword ; 
//		createEmptyStore () ;
//	}

	public void loadAllCredentialsFromFile(String file) throws KeyStoreException, Exception {
		KeyStore keystore = KeyStore.getInstance( "JKS" );
		keystore.load(new FileInputStream(file), _storePassword.toCharArray() );
		
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
		_ks = KeyStore.getInstance( "JKS" );
		_ks.load(null, _storePassword.toCharArray() );
	}
	
	public void addCredential( Credential credential ) throws Exception {
		super.addCredential( credential );
		X509Certificate cert = (X509Certificate)credential.getEncoded();
		_ks.setCertificateEntry( cert.getSubjectDN().getName(), cert );
	}

	public void saveAllCredentialsToFile( String file ) throws Exception {
		OutputStream os = new FileOutputStream( file );
		_ks.store(os, _storePassword.toCharArray());
	}
//	/**
//	 * @return Returns the _file.
//	 */
//	public String getFile() {
//		return _file;
//	}
	/**
	 * @param _file The _file to set.
	 */
	public void setFile(String _file) {
		this._file = _file;
	}
//	/**
//	 * @return Returns the _storePassword.
//	 */
//	public String getStorePassword() {
//		return _storePassword;
//	}
	/**
	 * @param password The _storePassword to set.
	 */
	public void setStorePassword(String password) {
		_storePassword = password;
	}
}
