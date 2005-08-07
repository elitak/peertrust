/*
 * Created on 18.02.2004
 */
package crypto;

import java.security.*;
import java.security.cert.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;

import java.util.*;
import java.io.*;

/**
 * This class provides a plugable gui for managing the keys of a keystore.
 * 
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public class KeyStorePanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final int KEY_LENGTH = 512;
	private static final String ALGORITHM = "RSA";
	private static final int debugLevel = 5;

	private PrivateKey caKey = null;
	private KeyStore ks;
	private JTextField aliasField;
	private JList aliasList;
	private PasswordDialog pwdDialog;
	private JButton createKS;
	private JButton loadKS;
	private JButton saveKS;
	private JButton closeKS;

	private JLabel outputLabel;
	private JTextPane outputPane;
	private char[] password = null;
	
	/**
	 * This Constructor accepts a password. This password is used to open keystores and 
	 * their keys. Use with care!
	 * @param owner the Parent of this JPanel
	 * @param password the password, that should be used
	 */
	public KeyStorePanel(JFrame owner, char[] password) {
		this.password = password;
		init(owner);
	}
	
	/**
	 * Constructs a new KeyStorePanel.
	 * @param owner the Parent of this JPanel
	 */
	public KeyStorePanel(JFrame owner) {
		init(owner);
	}

	private void init(JFrame owner) {
		CryptTools.installBouncyCastle();
		pwdDialog = new PasswordDialog(owner);
		this.setLayout(new BorderLayout());
		try {
			ks = KeyStore.getInstance("JKS");
		} catch (KeyStoreException e) {
			handleException(e);
		}
		// build up the key /certificate list
		buildAliasList();
		aliasList.addListSelectionListener(this);
		JScrollPane sp = new JScrollPane(aliasList);
		this.add(sp, BorderLayout.WEST);

		// init / add buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));
		createKS = new JButton("create");
		createKS.setToolTipText("Creates a new Keystore.");
		createKS.addActionListener(this);
		buttonPanel.add(createKS);
		loadKS = new JButton("load");
		loadKS.setToolTipText("Restores a Keystore from a file.");
		loadKS.addActionListener(this);
		buttonPanel.add(loadKS);
		saveKS = new JButton("save");
		saveKS.setToolTipText("Saves a Keystore to a file.");
		saveKS.addActionListener(this);
		buttonPanel.add(saveKS);
		closeKS = new JButton("close");
		closeKS.setToolTipText("Closes all open Keystores and disposes this panel.");
		closeKS.addActionListener(this);
		buttonPanel.add(closeKS);

		outputPane = new JTextPane();
		outputPane.setEditable(false);
		JScrollPane sp2 = new JScrollPane(outputPane);
		sp2.setMaximumSize(new Dimension(50, 50));
		//outputArea.setText("Key / Certificate:\n alias:\n
		// -------------------");
		this.add(sp, BorderLayout.WEST);
		this.add(buttonPanel, BorderLayout.EAST);
		this.add(outputPane, BorderLayout.CENTER);
	}

	private void buildAliasList() {
		if (aliasList == null) {
			aliasList = new JList();
		}
		if (ks != null) {
			try {
				Enumeration tmpE = ks.aliases();
				Vector tmpV = new Vector();
				while (tmpE.hasMoreElements()) {
					tmpV.add(tmpE.nextElement());
				}
				aliasList.setListData(tmpV);
			} catch (Exception e) {
				handleException(e);
			}
		} else {
			aliasList = new JList();
		}
	}

	private void fillOutputLabel() {
		try {
			String output = "";
			String alias = (String) aliasList.getSelectedValue();
			if (alias!=null) {
				if (ks.isCertificateEntry(alias)) {
					output += "This is a certificate. ";
					if (isCredentialCert((X509Certificate) ks.getCertificate(alias))) {
					output += alias + " Is it a Credential? "+ CryptTools.extractCredential((X509Certificate) ks.getCertificate(alias));
					}
				}
				// Normally every key contains Certificates that proof its
				// validity.
				output += getCertificateStringRep((X509Certificate) ks.getCertificate(alias));
				if (ks.isKeyEntry(alias)) {
					output += "\n" + "This is a key. \n";
					// TODO: Nicht jeder Schl?ssel hier kann ein PrivateKey sein!!!
					caKey = (PrivateKey) ks.getKey(alias, askForPassword("fetching key", "Please insert password for key '" + alias + "'."));
					output += getKeyStringRep(caKey);
				}
				outputPane.setText(output);}
		} catch (Exception e) {
			handleException(e);
		}
	}

	private String getCertificateStringRep(X509Certificate cert) throws Exception {
		String output = "Certificate:\n";
		output += cert.toString();

		output += "\n"+"----"+"\n";

		/*		String output = "Owner of Certificate: ";
				output += cert.getSubjectDN().getName();
				output += "\n valid from: " + cert.getNotBefore().toString();
				output += " until: " + cert.getNotAfter().toString();
				output += "\n Issuer of Certificate: " + cert.getIssuerDN().getName();
				output += "\n Version: " + String.valueOf(cert.getVersion());
				output += "\n Serialno.: " + "0x" + cert.getSerialNumber().toString(16);
				output += "\n Signature Algorithm: " + cert.getSigAlgName();
		//output += "\n Signature: " + new String(cert.getSignature());
		output += "\n Public Key: \n" + getKeyStringRep(cert.getPublicKey());
		output += "\n" + "------------------------------------------------------";
		output += "\n" + "is a Credential?____"+isCredentialCert( cert );
		classMessageWriter("getCertificateStringRep", "Fill in Certificate: \n" + output, 1);
		*/
		return output;
	}

	private boolean isCredentialCert( X509Certificate cert ) throws Exception {
		classMessageWriter("isCredentialCert","Testing Certificate " + cert.toString(),1);
		return CryptTools.extractCredential( cert ) != null;
	}	

	private String getKeyStringRep(Key key) throws Exception {
		String output = "Key:\n";
		output += key.toString();
		output += "\n"+"----"+"\n";
		return output;
		/*String output = "Algorithm: " + key.getAlgorithm();
		output += "\n Format: " + key.getFormat();
		output += "\n String Representation: " + key.toString();
		//output += "\n " + new String(key.getEncoded());
		classMessageWriter("getKeyStringRep", "Fill in Key: \n" + output, 1);
		return output + "\n" + "------------------------------------------------------";*/
	}

	private boolean ksExists() {
		return ks != null;
	}

	private void loadKeyStore() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				InputStream is = new FileInputStream(fc.getSelectedFile());
				ks.load(is, askForPassword("loading keystore", "Please insert password for " + fc.getSelectedFile().toString() + "."));
				classMessageWriter("loadKeyStore", "Keystore loaded, building List with aliases.", 3);
				buildAliasList();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void storeKeyStore() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				OutputStream os = new FileOutputStream(fc.getSelectedFile());
				ks.store(os, askForPassword("saving keystore", "Please insert password for " + fc.getSelectedFile().toString() + "."));
				classMessageWriter("storeKeyStore", "Keystore stored, building List with aliases.", 3);
				buildAliasList();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void handleException(Exception e) {
		classMessageWriter("handleException",e.toString(),1);
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i<3 && i<trace.length; i++) {
			classMessageWriter("handleException",trace[i].toString(),1);
		}
		//e.printStackTrace();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(createKS)) {
			classMessageWriter("actionPerformed", "Create KeyStore", 1);
			if ( caKey != null ) {
				// TODO: Irgendwie muss der public key noch von wo anders kommen
				try {
					PublicKey pubKey = CryptTools.makeKeyPair(ALGORITHM, KEY_LENGTH).getPublic();
					//outputPane.setText(getCertificateStringRep(createCert(pubKey, caKey)));
					X509Certificate cert = CryptTools.createCert(pubKey, caKey);
					ks.setCertificateEntry(cert.getSubjectDN().getName(),cert);
					buildAliasList();
				} catch (Exception e) {
					handleException(e);
				}
			}
		} else if (ae.getSource().equals(loadKS)) {
			classMessageWriter("actionPerformed", "Load KeyStore", 1);
			loadKeyStore();
		} else if (ae.getSource().equals(saveKS)) {
			classMessageWriter("actionPerformed", "Save KeyStore", 1);
		} else if (ae.getSource().equals(closeKS)) {
			classMessageWriter("actionPerformed", "Close KeyStore", 1);
			System.exit(0);
		}
	}

	private char[] askForPassword(String title, String message) {
		if (password != null) {
			return password;
		} else {
			String t = title;
			String m = message;
			if (t == null)
				t = "KeyStorePanel";
			if (m == null)
				m = "Password for keyStore needed.";
			pwdDialog.showDialog(t, m);
			return pwdDialog.getPwd();
		}
	}

	private static final void classMessageWriter(String method, String message, int prio) {
		if (debugLevel > prio) {
			CryptTest.printMessage("KeyStorePanel." + method, message, prio);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		//if (credential != null) {
		fillOutputLabel();
		//} else {
		//	credential = new CredentialPanel(credList.getSelectedCredential());
		//}

	}

	private void useCertificate() {
		try {
			Key k = ks.getKey((String) aliasList.getSelectedValue(), askForPassword("fetching alias", "Please insert password for key '" + (String) aliasList.getSelectedValue()));

		} catch (Exception e) {
			handleException(e);
		}
	}


}
