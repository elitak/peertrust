/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package g4mfs.impl.org.peertrust.security.credentials.x509;

import g4mfs.impl.org.peertrust.config.Configurable;
import g4mfs.impl.org.peertrust.exception.ConfigurationException;
import g4mfs.impl.org.peertrust.exception.CredentialException;
import g4mfs.impl.org.peertrust.security.credentials.Credential;
import g4mfs.impl.org.peertrust.security.credentials.CredentialStore;

import java.security.cert.*;
import java.security.*;
import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;
import g4mfs.impl.org.peertrust.security.credentials.*;

/**
 * <p>
 * Implementation of the abstract credentials.CredentialStore class. This class
 * allows creation and persistant storage of credentials as well as a
 * convinient method for accessing the information of the stored Credentials.
 * </p><p>
 * $Id: X509CredentialStore.java,v 1.1 2005/11/30 10:35:14 ionut_con Exp $
 * <br/>
 * Date: 31-Mar-2004
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:14 $
 * by $Author: ionut_con $
 * </p>
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public class X509CredentialStore extends CredentialStore implements Configurable {

	private static Logger log = Logger.getLogger(X509CredentialStore.class);

	private String _file ;
	private String _storePassword ;
	private KeyStore _ks;
	
	public X509CredentialStore() {
		super() ;
		log.debug("created") ;
	}
	
	public void init() throws ConfigurationException
	{
		String msg = null ;
		if (_file == null)
			msg = "A keystore file has not been given" ;
		else if (_storePassword == null)
			msg = "Store password has not been given" ;
		
		if (msg != null)
		{
			log.error (msg) ;
			throw new ConfigurationException(msg) ;
		}
		
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

	public void loadAllCredentialsFromFile(File file) throws CredentialException {
		KeyStore keystore;
        try {
            keystore = KeyStore.getInstance( "JKS" );
	        keystore.load(new FileInputStream(file), _storePassword.toCharArray() );
			
			addAllCredentials (keystore) ;
        } catch (Exception e) {
            throw new CredentialException(e) ;
        }

    }
	
	private void addAllCredentials (KeyStore ks) throws CredentialException, KeyStoreException
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
	
	public void addCredential( Credential credential ) throws CredentialException {
		super.addCredential( credential );
		X509Certificate cert = (X509Certificate) credential.getEncoded();
		try {
            _ks.setCertificateEntry( cert.getSubjectDN().getName(), cert );
        } catch (KeyStoreException e) {
            throw new CredentialException(e) ;
        }
	}

	public void saveAllCredentialsToFile( File file ) throws CredentialException {
		OutputStream os;
        try {
            os = new FileOutputStream( file );
            _ks.store(os, _storePassword.toCharArray());
        } catch (Exception e) {
            throw new CredentialException(e) ;
        }
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
