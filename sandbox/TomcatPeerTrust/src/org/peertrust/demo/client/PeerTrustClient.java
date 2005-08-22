/*
 * Created on 13.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import org.apache.log4j.Logger;
import org.peertrust.PTEngine;
import org.peertrust.TrustClient;
import org.peertrust.config.PTConfigurator;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.client.applet.FinalResponse;
import org.peertrust.demo.client.applet.SessionRegistrationMessage;
import org.peertrust.demo.client.applet.StartPasswordNegoCmd;
import org.peertrust.demo.client.applet.StartPeerTrustNegoCmd;
import org.peertrust.demo.common.ClientConstants;
import org.peertrust.demo.common.ConfigurationOption;

import org.peertrust.demo.common.NewsEventListener;
import org.peertrust.demo.common.StopCmd;
import org.peertrust.event.AnswerEvent;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.Answer;
import org.peertrust.net.EntitiesTable;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;

//import com.sun.net.ssl.internal.ssl.ByteBufferInputStream;


import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.io.*;

/**
 * @author kbs
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PeerTrustClient   implements PTEventListener{
	//private PTEventListener oldPTEventListener= null;
	private String[] parameters;
	private String sessionID;
	private String resourceID;
	private String appContextStr;
	private String serviceServletPath;
	
	private String peerIP;
	private int peerPort;
	private URL resourceReqURL=null;
	
	static public String CONFIG_MUFFIN_URL="PeerTrustConfig/ConfigMuffin.properties";
	static public int CONFIG_FILE_MAX_SIZE=1024*1024;
	
	private String allParams;
	
	private boolean finalResultNotshown=false;
	
	private NewsEventListener newsListener=null;
	
	private StringBuffer strBuffer= new StringBuffer(512);
	private long queryID;
	
	private PTEngine engine=null;
	
	private Logger logger;
	
	
	private URL codeBase;
	
	private String configFileName;
	
	private PTConfigurator appletPTConfigurator=null;
	
	private TrustClient trustClient;
	
	public String _getAppContextStr(){
		return appContextStr;
	}
	
	public URL _getCodeBaseURL(){
		return codeBase;
	}
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
	
	public PeerTrustClient(	String[]args, 
							NewsEventListener nl,
							String configFile) throws Exception{
		this.configFileName=configFile;
		int len=args.length;
    	
		for(int i=0;i<len;i++){
			strBuffer.append(args[i]);
			strBuffer.append("\n");
		}
		allParams=strBuffer.toString();
		//showMessage(allParams);
		try{
			
			Properties props=
				new Properties();
			props.load(
					new ByteArrayInputStream(strBuffer.toString().getBytes()));
			construct(props, nl);
		}catch(IOException e){
			throw(e);
		}
	}
	
    public PeerTrustClient(
    			Properties props, 
				NewsEventListener nl,
				String configFile,
				PTConfigurator appletPTConfigurator) throws Exception{
    	this.appletPTConfigurator=appletPTConfigurator;
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
    	initWebStartHelperClasses();  
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
    	startTrustNegotiation();
    	
    }
    
    public void setConfigFileName(String configFileName){
    	this.configFileName= configFileName;
    	return;
    }
    
    private void initWebStartHelperClasses(){
    		logger=ConfigurationOption.getLogger(this.getClass().getName());
            return ;
        
    }
    
    
    private void setParameters(Properties params){
    	sessionID=params.getProperty("negoSessionID").toString().trim();
		resourceID=params.getProperty("negoResource");
		peerIP=params.getProperty("remotePeerIP");
		appContextStr=params.getProperty("appContext");
		serviceServletPath=params.getProperty("serviceServletPath");
		
		try{
			peerPort= Integer.parseInt(params.getProperty("remotePeerPort","7703"));
		}catch(NumberFormatException numEx){
			peerPort=-1;
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
    
        
    
    
    private void startTrustNegotiation()throws Exception{
    	try {
			startPeerTrustClient();	
		} catch (Throwable th) {
			showMessage(th,"could not start PT Client");
			throw(new Exception("Cannot start pt",th));
		}
    	return;
    }
    
    public void sendQuery(StartPeerTrustNegoCmd cmd){
    	throw new RuntimeException("Not supported anymore");
    	//showMessage("user.home:"+System.getProperty("user.home"));
//    	final String PREFIX = "Client app.:";
//		Peer target=comFac.getServerPeer("ELearn");
//		Peer origin=
//			new	Peer(	comFac.getRandomAlias(),//randomPeerAlias,
//						"fake_adress"+comFac.getRandomAlias(),
//						-1);
//		queryID=comFac.getRandom();//Long.parseLong(sessionID);//comFac.getRandom();
//		resourceID=cmd.getResource();
//		Query newQuery = 
//			new Query(	cmd.getPtQueryGoal(),
//						origin, 
//						target, 
//						queryID, 
//						null);
//		
//		QueryEvent qe = new QueryEvent(this, newQuery) ;
//		
//		//dispatcher.event(qe) ;
//		comFac.createNetClient().send(newQuery,target);
//    	
//		showMessage(ConfigurationOption.getMessageAsString(newQuery));
//		return;
    }
    
    ClientSideHTTPCommunicationFactory comFac;
    
    private void startPeerTrustClient() throws ConfigurationException{
    	System.out.println("startPeerTrustClient.............................");
    	//showMessage("user.home:"+System.getProperty("user.home"));
    	
    	final String PREFIX = "Client app.:";
		
		String defaultConfigFile = null;

		if(configFileName==null){
			defaultConfigFile=
				ConfigurationOption.getPTClientConfigFilePath();
		}else{
			defaultConfigFile=configFileName;
		}
			logger.info("defaulConfigFile:"+defaultConfigFile);
//		} catch (MalformedURLException e1) {
//			showMessage(e1,"bad default config file:");
//		}
		
		String defaultComponent = Vocabulary.PeertrustEngine.toString() ;
		
		int TIMEOUT = 15000 ;
		int SLEEP_INTERVAL = 500 ;
		
		String newArgs[] = {defaultConfigFile} ;
		if(configFileName!=null){
			newArgs[0]=configFileName;
		}
		
		//PTConfigurator config = null;//(appletPTConfigurator==null)?new PTConfigurator():appletPTConfigurator ;
		
		String[] components = { defaultComponent } ;
		showMessage(defaultConfigFile);
		try {
			trustClient= new TrustClient(newArgs,components);
			//config.startApp(newArgs, components) ;
			//config=trustClient.getPTConfigurator();
			//engine = (PTEngine) config.getComponent(Vocabulary.PeertrustEngine) ;
			engine=(PTEngine)trustClient.getComponent(Vocabulary.PeertrustEngine);
			EventDispatcher dispatcher = engine.getEventDispatcher() ;
			ClientSidePTEventListener ptEventListener=
				(ClientSidePTEventListener)engine.getEventListener();
			ptEventListener.setParent(this);
			dispatcher.register(ptEventListener);//is that needed?
			
			//engine.setEventListener(this);
//			comFac=
//				(ClientSideHTTPCommunicationFactory)config.getComponent(Vocabulary.CommunicationChannelFactory);
			comFac=
				(ClientSideHTTPCommunicationFactory)trustClient.getComponent(
											Vocabulary.CommunicationChannelFactory);
			comFac.setServerIP(this.peerIP);
			comFac.setServerPort(this.peerPort);
			comFac.setWebAppURLPath(this.appContextStr+"/PeerTrustCommunicationServlet");
			
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
			Peer server= new Peer("eLearn",this.peerIP,this.peerPort);
			entitiesTable.put("eLearn",server);
			//todo change how to set local peer data
			
			entitiesTable.put("alice",new Peer("alice","dudududu",0));
			trustClient.setAlias("alice");
			
			logger.info("comfac:"+comFac);
			
			
		} catch (ConfigurationException e) {
			showMessage(e,"Problem staring pt client");
			throw(e);
		}
		return;
    }
    
    public void negotiate(){
//		String query="request(spanishCourse,Session) @ elearn";
//		Peer target=comFac.getServerPeer("ELearn");
//		Peer origin=
//			new	Peer(	comFac.getRandomAlias(),//randomPeerAlias,
//						"fake_adress"+comFac.getRandomAlias(),
//						-1);
//		
//		queryID=Long.parseLong(sessionID);//comFac.getRandom();
//		Query newQuery = 
//			new Query(query,origin, target, queryID, null);
//		
//		QueryEvent qe = new QueryEvent(this, newQuery) ;
//		
//		//dispatcher.event(qe) ;
//		comFac.createNetClient().send(newQuery,target);
//		logger.info("initial query send:"+query);
//		showMessage(ConfigurationOption.getMessageAsString(newQuery));
    	throw new RuntimeException("funtionality not available anymore!");
    }
    
    public void negotiate(String query){
//		//String query="request(spanishCourse,Session) @ elearn";
//		Peer target=comFac.getServerPeer("ELearn");
//		Peer origin=
//			new	Peer(	comFac.getRandomAlias(),//randomPeerAlias,
//						"fake_adress"+comFac.getRandomAlias(),
//						-1);
//		
//		queryID=comFac.getRandom();//Long.parseLong(sessionID);//comFac.getRandom();
		queryID=trustClient.sendQuery(query);
		trustClient.setTimeout(60*1000);//wait a minute
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
		
//		Query newQuery = 
//			new Query(query,origin, target, queryID, null);
//		
//		QueryEvent qe = new QueryEvent(this, newQuery) ;
//		
//		//dispatcher.event(qe) ;
//		comFac.createNetClient().send(newQuery,target);
//		logger.info("initial query send:"+query);
//		showMessage(ConfigurationOption.getMessageAsString(newQuery));
		
		//because of exception in MetaInterpreterListner this is necessary
		//todo fix it renewClient
		////reNewClient();
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
	
//		boolean isFinalResult=false;
//		///check if Answerevent and if it for initial query
//		//because than it is the final answer
//		if(event instanceof AnswerEvent){
//			Answer answer= ((AnswerEvent)event).getAnswer();
//			if(answer.getReqQueryId()==queryID){
//				isFinalResult=true;
//				showMessage("\n************final answer***************\n");
//			}
//		}
//		
//		if(isFinalResult){
//			//make URL from negoId and negoRes and show it
//			String negoID=sessionID;
//			String negoRes=resourceID;
//			
//			try {
//				strBuffer.delete(0,strBuffer.length());
//				strBuffer.append(".");
//				strBuffer.append(appContextStr);
//				strBuffer.append(serviceServletPath);				
//				strBuffer.append("?negoSessionID=");				
//				strBuffer.append(queryID);
//				strBuffer.append("&negoResource=");				
//				strBuffer.append(resourceID);
//				
//					resourceReqURL= new URL(codeBase,strBuffer.toString());
//					FinalResponse finalResponse=
//						new FinalResponse(	Long.toString(queryID),
//											"balbla",
//											resourceReqURL);
//					//objStreamToApplet.writeObject(finalResponse);
//					
//					newsListener.onNews(finalResponse);
//			} catch (MalformedURLException e1) {
//				showMessage(e1, "bad final url");//e1.printStackTrace();
//			}catch(Throwable th){
//				showMessage(th, "error making final url");
//			}
//		}else{
//			///may be show a message			
//			String mes="negoOngoing";
//			if(event instanceof QueryEvent){
//				mes="Query:"+((QueryEvent)event).getQuery().getGoal();
//			}else if(event instanceof AnswerEvent){
//				mes="Answer:"+((AnswerEvent)event).getAnswer().getGoal();
//			}
//			showMessage(mes);
//		}
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
	
    
    
    public void installPeerTrustConfigFiles(){
    	return;
    }
    private void reNewClient(){
    	try {
    		comFac.destroy();
    		engine.stop();			
			engine=null;
			comFac=null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			startPeerTrustClient();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	return;
    }
    
    public void destroy(){ 
    	cmdFIFO.offer(new StopCmd());
    	comFac.destroy();
    	engine.stop();    	
    }
    
    
    
	public PTConfigurator getAppletPTConfigurator() {
		return appletPTConfigurator;
	}
	public void setAppletPTConfigurator(PTConfigurator appletPTConfigurator) {
		this.appletPTConfigurator = appletPTConfigurator;
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
							}else if(cmd instanceof SessionRegistrationMessage){
								//System.out.println("Sending "+cmd);
								comFac.createNetClient().send(	(SessionRegistrationMessage)cmd,
																comFac.getServerPeer("eLearn"));
								System.out.println("Sending "+cmd);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
							
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
    
    ///////////////////////////////////////////////////////////////////////////
    
    
}
