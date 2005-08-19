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
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoginDlg extends JDialog implements ActionListener {

	private boolean isOk = false;
	private JButton okButton,canButton;
	private JTextField usernameTF;
	private JPasswordField passwordTF;

	
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.awt.HeadlessException
	 */
	public LoginDlg(Frame frame, String name) throws HeadlessException {
		super(frame, name, true);
		makeLayout();
	  }

	private void makeLayout(){
		//setLayout(new FlowLayout());
		
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

	 
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(okButton)){
			isOk=true;
		}
		setVisible(false);
		return;
	}

	public boolean isOk(){
		return isOk;
	}
	
	public String getUsername(){
		return usernameTF.getText();
	}
	
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
