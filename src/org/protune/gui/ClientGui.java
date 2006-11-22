package org.protune.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JApplet;

/**
 * 
 * @author cjin, swittler
 */
public class ClientGui extends JApplet implements ActionListener {
	
	private JTextField textClientAddress;
	private JTextField textServerAddress;
	private JTextField textClientPort;
	private JTextField textServerPort;

	private JTextField textAvailableService;
	private JTextField textRequestedService;
	private JTextArea textareaResult;
	private JButton buttonClientStart;
	private JButton buttonServerStart;


	private DummyClient client;
	private DummyServer server;
	
	private static final long serialVersionUID = 909678460071555084L;
	
	public static void main(String args[]) {
		new ClientGui();
	}
	
	public void init(){
		setSize(500,400);
		setLayout(new GridLayout(3,0));

		JTabbedPane tabbedpaneConfig=new JTabbedPane();
		tabbedpaneConfig.setSize(100, 100);
		JPanel panel=new JPanel(new GridLayout(3,3));
		textServerAddress=new JTextField();
		panel.add(new JLabel("Server Address"));
		panel.add(textServerAddress);
		textServerPort=new JTextField();
		panel.add(new JLabel("Server Port"));
		panel.add(textServerPort);
		textAvailableService=new JTextField();
		panel.add(new JLabel("Available Services"));
		panel.add(textAvailableService);
		tabbedpaneConfig.addTab("Server Configuration",panel);
		
		panel=new JPanel(new GridLayout(3,3));
		textClientAddress=new JTextField();
		panel.add(new JLabel("Client Address"));
		panel.add(textClientAddress);
		textClientPort=new JTextField();
		panel.add(new JLabel("Client Port"));
		panel.add(textClientPort);
		textRequestedService=new JTextField();
		panel.add(new JLabel("Requested Service"));
		panel.add(textRequestedService);
		tabbedpaneConfig.addTab("Client Configuration",panel);
		
		add(tabbedpaneConfig);

		panel=new JPanel(new GridLayout(1,2));
		textareaResult=new JTextArea();
		panel.add(new JLabel("Result"));
		panel.add(textareaResult);
		add(panel);

		panel=new JPanel(new GridLayout(2,2));
		buttonServerStart=new JButton("Start Server");
		buttonServerStart.addActionListener(this);
		panel.add(buttonServerStart);
		buttonClientStart=new JButton("Start Client");
		buttonClientStart.addActionListener(this);
		panel.add(buttonClientStart);
		add(panel);

		client=new DummyClient(this);
		server=new DummyServer(this);

		textServerAddress.setText("localhost");
		textServerPort.setText("1234");
		textClientAddress.setText("localhost");
		textClientPort.setText("1235");
	
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(buttonClientStart)) {
			try {
				client.startClient(
						textClientAddress.getText(),
						Integer.parseInt(textClientPort.getText()),
						textRequestedService.getText(),
						textServerAddress.getText(),
						Integer.parseInt(textServerPort.getText())
				);
			} catch (Exception exc) {
				showException(exc);
			}
		}
		else if(e.getSource().equals(buttonServerStart)) {
			try {
				String strServices = "";
				strServices = textAvailableService.getText();
				String[] sa;
				if(strServices.indexOf(",")!=-1){
					sa = strServices.split(",");
				}
				else {
					sa = new String[1];
					sa[0] = strServices;
				}
				for(int i=0; i<sa.length; i++) System.out.println(sa[i]);
				server.startServer(
						textServerAddress.getText(),
						Integer.parseInt(textServerPort.getText()),
						sa
				);
			} catch (Exception exc) {
				showException(exc);
			}
		}

	}
	
	public void showException(Exception e) {
		JOptionPane.showMessageDialog(this,e.getMessage());
	}
	
	public boolean showQuery(String strQuery) {
		return JOptionPane.showConfirmDialog(
				this,
				strQuery,
				"",
				JOptionPane.YES_NO_OPTION
		) == JOptionPane.YES_OPTION;
	}
}
