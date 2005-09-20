/*
 * Created on 13.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client;

import java.net.MalformedURLException;
import java.net.URL;
//import java.net.URLConnection;


import org.apache.log4j.Logger;
import org.peertrust.PTEngine;
import org.peertrust.TrustClient;
import org.peertrust.config.PTConfigurator;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.client.applet.FinalResponse;
import org.peertrust.demo.client.applet.StartPasswordNegoCmd;
import org.peertrust.demo.client.applet.StartPeerTrustNegoCmd;
import org.peertrust.demo.common.ClientConstants;
import org.peertrust.demo.common.ConfigurationOption;
import org.peertrust.demo.common.HttpSessionRegistrationRequest;

import org.peertrust.demo.common.NewsEventListener;
import org.peertrust.demo.common.StopCmd;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASPObject;
import org.peertrust.event.AnswerEvent;
//import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.exception.ConfigurationException;
//import org.peertrust.net.Answer;
import org.peertrust.meta.MetaInterpreter;
import org.peertrust.net.EntitiesTable;
import org.peertrust.net.Peer;
//import org.peertrust.net.Query;

//import com.sun.net.ssl.internal.ssl.ByteBufferInputStream;


import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * @author kbs
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PeerTrustClient   implements PTEventListener{
	final static public int TIMEOUT = 60000 ;
	final static public int SLEEP_INTERVAL = 100 ;
	
	private String resourceID;
	private String appContextStr;
	private String serviceServletPath;
	
	private String serverPeerIP;
	private int serverPeerPort;
	private String serverPeerName;
	private URL resourceReqURL=null;
	
	private Peer localPeer;
	private Peer serverPeer;
	
	static public String CONFIG_MUFFIN_URL="PeerTrustConfig/ConfigMuffin.properties";
	static public int CONFIG_FILE_MAX_SIZE=1024*1024;
	

	
	private NewsEventListener newsListener=null;
	
	private StringBuffer strBuffer= new StringBuffer(512);
	private long queryID;
	
	private PTEngine engine=null;
	
	private Logger logger;
	
	
	private URL codeBase;
	
	private String configFileName;
	
//	private PTConfigurator appletPTConfigurator=null;
	
	private TrustClient trustClient;
	
	private String localPeerName;
	
	public URL getResourceURL(){
		try {
			String rel=this.appContextStr+serviceServletPath+
			"?action=ask&resource="+
			this.resourceID+
			"&key="+this.queryID;
			URL url =
				new URL(codeBase,rel);
			
			return url;
		} catch (MalformedURLException e) {
			this.showMessage(e,"bad res url");
			return null;
		}
	}
	
	
    public PeerTrustClient(
    			Properties props, 
				NewsEventListener nl,
				String configFile,
				PTConfigurator appletPTConfigurator) throws Exception{
    	//this.appletPTConfigurator=appletPTConfigurator;
    	this.configFileName=configFile;
    	construct(props,nl);
        return;
    }
    
    private void construct(
    				Properties props, 
					NewsEventListener nl
					) throws Exception{
    	//AllPermission ap= new AllPermission();
    	makeCmdWorker();
    	
    	//initWebStartHelperClasses();***************************************
    	logger=ConfigurationOption.getLogger(this.getClass().getName());
        
        
    	setParameters(props);
    	String codeBaseUrl=props.getProperty(ClientConstants.CODEBASE_URL_STR_KEY);
    	if(codeBaseUrl==null){
    		//codeBaseUrl="http://127.0.0.1:7703/myapp-0.1-dev";
    		throw(new NullPointerException("codeBaseUrl is null"));
    	}
    	
    	try {
			codeBase=new URL(codeBaseUrl);
			System.out.println("URL CODE BASE:"+codeBaseUrl);
		} catch (MalformedURLException e) {
			System.err.println("codeBaseURL="+codeBaseUrl);
			throw(e);
		}
    	
    	configEventListening(nl);
    	startPeerTrustClient();
    	
    }
    
    public void setConfigFileName(String configFileName){
    	this.configFileName= configFileName;
    	return;
    }

    
    
    private void setParameters(Properties params){
    	//sessionID=params.getProperty("negoSessionID").toString().trim();
		resourceID=params.getProperty("negoResource");
		serverPeerIP=params.getProperty("remotePeerIP");
		serverPeerName=params.getProperty("serverPeerName").toLowerCase();
		appContextStr=params.getProperty("appContext");
		serviceServletPath=params.getProperty("serviceServletPath");
		
		try{
			serverPeerPort= Integer.parseInt(params.getProperty("remotePeerPort","7703"));
		}catch(NumberFormatException numEx){
			serverPeerPort=-1;
			showMessage(numEx.getMessage());			
		}
		
    }
    
    private void showMessage(String mes){
    	if(logger!=null){
    		logger.info(mes);
    	}else{
    		System.out.println(mes);
    	}
    	return;
    }
    
    private void showMessage(Throwable th, String mes){
    	if(logger!=null){
    		logger.error(mes,th);
    	}
    	return;
    }
    
    
    
    private void configEventListening(NewsEventListener nl){
    	newsListener=nl;
    	return;
    }
    
        
      
    
    ClientSideHTTPCommunicationFactory comFac;
    
    private void startPeerTrustClient() throws ConfigurationException{
    	System.out.println("startPeerTrustClient.............................");
    	//showMessage("user.home:"+System.getProperty("user.home"));
    	
    	//final String PREFIX = "Client app.:";
		
		String defaultConfigFile = null;

		if(configFileName==null){
			defaultConfigFile=
				ConfigurationOption.getPTClientConfigFilePath();
		}else{
			defaultConfigFile=configFileName;
		}
			logger.info("defaulConfigFile:"+defaultConfigFile);

		String defaultComponent = Vocabulary.PeertrustEngine.toString() ;
		
		String newArgs[] = {defaultConfigFile} ;
		if(configFileName!=null){
			newArgs[0]=configFileName;
		}
		
		//PTConfigurator config = null;//(appletPTConfigurator==null)?new PTConfigurator():appletPTConfigurator ;
		
		String[] components = { defaultComponent } ;
		showMessage(defaultConfigFile);
		try {
			trustClient= new TrustClient(newArgs,components);
			engine=(PTEngine)trustClient.getComponent(Vocabulary.PeertrustEngine);
//			EventDispatcher dispatcher = engine.getEventDispatcher() ;
//			ClientSidePTEventListener ptEventListener=
//				(ClientSidePTEventListener)engine.getEventListener();
//			ptEventListener.setParent(this);
//			dispatcher.register(ptEventListener);//is that needed?
			
			comFac=
				(ClientSideHTTPCommunicationFactory)trustClient.getComponent(
											Vocabulary.CommunicationChannelFactory);
			comFac.setServerIP(this.serverPeerIP);
			comFac.setServerPort(this.serverPeerPort);
			comFac.setServerAlias(this.serverPeerName);
			comFac.setWebAppURLPath(this.appContextStr+"/PeerTrustCommunicationServlet");
			
			System.out.println("******************info pt com *****************");
			System.out.println("ServerIP:"+this.serverPeerIP);
			System.out.println("ServerPort:"+this.serverPeerPort);
			System.out.println("WebAppURLPath:"+this.appContextStr+"/PeerTrustCommunicationServlet");
			
			ClientSideNetServer ptServer=
				((ClientSideNetServer)comFac.createNetServer());
			ptServer.addNewsListener(newsListener);
			
			ClientSideNetClient ptClient=
				(ClientSideNetClient)comFac.createNetClient();
			ptClient.addNewsListener(newsListener);
			ptServer.setConfigNotEnded(false);
			//EntitiesTable entitiesTable = (EntitiesTable) config.getComponent(Vocabulary.EntitiesTable) ;
			EntitiesTable entitiesTable = 
				(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
			serverPeer= //new Peer("elearn",this.serverPeerIP,this.serverPeerPort);
				new Peer(this.serverPeerName,this.serverPeerIP,this.serverPeerPort);
			entitiesTable.put(this.serverPeerName,serverPeer);
			//todo change how to set local peer data
			MetaInterpreter metaInter=
				(MetaInterpreter)trustClient.getComponent(Vocabulary.MetaInterpreter);
			this.localPeerName=
				metaInter.getPeerName().toLowerCase();
			comFac.setLocalPeerAlias(localPeerName);
			
			this.localPeer=new Peer(localPeerName,"_no_need_for_addi_",0);
			entitiesTable.put(	localPeerName,
								this.localPeer);
			trustClient.setAlias(localPeerName);
			trustClient.setTimeout(TIMEOUT);
			trustClient.setSleepInterval(SLEEP_INTERVAL);
			logger.info("comfac:"+comFac);			
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
			showMessage(e,"Problem staring pt client");
			throw(e);
		} catch(Throwable th){
			th.printStackTrace();
		}
		return;
    }
    
    
    public void negotiate(String query){
		queryID=trustClient.sendQuery(query);
		Boolean res=trustClient.waitForQuery(queryID);
		
		if(res.booleanValue()){	
			try{
			strBuffer.delete(0,strBuffer.length());
			strBuffer.append(".");
			strBuffer.append(appContextStr);
			strBuffer.append(serviceServletPath);				
			strBuffer.append("?negoSessionID=");				
			strBuffer.append(queryID);
			strBuffer.append("&negoResource=");				
			strBuffer.append(resourceID);
			
				resourceReqURL= new URL(codeBase,strBuffer.toString());
				FinalResponse finalResponse=
					new FinalResponse(	Long.toString(queryID),
										"balbla",
										resourceReqURL);
				//objStreamToApplet.writeObject(finalResponse);
				
				newsListener.onNews(finalResponse);
				
			} catch (MalformedURLException e1) {
				showMessage(e1, "bad final url");//e1.printStackTrace();
			}catch(Throwable th){
				showMessage(th, "error making final url");
			}
		}
    }
    
    private String getEventAsText(PTEvent event){
    	Object mes;
		if(event instanceof AnswerEvent){
			mes= ((AnswerEvent)event).getAnswer();
		}else if(event instanceof QueryEvent){
			mes=((QueryEvent)event).getQuery();
		}else{
			mes=event;
		}
		
		return ConfigurationOption.getMessageAsString(mes);
    }
    
    
    
	public void event(PTEvent event) {
		showMessage("A PTEvent:"+getEventAsText(event)+"\n******my query id:"+queryID);
	}
	
	public URL getServiveURL( String resource, String userName, String password){
		
		try {
			strBuffer.delete(0,strBuffer.length());
			strBuffer.append(".");
			strBuffer.append(appContextStr);
			strBuffer.append(serviceServletPath);				
			
			strBuffer.append("?userName=");				
			strBuffer.append(userName);
			
			strBuffer.append("&password=");				
			strBuffer.append(password);
			
			strBuffer.append("&negoResource=");				
			strBuffer.append(resource);

			resourceReqURL= new URL(codeBase,strBuffer.toString());
			return resourceReqURL;
		} catch (MalformedURLException e) {
			showMessage(e,"could not make url");
			return null;
		}
	}
	
    
    
//    public void installPeerTrustConfigFiles(){
//    	return;
//    }
    
    
//	public TrustClient getTrustClient(){
//		return this.trustClient;
//	}
	
    public void destroy(){ 
    	stopTrustClient(); 	
    }
    
    public void stopTrustClient(){ 
    	cmdFIFO.offer(new StopCmd());
    	comFac.destroy();
    	trustClient.destroy();
    	comFac=null;
    	trustClient=null;
    }
    //////////////////////////////////
    ArrayBlockingQueue cmdFIFO=
    	new ArrayBlockingQueue(8);
    
    public void makeCmdWorker(){
    	Runnable workerRunnable= 
    		new Runnable(){
    			public void run(){
    				while(true){
    					try {
    						System.out.println("waiting for cmd!");
							Object cmd= cmdFIFO.take();
							if(cmd instanceof StopCmd){
								break;
							}else if(cmd instanceof StartPeerTrustNegoCmd){
								StartPeerTrustNegoCmd negoCmd=
											(StartPeerTrustNegoCmd)cmd;
								resourceID=negoCmd.getResource();
								negotiate(negoCmd.getPtQueryGoal());
								//Answer.LAST_ANSWER
							}else if (cmd instanceof StartPasswordNegoCmd){
								FinalResponse finalResponse=
									new FinalResponse(	((StartPasswordNegoCmd)cmd).getPassword(),
														((StartPasswordNegoCmd)cmd).getResource(),
														getServiveURL(
																((StartPasswordNegoCmd)cmd).getResource(),
																((StartPasswordNegoCmd)cmd).getUserName(),
																((StartPasswordNegoCmd)cmd).getPassword()));
								//objStreamToApplet.writeObject(finalResponse);								
								newsListener.onNews(finalResponse);								
							}else if(cmd instanceof HttpSessionRegistrationRequest){
								//System.out.println("Sending "+cmd);
								PTCommunicationASPObject.send(
											comFac.createNetClient(),
											(HttpSessionRegistrationRequest)cmd,
											localPeer,//TODO check for source
											serverPeer);//comFac.getServerPeer("eLearn"));
//								comFac.createNetClient().send(	(HttpSessionRegistrationRequest)cmd,
//																comFac.getServerPeer("eLearn"));
								System.out.println("HttpSessionRegistrationRequest Send "+cmd);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
							
						}catch(Throwable th){
							th.printStackTrace();
						}
						//System.out.println("Stop waiting for cmd!");
    				}
    				System.out.println("Stop waiting for cmd!");
    			}
    		};
    	(new Thread(workerRunnable)).start();
    	return;
    }
    
    public void addCmdToFIFO(Object obj){
    	cmdFIFO.offer(obj);
    }


	/**
	 * @return Returns the trustClient.
	 */
	public TrustClient getTrustClient() {
		return trustClient;
	}


	/**
	 * @return Returns the localPeer.
	 */
	public Peer getLocalPeer() {
		return localPeer;
	}


	/**
	 * @return Returns the serverPeer.
	 */
	public Peer getServerPeer() {
		return serverPeer;
	}
    
	
    
    ///////////////////////////////////////////////////////////////////////////
    
    
}
