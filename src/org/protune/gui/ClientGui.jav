package org.protune.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author cjin, swittler
 */
public class ClientGui extends JFrame implements ActionListener {

	private JTextField textClientAddress;
	private JTextField textServerAddress;
	private JTextField textClientPort;
	private JTextField textServerPort;

	private JTextField textService;
	private JTextField textRequest;
	private JTextArea textareaResult;
	private JButton buttonClientStart;
	private JButton buttonServerStart;


	private DummyClient client;
	private DummyServer server;
	
	private static final long serialVersionUID = 909678460071555084L;
	
	public static void main(String args[]) {
		new ClientGui();
	}
	
	public ClientGui() {
		super("Protune GUI");
		setSize(500,400);
		setLayout(new GridLayout(5,0));

		JTabbedPane tabbedpaneConfig=new JTabbedPane();
		JPanel panel=new JPanel(new GridLayout(2,2));
		textClientAddress=new JTextField();
		panel.add(new JLabel("Client address"));
		panel.add(textClientAddress);
		textClientPort=new JTextField();
		panel.add(new JLabel("Client port"));
		panel.add(textClientPort);
		tabbedpaneConfig.addTab("Client",panel);
		panel=new JPanel(new GridLayout(2,2));
		textServerAddress=new JTextField();
		panel.add(new JLabel("Server address"));
		panel.add(textServerAddress);
		textServerPort=new JTextField();
		panel.add(new JLabel("Server port"));
		panel.add(textServerPort);
		tabbedpaneConfig.addTab("Server",panel);
		add(tabbedpaneConfig);

		panel=new JPanel(new GridLayout(1,2));
		textService=new JTextField();
		panel.add(new JLabel("Service"));
		panel.add(textService);
		add(panel);
		
		panel=new JPanel(new GridLayout(1,2));
		textRequest=new JTextField();
		panel.add(new JLabel("Request"));
		panel.add(textRequest);
		add(panel);

		panel=new JPanel(new GridLayout(1,2));
		textareaResult=new JTextArea();
		panel.add(new JLabel("Result"));
		panel.add(textareaResult);
		add(panel);

		panel=new JPanel(new GridLayout(2,2));
		buttonServerStart=new JButton("Start server");
		buttonServerStart.addActionListener(this);
		panel.add(buttonServerStart);
		buttonClientStart=new JButton("Start client");
		buttonClientStart.addActionListener(this);
		panel.add(buttonClientStart);
		add(panel);

		client=new DummyClient(this);
		server=new DummyServer(this);

		textServerAddress.setText("localhost");
		textServerPort.setText("1234");
		textClientAddress.setText("localhost");
		textClientPort.setText("1235");
		textService.setText("org.protune.net.DummyService");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(buttonClientStart)) {
			try {
				client.startClient(textClientAddress.getText(),Integer.parseInt(textClientPort.getText()),textService.getText(),textRequest.getText(),textServerAddress.getText(),Integer.parseInt(textServerPort.getText()));
			} catch (Exception exc) {
				showException(exc);
			}
		}
		else if(e.getSource().equals(buttonServerStart)) {
			try {
				server.startServer(textServerAddress.getText(),Integer.parseInt(textServerPort.getText()),textService.getText());
			} catch (Exception exc) {
				showException(exc);
			}
		}
	}
	
	public void showException(Exception e) {
		JOptionPane.showMessageDialog(this,e.getMessage());
	}
	
	public boolean showQuery(String strQuery) {
		return JOptionPane.showConfirmDialog(this,strQuery,"",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
	}

}
