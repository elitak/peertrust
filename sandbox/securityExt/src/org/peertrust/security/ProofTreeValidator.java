package org.peertrust.security;

import org.peertrust.*;
import org.peertrust.inference.*;
import org.peertrust.meta.*;
import org.peertrust.net.*;
import net.jxta.edutella.util.*;
import org.peertrust.security.credentials.*;
import org.peertrust.security.credentials.x509.*;
import java.io.*;
import java.util.*;
import java.security.cert.*;
import java.security.*;

/**
 * This class is responsible for verifying the proof tree of a query or answer. A party can
 * detect so, if the answer is really valid.
 * @author Sebastian Wittler
 */
public final class ProofTreeValidator {

	/**
	 * Checks, if a Tree is correct by verifying its proof tree.
	 * @param tree The tree whose proof tree should be verified.
	 * @param config The configuration-object of the local peer.
	 * @return boolean If the Tree has a valid proof tree.
	 */
	public static boolean isProofTreeOk(Tree tree,Configurator config) {
		ProofRule proofrule;
		try {
			//Create and initialize the inference engine
			MinervaProlog engine=new MinervaProlog(config);
			engine.loadFile("proof");
			//Get the proof tree of the Tree-object and iterate through its entries
			Vector vector=tree.getProofRuleVector();
			for(int i=0;i<vector.size();i++) {
				proofrule=(ProofRule)vector.elementAt(i);
				//If current rule is a signed one and is not correct, quit
				if((proofrule instanceof SignedProofRule)&&
					(!checkSignedProofRule((SignedProofRule)proofrule)))
					return false;
				//If current rule is an authenticatesTo one and is not correct,
				//quit
				else if((proofrule instanceof AuthenticatesToProofRule)&&
					(!checkAuthenticatesToProofRule(
					(AuthenticatesToProofRule)proofrule,config)))
					return false;
				//Add rule to knowledge base of the inference engine
				engine.execute("asserta("+proofrule.getRule()+")");
			}
			return engine.execute("proof("+tree.getLastExpandedGoal()+","+
				tree.getRequester().getAlias()+")");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Checks if a signed rule and the corresponding credential are correct in the
	 * proof tree.
	 * @param signedproofrule The signed rule that should be checked.
	 * @return boolean If the signed rule in the proof tree is correct or not.
	 */
	private static boolean checkSignedProofRule(SignedProofRule signedproofrule) {
		//Get the credential, if it doesn't exists, quit
		Credential cred=signedproofrule.getCredential();
		if(cred==null)
			return false;
		//Check if the certificate that contains the credential is valid
		X509Certificate cert;
		try {
			cert=(X509Certificate)cred.getEncoded();
			cert.checkValidity();
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		String subject=CertificateChain.getSubjectAlias(cert),
			issuer=CertificateChain.getIssuerAlias(cert);
		String rule=signedproofrule.getRule();
		rule.replaceAll("\\(r\\(","(rule(");
		//Check if rule is really the one specified in the credential
		if(!cred.getStringRepresentation().equals(rule)) {
			int a=rule.lastIndexOf("@");
			if((a==-1)||(cred.getStringRepresentation().equals(
				rule.substring(0,a))))
				return false;
		}
		//Check if the rule is really issued by the issuer of the certificate
		if(cred.getStringRepresentation().indexOf("@"+subject+",")==-1)
			return false;
		//Get the certificate chain, if it doesn't exist, quit
		CertificateChain certchain=signedproofrule.getCertificateChain();
		if(certchain==null)
			return false;
		X509Certificate certs[]=certchain.getCertificates();
		//The certificate which includes the credential must either be verified
		//successfull with it's own public key (if it's a self-signed one) or with the
		//public key of the next certificate in the chain
		try {
			cert.verify(certs[0].getPublicKey());
		}
		catch(Exception e) {
			try {
				cert.verify(certs[1].getPublicKey());
			}
			catch(Exception exc) {
				return false;
			}
		}
		//Verify alll the certificates in the chain, if they aren't expired and can be
		//verified with the public key of the successor
		try {
			for(int i=0;i<certs.length;i++) {
				certs[i].checkValidity();
				if(i<certs.length-1)
					certs[i].verify(certs[i+1].getPublicKey());
			}
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the answer to a authenticatesTo-predicate and the corresponding
	 * certificate chain are correct in the proof tree.
	 * @param authrule The answer to the authenticatesTo-predicate that
	 *	should be checked.
	 * @param config The configuration-object of the local peer.
	 * @return boolean If the answer to the predicate is correct or not.
	 */
	private static boolean checkAuthenticatesToProofRule(AuthenticatesToProofRule
		authrule,Configurator config) {
		//Get the certificate chain and quit if it is empty
		X509Certificate certs[]=authrule.getCertificateChain().getCertificates();
		if(certs.length==0)
			return false;
		//Parse identity and authority out of the parameters of the rule
		String str=authrule.getRule();
		int a=str.indexOf("(");
		a=str.indexOf("(",a+1);
		int b=str.indexOf(",",a);
		String identity=(a!=-1)&&(b!=-1)&&(b-a-1>0) ? str.substring(a+1,b) : "";
		a=str.indexOf(",",b+1);
		b=str.indexOf(")",a);
		String authority=(b!=-1)&&(b-a>0) ? str.substring(a+1,b) : "";
		//If the first certificate's subject isn't identity and the last one's issuer
		//isn't authority, quit
		if((!CertificateChain.getSubjectAlias(certs[0]).equals(identity))||
			(!CertificateChain.getIssuerAlias(certs[certs.length-1]).equals(
			authority)))
			return false;
		try {
			for(int i=0;i<certs.length;i++) {
				//Check if certificate isn't expired
				certs[i].checkValidity();
				//Each certificate must be verified with the public key of
				//its successor
				if(i<certs.length-1)
					certs[i].verify(certs[i+1].getPublicKey());
				//The last certificate chain must be verified with the public
				//key of the issuer which must be in the keystore
				else
					return checkLastCertificate(certs[i],config);
			}
		}
		catch(Exception e) {
		}
		return false;
	}

	/**
	 * Checks if the suitable public key for the issuer of a given certificate can be found
	 * in the keystore and the certificate can be verified with it.
	 * @param cert The certificate that should be verified.
	 * @param config The configuration-object of the local peer.
	 * @return boolean If the public key can be found and the certificate can be verfied
	 *	with it.
	 */
	private static boolean checkLastCertificate(X509Certificate cert,Configurator config) {
		try {
			//Prepare for examiming the keystore
			KeyStore ks=KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(config.getValue(
				Vocabulary.BASE_FOLDER_TAG)+config.getValue(
				Vocabulary.KEYSTORE_FILE_TAG)),config.getValue(
				Vocabulary.STORE_PASSWORD_TAG).toCharArray());
			Enumeration enum=ks.aliases();
			PublicKey publickey=null;
			Vector vectorPublicKeys=new Vector();
			String alias,subject=CertificateChain.getIssuerAlias(cert);
			X509Certificate cert2;
			//Iterate through all the entries in the keystore
			while(enum.hasMoreElements()) {
				alias=(String)enum.nextElement();
				try {
					if((ks.getCertificate(alias)==null)||
						!(ks.getCertificate(alias) instanceof
						X509Certificate))
						continue;
					cert2=(X509Certificate)ks.getCertificate(alias);
					//If certificate is found who has the given
					//certificates's subject as issuer, add the
					//corresponding public key to a Vector
					if((CertificateChain.getSubjectAlias(cert2).equals(
						subject))&&(cert2.getPublicKey()!=null))
						vectorPublicKeys.addElement(cert2.
							getPublicKey());
				}
				catch(Exception e) {
				}
			}
			//When having collected all the public keys that might be suitable,
			//try to verify the given certificate with all of them.
			for(int i=0;i<vectorPublicKeys.size();i++) {
				try {
					cert.verify((PublicKey)vectorPublicKeys.elementAt(i));
				}
				catch(Exception e) {
					continue;
				}
				return true;
			}
		}
		catch(Exception e) {
		}
		return false;
	}
}