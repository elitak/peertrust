package org.peertrust.demo.client.applet;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
//import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import netscape.javascript.JSObject;

//import org.peertrust.config.PTConfigurator;
import org.peertrust.demo.client.PeerTrustClient;
import org.peertrust.demo.common.ClientConstants;
import org.peertrust.demo.common.EchoPane;
import org.peertrust.demo.common.Executable;
import org.peertrust.demo.common.Helpers;
import org.peertrust.demo.common.InstallationSession;
import org.peertrust.demo.common.NewsEvent;
import org.peertrust.demo.common.NewsEventListener;
import org.peertrust.demo.common.StopCmd;


/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DemoApplet extends JApplet implements NewsEventListener{
	public static String PEERTRUST_FOLDER_NAME=".peertrust";
	//private Socket socket=null;
	private ObjectOutputStream objOut=null;
	//private ObjectInputStream  objIn=null;
	private Thread cmdWorker=null;
	private PeerTrustClient ptClient=null;
	private JSObject win;
	
	//transient private long key=0;
	
	private String trustScheme="User Password";
	
	private EchoPane echoPane=new EchoPane();
	
	private InstallationSession installationSession;
	
	StringBuffer strBuffer= new StringBuffer(512);
		
	
	private void showURLInDisplayFrame(URL url){
		echoPane.echo("showing:"+url);
		getAppletContext().showDocument(url,"display_frame");
	}
	
	
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
		echoPane.echo("Registering session:"+sessionKey);
		
			workerFIFO.offer( new SessionRegistrationMessage(sessionKey,null,null));
		return;
	}
	
//	private void sendCommandd(Object objToSend){
//		
//		try {
//			if(objOut!=null){
//				objOut.writeObject(objToSend);	
//				echoPane.echo("sendCmd:"+objToSend);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
	
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
						}else if(obj instanceof SessionRegistrationMessage){
							System.out.println("SessionRegistrationMessage added "+obj);
							if(ptClient!=null){
								ptClient.addCmdToFIFO(obj);
							}
						}else if(obj instanceof StopCmd){
						
							return;
						}else if(obj instanceof Executable){
							((Executable)obj).execute();
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
	
	public void init() {
		super.init();
		win=JSObject.getWindow(this);
		return;
	}
		
	
	public void start() {
		super.start();		
		//////////////////////////gui
		this.getContentPane().setLayout(new GridLayout(1,1));
		this.getContentPane().add(echoPane);
		
		//////////////////worker
		makeCmdWorker();
		
		///////////////////PeerTrust
		//URL url=getDocumentBase();		
		Properties props= new Properties();
		props.setProperty("negoSessionID",getParameter("negoSessionID"));
		props.setProperty("negoResource",getParameter("negoResource"));
		props.setProperty("remotePeerIP", getParameter("remotePeerIP"));
		props.setProperty("appContext",getParameter("appContext"));
		props.setProperty("serviceServletPath",getParameter("serviceServletPath"));
		props.setProperty("webAppURLPath",getParameter("webAppURLPath"));
		props.setProperty(ClientConstants.CODEBASE_URL_STR_KEY,
						getParameter(ClientConstants.CODEBASE_URL_STR_KEY));
		
		String home= null;
		//TODO get home in a more flexible way
		try{
			home=System.getProperty("user.home");
		}catch(Exception e){
			e.printStackTrace();
		}
		File instDir= new File(home,PEERTRUST_FOLDER_NAME);
		String question="This demo need to install peetrust file in: \n"+
						instDir+
						"\nYou can savely remove the folder either manualy or using"+
						"<session>/<uninstall peertrust files>\n"+
						"Allow installation?";
		boolean installtionAllowed=Helpers.askYesNoQuestion("Instalation Dialog",question,this,null);
		if(installtionAllowed){
			try {
				URL sourceBase= new URL(getCodeBase(),"./PeerTrustConfig/");
				echoPane.echo("sourceBase:"+sourceBase);
				installationSession= 
					new InstallationSession(
									sourceBase.toString(),
									"install.xml",
									instDir.toString());
				if(!installationSession.isPeerTrustInstall()){
					installationSession.install();
				}
				
				echoPane.echo("file in "+ instDir+
						":"+Arrays.toString(instDir.list())+"\n");
				ptClient= 
					new PeerTrustClient(
							props,
							(NewsEventListener)this,
							installationSession.getConfigFileURL(),
							null);//this.configurator);
				trustScheme="PeerTrust";
				registerSession(getParameter("negoSessionID"));
			}catch (MalformedURLException e){
				echoPane.echo("bad ur",e);clearPTClient();
			} catch (IOException e) {
				echoPane.echo("IO problem!",e);clearPTClient();
			}catch(Throwable th){
				echoPane.echo("could not install or initiate pt using http source directely",th);
				clearPTClient();
			}
		}else{
			String info=
				"You choose not to install peertrust files.\n"+
				"Therefore you will not be able to experience any negotiation \n"+
				"and your access will be limited to unprotected content!";
			Helpers.inform("Installation Info",info,this);
		}
		
		return;
	}
	
	private void clearPTClient(){
		if(ptClient!=null){
			try{
				ptClient.destroy();
			}catch(Throwable th){}
			ptClient=null;
			echoPane.echo("ptClient cleared!");
		}				
		trustScheme="User Password";
	}
	
	public void uninstallPeerTrust(){
		Executable executable= new Executable(){
			public void execute(){
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
				boolean res=Helpers.askYesNoQuestion(	"Uninstall Dialog",
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
							Helpers.inform("Uninstall Info","Uninstall successfull!",DemoApplet.this.getParent());
						}else{
							Helpers.inform("Uninstall Info","could nor delete:"+instDir,DemoApplet.this);
						}
						return;
					}catch(SecurityException e){
						Helpers.inform("Uninstall Info",e.getLocalizedMessage(),DemoApplet.this);
						return;
					}			
				}
			}
		};
		workerFIFO.offer(executable);
	}
	
	public void toggleVisualization(){
		Executable executable= new Executable(){
			public void execute(){
				Helpers.inform("Info","Visualization not implemented yet!",
						DemoApplet.this.getParent());
			}
		};
		workerFIFO.offer(executable);
	}
	
	public void managePolicies(){
		Executable executable= new Executable(){
			public void execute(){
				Helpers.inform("Info","Policy Management not available yet!",DemoApplet.this);
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
}
