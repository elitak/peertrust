/*
 * Created on 16.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client.applet;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


/**
 * This is a dialog that prompt the user to enter a
 * user name and password
 * @author pat_dev
 *
 */
public class LoginDlg extends JDialog implements ActionListener {
	/**
	 * holds the user answer.
	 */
	private boolean isOk = false;
	
	/** ok button to confirm user entry*/
	private JButton okButton;
	
	/** button to cancel dialog*/
	private JButton canButton;
	
	/** text field to enter user name*/
	private JTextField usernameTF;
	
	/** text field to enter password*/
	private JPasswordField passwordTF;

	
	/**
	 * Show a password dlg
	 * @param frame -- the parent frame, can be null.
	 * @param name -- the title of the dialog window.
	 * @throws java.awt.HeadlessException
	 * @see javax.swing.JDialog
	 */
	public LoginDlg(Frame frame, String name) throws HeadlessException {
		super(frame, name, true);
		makeLayout();
	  }

	/**
	 * make the dialog layout.
	 *
	 */
	private void makeLayout(){
		usernameTF = new JTextField(15);
		passwordTF = new JPasswordField(15);
		canButton= new JButton("Cancel"); canButton.addActionListener(this);
		okButton= new JButton("Ok");okButton.addActionListener(this);
	  //password.setEchoChar('*');
		java.awt.Container container= this.getContentPane();
		
		JPanel userPanel= new JPanel();
		userPanel.add(new JLabel("User :"));
		userPanel.add(usernameTF);
		
		JPanel passwordPanel= new JPanel();
		passwordPanel.add(new JLabel("Password :"));
		passwordPanel.add(passwordTF);
	  	
	  	container.setLayout(new GridLayout(4,1));
	  	container.add(userPanel);
	  	container.add(passwordPanel);
	  	container.add(okButton);
	  	container.add(canButton);//addOKCancelPanel();
	  	//createFrame();
	  	pack();
	  	setVisible(true);
	}
//	 void addOKCancelPanel() {
//	  Panel p = new Panel();
//	  p.setLayout(new FlowLayout());
//	  createButtons( p );
//	  add( p );
//	  }

//	 void createButtons(Panel p) {
//	  p.add(ok = new Button("OK"));
//	  ok.addActionListener(this); 
//	  p.add(can = new Button("Cancel"));
//	  can.addActionListener(this);
//	  }

//	 void createFrame() {
//	  Dimension d = getToolkit().getScreenSize();
//	  setLocation(d.width/4,d.height/3);
//	  }

	 
	
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(okButton)){
			isOk=true;
		}
		setVisible(false);
		return;
	}

	/**
	 * get the user response.
	 * @return true if user has  klicked the ok 
	 * 			or false if the user has canceled the dialog
	 */
	public boolean isOk(){
		return isOk;
	}
	
	/**
	 * @return the user name
	 */
	public String getUsername(){
		return usernameTF.getText();
	}
	
	/**
	 * @return returns the user passwork
	 */
	public String getPassword(){
		return new String(passwordTF.getPassword());
	}
	
	static public void main(String[] args){
		JFrame frame= new JFrame();
		frame.setSize(100,100);
		frame.setVisible(true);
		LoginDlg login= new LoginDlg(frame, "Login");
		frame.getContentPane().add(
				new JTextField(
						"resp:"+login.isOk()+
						" userName:"+login.getUsername()+
						" password:"+login.getPassword()));
		frame.pack();
	}
	
}
