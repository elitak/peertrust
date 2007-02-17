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
package org.policy.crypt.credentials;

import java.io.File;
import java.util.*;

import org.apache.log4j.Logger;
import org.peertrust.exception.CredentialException;

/**
 * <p>
 * Manages storage and retrieval of Credentials for automated trust
 * negotiation.
 * </p><p>
 * $Id: CredentialStore.java,v 1.1 2007/02/17 16:59:29 dolmedilla Exp $
 * <br/>
 * Date: 14-Jan-2004
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */

public abstract class CredentialStore {

	protected Vector credentials;

	private static Logger log = Logger.getLogger(CredentialStore.class);
	
	public CredentialStore()
	{
		log.debug( "Created");
		credentials = new Vector();
	}

	/**
	 * Restores all Credential from a given file
	 * 
	 * @param file The file from which the credentials should be retrieved.
	 */
	public abstract void loadAllCredentialsFromFile( File file ) throws CredentialException ;
	
	/**
	 * Restores all Credential from a given file
	 * 
	 * @param file The file from which the credentials should be retrieved.
	 */
	public void loadAllCredentialsFromFile(String file) throws CredentialException
	{
	    File f = new File(file) ;
	    
	    loadAllCredentialsFromFile(f) ; 
	}
	
	/**
	 * Adds a Credential to the store. It will be stored, the next time <code>saveAllCredentials</code>
	 * is invoked.
	 * 
	 * @param arg The encoded Credential that should be stored. The class of
	 *            this Object depends on the implementation of the class
	 *            Credential.
	 */
	public void addCredential( Credential credential ) throws CredentialException {
		credentials.add( credential );
	}

	
	/**
	 * Saves all credentials to the given file.
	 * 
	 * @param file The file the credentials should be stored into.
	 */
	public abstract void saveAllCredentialsToFile( File file ) throws CredentialException;
	
	/**
	 * Saves all credentials to the given file.
	 * 
	 * @param file The file the credentials should be stored into.
	 */
	public void saveAllCredentialsToFile( String file ) throws CredentialException
	{
	    File f = new File (file) ;
	    saveAllCredentialsToFile (f) ;
	}

	/**
	 * Gives a Vector of the stored credentials.
	 * 
	 * @return An Vector of credentials.
	 */
	public Vector getAllCredentials() {
		return credentials;
	}

	public Credential getCredential(String stringRepresentation) {
		Credential result;
		for ( int i = 0; i < credentials.size(); i++ ) {
			result = (Credential)credentials.get( i );
			if ( result.getStringRepresentation().equalsIgnoreCase(stringRepresentation) )
				return result ;
		}
		return null ;
	}

	protected String printAllCredentials() {
		Enumeration e = credentials.elements();
		String message = "AllCredentials:\n" ;
		while ( e.hasMoreElements() ) {
			message += ("Credential: " + ((Credential)e.nextElement()).getStringRepresentation() + "\n");
		}
		log.info(message) ;
		
		return message ;
	}
	
	public String toString()
	{
		return printAllCredentials() ;
	}
}
