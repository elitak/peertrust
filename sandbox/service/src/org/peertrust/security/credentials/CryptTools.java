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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.X509V3CertificateGenerator;

/**
 * $Id: CryptTools.java,v 1.1 2004/08/07 12:51:54 magik Exp $
 * 
 * @author Eric Knauss
 * @date 05-Dec-2003 Last changed $Date: 2004/08/07 12:51:54 $ by $Author:
 *       dolmedilla $
 * @description
 */
public class CryptTools {

    private static Logger log = Logger.getLogger(CryptTools.class);

    private static MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            log.error("Algorithm not found");
            nsae.printStackTrace();
        }
        return null;
    }

    /**
     * Prints the available information about the providers installed on this
     * system.
     */
    public static void printProviders() {
        Provider p[] = Security.getProviders();
        for (int i = 0; i < p.length; i++) {
            log.debug("Provider " + i + ": " + p[i].getInfo());
        }
    }

    /**
     * Reads the file and computes the MessageDigest of it.
     * 
     * @param file
     *            the file to be read
     * @return an array of byte with the MessageDigest
     */
    public static byte[] computeMessageDigestOf(File file) throws IOException,
            FileNotFoundException {
        MessageDigest md = getMessageDigest();
        FileInputStream is = new FileInputStream(file);
        DigestInputStream mdis = new DigestInputStream(is, md);
        byte buf[] = new byte[2048];
        while (mdis.read(buf) > 0)
            ;
        byte[] result = md.digest();
        is.close();
        return result;
    }

    /**
     * Computes a messageDigest for a given String.
     * 
     * @param arg
     *            the String to that the hash function should be applied.
     */
    public static byte[] hashString(String arg) {
        MessageDigest md = getMessageDigest();
        md.update(arg.getBytes());
        return md.digest();
    }

    /**
     * Generates a new key pair with DSA as the algorithm and a keyLength of
     * 1024.
     * 
     * @return a KeyPair that consists of a private and a corresponding public
     *         key
     */
    public static KeyPair makeKeyPair() {
        try {
            return makeKeyPair("DSA", 1024);
        } catch (NoSuchAlgorithmException nsae) {
            log.error("key generation with DSA not possible");
        }
        return null;
    }

    /**
     * Generates a new key pair.
     * 
     * @param algorithm
     *            the algorithm that should be used.
     * @param keyLength
     *            the amount of bits that should be used. (A multiple of 64)
     * @return a KeyPair that consists of a private and a corresponding public
     *         key
     * @exception NoSuchAlgorithmException
     *                if no implementation for this algorithm is found
     */
    public static KeyPair makeKeyPair(String algorithm, int keyLength)
            throws NoSuchAlgorithmException {
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
        log.debug("Adding Bouncing Castle JCE Provider");
        java.security.Security
                .addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * A method for signing a file. This was only written for testing purposes
     * and signs a file called test.
     */
    public static void signData(PrivateKey pk) {
        Signature dsa = null;
        try {
            dsa = Signature.getInstance("DSA");
            dsa.initSign(pk);
            FileInputStream is = new FileInputStream("Test");
            byte buffer[] = new byte[1024];
            int r;
            while ((r = is.read(buffer)) != -1) {
                dsa.update(buffer, 0, r);
            }
            is.close();
            byte[] signature = dsa.sign();
            FileOutputStream os = new FileOutputStream("Test.sign");
            os.write(signature);
            os.close();
        } catch (NoSuchAlgorithmException nsae) {
            log.error("signature with DSA not possible");
        } catch (InvalidKeyException ike) {
            log.error("invalid key");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SignatureException se) {
            se.printStackTrace();
        }
    }

    /**
     * Creates a certificate that contains a credential. The credential is
     * stored in its String representation into the SubjectAlternativeName in a
     * field called OtherName. This method uses the Bouncy Castle security
     * provider that has to be added first (via the installBouncyCastle) method.
     * 
     * @param pubKey
     *            the public key of the subject of the credential. This is
     *            needed to create a valid X509Certificate and might be usefull
     *            to improve security, when a public key infrastructure is used
     *            in this prototype environment.
     * @param caPrivKey
     *            the key that is used to sign the credential
     */
    public static X509Certificate createCert(PublicKey pubKey,
            PrivateKey caPrivKey) throws Exception {
        String issuer = "CN=LearningLab, L=Hannover, C=DE, O=University of Hannover, OU=KBS";
        String subject = "CN=Alice, L=Hannover, C=DE, O=University of Hannover, OU=KBS";
        String credential = "member(alice)@visa";
        return createCert(subject, credential, pubKey, issuer, caPrivKey);
    }

    /**
     * Generate a Certificate that holds a Credential as a critical Extension in
     * it.
     * 
     * @param pubKey
     *            the public key of the subject of the credential. This is
     *            needed to create a valid X509Certificate and might be usefull
     *            to improve security, when a public key infrastructure is used
     *            in this prototype environment.
     * @param subject
     *            the distinguished name of the subject of this credential.
     *            Important: There exists a certain format for distinguished
     *            names. For example "CN=alice" is a valid DN.
     * @param credential
     *            the String Representation of the credential
     * @param issuer
     *            the name of the signer of the credential. This should be the
     *            person the caPrivKey belongs to.
     * @param caPrivKey
     *            the key that is used to sign the credential
     * @return A X.509Certificate
     */
    public static X509Certificate createCert(String subject, String credential,
            PublicKey pubKey, String issuer, PrivateKey caPrivKey)
            throws Exception {

        X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();

        // create the certificate - version 3 (only v3 allows usage of
        // extensions)
        v3CertGen.reset();
        // TODO: Eindeutige Serialno
        v3CertGen.setSerialNumber(java.math.BigInteger.valueOf(3));
        v3CertGen.setIssuerDN(new X509Principal(issuer));
        v3CertGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60
                * 60 * 24 * 30));
        v3CertGen.setNotAfter(new Date(System.currentTimeMillis()
                + (1000L * 60 * 60 * 24 * 30)));
        v3CertGen.setSubjectDN(new X509Principal(subject));
        v3CertGen.setPublicKey(pubKey);
        v3CertGen.setSignatureAlgorithm("SHA1WithRSAEncryption");

        // add the extensions
        // - the credential as an extension
        //   - try to create a "SubjectAlternativeName" Extension in the othername
        // field
        //     - create an OtherName (there is no OtherName class, so I have to
        // improvise)
        int tag = 2; // Tag-Class 'Universal', BIT STRING: 2(works fine), OCTET
                     // STRING: 3
        DERObject derO = new DERPrintableString(credential);

        //     - create a GeneralName
        GeneralName gn = new GeneralName(derO, tag);

        //     - create a GeneralNames set of it:
        DERSequence ders = new DERSequence(gn);
        GeneralNames gns = new GeneralNames(ders);

        v3CertGen
                .addExtension(X509Extensions.SubjectAlternativeName, true, gns);

        // generate the cert
        X509Certificate cert = v3CertGen.generateX509Certificate(caPrivKey);

        // Testing:
        cert.checkValidity(new Date());

        return cert;
    }

    static public String getCertificateName(
            javax.security.cert.Certificate [] chain) {
        javax.security.cert.X509Certificate [] certs = (javax.security.cert.X509Certificate []) chain;
        String name;

        //		for (int i = 0 ; i < certs.length ; i++)
        //			{
        name = certs[0].getSubjectDN().getName();
        int index = name.indexOf("CN=") + 3;
        int index2 = name.indexOf(",", index);
        String dn = name.substring(index, index2);
        System.err.println("Cert to " + dn + " valid till "
                + certs[0].getNotAfter());
        //			}
        return dn;
    }
}

