package org.peertrust.security;

import java.security.KeyStore;
import java.security.cert.*;
import java.util.*;
import java.io.*;
import net.jxta.edutella.util.*;
import org.peertrust.*;
import org.peertrust.meta.*;
import org.peertrust.security.credentials.x509.*;

/**
 * This class represents a X.509 certificate chain. It offers two different approaches for
 * automatically building such a chain out of the entries in the keystore.
 * @author Sebastian Wittler
 */
public class CertificateChain implements Serializable {
	/** The certificate chain. */
	private X509Certificate x509certificate[];

	/**
	 * Constructor.
	 * Builds and stores the certificate chain.
	 * @param config The configuration-object of the local peer.
	 * @param identity The subject that the first certificate must have, null, if it
	 *	doesn't matter.
	 * @param firstissuer The issuer that the first certificate must have, null, if it
	 *	doesn't matter.
	 * @param authority The issuer that the last certificate of the chain must have, null,
	 *	if it doesn't matter.
	 */
	public CertificateChain(Configurator config,String identity,String firstissuer,
		String authority) {
		Vector vectorCerts=new Vector();
		try {
			//Initialize keystore
			KeyStore ks=KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(config.getValue(Vocabulary.
				BASE_FOLDER_TAG)+config.getValue(Vocabulary.
				KEYSTORE_FILE_TAG)),config.getValue(Vocabulary.
				STORE_PASSWORD_TAG).toCharArray());
			//Simple construction of chain if no authority is specified
			if(authority==null)
				searchSimpleChain(identity,firstissuer,ks,vectorCerts);
			//If authority is specified, the complex construction of the chain
			//must be made
			else
				searchComplexChain(identity,authority,ks,vectorCerts);
		}
		catch(Exception e) {
		}
		x509certificate=new X509Certificate[vectorCerts.size()];
		vectorCerts.toArray(x509certificate);
	}

	/**
	 * A simple concept for building certificate chains. If an authority wasn't
	 * specified in the constructor, this method is called.
	 * @param identity The subject that the first certificate must have, null, if it
	 *	doesn't matter.
	 * @param firstissuer The issuer that the first certificate must have, null, if it
	 *	doesn't matter.
	 * @param keystore The object that refers to the local keystore.
	 * @param vectorCerts An out-parameter that contains the resulting chain after this
	 *	method ends.
	 */
	private void searchSimpleChain(String identity,String firstissuer,KeyStore ks,
		Vector vectorCerts) throws
		Exception {
		String alias,goal=identity,last_goal="";
		X509Certificate x509cert=null;
		Certificate cert=null,certchain[]=null;
		//Build the chain
		while(true) {
			//Iterate through all entries of the keystore
			Enumeration enum=ks.aliases();
			while(enum.hasMoreElements()) {
				alias=(String)enum.nextElement();
				try {
					//If a suitable certificate chain already exists in
					//the keystore, take it and leave
					certchain=ks.getCertificateChain(alias);
					if((goal.equals(identity))&&(certchain!=null)&&
						(certchain.length>0)&&
						(certchain[0] instanceof X509Certificate)) {
						x509cert=(X509Certificate)certchain[0];
						if((getSubjectAlias(x509cert).equals(goal))
							&&(isCertificateValid(x509cert))&&
							((firstissuer==null)||(getIssuerAlias(
							x509cert).equals(firstissuer)))) {
							vectorCerts.clear();
							for(int i=0;i<certchain.length;i++)
								if(certchain[i] instanceof
									X509Certificate)
									vectorCerts.add(
										certchain[i]);
							return;
						}
					}
					cert=ks.getCertificate(alias);
					if((cert==null)||!(cert instanceof X509Certificate))
						continue;
					//If the chain is empty, specify the first certificate,
					//otherwise search for a certificate that has the
					//issuer of his predecessor has subject.
					x509cert=(X509Certificate)cert;
					if((getSubjectAlias(x509cert).equals(goal))&&
						(isCertificateValid(x509cert))&&
						(!goal.equals(last_goal))&&((!goal.equals(
						identity)||(firstissuer==null)||
						(getIssuerAlias(x509cert).equals(
						firstissuer))))) {
						vectorCerts.add(x509cert);
						last_goal=goal;
						goal=getIssuerAlias(x509cert);
						break;
					}
					else if(!enum.hasMoreElements())
						return;
				}
				catch(Exception e) {
				}
			}
		}
	}

	/**
	 * A complex concept for building certificate chains. If an authority is
	 * specified in the constructor, this method is called.
	 * @param identity The subject that the first certificate must have, null, if it
	 *	doesn't matter.
	 * @param authority The issuer that the last certificate of the chain must have.
	 * @param keystore The object that refers to the local keystore.
	 * @param vectorCerts An out-parameter that contains the resulting chain after this
	 *	method ends.
	 */
	private void searchComplexChain(String identity,String authority,KeyStore ks,
		Vector vectorCerts)
		throws Exception {
		String alias;
		X509Certificate x509cert=null;
		Certificate cert=null,certchain[]=null;
		Vector vectorTree=new Vector(),vectorTemp=new Vector();
		//All entries with a private key in the keystore are examined, as these
		//are potential starting points to build the chain
		Enumeration enum=ks.aliases();
		while(enum.hasMoreElements()) {
			alias=(String)enum.nextElement();
			if(!ks.isKeyEntry(alias))
				continue;
			//An entry with a private key may have a certificate chain that might
			//be the one we search for, this is checked here
			certchain=ks.getCertificateChain(alias);
			if((certchain!=null)&&(certchain.length>0)&&
				(certchain[0] instanceof X509Certificate)&&(isCertificateValid(
				x509cert=(X509Certificate)certchain[0]))&&((identity==null)||
				(getSubjectAlias(x509cert).equals(identity)))) {
				for(int i=0;i<certchain.length;i++) {
					if(!(certchain[i] instanceof X509Certificate))
						break;
					x509cert=(X509Certificate)certchain[i];
					if(!isCertificateValid(x509cert))
						break;
					if(getIssuerAlias(x509cert).equals(authority)) {
						vectorCerts.clear();
						for(int j=0;j<=i;j++)
							vectorCerts.add(certchain[j]);
						return;
					}
				}
			}
			//An entry with a private key may have a certificate that may be a
			//valid chain or can be a starting point for one
			cert=ks.getCertificate(alias);
			if((cert!=null)&&(cert instanceof X509Certificate)) {
				x509cert=(X509Certificate)cert;
				if((isCertificateValid(x509cert))&&((identity==null)||
					(getSubjectAlias(x509cert).equals(identity))))
						if(getIssuerAlias(x509cert).equals(
							authority)) {
							vectorCerts.add(x509cert);
							return;
						}
						else
							vectorTemp.add(x509cert);
			}
		}
		//If a valid certificate chain was found in the entries of the keystore that
		//corresponds to a private key, the method would have returned before.
		//Otherwise, certificates that are potential candidates for being starting
		//points of a chain are stored in the vectorTemp-Vector. When no such
		//candidates were found, the chain can't be built. If no valid certificate
		//with authority exists, the chain can't be built.
		if(vectorTemp.size()==0)
			return;
		vectorTree.add(vectorTemp);
		String temp_issuer;
		X509Certificate lastcert=null;
		//The certificate chain is built here
		while(true) {
			//When there are no more possibilities to search for a valid
			//certificate chain, quit
			if(vectorTree.size()==0)
				return;
			//Get current candidate list
			vectorTemp=(Vector)vectorTree.lastElement();
			//If candidate list is empty, delete it from vectorTree
			if(vectorTemp.size()==0) {
				vectorTree.remove(vectorTree.size()-1);
				vectorCerts.remove(vectorCerts.size()-1);
				continue;
			}
			x509cert=(X509Certificate)vectorTemp.elementAt(0);
			temp_issuer=getIssuerAlias(x509cert);
			vectorCerts.add(vectorTemp.remove(0));
			if(x509cert.equals(lastcert))
				continue;
			lastcert=x509cert;
			//If temp_issuer is authority, we have found a certificate chain
			if(temp_issuer.equals(authority))
				return;
			//Create a new list with all new candidates found in the keystore. The
			//candidates must be valid successors of the current certificate.
			vectorTemp=new Vector();
			enum=ks.aliases();
			while(enum.hasMoreElements()) {
				alias=(String)enum.nextElement();
				cert=ks.getCertificate(alias);
				if((cert!=null)&&(cert instanceof X509Certificate)&&
					(isCertificateValid(x509cert=(X509Certificate)cert))&&
					(getSubjectAlias(x509cert).equals(temp_issuer)))
					vectorTemp.add(x509cert);
			}
			vectorTree.add(vectorTemp);
		}
	}

	/**
	 * Returns the certificate chain as an array.
	 * @return X509Certificate[] Chain as an Array of certificates.
	 */
	public X509Certificate[] getCertificates() {
		return x509certificate;
	}

	/**
	 * Checks if a given certificate is still valid or expired.
	 * @param cert The certificate that should be checked.
	 * @return boolean true, if certificate is still valid, false, if it's expired.
	 */
	private boolean isCertificateValid(X509Certificate cert) {
		try {
			cert.checkValidity();
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the subject-alias of a certificate.
	 * @param cert The certificate which alias should be returned.
	 * @return String the subject-alias of the certificate.
	 */
	public static String getSubjectAlias(X509Certificate cert) {
		String subject=cert.getSubjectDN().getName();
		int a=subject.indexOf("CN="),b=subject.indexOf(",",a);
		subject=(b==-1) ? subject.substring(a+3) : subject.substring(a+3,b);
		return subject;
	}

	/**
	 * Returns the issuer-alias of a certificate.
	 * @param cert The certificate which alias should be returned.
	 * @return String the issuer-alias of the certificate.
	 */
	public static String getIssuerAlias(X509Certificate cert) {
		String issuer=cert.getIssuerDN().getName();
		int a=issuer.indexOf("CN="),b=issuer.indexOf(",",a);
		issuer=(b==-1) ? issuer.substring(a+3) : issuer.substring(a+3,b);
		return issuer;
	}
}