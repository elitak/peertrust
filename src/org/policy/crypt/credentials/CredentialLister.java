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

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.peertrust.exception.CredentialException;
import org.peertrust.security.credentials.gui.DummyCredential;
import org.peertrust.security.credentials.x509.*;

/**
 * <p>
 * Lists all credentials a given CredentialStore contains. This class encapsulates the CredentialStore.
 * </p><p>
 * $Id: CredentialLister.java,v 1.1 2007/02/17 16:59:29 dolmedilla Exp $
 * <br/>
 * Date: 14-Jan-2004
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public class CredentialLister extends JPanel {

	private static Logger log = Logger.getLogger(CredentialLister.class);
	
	private static final int classPriority = 3;
	private CredentialStore credStore = new X509CredentialStore();
	private ListSelectionListener listener;
	private JList credList;

	public CredentialLister() {
		log.debug("created") ;
		initList();
		this.add(new JScrollPane(credList));
	}

	private void initList() {
		credList = new JList();
		addNewCredential();
		credList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		log.debug ("CredentialStore with " + credStore.getAllCredentials().size() + " credentials loaded.");
	}

	public void addListSelectionListener(ListSelectionListener l) {
		listener = l;
		credList.addListSelectionListener(listener);
		credList.setSelectedIndex(0);
	}

	public void addNewCredential() {
		try {
		    credStore.addCredential(getDummyCredential());
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		renewList();
		credList.setSelectedIndex(credStore.getAllCredentials().size() -1 );
	}

	public Credential getSelectedCredential() {
		int i = credList.getMinSelectionIndex();
		
		// still to check if the code after this paragraph is equivalent with the code right below
		//if ( i < 0 ) return credStore.getCredentialAt(0);
		//return credStore.getCredentialAt(i);
		
		if ( i < 0 ) return (Credential) credStore.getAllCredentials().get(0);
		return (Credential) credStore.getAllCredentials().get(i);
	}

	private CredentialStore getCredentialStore() {
		return credStore;
	}

	public void loadCredentials(java.io.File file) throws CredentialException {
		credStore.loadAllCredentialsFromFile(file);
		renewList();
	}

	public void renewList() {
		credList.setListData(credStore.getAllCredentials());
	}

	public void saveCredentials(java.io.File file) throws CredentialException {
		credStore.saveAllCredentialsToFile(file);
	}
	
	public static Credential getDummyCredential() {
		return new DummyCredential();
	}
}
