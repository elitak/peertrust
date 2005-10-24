package org.peertrust.demo.client.applet;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
//import java.net.Socket;
import java.net.URL;
//import java.util.ArrayList;
import java.util.Arrays;
//import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import netscape.javascript.JSObject;

//import org.peertrust.config.PTConfigurator;
import org.jgraph.JGraph;
import org.peertrust.TrustClient;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.client.PeerTrustClient;
import org.peertrust.demo.common.ClientConstants;
import org.peertrust.demo.common.EchoPane;
import org.peertrust.demo.common.Executable;
import org.peertrust.demo.common.Utils;
import org.peertrust.demo.common.InstallationSession;
import org.peertrust.demo.common.NewsEvent;
import org.peertrust.demo.common.NewsEventListener;
import org.peertrust.demo.common.StopCmd;
import org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASPObject;
import org.peertrust.demo.session_registration.HttpSessionRegistrationRequest;
import org.peertrust.demo.session_registration.HttpSessionRegistrationResponse;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEventListener;

import org.peertrust.net.Message;
import org.peertrust.net.Peer;
import org.peertrust.tnviz.app.Graphics;
import org.peertrust.tnviz.app.TNGraphics;
import org.peertrust.tnviz.app.TNVizListener;
import org.peertrust.tnviz.gui.TNGui;

import org.xml.sax.SAXException;


/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DemoApplet extends JApplet implements NewsEventListener{
	////////////////////////////////////////////////////////////////////////////////////////////
	class HttpSessionRegistrant implements PTComASPMessageListener{
			private String protectedUrl=null;
			public void PTMessageReceived(Serializable payload,Peer source, Peer target) {
				System.out.println("RegistrationResponse:"+payload);
				if(((HttpSessionRegistrationResponse)payload).isAcknowledgment()){
					if(protectedUrl!=null){//registration was caused by a protected page
						try {
							URL url= new URL(protectedUrl);
							DemoApplet.this.getAppletContext().showDocument(url,"display_frame");
							protectedUrl=null;
						} catch (MalformedURLException e) {
							protectedUrl=null;
							e.printStackTrace();
						}
						//stop progress bar or alert user
					}else{
						//stop progress bar or alert user
					}
				}
				
			}
			/**
			 * 
			 * @param sessionKey
			 * @param protectedURL
			 */
			public void registerSession(String sessionKey,String protectedURL){
				echoPane.echo("Registering session:"+sessionKey);
				HttpSessionRegistrationRequest req=
					new HttpSessionRegistrationRequest(
						sessionKey,
						ptClient.getLocalPeer(),
						ptClient.getServerPeer());
//				
//				workerFIFO.offer( 
//						new HttpSessionRegistrationRequest(
//									sessionKey,
//									ptClient.getLocalPeer(),
//									ptClient.getServerPeer()));
				Message mes=
					new PTCommunicationASPObject(ptClient.getLocalPeer(),ptClient.getServerPeer(),req);
				ptClient.sendToHttpServer(mes);
				this.protectedUrl=protectedURL;
				return;
			}
			
		};
	////////////////////////////////////////////////////////////////////////////////////////////
	public static String PEERTRUST_FOLDER_NAME=".peertrust";
	//private Socket socket=null;
	//////////////////////////////////////////////private ObjectOutputStream objOut=null;
	//private ObjectInputStream  objIn=null;
	private Thread cmdWorker=null;
	private PeerTrustClient ptClient=null;
	private JSObject win;
	
	//transient private long key=0;
	
	private String trustScheme="User Password";
	
	private EchoPane echoPane=new EchoPane();
	
	private InstallationSession installationSession;
	
	private StringBuffer strBuffer= new StringBuffer(512);
	
	private HttpSessionRegistrant httpSessionRegistrant;
	
	private Executable userAlert;
	
	private TestTreeDiagramm ttDiag= new TestTreeDiagramm();
	//private TNVizListener tnVizListener;
	//private TNGraphics tnGrapnhics;
	
	private NegotiationVisualizationPane negotiationVisualizationPane=
											new NegotiationVisualizationPane();
	private NegotiationVisualization negotiationVisualization;
	private TestNegotiationVisualizationPane testVisPane=
				new TestNegotiationVisualizationPane();
	private Component visibleComponent; 
	
	private void showURLInDisplayFrame(URL url){
		echoPane.echo("showing:"+url);
		getAppletContext().showDocument(url,"display_frame");
	}
	
	private Executable makeUserAlert(){
		return new Executable(){

			public void execute(Object param) {
				jsAlert((String)param);
			}
			
		};
	}
	
	private TNGraphics makeTNGraphics(){
		TNGraphics g= new TNGraphics();
		g.getGui().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		g.getGui().setVisible(false);
		return g;
	}
	

	private NegotiationVisualization makeNegotiationVisualization(){
		NegotiationVisualization nv=
				new NegotiationVisualization();
		EventDispatcher ed=
			(EventDispatcher)ptClient.getTrustClient().getComponent(Vocabulary.EventDispatcher);
		nv.setEventDispatcher(ed);
		return nv;
	}
	
//	private NegotiationVisualizationPane makeNegotiationVisualizationPane(){
//		NegotiationVisualizationPane nvp=
//				new NegotiationVisualizationPane();
//		return nvp;
//	}
	
	
	
	public URL getServiveURL( String resource, String userName, String password){
		
		try {
			
			strBuffer.delete(0,strBuffer.length());
			strBuffer.append(".");
			//strBuffer.append(getParameter("appContext"));//TODO make them local vars
			strBuffer.append(getParameter("serviceServletPath"));				
			
			strBuffer.append("?userName=");				
			strBuffer.append(userName);
			
			strBuffer.append("&password=");				
			strBuffer.append(password);
			
			strBuffer.append("&negoResource=");				
			strBuffer.append(resource);

			URL resourceReqURL= new URL(this.getCodeBase(),strBuffer.toString());
			return resourceReqURL;
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	
	public void getResource(String res){
		echoPane.echo("GETTING RESOURCE:"+res +" scheme:"+trustScheme);
		echoPane.echo("\ngetting:"+res);
		if(trustScheme==null){
			jsAlert("Please choose a trust scheme!");
		}else if(trustScheme.equalsIgnoreCase("PeerTrust")){
			if(ptClient==null){
				jsAlert("ptClient not available!");
			}
			String queryGoal="request("+res+",Session)@eLearn";// @ eLearn";
			try {
				//ptClient.negotiate(queryGoal);
				StartPeerTrustNegoCmd negoCmd=
					new StartPeerTrustNegoCmd(res,queryGoal);
				ptClient.addCmdToFIFO(negoCmd);
			} catch (Exception e) {
				e.printStackTrace();
				echoPane.echo("problem while negotiating!",e);
				
			}
		}else if(trustScheme.equalsIgnoreCase("User Password")){
			workerFIFO.offer( new PasswordBasedResourceRequest(res,this));
		}else{
			jsAlert("Sorry "+trustScheme+" is not supported yet");
		}
		return;
	}
	
	public void registerSession(String sessionKey){
//		echoPane.echo("Registering session:"+sessionKey);
//		
//			workerFIFO.offer( new HttpSessionRegistrationRequest(sessionKey,null,null));
		registerSession(sessionKey,null);
		return;
	}
	
	public void registerSession(String sessionKey, String postponedURL){
		System.out.println("Registering session:"+sessionKey+ " causedBy:"+postponedURL);
		echoPane.echo("Registering session:"+sessionKey+ "\ncausedBy:"+postponedURL);
		httpSessionRegistrant.registerSession(sessionKey,postponedURL);
		return;
	}
	
	
	ArrayBlockingQueue workerFIFO = new ArrayBlockingQueue(4);
	//private PTConfigurator configurator;
	class PasswordBasedResourceRequest{
		private String resource;
		private String password=null;
		private String userName=null;
		private DemoApplet requester;
		public PasswordBasedResourceRequest(String res,DemoApplet requester){
			resource=res;
			this.requester=requester;
		}
		
		public void getUserData(){
			LoginDlg loginDlg= new LoginDlg(new JFrame(),"Login");
			if(loginDlg.isOk()){
				if(		loginDlg.getUsername().length()>2&&
						loginDlg.getPassword().length()>2){
					
						userName=loginDlg.getUsername();
						password=loginDlg.getPassword();
				}
			}
		}
		
		public void request(){
			getUserData();
			URL url=requester.getServiveURL(resource,userName,password);
			if(url!=null){
				requester.showURLInDisplayFrame(url);
			}
		}
		
	}
	

	private void makeCmdWorker(){
		Runnable runnable= new Runnable(){

			public void run() {
				
				//objIn= new ObjectInputStream(socket.getInputStream());
				while(true){
					Object obj;
					try {
						obj = workerFIFO.take();	
						if(obj==null){
						
						}if(obj instanceof FinalResponse){
							URL url=
								((FinalResponse)obj).getResourceURL();
							showURLInDisplayFrame(url);
						}else if(obj instanceof PasswordBasedResourceRequest){
							((PasswordBasedResourceRequest)obj).request();
						}else if(obj instanceof HttpSessionRegistrationRequest){
							System.out.println("SessionRegistrationMessage added::::: "+obj);
							if(ptClient!=null){
								ptClient.addCmdToFIFO(obj);
							}else{
								System.out.println("=====>ptClient==null!");
							}
						}else if(obj instanceof StopCmd){
						
							return;
						}else if(obj instanceof Executable){
							((Executable)obj).execute(null);
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
		};
		
		cmdWorker=
			new Thread(runnable);
		cmdWorker.start();
	}
	
	private void createGUI(){
		//////////////////////////gui
		Container c=this.getContentPane(); 
		c.setLayout(new GridLayout(1,1));
		showEchoPane();
		testVisPane.setParentContainer(this.getContentPane());
	}
	
	private void showEchoPane(){
		Container c=this.getContentPane(); 
		c.removeAll();
		visibleComponent=echoPane;
		c.add(visibleComponent);
		c.validate();
		c.repaint();
	}
	
	private void showNegotiationVisualizationPane(){
		//////////////////////////gui
		Container c=this.getContentPane(); 
		c.removeAll();
		negotiationVisualizationPane.getJPanelGuiBasedTNGraphics().wipeGraph();
		//c.add(tnGrapnhics.getGui());
		visibleComponent=negotiationVisualizationPane;
		c.add(visibleComponent);
		c.validate();
		c.repaint();
	}
	
	public void init() {
		super.init();
		win=JSObject.getWindow(this);
		try {
	        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
	            public void run() {
	            	createGUI();	                
	            }
	        });
	    } catch (Exception e) {
	        System.err.println("createGUI didn't successfully complete");
	        e.printStackTrace();
	    }
	    userAlert=makeUserAlert();
	    //tnGrapnhics= makeTNGraphics();
	    //negotiationVisualizationPane= makeNegotiationVisualizationPane();
	    
		return;
	}
		
	
	public void start() {
		super.start();	
		
		//////////////////worker
		makeCmdWorker();		
		
		///////////////////PeerTrust
		startPeerTrustClient();
		
		//////////////////registrant
		makeHttpSessionRegistrant();
		
		registerSession(getParameter("negoSessionID"),null);
		//negotiationVisualization= makeNegotiationVisualization();
		EventDispatcher ed=
			(EventDispatcher)ptClient.getTrustClient().getComponent(Vocabulary.EventDispatcher);
		PTCommunicationASP comASP=
			(PTCommunicationASP)this.ptClient.getTrustClient().getComponent(Vocabulary.EventListener);
		testVisPane.setEventDispatcher(ed);
		return;
	}
	

	public void startPeerTrustClient() {
		
		///////////////////PeerTrust
		//URL url=getDocumentBase();		
		Properties props= new Properties();
		props.setProperty("negoSessionID",getParameter("negoSessionID"));
		props.setProperty("negoResource",getParameter("negoResource"));
		props.setProperty("remotePeerIP", getParameter("remotePeerIP"));
		props.setProperty("appContext",getParameter("appContext"));
		props.setProperty("serviceServletPath",getParameter("serviceServletPath"));
		props.setProperty("webAppURLPath",getParameter("webAppURLPath"));
		props.setProperty("serverPeerName",getParameter("serverPeerName"));
		
		props.setProperty(ClientConstants.CODEBASE_URL_STR_KEY,
						getParameter(ClientConstants.CODEBASE_URL_STR_KEY));
		
		String home= null;
	
		try{
			home=System.getProperty("user.home");
		}catch(Exception e){
			e.printStackTrace();
			jsAlert("Cannot create peertrust client. cause:"+e.getLocalizedMessage());
			return;
		}
		
		File instDir= new File(home,PEERTRUST_FOLDER_NAME);
		URL sourceBase;
		
		try{
			sourceBase= new URL(getCodeBase(),"./PeerTrustConfig/");
		}catch(MalformedURLException e){
			e.printStackTrace();
			jsAlert("Bad remote installation source folder:"+e.getLocalizedMessage());
			return;
		}
		
		try {
			installationSession= 
				new InstallationSession(
								sourceBase.toString(),
								"install.xml",
								instDir.toString());
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			jsAlert("Could not create Installation session:"+e1.getLocalizedMessage());
			return;
		} catch (SAXException e1) {
			e1.printStackTrace();
			jsAlert("Could not create Installation session:"+e1.getLocalizedMessage());
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
			jsAlert("Could not create Installation session:"+e1.getLocalizedMessage());
			return;
		}
		
		if(installationSession.isInstallationUptodate()){
			echoPane.echo(	"\nInstallation is uptodate. "+
							"RemoteVersion:"+installationSession.getRemoteVersion()+
							" LocalVersion:"+installationSession.getRemoteVersion());
			try {
				ptClient= 
					new PeerTrustClient(
							props,
							installationSession.getConfigFileURL(),
							installationSession.getPrologFile(),
							userAlert);
				trustScheme="PeerTrust";
				System.out.println("=========>PTclient created:"+ptClient);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}else{
			echoPane.echo(	"\nInstallation is not uptodate. "+
					"RemoteVersion:"+installationSession.getRemoteVersion()+
					"LocalVersion:"+installationSession.getRemoteVersion());
			
			String question="This demo need to install peetrust file in: \n"+
							instDir+
							"\nYou can savely remove the folder either manualy or using"+
							"<session>/<uninstall peertrust files>\n"+
							"Allow installation?";
			boolean installtionAllowed=
				Utils.askYesNoQuestion("Instalation Dialog",question,this,null);
			if(installtionAllowed){
				try {
					
					echoPane.echo("installing peertrust from:"+sourceBase);
					installationSession.install();
					
					echoPane.echo("file in "+ instDir+
							":"+Arrays.toString(instDir.list())+"\n");
					ptClient= 
						new PeerTrustClient(
								props,
								installationSession.getConfigFileURL(),
								installationSession.getPrologFile(),
								userAlert);//this.configurator);
					trustScheme="PeerTrust";
					System.out.println("=========>PTclient created:"+ptClient);
				}catch (MalformedURLException e){
					e.printStackTrace();echoPane.echo("bad ur",e);clearPTClient();
				} catch (IOException e) {
					e.printStackTrace();echoPane.echo("IO problem!",e);clearPTClient();
				}catch(Throwable th){
					th.printStackTrace();
					echoPane.echo("could not install or initiate pt using http source directely",th);
					clearPTClient();
				}
			}else{
				String info=
					"You choose not to install peertrust files.\n"+
					"Therefore you will not be able to experience any negotiation \n"+
					"and your access will be limited to unprotected content!";
				Utils.inform("Installation Info",info,this);
			}
			
			return;
		}
	}
	
	private void clearPTClient(){
		System.out.println("=======>clearing PTClient!");
		if(ptClient!=null){
			try{
				ptClient.destroy();
			}catch(Throwable th){th.printStackTrace();}
			ptClient=null;
			echoPane.echo("ptClient cleared!");
		}				
		trustScheme="User Password";
	}
	
	public void uninstallPeerTrust(){
		Executable executable= new Executable(){
			public void execute(Object param){
				echoPane.echo("Unstall dialog starting .....");
				String home=null;
				try{
					home=System.getProperty("user.home");
				}catch(Exception e){
					jsAlert("Could not start unintall-Process.\n"+
							"Reason unable to get your home directory\n"+
							"Exception message:"+e.getLocalizedMessage());
					return;
				}
				
				File instDir= new File(home,PEERTRUST_FOLDER_NAME);
				
				String question="Uninsstall will stopt the currently running peertrust engine.\n"+
								"and delete the installation directory:\n\t"+
								instDir+
								"Go ahead and uninstall?";
				boolean res=Utils.askYesNoQuestion(	"Uninstall Dialog",
																question,
																DemoApplet.this,
																new Object[]{"unstall","cancel"});
				if(res){
					workerFIFO.offer(new StopCmd());
					if(ptClient!=null){
						ptClient.destroy();
					}
					try{
						res=instDir.delete();
						if(res){
							Utils.inform("Uninstall Info","Uninstall successfull!",DemoApplet.this.getParent());
						}else{
							Utils.inform("Uninstall Info","could nor delete:"+instDir,DemoApplet.this);
						}
						return;
					}catch(SecurityException e){
						Utils.inform("Uninstall Info",e.getLocalizedMessage(),DemoApplet.this);
						return;
					}			
				}
			}
		};
		workerFIFO.offer(executable);
	}
	
	
 Executable toggleVisExe=
	 new Executable(){
		public void execute(Object param){
			if(negotiationVisualization==null){
				negotiationVisualization= makeNegotiationVisualization();
//				EventDispatcher ed=
//					(EventDispatcher)ptClient.getTrustClient().getComponent(Vocabulary.EventDispatcher);
//				PTCommunicationASP comASP=
//					(PTCommunicationASP)this.ptClient.getTrustClient().getComponent(Vocabulary.EventListener);
//				negotiationVisualization.setEventDispatcher(ed);
			}else{
				negotiationVisualization.toggleVisualization();
			}
				//testVisPane.toggleVisualization();
			}
	};
	
	public void toggleLeftFrameVisualization(){
		testVisPane.toggleVisualization();		
	}
	
	public void toggleExternalVisualization(){
		workerFIFO.offer(toggleVisExe);
		
	}
	
	public void toggleVisualization(){
//		Executable executable= new Executable(){
//			public void execute(Object param){
////				Utils.inform("Info","Visualization not implemented yet!",
////						DemoApplet.this.getParent());
//				if(negotiationVisualization==null){
//					negotiationVisualization= makeNegotiationVisualization();
//				}
//					negotiationVisualization.toggleVisualization();
//					testVisPane.toggleVisualization();
//				}
//		};
		workerFIFO.offer(toggleVisExe);
		
//		EventDispatcher ed=
//			(EventDispatcher)ptClient.getTrustClient().getComponent(Vocabulary.EventDispatcher);
//		PTCommunicationASP comASP=
//			(PTCommunicationASP)this.ptClient.getTrustClient().getComponent(Vocabulary.EventListener);
		
		/*if(visibleComponent==negotiationVisualizationPane){
			//negotiationVisualizationPane.stopListenToPTEvent(ed);				
			showEchoPane();
			comASP.unregisterPTEventListener(negotiationVisualizationPane);
		}else{
			showNegotiationVisualizationPane();
			//negotiationVisualizationPane.startListenToPTEvent(ed);
			comASP.registerPTEventListener(negotiationVisualizationPane);
			negotiationVisualizationPane.getJPanelGuiBasedTNGraphics().setLayout(Graphics.SEQ_LAYOUT);
		}*/
		
//		if(negotiationVisualization==null){
//			negotiationVisualization= makeNegotiationVisualization();
//		}
//		negotiationVisualization.toggleVisualization();
		
//		testVisPane.toggleVisualization();
		/*
		JGraph graph=ttDiag.getGraph();
		if(visibleComponent==graph){
			//negotiationVisualizationPane.stopListenToPTEvent(ed);				
			System.out.println("ttDiag  visible");
			showEchoPane();
			comASP.unregisterPTEventListener(ttDiag.ptListener);
			
		}else{
			//negotiationVisualizationPane.startListenToPTEvent(ed);
			System.out.println("ttDiag not visible");
			Container c=this.getContentPane(); 
			c.removeAll();
			visibleComponent=graph;
			graph.setVisible(true);
			c.add(visibleComponent);
			ttDiag.calculateGraphLayout();
			c.validate();
			c.repaint();
			comASP.registerPTEventListener(ttDiag.ptListener);
		}/**/
	}
	
	
	public void managePolicies(){
		Executable executable= new Executable(){
			public void execute(Object param){
				Utils.inform("Info","Policy Management not available yet!",DemoApplet.this);
			}
		};
		workerFIFO.offer(executable);
	}
	
	public void destroy() {
		System.out.println("Destroying!");
		workerFIFO.offer(new StopCmd());
		if(ptClient!=null){
			ptClient.destroy();
		}
		super.destroy();
	}
	
	
	public void onNews(NewsEvent newsEvents) {
		echoPane.echo(newsEvents.getNews());
		return;
	}
	
	
	
	public void onNews(FinalResponse fr){
		URL url=fr.getResourceURL();
		showURLInDisplayFrame(url);
		return;
	}
	
	public void setTrustScheme(String scheme){
		
		if((ptClient==null) && scheme.equals("PeerTrust")){
			jsAlert("PeerTrust client not available.\n You must use password");
			return;
		}else{
			this.trustScheme=scheme;
			echoPane.echo("SET SCHEME="+scheme);
			return;
		}
	}
	
//	public void stop() {
//		destroy();
//		super.stop();
//	}
	
	private void jsAlert(String text){
		//win.eval("alert('"+text+"'))");
		win.call("alert", new Object[]{text});		
		return;
	}
	
	
	
	public String getTrustScheme() {
		return trustScheme;
	}
	
	public void makeHttpSessionRegistrant(){
		httpSessionRegistrant= 
						new HttpSessionRegistrant();		
		
		
		if(ptClient==null){
			return;
		}else{
			TrustClient trustClient=ptClient.getTrustClient();
			if(trustClient==null){
				throw 
					new RuntimeException("TrustClient not started therefore Registerer cannot be registered\n"+
									"to listen to HttpSessionRegistrationRequest");
			}else{
				PTEventListener eventL=
					(PTEventListener)trustClient.getComponent(Vocabulary.EventListener);
				if(eventL instanceof PTCommunicationASP){
					((PTCommunicationASP)eventL).registerPTComASPMessageListener(
															httpSessionRegistrant,
															HttpSessionRegistrationRequest.class);
					
				}else{
					throw new Error("Cannot  register HttpRegisterer since pt event listener is\n"+
									"not a PTCommunicationASP but a "+
									((eventL==null)?null:eventL.getClass()) );
				}
			}
		}
	}
	
	public void requestCredential(String credentialName){
		System.out.println("request credential:"+credentialName);
		ptClient.requestCredential(credentialName);
	}
}
