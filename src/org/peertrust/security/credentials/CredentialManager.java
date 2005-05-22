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
package org.peertrust.security.credentials;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;

import java.security.cert.X509Certificate;

import net.jxta.edutella.util.Configurable;
import net.jxta.edutella.util.Option;

import org.apache.log4j.Logger;
import org.peertrust.security.credentials.gui.Editor;


/**
 * <p>
 * 
 * </p><p>
 * $Id: CredentialManager.java,v 1.3 2005/05/22 17:56:43 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:43 $
 * by $Author: dolmedilla $
 * </p>
 * @author $Author: dolmedilla $
 */
public class CredentialManager implements Configurable {

	private String baseFolder ;
	private String adminKeystoreFile ;
	private String adminKeyPassword ;
	private String adminStorePassword ;
	private String keystoreFile ;
	private String keyPassword ;
	private String storePassword ;
	
	private KeyStore adminKeystore ;
	private KeyStore keystore ;
	
	private static Logger log = Logger.getLogger(CredentialManager.class);
	
	public void init() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException
	{
		CryptTools.installBouncyCastle() ;

		adminKeystore = KeyStore.getInstance( "JKS" );
		adminKeystore.load(new FileInputStream(baseFolder + adminKeystoreFile), adminStorePassword.toCharArray());

		keystore = KeyStore.getInstance( "JKS" );
		if (keystoreFile == null)
			keystore.load(null, storePassword.toCharArray());
		else
			keystore.load(new FileInputStream(baseFolder + keystoreFile), storePassword.toCharArray());
	}
	
	public void saveKeystore (String file) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	{
		OutputStream os = new FileOutputStream( file );
		keystore.store(os, storePassword.toCharArray());
	}
	

	private PrivateKey getCAKey(String alias, String keyPassword) throws Exception {
		return (PrivateKey) adminKeystore.getKey( alias, adminKeyPassword.toCharArray() );
	}
	
	public void addCredential (String alias, String text, String subject, String issuer) throws Exception
	{
		log.debug("Adding credential " + text + " to alias") ;
		PrivateKey caKey = getCAKey(alias, keyPassword);
		// Creation of a new key pair
		PublicKey pubKey = CryptTools.makeKeyPair( "RSA", 512 ).getPublic();
		
		X509Certificate cert = CryptTools.createCert(  subject, text, pubKey, issuer, caKey );
		keystore.setCertificateEntry( cert.getSubjectDN().getName(), cert );
	}
	
	/**
	 * @see net.jxta.edutella.util.Configurable#getOptions()
	 */
	public Option[] getOptions()
	{
		Option baseOpt =	new Option(
			'b',
			"admin.baseFolder",
			"Base Folder",
			"Folder with local files",
			true) ;

		Option adminKeystoreOpt =	new Option(
			'f',
			"admin.adminKeystoreFile",
			"Admin Keystore File",
			"Admin Keystore File",
			true) ;
	
		Option adminKeyPwdOpt =	new Option(
			's',
			"admin.adminKeyPassword",
			"Admin Keystore Password",
			"Admin Keystore Password",
			true) ;
		adminKeyPwdOpt.setIsPassword(true) ;
			
		Option adminStorePwdOpt =	new Option(
			'c',
			"admin.adminStorePassword",
			"Admin Keystore Password",
			"Admin Keystore Password",
			false) ;
		adminStorePwdOpt.setIsPassword(true) ;

		Option keystoreOpt =	new Option(
			't',
			"admin.keystoreFile",
			"Keystore File",
			"Keystore File",
			true) ;

		Option keypwdOpt =	new Option(
			'k',
			"admin.keyPassword",
			"Key Password",
			"Key Password",
			true) ;
		keypwdOpt.setIsPassword(true) ;			
		
		Option storepwdOpt =	new Option(
			'n',
			"admin.storePassword",
			"Keystore Password",
			"Keystore Password",
			true) ;
		storepwdOpt.setIsPassword(true) ;

		return new Option[] {baseOpt, adminKeystoreOpt, adminKeyPwdOpt, adminStorePwdOpt, keystoreOpt, keypwdOpt, storepwdOpt};
	}

	/**
	 * @see net.jxta.edutella.util.Configurable#getPropertyPrefix()
	 */
	public String getPropertyPrefix() {
		return "admin";
	}
	/**
	 * @return Returns the baseFolder.
	 */
	public String getBaseFolder() {
		return baseFolder;
	}
	/**
	 * @param baseFolder The baseFolder to set.
	 */
	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}
	/**
	 * @return Returns the keyPassword.
	 */
	public String getKeyPassword() {
		return keyPassword;
	}
	/**
	 * @param keyPassword The keyPassword to set.
	 */
	public void setKeyPassword(String keyPassword) {
		this.keyPassword = keyPassword;
	}
	/**
	 * @return Returns the keystoreFile.
	 */
	public String getKeystoreFile() {
		return keystoreFile;
	}
	/**
	 * @param keystoreFile The keystoreFile to set.
	 */
	public void setKeystoreFile(String keystoreFile) {
		this.keystoreFile = keystoreFile;
	}
	/**
	 * @return Returns the storePassword.
	 */
	public String getStorePassword() {
		return storePassword;
	}
	/**
	 * @param storePassword The storePassword to set.
	 */
	public void setStorePassword(String storePassword) {
		this.storePassword = storePassword;
	}

	public static void main(String[] args) {
		Editor editor = new Editor() ;
	}
	
//	public static void main(String[] args) {
//		String PEERTRUST_ALIAS = "peertrust" ;
//		String PEERTRUST_AUTHORITY = "CN=Universitaet Hannover, OU=Learning Lab Niedersachsen, O=Universitaet Hannover, L=Hannover, ST=Niedersachsen, C=DE" ;
//		try {
//			Configurator cf = new Configurator("admin.trust.properties", args);
//			cf.setAppInfo("Atomated Trust Negotiation Peer");
//
//			// register objects which need configuration
//			CredentialManager manager = new CredentialManager();
//			cf.register(manager) ;
//			
//			// configure objects
//			cf.finishConfig();
//
//			manager.addCredential(PEERTRUST_ALIAS,
//					"signed(rule(drivingLicense(alice) @ caState, [], []), caState, signature(caState))",
//					"CN=caState",
//					PEERTRUST_AUTHORITY) ;
//			
//			manager.addCredential(PEERTRUST_ALIAS,
//					"signed(rule(policeOfficer(alice) @ caStatePolice, [], []), caStatePolice, signature(caStatePolice))",
//					"CN=caStatePolice",
//					PEERTRUST_AUTHORITY) ;
//			
//			manager.saveKeystore("/home/olmedilla/workspace/peertrust/newKeystore") ;
//			
//		} catch (Exception e) {
//			e.printStackTrace() ;
//		}
//	
//	}
	/**
	 * @return Returns the adminKeystoreFile.
	 */
	public String getAdminKeystoreFile() {
		return adminKeystoreFile;
	}
	/**
	 * @param adminKeystoreFile The adminKeystoreFile to set.
	 */
	public void setAdminKeystoreFile(String adminKeystoreFile) {
		this.adminKeystoreFile = adminKeystoreFile;
	}
	/**
	 * @return Returns the adminKeyPassword.
	 */
	public String getAdminKeyPassword() {
		return adminKeyPassword;
	}
	/**
	 * @param adminKeyPassword The adminKeyPassword to set.
	 */
	public void setAdminKeyPassword(String adminKeyPassword) {
		this.adminKeyPassword = adminKeyPassword;
	}
	/**
	 * @return Returns the adminKeystore.
	 */
	public KeyStore getAdminKeystore() {
		return adminKeystore;
	}
	/**
	 * @param adminKeystore The adminKeystore to set.
	 */
	public void setAdminKeystore(KeyStore adminKeystore) {
		this.adminKeystore = adminKeystore;
	}
	/**
	 * @return Returns the adminStorePassword.
	 */
	public String getAdminStorePassword() {
		return adminStorePassword;
	}
	/**
	 * @param adminStorePassword The adminStorePassword to set.
	 */
	public void setAdminStorePassword(String adminStorePassword) {
		this.adminStorePassword = adminStorePassword;
	}
}
