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
package org.policy.crypt.credentials.x509;

import java.security.*;

import org.apache.log4j.Logger;
import org.peertrust.security.credentials.CryptTools;

/**
 * <p>
 * A command-line tool that accepts a String-Representation of a credential,
 * a private-key, that is used as the CA-Key and a public-key that is used
 * as the subject-key.</p>
 * <p>All variables that need to be adjusted should be inside the main method.
 * Of course the methods of this class are usefull to build graphical tools
 * for credential management. They also show how X509Credentials can be 
 * created and added to a X509CredentialStore.
 * </p><p>
 * $Id: X509CredentialBuilder.java,v 1.1 2007/02/17 16:59:29 dolmedilla Exp $
 * <br/>
 * Date: 31-Mar-2004
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public class X509CredentialBuilder
{
    private static Logger log = Logger.getLogger(X509CredentialBuilder.class);
    
	private static String _helpString = "Usage:\n"
		+ "java X509CredentialBuilder -privKey <private key>"
		+ " -pubKey <public key> -credential <credString\n"
		+"\nwith <private key>: the Private Key of this CA\n"
		+"     <public key>:  the public key of the subject of the credential\n"
		+"     <credString>:  the String representation of the credential";

	private PrivateKey _caKey;
	private String _issuerDN;
	private PublicKey _subjectKey;
	private String _subjectAlias;
	private String _credential;

	/**
	 * Initializes a CredentialBuilder with the private key that should be
	 * used to sign new credentials. This should be the private key of the
	 * "signer" of the credential. The distinguished name of the signer
	 * must be added also. It should have the form "CN='name'".
	 * @param issuerDN the distinguished name of the signer. It is critical
	 * that this String is X509 conform.
	 * @param caKey the private key of the signer of the credential.
	 */
	public X509CredentialBuilder( String issuerDN, PrivateKey caKey ) {
		_caKey = caKey;
		_issuerDN = issuerDN;
	}

	/**
	 * Sets the public key of the subject of the credential. 
	 * @subKey Public key that belongs to the subject of the credential.
	 */
	public void setSubjectKey( PublicKey subKey ) {
		_subjectKey = subKey;
	}

	/**
	 * This method is intended for internal use only. It returns the 
	 * public key of the subject. If this key was not set, a new key
	 * is created with default parameters. Note that because the 
	 * private key to this public key is discarded, this is only 
	 * usefull for testing purposes or a prototype environment that
	 * does not make use of a public key infrastructure. By overwiting
	 * this method it should be easy to load keys from a given Key Store.
	 * @return Public key of the subject of the credential.
	 */
	public PublicKey getSubjectKey() {
		if ( _subjectKey != null ) {
			return _subjectKey;
		}
		else {
			try {
				return CryptTools.makeKeyPair( "RSA", 512 ).getPublic();
			} catch ( NoSuchAlgorithmException nsae ) {
				nsae.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * Sets the String that will be encoded into the X509Credential.
	 */
	public void setCredentialString( String credentialString ) {
		_credential = credentialString;
	}

	public String getCredentialString() {
		return _credential;
	}

	/**
	 * The distinguished name of the subject. It should contain
	 * at least the CN field. (i.e. "CN=alice").
	 */
	public void setSubjectAlias( String subjectAlias ) {
		_subjectAlias = subjectAlias;
	}

	public String getSubjectAlias() {
		return _subjectAlias;
	}

	public String getIssuerDN() {
		return _issuerDN;
	}

	/**
	 * Creates a new X509Credential and adds it to the given X509CredentialStore. 
	 * Use the setters of this class for setting the information that should be stored
	 * before invokation of this method.
	 * @param credentialStore The credential store this credential should be added to.
	 */
//	public void createCredential( X509CredentialStore credentialStore ) throws Exception {
//		X509Certificate cert = CryptTools.createCert( getSubjectAlias(), getCredentialString(), getSubjectKey(), getIssuerDN(), _caKey );
//		credentialStore.addCredential( credentialStore.buildCredential( cert ) );
//	}

	/**
	 * <p>
	 * Accepts the information needed for creation of one credential as 
	 * arguments. The signer specific variables are set locally in this 
	 * main method and should be adjusted to fit this CA. Valid arguments
	 * are:
	 * </p>
	 * <p>
	 * -subjectAlias (The distinguished name of the subject)
	 * <p>
	 * -credString (The value of the credential. This is the String that
	 * will be returned by the new credentials getStringRepresentation()
	 * method)
	 * </p>
	 * <p>
	 * Note that it is not possible to give a public key. This is because
	 * I have no knowledge about where to obtain the public keys. Methods
	 * to fetch public keys for a given alias are easy to write. Also it 
	 * should be easy to add the alias and its automatical created KeyPair
	 * into a Key Store. Just insert the needed methods. Right now a random
	 * key is created which is sufficient for testing purposes.
	 */
	public static void main( String[] args ) {
		// For each new credential I need the following information:
		// - the subject alias (like "CN=alice")
		// - the subjects public key 
		// - the String Representation of the credential (like member(alice)@visa)
		// 
		// I preferred to make the CA specific values static. Set
		// the following variables to correct values:
		// - the signer alias (like "CN=verisign")
		// - the signer private key (read it from a valid keystore...)

		try {
			//////////////////////////////////////
			// Signer-Section
			String signerAlias = "CN=X509CredentialBuilderAlias";
			//////////////////////////////////////
			// Fetching signerKey from a keyStore:
			KeyStore ks;
			CryptTools.installBouncyCastle();
			ks = KeyStore.getInstance( "JKS" );
			java.io.InputStream is = new java.io.FileInputStream( "test_keystore" );
			ks.load( is,  "testpwd".toCharArray() );
			PrivateKey signerKey = (PrivateKey) ks.getKey( "test", "testpwd".toCharArray() );
			// signerKey loaded.
			//////////////////////////////////////
			
			// End Signer-Section.
			//////////////////////////////////////

			//////////////////////////////////////
			// Initializing CredentialBuilder
			X509CredentialBuilder builder = new X509CredentialBuilder( signerAlias, signerKey );
			String subjectAlias = null;
			String credentialString = null;
			PublicKey subjectKey = null;
			for ( int i = 0; i < args.length; i++ ) {
				if ( args[i].trim().startsWith("-subjectAlias") ) {
					subjectAlias = args[i].substring( args[i].indexOf( ' ' ) );
					System.out.println( "Subject Alias = " + subjectAlias );
				} else if ( args[i].trim().startsWith("-credString") ) {
					credentialString = args[i].substring( args[i].indexOf( ' ' ) );
					System.out.println( "Credential = " + credentialString );
				}  
			}
			builder.setSubjectAlias( subjectAlias );
			builder.setSubjectKey( subjectKey );
			builder.setCredentialString( credentialString );
			// End: Initialization of builder
			//////////////////////////////////////////

			//////////////////////////////////////////
			// Loading CredentialStore
			java.io.File storeFile = new java.io.File( "builderStore" );
			X509CredentialStore store = new X509CredentialStore();
			try {
				store.loadAllCredentialsFromFile( storeFile );
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			// Adding new credential:
//DOC			builder.createCredential( store );

			// Saving CredentialStore
			store.saveAllCredentialsToFile( storeFile );

			// CredentialStore-Section
			//////////////////////////////////////////


		} catch (Exception e) {
			System.out.println( "Error while setting up test environment." );
			e.printStackTrace();
			System.exit(1);
		}

	}
}
