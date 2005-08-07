package crypto;

import javax.swing.*;

//import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.security.*;

public class CryptGui extends JFrame implements ActionListener {

	private JTextArea editArea;
	private JTextArea hashArea;
	private JButton saveButton;
	private JButton signButton;
	private JButton hashButton;

	private KeyPair keyPair;

	public CryptGui() {
		super("CryptTest - Testing java.jca / java.jce");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				quit();
			}
		});

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		/*

		JPanel buttonPane = new JPanel();

		saveButton = new JButton("save");
		saveButton.setToolTipText("Save Text");
		buttonPane.add(saveButton);
		saveButton.addActionListener(this);

		signButton = new JButton("sign");
		saveButton.setToolTipText("Sign Textfile");
		buttonPane.add(signButton);
		signButton.addActionListener(this);

		hashButton = new JButton("hash");
		hashButton.setToolTipText("Compute hash-value of text");
		buttonPane.add(hashButton);
		hashButton.addActionListener(this);

		contentPane.add(buttonPane, "South");

		JScrollPane scrEditPane;
		JPanel editPane = new JPanel();

		editArea = new JTextArea("I am a certificate.\nTrust me.", 30, 20);

		editPane.add(editArea);
		scrEditPane = new JScrollPane(editPane);

		JPanel showPane = new JPanel(new BorderLayout());

		hashArea = new JTextArea("", 30, 20);
		hashArea.setEditable(false);

		showPane.add(hashArea);
		JScrollPane scrHashPane = new JScrollPane(showPane);

		contentPane.add(scrEditPane, "Center");
		contentPane.add(scrHashPane, "East");
*/
		contentPane.add(new KeyStorePanel(this,"testpwd".toCharArray()), "Center");
		//contentPane.add(new KeyStorePanel(this), "Center");
		setLocationRelativeTo(getParent());
		pack();
		show();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Quit")) {
			quit();
		}
		else if (ae.getSource().equals(saveButton)) {
			CryptTest.writeFile(editArea.getText());
			System.exit(0);
		}
		else if (ae.getSource().equals(hashButton)) {
			byte[] hv = CryptTools.hashString(editArea.getText());
			String temp = "";
			for (int i = 0; i < hv.length; i++) {
				temp += hv[i];
			}
			hashArea.setText(temp);
		}
		else if (ae.getSource().equals(signButton)) {
			sign();
			System.exit(0);
		}
	}

	private void quit() {
		CryptTest.printMessage("CryptGui.actionPerformed", "Shutting down.", 1);
		System.exit(0);
	}
	
	private void sign() {
		// 1. If no key available, build one:
		if (keyPair == null) {
			keyPair = CryptTools.makeKeyPair();
		}
		// 2. Text signieren:
		CryptTools.signData(keyPair.getPrivate());
	}
	
	public static void main ( String[] args ) {
		CryptGui gui = new CryptGui();
	}
}
