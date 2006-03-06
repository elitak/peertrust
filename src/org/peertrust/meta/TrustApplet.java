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
package org.peertrust.meta;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.io.* ;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.peertrust.*;
import org.peertrust.inference.*;
import org.peertrust.inference.prolog.minerva.MinervaProlog;
import org.peertrust.net.*;
import org.peertrust.strategy.*;

import net.jxta.edutella.util.Configurator ;

/**
 * <p>
 * 
 * </p><p>
 * $Id: TrustApplet.java,v 1.5 2006/03/06 12:47:59 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2006/03/06 12:47:59 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class TrustApplet extends JApplet
{
	MetaInterpreter metaI ;
	 JTextField queryText = new JTextField("") ;
	JTextArea textField = new JTextArea(); 
	JButton initButton = new JButton("Init negotiation") ;
	JScrollPane scrollPane = new JScrollPane();
	private Console console = null ;
	Configurator config ;
	private String session = "" ;
	
	private static Logger log = Logger.getLogger(Tree.class);
	
	public void init() {
		//java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		consoleRedirection() ;

		this.getContentPane().setLayout(new BorderLayout());
		//textField.setSize(65,500) ;
		textField.setEditable(false) ;
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		this.getContentPane().add(queryText,BorderLayout.NORTH) ;
		scrollPane.getViewport().add(textField,null) ;
		initButton.addMouseListener(new applet_initButton_mouseAdapter(this));
		this.getContentPane().add(initButton,BorderLayout.SOUTH) ;
	
		try {
			config = new Configurator(System.getProperty("user.home") + "/trust/trust.properties",new String[0]);
			config.setAppInfo("Automated Trust Negotiation Peer");

			MinervaProlog engine = new MinervaProlog(this, config) ;
			config.register(engine) ;
			// register objects which need configuration
			metaI = new MetaInterpreter (new FIFOQueue(), engine, config) ;
			config.register(metaI) ;
			// configure objects
			config.finishConfig();
		} catch (Exception e) {
			System.err.println("Applet: ERROR exception " + e.getMessage()) ;
		}
		//metaI.run() ;
	}

	void initButton_mouseClicked(MouseEvent e) {
		String message ;
		
		String query = queryText.getText() ;
		int index = query.lastIndexOf(')') ;
		query = query.substring(0, index) + ",Session" + query.substring(index, query.length()) ;
		
		message = "Starting a new request: " + query ;
		log.debug("Requester: " + message) ;
		System.out.println(message) ;
		
		TrustClient client = new TrustClient(query, new Peer("alice","localhost", 32000),config) ;
		client.run() ;
		String solution = client.getSolution() ;
		if (solution.equals(""))
			session = "" ;
		else
		{ 
			int newIndex = solution.lastIndexOf(')') ;
			session = solution.substring(index+1,newIndex) ;
		}
		
		//TrustClient client = new TrustClient("document(project7,V15595312)", new Peer("company7","localhost", 37000)) ;
		//TrustClient client = new TrustClient("employee(alice7,V32048085)@V32048085@alice7", new Peer("company7","localhost", 37000)) ;
		//TrustClient client = new TrustClient("constraint(peerName(alice7))", new Peer("alice7","localhost", 32000)) ;
	  }
	  
//	public void start() {
//		
//	}
//
//	public void stop() {
//		//metaI.stop() ;
//		log.debug ("calling stop") ;
//	}

	public void destroy() {
		//log.debug("calling destroy") ;
		metaI.stop() ;
	}
	
	void consoleRedirection()
	{
		console = new Console(textField) ;
	}
	
	public String getSession()
	{
		return session ;
	}
}

class applet_initButton_mouseAdapter extends java.awt.event.MouseAdapter {
  	TrustApplet adaptee;

 	 applet_initButton_mouseAdapter(TrustApplet adaptee) {
		this.adaptee = adaptee;
  	}
  	public void mouseClicked(MouseEvent e) {
		adaptee.initButton_mouseClicked(e);
  	}
}

class Console implements Runnable
{
	private JTextArea textArea;
	private Thread reader;
//	private Thread reader2;
	private boolean quit;
	
	private final PipedInputStream pin=new PipedInputStream();
//	private final PipedInputStream pin2=new PipedInputStream();

	public Console(JTextArea text)
	{
		textArea=text;

		try
		{
			PipedOutputStream pout=new PipedOutputStream(this.pin);
			System.setOut(new PrintStream(pout,true));
		}
		catch (java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDOUT to this console\n"+io.getMessage());
		}
		catch (SecurityException se)
		{
			textArea.append("Couldn't redirect STDOUT to this console\n"+se.getMessage());
		}
	
//		try
//		{
//			PipedOutputStream pout2=new PipedOutputStream(this.pin2);
//			System.setErr(new PrintStream(pout2,true));
//		}
//		catch (java.io.IOException io)
//		{
//			textArea.append("Couldn't redirect STDERR to this console\n"+io.getMessage());
//		}
//		catch (SecurityException se)
//		{
//			textArea.append("Couldn't redirect STDERR to this console\n"+se.getMessage());
//		}
	
		quit=false; // signals the Threads that they should exit
	
		// Starting two separate threads to read from the PipedInputStreams
		//
		reader=new Thread(this);
		reader.setDaemon(true);
		reader.start();
	
//		reader2=new Thread(this);
//		reader2.setDaemon(true);
//		reader2.start();
	}

	public synchronized void run()
	{
		try
		{
			while (Thread.currentThread()==reader)
			{
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (pin.available()!=0)
				{
					String input=this.readLine(pin);
					textArea.append(input);
				}
				if (quit) return;
			}

//141:			while (Thread.currentThread()==reader2)
//142:			{
//143:				try { this.wait(100);}catch(InterruptedException ie) {}
//144:				if (pin2.available()!=0)
//145:				{
//146:					String input=this.readLine(pin2);
//147:					textArea.append(input);
//148:				}
//149:				if (quit) return;
//150:			}
		} catch (Exception e)
		{
		textArea.append("\nConsole reports an Internal error.");
		textArea.append("The error is: "+e);
		}
	}

	public synchronized String readLine(PipedInputStream in) throws IOException
	{
		String input="";
		do
		{
			int available=in.available();
			if (available==0) break;
			byte b[]=new byte[available];
			in.read(b);
			input=input+new String(b,0,b.length);
		}while( !input.endsWith("\n") &&  !input.endsWith("\r\n") && !quit);
		return input;
	}

}