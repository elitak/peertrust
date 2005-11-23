/*
 * Created on 18.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.common;

import java.awt.GridLayout;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * A pane with an interace to show text message on it.
 * 
 * @author Patrice Congo (token77)
 */
public class EchoPane extends JPanel {
	/** the limiter between the messages*/
	public static final String LIMIT_STR=
		"\n*******************************************************************\n";
	
	/** text area used to show the tex messages*/
	JTextArea textArea= new JTextArea();
	
	/** the popup menu of the pane*/
	JPopupMenu popupMenu=new JPopupMenu("Edit");
	
	/**
	 * Creates a new echo pane.
	 */
	public EchoPane(){
		makeLayout();
		makePopupMenu();
	}
	
	/**
	 * Makes the echo pane layout.
	 */
	private void makeLayout(){
		setLayout(new GridLayout(1,1));
		JScrollPane sPane= new JScrollPane(textArea);		
		add(sPane);
		validate();
		return;
	}
	/**
	 * create the popup menu for this echo pane.
	 * The menu provides a an entry to clear the pane.
	 */
	private void makePopupMenu(){
		popupMenu.setLabel("Menu");
		JMenuItem menuItem= new JMenuItem("clear");
		popupMenu.add(menuItem);
		
		MouseListener mouseL= new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				System.out.println("txt1:"+e);
				Object source=e.getSource();
				if(source.equals(popupMenu)){
					System.out.println("txt2:"+e);
				}else if(source.equals(textArea)){
					System.out.println("txt3:"+e);
					if(e.getButton()!=MouseEvent.BUTTON1){
					    popupMenu.show(e.getComponent(),
			                       e.getX(), e.getY());			        
			    	}
				}else{
					System.out.println("txt4"+e);
				}
			}
			public void mouseReleased(MouseEvent e) {
		        Object source= e.getSource();
		        if(source instanceof JMenuItem){
		        	String text=((JMenuItem)source).getText();
		        	if(text.equals("clear")){
		        		textArea.setText("");
		        	}
		        }
		    }
		};
		
		textArea.addMouseListener(mouseL);
		menuItem.addMouseListener(mouseL);
	}
	
	/**
	 * Shows a text message in this echo pane.
	 * @param message -- the string message to show
	 */
	public void echo(String message){
		echo(message,null);
		return;
	}
	/**
	 * show a thowable allong with explanation message
	 * int the echo pane.
	 *  
	 * @param message -- the explanation massage
	 * @param th -- the throwable to show
	 */
	public void echo(String message, Throwable th){
		textArea.append(LIMIT_STR);
		textArea.append(message);
		
		if(th!=null){
			StringWriter strWriter= new StringWriter();
			PrintWriter printWriter= new PrintWriter(strWriter);
			th.printStackTrace(printWriter);
			textArea.append("\n============\n");
			textArea.append(strWriter.toString());
		}
		return;
	}
	
	static public void main(String[] args){
		JFrame frame= new JFrame();
		EchoPane echoPane= new EchoPane();
		echoPane.echo("adadadada");
		frame.add(echoPane);
		frame.setSize(500,500);
		frame.setVisible(true);
		echoPane.echo("adadadada");
		echoPane.echo("adadadada");
		echoPane.echo("adadadada");
		echoPane.echo("adadadada");
		echoPane.echo("adadadada");
		try{
			Integer.parseInt("ewassd");
		}catch(Throwable th){
			echoPane.echo("My Own Exception:",th);
		}
		return;
	}	
	
}
