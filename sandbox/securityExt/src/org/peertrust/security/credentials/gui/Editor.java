package org.peertrust.security.credentials.gui;

import java.security.*;
import javax.swing.*;
import javax.swing.event.*;

import org.peertrust.security.credentials.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * This class allows editing of credentials.
 * 
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public class Editor extends JFrame implements ActionListener, ListSelectionListener {

	private String signer; // The one who edits the credential
	private PrivateKey privKey; // The privateKey that should be
											// used to sign the credential
	private CredentialPanel credential;
	private JButton saveButton;
	private JButton loadButton;
	private JButton signButton;
	private JButton addButton;
	private CredentialLister credList;
	
	private File lastFile;
	
	public Editor() {
		super("Credential Editor - <new credential>");
	}

	public Editor(Credential cred) {
		super("Credential Editor - <" + cred.getStringRepresentation() + ">");
	}

	private void init() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				quit();
			}
		});

		JPanel buttonPane = new JPanel();

		saveButton = new JButton("save & exit");
		saveButton.setToolTipText("Save credential store to file and exit");
		buttonPane.add(saveButton);
		saveButton.addActionListener(this);

		loadButton = new JButton("load");
		loadButton.setToolTipText("Load credential store from file");
		buttonPane.add(loadButton);
		loadButton.addActionListener(this);

		signButton = new JButton("sign");
		signButton.setToolTipText("Sign Textfile");
		buttonPane.add(signButton);
		signButton.addActionListener(this);

		addButton = new JButton("add");
		signButton.setToolTipText("Add new credential to store");
		buttonPane.add(addButton);
		addButton.addActionListener(this);

		credential = new CredentialPanel(CredentialLister.getDummyCredential());
		credList = new CredentialLister();
		credList.addListSelectionListener(this);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(buttonPane, "South");
		contentPane.add(credential, "Center");
		contentPane.add(credList, "West");

		setLocationRelativeTo(getParent());

		pack();
		show();

	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Quit")) {
			quit();
		} else if (ae.getSource().equals(saveButton)) {
			saveCredential();
			quit();
		} else if (ae.getSource().equals(signButton)) {
			signCredential();
		} else if (ae.getSource().equals(loadButton)) {
			loadCredential();
		} else if (ae.getSource().equals(addButton)) {
			addCredential();
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		//if (credential != null) {
			credential.setCredential(credList.getSelectedCredential());
		//} else {
		//	credential = new CredentialPanel(credList.getSelectedCredential());
		//}
	}

	public void repaint() {
		credList.renewList();
		super.repaint();
	}
	
	private void signCredential() {

	}

	private void saveCredential() {
		try {
			JFileChooser fc = new JFileChooser();
			if (lastFile!=null)fc.setSelectedFile(lastFile);
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				lastFile = fc.getSelectedFile();
				credList.saveCredentials(fc.getSelectedFile());
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadCredential() {
		JFileChooser fc = new JFileChooser();
		if (lastFile!=null)fc.setSelectedFile(lastFile);
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				lastFile = fc.getSelectedFile();
				credList.loadCredentials(fc.getSelectedFile());
				this.setTitle("Credential Editor - "+lastFile.toString());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void addCredential() {
		credList.addNewCredential();
	}

	private void quit() {
		log.debug("Shutting down...");
		this.dispose();
	}
	

}
