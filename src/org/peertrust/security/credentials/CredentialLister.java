package org.peertrust.security.credentials;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.peertrust.security.credentials.gui.DummyCredential;
import org.peertrust.security.credentials.x509.*;


/**
 * Lists all credentials a given CredentialStore contains. This class encapsulates the CredentialStore.
 * 
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public class CredentialLister extends JPanel {

	private static final int classPriority = 3;
	private CredentialStore credStore = new X509CredentialStore();
	private ListSelectionListener listener;
	private JList credList;

	public CredentialLister() {
		classMessageWriter("<init>","new CredentialLister created",1);
		initList();
		this.add(new JScrollPane(credList));
	}

	private void initList() {
		credList = new JList();
		addNewCredential();
		credList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		classMessageWriter("reload", "CredentialStore with " + credStore.getAllCredentials().size() + " credentials loaded.", 1);
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
		if ( i < 0 ) return credStore.getCredentialAt(0);
		return credStore.getCredentialAt(i);
	}

	private CredentialStore getCredentialStore() {
		return credStore;
	}

	public void loadCredentials(java.io.File file) throws Exception {
		credStore.loadAllCredentialsFromFile(file);
		renewList();
	}

	public void renewList() {
		credList.setListData(credStore.getAllCredentials());
	}

	public void saveCredentials(java.io.File file) throws Exception {
		credStore.saveAllCredentialsToFile(file);
	}
	
	public static Credential getDummyCredential() {
		return new DummyCredential();
	}

	private static void classMessageWriter(String method, String message, int priority) {
		if (classPriority >= priority) {
			CryptTools.messageWriter("credentials.CredentialLister." + method, message, priority);
		}
	}
}
