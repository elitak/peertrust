/*
 * Created on 18.02.2004
 */
package crypto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public class PasswordDialog extends JDialog implements ActionListener{
	
	private static final int debugLevel = 5; 
	private JLabel message;
	private JPasswordField pwdField;
	private JButton pwdOkay;
	private JButton pwdCancel;
	private char[] p;
	
	public PasswordDialog(JFrame owner) {
		super(owner, "unknown title", true);
		pwdField = new JPasswordField();
		pwdOkay = new JButton("okay");
		pwdOkay.addActionListener(this);
		pwdCancel = new JButton("cancel");
		pwdCancel.addActionListener(this);
		JPanel btnPane = new JPanel();
		btnPane.add(pwdOkay);
		btnPane.add(pwdCancel);
		message = new JLabel();
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(btnPane, BorderLayout.SOUTH);
		contentPane.add(message, BorderLayout.NORTH);
		contentPane.add(pwdField, BorderLayout.CENTER);
		setLocationRelativeTo(getParent());
		pack();
		hide();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(pwdOkay)) {
			classMessageWriter("actionPerformed","Okay pressed.",3);
			p = pwdField.getPassword();
			this.hide();
		}
		else if (ae.getSource().equals(pwdCancel)) {
			//pwdField = new JPasswordField();
			p = null;
			this.hide();
		}
	}
	
	public void showDialog(String title, String message) {
		//pwdField = new JPasswordField();
		p = null;
		this.setTitle(title);
		this.message.setText(message);
		pack();
		show();
	}
	
	public char[] getPwd() {
		return p;
	}
	
	public static void main (String[] args) {
		PasswordDialog test = new PasswordDialog(new JFrame());
		test.showDialog("Test","Bitte einen Text zur Ausgabe auf der Konsole eingeben.");
		char[] c = test.getPwd();
		classMessageWriter("main","Eingabe: "+new String(c),0);
	}
	
	private static final void classMessageWriter(String method, String message, int prio) {
			if (debugLevel > prio) {
				CryptTest.printMessage("PasswordDialog." + method, message, prio);
			}
		}
	
}
