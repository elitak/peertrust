/*
 * Created on 14.01.2004
 */
package org.peertrust.security.credentials;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Manages storage and retrieval of Credentials for automated trust negotiation.
 * 
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public abstract class CredentialStore {

    protected Vector credentials;

    private static Logger log = Logger.getLogger(CredentialStore.class);

    public CredentialStore() {
        log.debug("Created");
        credentials = new Vector();
    }

    /**
     * Restores all Credential from a given file and returns them in a Vector,
     * 
     * @param file
     *            The file from which the credentials should be retrieved.
     * @return A Vector of credentials
     */
    public abstract void loadAllCredentialsFromFile(String file)
            throws Exception;

    /**
     * Adds a Credential to the store. It will be stored, the next time
     * <code>saveAllCredentials</code> is invoked.
     * 
     * @param arg
     *            The encoded Credential that should be stored. The class of
     *            this Object depends on the implementation of the class
     *            Credential.
     */
    public void addCredential(Credential credential) throws Exception {
        credentials.add(credential);
    }

    /**
     * Saves all credentials to the given file.
     * 
     * @param file
     *            The file the credentials should be stored into.
     */
    public abstract void saveAllCredentialsToFile(String file) throws Exception;

    /**
     * Gives a Vector of the stored credentials.
     * 
     * @return An Vector of credentials.
     */
    public Vector getCredentials() {
        return credentials;
    }

    public Credential getCredential(String stringRepresentation) {
        Credential result;
        for (int i = 0; i < credentials.size(); i++) {
            result = (Credential) credentials.get(i);
            if (result.getStringRepresentation().equalsIgnoreCase(
                    stringRepresentation))
                return result;
        }
        return null;
    }

    protected String printAllCredentials() {
        Enumeration e = credentials.elements();
        String message = "AllCredentials:\n";
        while (e.hasMoreElements()) {
            message += ("Credential: "
                    + ((Credential) e.nextElement()).getStringRepresentation() + "\n");
        }
        log.info(message);

        return message;
    }

    public String toString() {
        return printAllCredentials();
    }
}