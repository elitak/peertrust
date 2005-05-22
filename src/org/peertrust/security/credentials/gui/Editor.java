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
package org.peertrust.security.credentials.gui;

import java.security.*;
import javax.swing.*;
import javax.swing.event.*;

import org.apache.log4j.Logger;
import org.peertrust.exception.CredentialException;
import org.peertrust.security.credentials.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * <p>
 * This class allows editing of credentials.
 * </p><p>
 * $Id: Editor.java,v 1.2 2005/05/22 17:56:46 dolmedilla Exp $
 * <br/>
 * Date: 14-Jan-2004
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:46 $
 * by $Author: dolmedilla $
 * </p>
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public class Editor extends JFrame implements ActionListener, ListSelectionListener
{
    private static Logger log = Logger.getLogger(Editor.class);
    
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
		} catch (CredentialException e) {
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
			} catch (CredentialException e) {
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

	public static void main(String[] args) {
		Editor test = new Editor();
		test.init() ;
	}
}
