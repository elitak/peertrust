package org.peertrust.demo.client.applet;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import netscape.javascript.JSObject;
import org.peertrust.TrustClient;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.client.PeerTrustClient;
import org.peertrust.demo.common.PTDemoConstants;
import org.peertrust.demo.common.EchoPane;
import org.peertrust.demo.common.Executable;
import org.peertrust.demo.common.UninstallPeerTrust;
import org.peertrust.demo.common.Utils;
import org.peertrust.demo.common.InstallationSession;

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

import org.xml.sax.SAXException;


/**
 *The DemoApplet extends the browser with peertrust negotiation capability.
 *Its PeerTrustClient is reponsible for the trust negotiation.
 *DemoApplet also provides visualization mechanisms to show the negotiation
 *process. This is done either embedded in the applet with a 
 *TestNegotiationVisualizationPane or in a full feature manner with the external 
 *Visualization using  NegotiationVisualization.
 *The connection to the http peer ist not staticaly specified. 
 *Therefore, a mean must exist to reveal the browser peer identity and 
 *location to the hhtp peer. This mechanism is provided by the 
 *HttpSessionRegistrant.
 *Mechanism to install, uninstall file require for the peertrust engine are
 *also provided 
 *   
 *@author Congo Patrice(token77)
 *@see org.peertrust.demo.client.applet.NegotiationVisualization
 *@see org.peertrust.demo.client.TestNegotiationVisualizationPane
 *@see org.peertrust.demo.client.PeerTrustClient
 *@see org.peertrust.demo.common.InstallationSession
 */
public class DemoApplet extends JApplet 
						implements PTDemoConstants
{
	////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 *The connection to the http peer ist not staticaly specified. 
	 *Therefore, HttpSessionRegistrant provide a mechnism to reveal the 
	 *browser peer identity and location to the hhtp peer. 
	 *@see org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener
	 */
	class HttpSessionRegistrant implements PTComASPMessageListener
	{
			/** hold the session key of the last registered session*/
			String lastSessionKey=null;
			
			/** denotes the hhtp url of the resource which because of it
			 * is protected require trust negotiation and therefore a
			 * registration of the browser peer.
			 */
			private String protectedUrl=null;
			
			/**
			 * @see org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener#PTMessageReceived(Serializable, Peer, Peer) 
			 */
			public void PTMessageReceived(	Serializable payload,
											Peer source, 
											Peer target) 
			{
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
			 *To register an http session for negotiation trust. 
			 * @param sessionKey -- the hhtp session key
			 * @param protectedURL -- the url of the resource which request
			 * 						triggers a trust negotiation and subsequently
			 * 						a session registration.
			 */
			public void registerSession(String sessionKey,
										String protectedURL)
			{
				echoPane.echo("Registering session:"+sessionKey);
				HttpSessionRegistrationRequest req=
					new HttpSessionRegistrationRequest(
						sessionKey/*,
						ptClient.getLocalPeer(),
						ptClient.getServerPeer()*/);
				Message mes=
					new PTCommunicationASPObject(ptClient.getLocalPeer(),ptClient.getServerPeer(),req);
				ptClient.sendToHttpServer(mes);
				echoPane.echo("serverPeer:"+ptClient.getServerPeer());
				this.protectedUrl=protectedURL;
				this.lastSessionKey=sessionKey;
				return;
			}
			
			public void unregisterLastSession()
			{
				if(lastSessionKey!=null){
					echoPane.echo("Unregistering session:"+lastSessionKey);
					HttpSessionRegistrationRequest req=
								new HttpSessionRegistrationRequest(
															lastSessionKey);
					req.setRegistrationCmd(
							HttpSessionRegistrationRequest.REMOVE_REGISTRATION);
					
					Message mes=
						new PTCommunicationASPObject(
										ptClient.getLocalPeer(),
										ptClient.getServerPeer(),
										req);
					ptClient.sendToHttpServer(mes);
					
					this.protectedUrl=null;					
					lastSessionKey=null;
				}
				return;
			}
			
			
		};
	////////////////////////////////////////////////////////////////////////////////////////////
		/** The folder name which holds peertrust files*/
		public static final String PEERTRUST_FOLDER_NAME=".peertrust";
		
		/** a thread which executes queued executable cmd*/
		private Thread cmdWorker=null;
		
		/** the peertrust client*/
		private PeerTrustClient ptClient=null;
		
		/** Represents the browser window, provide java script functionality
		 * like alert
		 */
		private JSObject win;
		
		private String trustScheme="User Password";
		
		/**
		 * pane use to echo  info for the user.
		 */
		private EchoPane echoPane=new EchoPane();
		
		/**An installationSession checks the install peertrust files and 
		 * install them if necessary 
		 */
		private InstallationSession installationSession;
		
		/** work buffer*/
		private StringBuffer strBuffer= new StringBuffer(512);
		
		/** An HttpSessionRegistrant used to register the http session
		 * for peertrust negotiation
		 */
		private HttpSessionRegistrant httpSessionRegistrant;
	
		/** 
		 * provide alert, user noctification functionatlity packed 
		 * as an executable. 
		 * The message to show is passed parameter to the execute
		 * method 
		 */
		private Executable userAlert;
		
		/**
		 * An executable containing the task to execute after
		 * the peertrust client has been restarted
		 */
		private Executable todoAfterPeerTrustRestart=
								makeTodoAfterPeerTrustRestart(); 
	
		/**
		 * A wrapper of the TNViz component for external visualizatio of
		 * the nogotiation process.
		 */
		private NegotiationVisualization negotiationVisualization;
	
		/**
		 *A viualization pane of negotiation process which kann be
		 *shown on the applet client pane. 
		 */
		private TestNegotiationVisualizationPane testVisPane=
								new TestNegotiationVisualizationPane();
		/** hold the currently vissible component*/
		private Component visibleComponent; 
		
		/**
		 * Get browser to show an url in the display frame.
		 * @param url -- theurl to show
		 */
		private void showURLInDisplayFrame(URL url){
			echoPane.echo("showing:"+url);
			getAppletContext().showDocument(url,"display_frame");
		}
	
		/**
		 * create a user alert executable.
		 * It basicaly wrapps the funtionaly of the jsAlert
		 * in an executable. The text to show is passed as parameter
		 * to the executable execute method 
		 * @return a user allert executable
		 */
		private Executable makeUserAlert(){
			return new Executable(){
	
				public void execute(Object param) {
					jsAlert((String)param);
				}
				
			};
		}
	
		/**
		 * Wraps actions to do after a peertrust client restart in an
		 * Executable. It reconnect the visualization object with the
		 * peertrust event dispatcher.
		 * 
		 * @return an exevutable, which actions to do after peertrust
		 * 			restart.
		 */
		private Executable makeTodoAfterPeerTrustRestart()
		{
			return new Executable(){
	
				public void execute(Object param) {
					System.out.println("makeTodoAfterPeerTrustRestart()");
					EventDispatcher ed=
						(EventDispatcher)ptClient.getTrustClient().getComponent(Vocabulary.EventDispatcher);
					if(negotiationVisualization!=null){
						negotiationVisualization.setEventDispatcher(ed);					
					}
					if(testVisPane!=null){
						testVisPane.setEventDispatcher(ed);
					}
				}
				
			};
		}
	

		/**
		 * Creates a NegotiationVisualization and connects it to the
		 * peertrust event dispatcher.
		 * @return the created NegotiationVisualization object
		 */	
		private NegotiationVisualization makeNegotiationVisualization()
		{
			NegotiationVisualization nv=
					new NegotiationVisualization();
			EventDispatcher ed=
				(EventDispatcher)ptClient.getTrustClient().getComponent(Vocabulary.EventDispatcher);
			nv.setEventDispatcher(ed);
			return nv;
		}
		
		/**
		 * Builds the full resource url with user name and password.
		 * And Requests the resource 
		 * @param resource
		 * @param userName
		 * @param password
		 * @return
		 */
		public URL getServiveURL( 	
								String resource, 
								String userName, 
								String password)
		{
			
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
		
	
	/**
	 * To register a session key
	 * @param sessionKey -- the session key to register
	 */
	public void registerSession(String sessionKey)
	{
		registerSession(sessionKey,null);
		return;
	}
	
	/**
	 * To register a session key assotiated with the triggering url to
	 * allow lated automatic request of the corresponding resource once
	 * registration was successfull.
	 * @param sessionKey
	 * @param postponedURL
	 */
	public void registerSession(String sessionKey, String postponedURL)
	{
		System.out.println("Registering session:"+sessionKey+ " causedBy:"+postponedURL);
		echoPane.echo("Registering session:"+sessionKey+ "\ncausedBy:"+postponedURL);
		httpSessionRegistrant.registerSession(sessionKey,postponedURL);
		return;
	}
	
	/** 
	 * Communication fifo for the applet worker thread.
	 */
	ArrayBlockingQueue workerFIFO = new ArrayBlockingQueue(4);
	
	/**
	 *This Class provides mechanism to:
	 *<ul>
	 *<li/>get user login info <code>LoginDlg</code>
	 *<li/>get the protected url using <code>DemoApplet#showURLInDisplayFrame</code>
	 *</ul> 
	 * @author Patrice Congo (token77)
	 *
	 */
	class PasswordBasedResourceRequest
	{
		/** the url as string of the password protected resource.*/
		private String resource;
		
		/** the password of the user*/
		private String password=null;
		
		/** the user name*/
		private String userName=null;
		
		/** the DemoApplet which is doing the request*/
		private DemoApplet requester;
		
		/**
		 * Creates a PasswordBasedResourcceRequest.
		 * @param res -- the url of the protected resource.
		 * @param requester -- the requesting DemoApplet
		 */
		public PasswordBasedResourceRequest(
									String res,
									DemoApplet requester)
		{
			resource=res;
			this.requester=requester;
		}
		
		/**
		 * Gets the user password authentication data (user name and password.
		 * Done using LoginDlg.
		 */
		public void getUserData()
		{
			LoginDlg loginDlg= new LoginDlg(new JFrame(),"Login");
			if(loginDlg.isOk()){
				if(		loginDlg.getUsername().length()>2&&
						loginDlg.getPassword().length()>2){
					
						userName=loginDlg.getUsername();
						password=loginDlg.getPassword();
				}
			}
		}
		
		/**
		 * Do request the protected resource.
		 */
		public void request(){
			getUserData();
			URL url=requester.getServiveURL(resource,userName,password);
			if(url!=null){
				requester.showURLInDisplayFrame(url);
			}
		}
		
	}
	
	/**
	 * This method creates and and start the worker thread.
	 * This worker wait for executables, messages, command ... 
	 * using the workerFIFO and interpretes the receive objects
	 * and execute require actions. E.g uninstall is done by wraping 
	 * the uninstall code in an executale and adding it to the
	 * wokerfifo; the worker than receive it and invoque execute. 
	 * This work around is necessary because firefox complains with
	 * security access exception when the corresponding method is
	 * directly call using javascript.  
	 *
	 */
	private void makeCmdWorker(){
		Runnable runnable= new Runnable(){

			public void run() {
				
				//objIn= new ObjectInputStream(socket.getInputStream());
				while(true){
					Object obj;
					try {
						System.out.println("\nworkerWaiting");
						obj = workerFIFO.take();	
						System.out.println("\nworker got obj:"+obj);
						if(obj==null){
						
//						}if(obj instanceof FinalResponse){
//							URL url=
//								((FinalResponse)obj).getResourceURL();
//							showURLInDisplayFrame(url);
						}else if(obj instanceof PasswordBasedResourceRequest){
							((PasswordBasedResourceRequest)obj).request();
						}else if(obj instanceof StopCmd){
							System.out.println("\n=========>stopping worker\b");
							return;
						}else if(obj instanceof Executable){
							((Executable)obj).execute(null);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				
			}
			
		};
		
		cmdWorker=
			new Thread(runnable);
		cmdWorker.start();
	}
	/**
	 * Makes the initial layout of the applet.
	 * The echo pane is then shown by default. 
	 */
	private void createGUI(){
		//////////////////////////gui
		Container c=this.getContentPane(); 
		c.setLayout(new GridLayout(1,1));
		showEchoPane();
		testVisPane.setParentContainer(this.getContentPane());
	}
	
	/**
	 * Shows the EchoPane in the applet content pane.
	 */
	private void showEchoPane(){
		Container c=this.getContentPane(); 
		c.removeAll();
		visibleComponent=echoPane;
		c.add(visibleComponent);
		echoPane.invalidate();
		echoPane.setVisible(true);
		c.validate();
		Rectangle r=c.getBounds();
		c.repaint((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());		
	}
	
	/**
	 * Overridden to:<br/> 
	 * <ul>
	 * <li/>init members win and userAlert, 
	 * <li/>and to create the applet initial layout.
	 * </ul>
	 * @see java.awt.applet.Applet#init()
	 */
	public void init() {
		super.init();
		win=JSObject.getWindow(this);	    
	    createGUI();
	    userAlert=makeUserAlert();
		return;
	}
	/**
	 * This method creates an Executable which wraps the code 
	 * necessary to:
	 * <ul>
	 *		<li/>wait for the echopane to be visible befre starting 
	 *			anyother action. otherwise some dialog may pop while
	 *			while the applet jars are being download. 
	 *			This reaveal to be weird from the user point of view.
	 * 		<li/>start the peertrust client 
	 * 		<li/>and finish other initializations which depends on the state of the peertrust client.
	 * </ul>
	 * @return
	 */
	private Executable makePeerTrustInitTask(){
		Executable exe= new Executable(){

			public void execute(Object param) {
				//wait for the echo pane to becaome visible
				long startTime=System.currentTimeMillis();				
				loop_time_out:while(!echoPane.isVisible()){
					if((System.currentTimeMillis()-startTime)>1000*60){
						break loop_time_out;
					}else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
				
				//start peertrust
				startPeerTrustClient();
				ptClient.setTodoAfterPeerTrustRestart(todoAfterPeerTrustRestart);
				
				//htt session registrant
				makeHttpSessionRegistrant();
				
				//register session
				registerSession(getParameter("negoSessionID"),null);
				
				//connect visialization tools to peertrust event dispatcher
				EventDispatcher ed=
					(EventDispatcher)ptClient.getTrustClient().getComponent(Vocabulary.EventDispatcher);
				testVisPane.setEventDispatcher(ed);				
				
			}
			
		};
		
		return exe;
	}
		
	/**
	 * Overriden to:
	 * <ul>
	 * <li/> start the worker 
	 * <li/> schedule peertrust init task to be execute by the worker. 
	 * 		the init task is wrap in an executable and will init start
	 * 		the peertrust client and finish other initializations which
	 * 		depends on the state of the peertrust client.  
	 * </ul>
	 * @see java.awt.applet.Applet#start()
	 */
	public void start() {
		super.start();	
		
		//make applet worker
		makeCmdWorker();		
		
		//schedule peertrust init task	
		workerFIFO.offer(makePeerTrustInitTask());
		return;
	}
	
	/**
	 * Starts the peertrust client.
	 *
	 */
	public void startPeerTrustClient() {
		
		///////////////////PeerTrust
		//URL url=getDocumentBase();		
		Properties props= new Properties();
		props.setProperty(	
							NEGOTIATION_ID_KEY,//"negoSessionID",
							getParameter(NEGOTIATION_ID_KEY));
		props.setProperty(	
							NEGO_RESOURCE_KEY,//"negoResource",
							getParameter(NEGO_RESOURCE_KEY));
		
		props.setProperty(
						REMOTE_PEER_IP_KEY,//"remotePeerIP", 
						getParameter(REMOTE_PEER_IP_KEY));
		
		props.setProperty(
						APP_CONTEXT_KEY,//"appContext",
						getParameter(APP_CONTEXT_KEY));
		
		props.setProperty(
					SERVICE_SERVLET_PATH_KEY,//"serviceServletPath",
					getParameter(SERVICE_SERVLET_PATH_KEY));
		
		props.setProperty(
						WEB_APP_URL_PATH_KEY,//"webAppURLPath",
						getParameter(WEB_APP_URL_PATH_KEY));
		props.setProperty(
						SERVER_PEER_NAME_KEY,//"serverPeerName",
						getParameter(SERVER_PEER_NAME_KEY));
		
		props.setProperty(	CODEBASE_URL_STR_KEY,
							getParameter(CODEBASE_URL_STR_KEY));
		
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
								instDir.toString(),null);
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
					String newAlias=Utils.getUserInput(this,"please enter an alias");
					if(newAlias==null){
						Utils.inform("install info","Cannot carry on installion!",this);
						clearPTClient();
						return;
					}
					installationSession.setRealAlias(newAlias);
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
	
	/**
	 * Destroys and remove reference to peertrust client.
	 */
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
	
	/**
	 * Stops the peertrustclient and removes peertrust files.
	 */
	public void uninstallPeerTrust(){
		Executable executable= new Executable(){
			public void execute(Object param){
				echoPane.echo("Unstall dialog starting .....");
				
				String question="Uninsstall will stopt the currently running peertrust engine.\n"+
								"and delete the installation directory:\n\t"+
								"<user.home>/.peertrust\n"+
								"Go ahead and uninstall?";
				boolean res=Utils.askYesNoQuestion(	"Uninstall Dialog",
																question,
																DemoApplet.this,
																new Object[]{"unstall","cancel"});
				if(res){
					//workerFIFO.offer(new StopCmd());
					if(ptClient!=null){
						ptClient.destroy();
					}
					UninstallPeerTrust uninst=
								new UninstallPeerTrust();
					uninst.execute(null);
					Utils.inform(	"Uninstall Info",
									uninst.getMessage(),
									DemoApplet.this.getParent());
				}
			}
		};
		workerFIFO.offer(executable);
	}
	
 /**
  * Executable which toggle the external visualization.
  */	
 Executable toggleVisExe=
	 new Executable(){
		public void execute(Object param){
			if(negotiationVisualization==null){
				negotiationVisualization= makeNegotiationVisualization();
			}else{
				negotiationVisualization.toggleVisualization();
			}
		}
	};
	
	/**
	 * Toggles the internal visualization; testVisPane or Echopane
	 * are alternately made visible on the applet content pane.
	 */
	public void toggleLeftFrameVisualization(){
		testVisPane.toggleVisualization();
		if(!testVisPane.getIsDisplayed()){
			showEchoPane();
		}
	}
	
	/**
	 * Schedule toggleVisExe to be execute by putting it
	 * in the worker fifo. This result in setting the 
	 * external visualization visible or not. 
	 *
	 */
	public void toggleExternalVisualization(){		
		/*boolean bol=*/workerFIFO.offer(toggleVisExe);
	}
	
	//checks external reference and remove it since replaced by toggleExternalVisualization
	public void toggleVisualization(){
		workerFIFO.offer(toggleVisExe);
	}
	
	/**
	 * Shows thepolicy management pane.
	 */
	public void managePolicies(){
		Executable executable= new Executable(){
			public void execute(Object param){
				Utils.inform("Info","Policy Management not available yet!",DemoApplet.this);
			}
		};
		workerFIFO.offer(executable);
	}
	
	/**
	 * Overriden to stop the worker and destroy the peertrust client.
	 * @see java.awt.applet.Applet#destroy()
	 */
	public void destroy() {
	
		System.out.println("Destroying!");
		httpSessionRegistrant.unregisterLastSession();
		workerFIFO.offer(new StopCmd());
		if(ptClient!=null){
			ptClient.destroy();
		}
		super.destroy();
	}
	
	
//	public void onNews(NewsEvent newsEvents) {
//		echoPane.echo(newsEvents.getNews());
//		return;
//	}
	
	
	
//	public void onNews(FinalResponse fr){
//		URL url=fr.getResourceURL();
//		showURLInDisplayFrame(url);
//		return;
//	}
	
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
	
	/**
	 * Uses javascript alert to show some info to the user
	 */
	private void jsAlert(String text){
		//win.eval("alert('"+text+"'))");
		win.call("alert", new Object[]{text});		
		return;
	}
	
	
	
	public String getTrustScheme() {
		return trustScheme;
	}
	/**
	 * Makes the http session registrant.
	 */
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
	
	/**
	 * Request a named credential.
	 * @param credentialName -- the name of the credential to get
	 */
	public void requestCredential(String credentialName){
		echoPane.echo("request credential:"+credentialName);
		ptClient.requestCredential(credentialName);
	}
}
