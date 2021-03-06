/*
 * X509Credential.java
 *
 * Version 1.0: Initial implementation
 *     Author: Eric Knauss
 *     Date:   31/03/2004
 */
package org.peertrust.security.credentials.x509;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.peertrust.security.credentials.Credential;

/**
 * Implementation of the abstract credentials.Credential class. It uses a
 * X509Certificate for encoding information about the signer together with the
 * credential. Such Certificates can be created with the
 * crypto.CryptTools.createCert method. A X509CredentialStore should be used to
 * build a X509Credential of such a certificate and add it.
 * 
 * @author Eric Knauss
 */
public class X509Credential extends Credential implements Serializable {

    private X509Certificate cert;

    private String stringRepresentation;

    private static Logger log = Logger.getLogger(X509Credential.class);

    /**
     * Initializes this Credential with a X509Certificate. This Certificate must
     * contain a credential in String Representation within the extension
     * SubjectAlternativeName.OtherName.
     * 
     * @param arg
     *            a valid X509Certificate
     */
    public X509Credential(Object arg) throws Exception {
        loadCredential(arg);
    }

    public void loadCredential(Object arg) throws Exception {
        cert = (X509Certificate) arg;
        stringRepresentation = extractAttribute(cert);
    }

    static public boolean isX509Credential(X509Certificate cert) {
        boolean ret = false;

        try {
            if (extractAttribute(cert) != null)
                ret = true;
        } catch (Exception e) {
        }

        return ret;
    }

    /**
     * This method takes a X509Certificate and tries to extract the String
     * representation of a credential. The credential String is looked for in
     * the SubjectAlternativeName.OtherName of the certificate.
     * 
     * @param cert
     *            the Certificate that should be investigated.
     */
    private static String extractAttribute(X509Certificate cert)
            throws Exception {
        Collection col = cert.getSubjectAlternativeNames();
        if (col != null) {
            Iterator it = col.iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                Object extension = it.next();
                String credential = extension.toString();
                log.debug("Test if <<" + credential
                        + ">> is a Credential. (was " + extension + ")");
                log.debug("SubjectAlternativeName " + i + ": "
                        + extension.toString());
                // We need to extract the original String from the Certificate
                // (alternatively we would have to parse it from the
                // extension...)
                log.debug("Casting the extension to a Collection. ("
                        + extension.getClass().getName() + ")");
                credential = ((Collection) extension).toArray()[1].toString();

                //				GeneralNames ext = (GeneralNames) extension;
                /*
                 * classMessageWriter("isCredentialCert","Casting the extension
                 * to a DERPrintableString.",3); DERPrintableString print =
                 * (DERPrintableString)ext.getDERObject();
                 * classMessageWriter("isCredentialCert","Fetching the original
                 * String.",3); credential = print.getString();
                 */
                log.debug("Extracted String: " + credential);
                return credential;
            }
        } else {
            log.error("This Certificate has no 'SubjectAlternativeExtension.");
        }
        return null;
    }

    /**
     * Returns a String that represents the value of this Credential.
     * 
     * @return a Stringrepresentation of this Credential.
     */
    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public String getSignerName() {
        return cert.getIssuerDN().getName();
    }

    /**
     * Tries to read a X509Certificate from the Filesystem and extract a
     * credential from it. For this the certificate is expected to have a
     * credential attached in its String Representation as
     * "SubjectAlternativeName, OtherName". (See ASN.1 definition fo details.)
     * 
     * @param file
     *            The file that should be read.
     */
    public void importCredential(File file) throws Exception {
        FileInputStream is = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(is);
        loadCredential(ois.readObject());
    }

    /**
     * Writes the X509Certificate that contains this Credential to the given
     * file.
     * 
     * @param file
     *            The File that the Certificate should be written to.
     */
    public void exportCredential(File file) throws Exception {
        FileOutputStream os = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(cert);
    }

    /**
     * Returns the X509Certificate that holds this credential and its signature.
     * 
     * @return A X509Certificate
     * @see org.peertrust.security.credentials.Credential#getEncoded()
     */
    public Object getEncoded() {
        return cert;
    }
}