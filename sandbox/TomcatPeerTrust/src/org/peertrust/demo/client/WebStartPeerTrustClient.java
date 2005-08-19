/*
 * Created on 13.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.jnlp.*; 

import org.apache.log4j.Logger;
import org.peertrust.PTEngine;
import org.peertrust.demo.client.applet.FinalResponse;
import org.peertrust.demo.client.applet.StartPeerTrustNegoCmd;
import org.peertrust.demo.common.ConfigurationOption;
import org.peertrust.demo.common.NewsEvent;
import org.peertrust.demo.common.NewsEventListener;
import org.peertrust.meta.Trace;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;

import sun.net.www.http.HttpClient;

import java.util.*;
import java.net.*;
import java.io.*;

/**
 * @author kbs
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WebStartPeerTrustClient extends JFrame {
	//private PTEventListener oldPTEventListener= null;
	private String[] parameters;
	private String sessionID;
	private String resourceID;
	
	private String peerIP;
	private int peerPort;
	private URL resourceReqURL=null;
	
	private TextArea textArea= new TextArea();
	private JButton negotiationButton= new JButton("Negotiate");
	
	static public String CONFIG_MUFFIN_URL="PeerTrustConfig/ConfigMuffin.properties";
	static public int CONFIG_FILE_MAX_SIZE=1024*1024;
	//peer info
	private PropertyResourceBundle paramRes=null;
	private PropertyResourceBundle trustProperties=null;
	//web start helper classe
	private BasicService bs=null;
	private PersistenceService ps=null;
	private FileOpenService fos=null;
	private String allParams;
	
	private boolean finalResultNotshown=false;
	
	private NewsEventListener newsListener=null;
	
	private StringBuffer strBuffer= new StringBuffer(512);
	private long queryID;
	
	private PTEngine engine=null;
	//private 
	private Logger logger;
	
	PeerTrustClient ptClient=null;
	
	
    public WebStartPeerTrustClient(String[] params){    	
    	
    	initWebStartHelperClasses();  
    	   	     
    	configLayout();
    	configEventListening();
    	try {
			//tryCommunicationFrameWork();
			ptClient = new PeerTrustClient(params,newsListener,null);
		} catch (Exception e) {
			e.printStackTrace();ptClient=null;
		}
    	//startTrustNegotiation();
        return;
    }
    
    private void initWebStartHelperClasses(){
    	try {
            // Lookup the javax.jnlp.BasicService object
            bs 	= 	(BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
            ps	=	(PersistenceService)ServiceManager.lookup("javax.jnlp.PersistenceService");
            fos	=	(FileOpenService)ServiceManager.lookup("javax.jnlp.FileOpenService");

            logger= 
            	ConfigurationOption.getLogger(
            			WebStartPeerTrustClient.class.getName());
            return ;
        } catch(UnavailableServiceException ue) {
            showMessage(ue.getMessage());
        	return ;
        }
        
    }
    ServerSocket serverSocket=null;
    Socket socket=null;
    ObjectOutputStream objStreamToApplet=null;
    Thread rcvWorkerThread=null;
    
    private void makeAppletComWorker(){
    	
		Runnable rcvWorker=
			new Runnable(){
				public void run() {
					try {
						showMessage("STREAMS MAKING!!!!");
						serverSocket= 
							new ServerSocket(8899);
						socket= serverSocket.accept();
			    		//socket=new Socket("127.0.0.1",8899);
			    		ObjectInputStream objIn=
							new ObjectInputStream(socket.getInputStream());	
			    		
						objStreamToApplet=
							new ObjectOutputStream(socket.getOutputStream());
						
						
						showMessage("STREAMS READY!!!!");
						Object obj=null;
						while(true){
							try {
								obj=objIn.readObject();
							} catch (Throwable e1) {
								e1.printStackTrace();
							}
							showMessage("\nrcvObject:::"+obj);
							if(obj instanceof StartPeerTrustNegoCmd){
								if(ptClient!=null){
									ptClient.sendQuery((StartPeerTrustNegoCmd)obj);
								}
								
							}else if(obj instanceof String){
								if(obj.equals("SET_UP")){
									objStreamToApplet.writeObject("SET_UP");
								}
							}
						}
					} catch (Exception e) {
						showMessage(e);
					}
				}
			
			};
			showMessage("\nRUNNABLE CREATED");
			rcvWorkerThread=
				new Thread(rcvWorker);
			rcvWorkerThread.start();
			showMessage("\nWORKERTHREAD STARED"+rcvWorkerThread.isAlive());
    }
    
    private void getRemotePeerParameter(){
    	try {
			HttpClient httpClient= HttpClient.New(new URL("http://127.0.0.1:7703/myapp-0.1-dev/jsp/parameter.jsp"));
			String content=httpClient.getURLFile();
			
			textArea.append("\ngetUrlFile:"+content);
//			HttpURLConnection httpConnection=
//				new HttpURLConnection();
			URL url = new URL("http://127.0.0.1:7703/myapp-0.1-dev/jsp/parameter.jsp");
			//FileOutputStream fos = new FileOutputStream(file);

			byte [] bytes = new byte [4096];

			InputStream stream = url.openStream();
//			ByteArrayInputStream byteStream= 
//				new ByteArrayInputStream(url.openStream());
			stream.read(bytes);
			String str= new String(bytes);
			System.out.println("\nstr:\n"+str);
					
//			while ((read = is.read(bytes)) != -1) {
//			    //fos.write(bytes, 0, read);
//			}
//
//			is.close();
			
			
			return;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    
    
    private void showMessage(String mes){
    	textArea.append("\n......................................................................\n");
    	textArea.append(mes);
    	return;
    }
    
    private void showMessage(Throwable th){
    	StringWriter pStream= 
    				new StringWriter();
    	th.printStackTrace(new PrintWriter(pStream));
    	textArea.append("\n......................................................................\n");
    	textArea.append(pStream.getBuffer().toString());
    	return;
    }
    
    private void configLayout(){
        Container pane=this.getContentPane();
        pane.setLayout(new BorderLayout());
        this.setSize(500,500);
        pane.add(negotiationButton,BorderLayout.SOUTH);
        pane.add(textArea, BorderLayout.CENTER);
        this.validate();
        this.setVisible(true);
        return;
    }
    
    private void configEventListening(){
    	//window closing
    	WindowAdapter winAdapter= new WindowAdapter(){
    		public void windowClosed(WindowEvent e){
    			System.out.println("closing the window!");
    			ptClient.destroy();
    			System.exit(0);
    		}
    		    		 
    	};
    	this.addWindowListener(winAdapter);
    	//nego button
    	ActionListener aListener= new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				//startTrustNegotiation();
				//boolean started=showRequestURL();
				//showMessage("STARTED:"+started+"\n");
				//installPeerTrustConfigFiles();
				showMessage("STATUS:"+rcvWorkerThread.isAlive());
			}
    		
    	};
    	negotiationButton.addActionListener(aListener);
    	////////////////////////
    	newsListener=
    		new NewsEventListener(){

				public void onNews(NewsEvent newsEvents) {
					
					showMessage(newsEvents.getNews());
				}

				public void onNews(FinalResponse finalResponse) {
					System.exit(0);
				}
				
    		
    		};
		
    	
    }
    
        
    public boolean showRequestURL(URL resourceToShow) {
        try {
        	//bs.
        	return bs.showDocument(resourceToShow);
        } catch(Throwable th) {
        	showMessage(th.getMessage());
            return false;
        }
     }
    
    private void startTrustNegotiation(){
    	showMessage("........nego........");
    	ptClient.negotiate();
    }
    
    
    private void tryCommunicationFrameWork(){
    	ClientSideHTTPCommunicationFactory factory=
    		new ClientSideHTTPCommunicationFactory();
    	factory.setServerIP("127.0.0.1");
    	factory.setServerPort(7703);
    	factory.setWebAppURLPath(
    				"/myapp-0.1-dev/PeerTrustCommunicationServlet");
    	ClientSideNetClient netClient=
    		(ClientSideNetClient)factory.createNetClient();
    	netClient.addNewsListener(newsListener);
    	ClientSideNetServer netServer=
    		(ClientSideNetServer)factory.createNetServer();
    	
    	netClient.send(
    			new Query(	"dada",
    						new Peer("client","127.0.0.1",5252),
    						new Peer("server","127.0.0.1",7703),
							7703,
							new Trace()),
				new Peer("server","127.0.0.1",7703));
    	
    	netServer.addNewsListener(newsListener);
    	netServer.listen();
    	return;
    }
    
    public void installPeerTrustConfigFiles(){
    	return;
    }
    
    public static void main(String[] args) {
        WebStartPeerTrustClient client= new WebStartPeerTrustClient(args);  
        
        client.startTrustNegotiation();
        
        
        return;
    }
}
