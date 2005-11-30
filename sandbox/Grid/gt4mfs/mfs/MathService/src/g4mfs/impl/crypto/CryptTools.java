/*
 * CryptTools.java
 *
 * Version 1.0: Initial implementation
 *     Author: Eric Knauss
 *     Date:   31/03/2004
 */
package g4mfs.impl.crypto;

import java.security.*;
import java.security.cert.*;
import org.bouncycastle.asn1.*;
import org.bouncycastle.jce.*;
import org.bouncycastle.asn1.x509.*;
import java.util.*;
import java.io.*;

/**
 * This class contains usefull cryptographical methods. It also 
 * handles verbosity of most classes. By setting the final static
 * field globalDebugLevel to '0' output is reduced to a minimum.
 * @author Eric Knauss
 */
public class CryptTools {

	private static final int debugLevel = 10;

	private static final int globalDebugLevel = 0;

	private static MessageDigest getMessageDigest() {
		try {
			return MessageDigest.getInstance("MD5");
		}
		catch ( NoSuchAlgorithmException nsae ) {
			messageWriter("CryptTools.getMessageDigest","Algorithm not found.",0);
			nsae.printStackTrace();
		}
		return null;
	}

	/**
	 * Prints the available information about the providers installed on this system.
	 */
	public static void printProviders() {
		Provider p[] = Security.getProviders();
		for ( int i = 0; i < p.length; i++ ) {
			messageWriter("CryptTools.printProviders","Provider "+i+": "+p[i].getInfo(),0);
		}
	}

	/**
	 * Reads the file and computes the MessageDigest of it.
	 * @param file the file to be read
	 * @return an array of byte with the MessageDigest
	 */
	public static byte[] computeMessageDigestOf(File file) throws IOException, FileNotFoundException{
		MessageDigest md = getMessageDigest();
		FileInputStream is = new FileInputStream( file );
		DigestInputStream mdis = new DigestInputStream( is, md );
		byte buf[] = new byte[2048];
		while (mdis.read(buf) > 0);
		byte[] result = md.digest();
		is.close();
		return result;
	}

	/** 
	 * Computes a messageDigest for a given String.
	 * @param arg the String to that the hash function should be applied.
	 */
	public static byte[] hashString(String arg) {
		MessageDigest md = getMessageDigest();
		md.update(arg.getBytes());
		return md.digest();
	}


	/**
	 * Generates a new key pair with DSA as the algorithm and a keyLength of 1024.
	 * 
	 * @return a KeyPair that consists of a private and a corresponding public key
	 */
	public static KeyPair makeKeyPair() {
		try {
			return makeKeyPair("DSA",1024);
		}
		catch (NoSuchAlgorithmException nsae) {
			messageWriter("CryptTools.makeKeyPair","key generation with DSA not possible",0);
		}
		return null;
	}

	/**
	 * Generates a new key pair.
	 * 
	 * @param algorithm the algorithm that should be used.
	 * @param keyLength the amount of bits that should be used. (A multiple of 64)  
	 * @return a KeyPair that consists of a private and a corresponding public key
	 * @exception NoSuchAlgorithmException if no implementation for this algorithm is found
	 */
	public static KeyPair makeKeyPair(String algorithm, int keyLength) throws NoSuchAlgorithmException {
		KeyPairGenerator dsaKeyGen;
		KeyPair keyPair = null;
		dsaKeyGen = KeyPairGenerator.getInstance(algorithm);
		dsaKeyGen.initialize(keyLength);
		keyPair = dsaKeyGen.genKeyPair();
		return keyPair;
	}

	/**
	 * Adds the BouncyCastle JCE Provider to java.security.Security.
	 */
	public static void installBouncyCastle() {
		messageWriter("CryptTools.installBouncyCastle", "Adding Bouncing Castle JCE Provider", 1);
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	/**
	 * A method for signing a file. This was only written for testing
	 * purposes and signs a file called test. 
	 */
	public static void signData( PrivateKey pk ) {
		Signature dsa = null;
		try {
			dsa = Signature.getInstance("DSA");
			dsa.initSign(pk);
			FileInputStream is = new FileInputStream("Test");
			byte buffer[] = new byte[1024];
			int r;
			while((r = is.read(buffer)) != -1) {
				dsa.update(buffer, 0, r);
			}
			is.close();
			byte[] signature = dsa.sign();
			FileOutputStream os = new FileOutputStream("Test.sign");
			os.write(signature);
			os.close();
		}
		catch (NoSuchAlgorithmException nsae) {
			messageWriter("CryptTools.signData","signiture with DSA not possible",0);
		}
		catch (InvalidKeyException ike) {
			messageWriter("CryptTools.signData","invalid key.",0);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (SignatureException se) {
			se.printStackTrace();
		}
	}	

	/**
	 * This method takes a X509Certificate and tries to extract the
	 * String representation of a credential. The credential String
	 * is looked for in the SubjectAlternativeName.OtherName of the
	 * certificate.
	 * @param cert the Certificate that should be investigated.
	 */
	public static String extractCredential( X509Certificate cert ) throws Exception {
		Collection col = cert.getSubjectAlternativeNames();
		if (col != null) {
			Iterator it = col.iterator();
			int i = 0;
			while (it.hasNext()) {
				i++;
				Object extension = it.next();
				String credential = extension.toString();
				classMessageWriter("isCredentialCert","Test if <<" + credential + ">> is a Credential. (was "+extension+")",3);
				classMessageWriter("isCredentialCert","SubjectAlternativeName "+i+": "+extension.toString(),3);
				// We need to extract the original String from the Certificate (alternatively we would have to parse it from the extension...)
				classMessageWriter("isCredentialCert","Casting the extension to a Collection. ("+extension.getClass().getName()+")",3);
				credential = ((Collection)extension).toArray()[1].toString();

				//				GeneralNames ext = (GeneralNames) extension;
				/*classMessageWriter("isCredentialCert","Casting the extension to a DERPrintableString.",3);
				  DERPrintableString print = (DERPrintableString)ext.getDERObject();
				  classMessageWriter("isCredentialCert","Fetching the original String.",3);
				  credential = print.getString();*/
				classMessageWriter("isCredentialCert","Extracted String: "+credential,3);
				return credential;
			}
		}
		else {
			classMessageWriter("isCredentialCert","This Certificate has no 'SubjectAlternativeExtension.",1);
		}
		return null;
	}

	/**
	 * Creates a certificate that contains a credential. The credential is
	 * stored in its String representation into the SubjectAlternativeName
	 * in a field called OtherName. This method uses the Bouncy Castle
	 * security provider that has to be added first (via the installBouncyCastle)
	 * method.
	 * @param pubKey the public key of the subject of the credential. This
	 * is needed to create a valid X509Certificate and might be usefull to
	 * improve security, when a public key infrastructure is used in this
	 * prototype environment.
	 * @param caPrivKey the key that is used to sign the credential 
	 */
	public static X509Certificate createCert( PublicKey pubKey, PrivateKey caPrivKey ) throws Exception {
		String  issuer = "CN=LearningLab, L=Hannover, C=DE, O=University of Hannover, OU=KBS";
		String  subject = "CN=Alice, L=Hannover, C=DE, O=University of Hannover, OU=KBS";
		String credential = "member(alice)@visa";
		return createCert( subject, credential, pubKey, issuer, caPrivKey );
	}


	/**
	 * Generate a Certificate that holds a Credential as a critical Extension in it.
	 * @param pubKey the public key of the subject of the credential. This
	 * is needed to create a valid X509Certificate and might be usefull to
	 * improve security, when a public key infrastructure is used in this
	 * prototype environment.
	 * @param subject the distinguished name of the subject of this 
	 * credential. Important: There exists a certain format for distinguished 
	 * names. For example "CN=alice" is a valid DN.
	 * @param credential the String Representation of the credential
	 * @param issuer the name of the signer of the credential. This should
	 * be the person the caPrivKey belongs to.
	 * @param caPrivKey the key that is used to sign the credential
	 * @return A X.509Certificate 
	 */
	public static X509Certificate createCert( String subject, String credential, PublicKey pubKey, String issuer, PrivateKey caPrivKey ) throws Exception {

		X509V3CertificateGenerator  v3CertGen = new X509V3CertificateGenerator();	

		// create the certificate - version 3 (only v3 allows usage of extensions)
		v3CertGen.reset();
		// TODO: Eindeutige Serialno
		v3CertGen.setSerialNumber(java.math.BigInteger.valueOf(3));
		v3CertGen.setIssuerDN(new X509Principal(issuer));
		v3CertGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30));
		v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)));
		v3CertGen.setSubjectDN(new X509Principal(subject));
		v3CertGen.setPublicKey(pubKey);
		v3CertGen.setSignatureAlgorithm("SHA1WithRSAEncryption");

		// add the extensions
		// - the credential as an extension
		//   - try to create a "SubjectAlternativeName" Extension in the othername field
		//     - create an OtherName (there is no OtherName class, so I have to improvise)
		int tag = 2; // Tag-Class 'Universal', BIT STRING: 2(works fine), OCTET STRING: 3
		DERObject derO = new DERPrintableString( credential );


		//     - create a GeneralName
		GeneralName gn = new GeneralName(derO, tag);

		//     - create a GeneralNames set of it:
		DERSequence ders = new DERSequence(gn);
		GeneralNames gns = new GeneralNames(ders);


		v3CertGen.addExtension(X509Extensions.SubjectAlternativeName, true, gns);

		// generate the cert
		X509Certificate cert = v3CertGen.generateX509Certificate(caPrivKey);

		// Testing:
		cert.checkValidity(new Date());

		return cert;
	}

	private static final void classMessageWriter(String method, String message, int prio) {
		if (debugLevel > prio) {
			messageWriter("crypto.CryptTools." + method, message, prio);
		}
	}

	/**
	 * A method for writing debug information. Classes from credential and
	 * crypto packages use this. 
	 */
	public static void messageWriter(String from, String message, int priority) {
		if (globalDebugLevel >= priority) {
			System.out.println("[" + from + "] - " + message);
		}
	}

}

