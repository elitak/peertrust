/*
 * Created on 22.01.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.peertrust.security.credentials;

import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author eric
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class CredentialPanel extends JPanel implements ActionListener{

	private static final int classPriority = 3;
	private Credential credential;
	private JLabel valLabel = new JLabel("Value:");
	private JLabel issLabel = new JLabel("Issuer:");
	private JLabel signerLabel = new JLabel("Signer:");
	private JLabel signatureLabel = new JLabel("Signature:");
	private JTextField valText = new JTextField();
	private JTextField issText = new JTextField();
	private JTextField signerText = new JTextField();
	private JTextField signatureText = new JTextField();
	private JButton applyButton;
	private JButton resetButton;

	public CredentialPanel(Credential cred) {
		super(new GridLayout(5, 1));
		classMessageWriter("<init>", "new CredentialPanel created.", 1);
		credential = cred;
		init();
	}

	public void setCredential(Credential cred) {
		classMessageWriter("setCredential","Invoked with "+cred.toString(),2);
		credential = cred;
		initTextFields();
		setButtonsEnabled(false);
	}

	public Credential getCredential() {
		classMessageWriter("getCredential", "Returning " + credential.toString(), 1);
		return credential;
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(issText) || ae.getSource().equals(valText) || ae.getSource().equals(signerText)) {
			setButtonsEnabled(true);
		} else if (ae.getSource().equals(resetButton)) {
			setButtonsEnabled(false);
			initTextFields();
		} else if (ae.getSource().equals(applyButton)) {
			setButtonsEnabled(false);
			setCredentialFields();
			getParent().repaint();
		}
	}

	private void setButtonsEnabled(boolean arg) {
		applyButton.setEnabled(arg);
		resetButton.setEnabled(arg);
	}

	private void init() {
		JPanel valuePanel = new JPanel(new GridLayout(1, 2));
		valText.addActionListener(this);
		valuePanel.add(valLabel);
		valuePanel.add(valText);

		JPanel issuerPanel = new JPanel(new GridLayout(1, 2));
		issText.addActionListener(this);
		issuerPanel.add(issLabel);
		issuerPanel.add(issText);

		JPanel signerPanel = new JPanel(new GridLayout(1, 2));
		signerText.addActionListener(this);
		signerPanel.add(signerLabel);
		signerPanel.add(signerText);

		JPanel signaturePanel = new JPanel(new GridLayout(1, 2));
		signaturePanel.add(signatureLabel);
		signaturePanel.add(signatureText);
		signatureText.setEditable(false);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		applyButton = new JButton("apply");
		applyButton.setEnabled(false);
		applyButton.setToolTipText("Apply changes to credential.");
		applyButton.addActionListener(this);

		resetButton = new JButton("reset");
		resetButton.setEnabled(false);
		resetButton.setToolTipText("Reset credential to old values.");
		resetButton.addActionListener(this);

		buttonPanel.add(applyButton);
		buttonPanel.add(resetButton);

		this.add(valuePanel);
		this.add(issuerPanel);
		this.add(signerPanel);
		this.add(signaturePanel);

		this.add(buttonPanel);

		initTextFields();
	}

	private void initTextFields() {
		setValueText();
		setIssuerText();
		setSignerText();
	}

	private void setValueText() {
		String tmp = credential.getStringRepresentation();
		valText.setText( tmp.substring( 0, tmp.indexOf( '@' ) ) );
	}

	private void setIssuerText() {
		String tmp = credential.getStringRepresentation();
		issText.setText( tmp.substring( tmp.indexOf( '@' ) +1 ) );
	}

	private void setSignerText() {
		signerText.setText(credential.getSignerName());
	}

	/*
	 * public void setNewSignature(byte[] sig) { String tmp = new String(sig);
	 * credential.setSignature(sig); signatureText.setText(tmp);
	 */

	private void setCredentialFields() {
		classMessageWriter("setCredentialFields", "Setting issuer to " + issText.getText() + ", signer to " + signerText.getText() + " and value to " + valText.getText(), 1);
/*		credential.setIssuer(issText.getText());
		credential.setSigner(signerText.getText());
		credential.setValue(valText.getText());
		credential.setSignature(null);*/
	}

	private static void classMessageWriter(String method, String message, int priority) {
		if (classPriority >= priority) {
			CryptTools.messageWriter("CredentialPanel." + method, message, priority);
		}
	}
}
