/*
 * Created on 13.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client;


//import java.net.URLConnection;


import org.apache.log4j.Logger;
import org.peertrust.TrustClient;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.client.applet.StartPasswordNegoCmd;
import org.peertrust.demo.client.applet.StartPeerTrustNegoCmd;
import org.peertrust.demo.common.ConfigurationOption;
import org.peertrust.demo.common.Executable;
import org.peertrust.demo.common.PrologFileManipulator;

//import org.peertrust.demo.common.NewsEventListener;
import org.peertrust.demo.common.StopCmd;
import org.peertrust.demo.credential_distribution.CredentialDistributionClient;
import org.peertrust.demo.credential_distribution.CredentialResponse;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASPObject;
import org.peertrust.demo.session_registration.HttpSessionRegistrationRequest;
import org.peertrust.event.AnswerEvent;
//import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEvent;
import org.peertrust.event.QueryEvent;
import org.peertrust.exception.ConfigurationException;
//import org.peertrust.net.Answer;
import org.peertrust.meta.MetaInterpreter;
import org.peertrust.net.EntitiesTable;
import org.peertrust.net.Message;
import org.peertrust.net.Peer;

//import org.peertrust.net.Query;

//import com.sun.net.ssl.internal.ssl.ByteBufferInputStream;


import java.io.File;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * @author kbs
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PeerTrustClient   /*implements PTEventListener*/{
	final static public int TIMEOUT = 30000 ;
	final static public int SLEEP_INTERVAL = 100 ;
	
	//private String resourceID;
	private String appContextStr;
	//private String serviceServletPath;
	
	private String serverPeerIP;
	private int serverPeerPort;
	private String serverPeerName;
	//private URL resourceReqURL=null;
	
	private Peer localPeer;
	private Peer serverPeer;
	
	static public String CONFIG_MUFFIN_URL="PeerTrustConfig/ConfigMuffin.properties";
	static public int CONFIG_FILE_MAX_SIZE=1024*1024;
	
	private Logger logger;
	
	
	private String configFileName;
	
	private TrustClient trustClient;
	private CredentialDistributionClient credentialDistributionClient;
	
	
	private String localPeerName;
	
	private File prologFile;
	
	private Executable userAlert;
	private Executable todoAfterPeerTrustRestart;
	
	private ClientSideHTTPCommunicationFactory comFac;
	
    public PeerTrustClient(
    			Properties props, 
				String configFile,
				File prologFile,
				Executable userAlert) throws Exception{
    	//this.appletPTConfigurator=appletPTConfigurator;
    	this.configFileName=configFile;
    	this.prologFile=prologFile;
    	this.userAlert=userAlert;
    	construct(props);
    	
        return;
    }
    
    private void construct(Properties props) throws Exception{
			//AllPermission ap= new AllPermission();
			makeCmdWorker();
			
			logger=ConfigurationOption.getLogger(this.getClass().getName());
			
			setParameters(props);
						
			startPeerTrustClient();
			
		}
    
    
    private void setParameters(Properties params){
    	//sessionID=params.getProperty("negoSessionID").toString().trim();
		//resourceID=params.getProperty("negoResource");
		serverPeerIP=params.getProperty("remotePeerIP");
		serverPeerName=params.getProperty("serverPeerName").toLowerCase();
		appContextStr=params.getProperty("appContext");
		//serviceServletPath=params.getProperty("serviceServletPath");
		
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
    

    
    
    private void startPeerTrustClient() throws ConfigurationException{
    	showMessage("startPeerTrustClient.............................");
    	
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
			//engine=(PTEngine)trustClient.getComponent(Vocabulary.PeertrustEngine);
			comFac=
				(ClientSideHTTPCommunicationFactory)trustClient.getComponent(
											Vocabulary.CommunicationChannelFactory);
			comFac.setHttpServerIP(this.serverPeerIP);
			comFac.setServerPort(this.serverPeerPort);
			comFac.setHttpServerAlias(this.serverPeerName);
			comFac.setWebAppURLPath(this.appContextStr+"/PeerTrustCommunicationServlet");
			
			showMessage("\n******************info pt com *****************"+
						"\nServerIP:"+this.serverPeerIP+
						"\nServerPort:"+this.serverPeerPort+
						"\nWebAppURLPath:"+this.appContextStr+"/PeerTrustCommunicationServlet\n");
			
			ClientSideNetServer ptServer=
				((ClientSideNetServer)comFac.createNetServer());
			/*ptServer.addNewsListener(newsListener);*/
			
//			ClientSideNetClient ptClient=
//				(ClientSideNetClient)comFac.createNetClient();
			/*ptClient.addNewsListener(newsListener);*/
			ptServer.setConfigNotEnded(false);
			//EntitiesTable entitiesTable = (EntitiesTable) config.getComponent(Vocabulary.EntitiesTable) ;
			EntitiesTable entitiesTable = 
				(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
			//TODO recheck localhost setting
			serverPeer= //new Peer("elearn",this.serverPeerIP,this.serverPeerPort);
				//new Peer(this.serverPeerName,this.serverPeerIP,this.serverPeerPort);
				new Peer(this.serverPeerName,"127.0.0.1",this.serverPeerPort);
			
			entitiesTable.put(this.serverPeerName,serverPeer);
			//todo change how to set local peer data
			MetaInterpreter metaInter=
				(MetaInterpreter)trustClient.getComponent(Vocabulary.MetaInterpreter);
			this.localPeerName=
				metaInter.getPeerName().toLowerCase();
			//comFac.setLocalPeerAlias(localPeerName);
			
			this.localPeer=comFac.getServerPeer(localPeerName);//ssssssssssss new Peer(localPeerName,"_no_need_for_addi_",0);
			entitiesTable.put(	localPeerName,
								this.localPeer);
			//trustClient.setAlias(localPeerName);
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
    
    private Executable makeRestartTrustClientTask(){
    	Executable exe=
    		new Executable(){

				public void execute(Object param) {
					if(param instanceof CredentialResponse){
						CredentialResponse resp=
							(CredentialResponse)param;
						//resp.getValue();
						PrologFileManipulator pfm=
							new PrologFileManipulator();
						pfm.loadPrologFile(prologFile);
						String value=resp.getValue();
						if(value==null){
							if(userAlert!=null){
								userAlert.execute("Invalid credential:"+resp.getName());
								return;
							}
						}
						pfm.addCredential(value);
						
						
						try{
							restartTrustClient();
							if(todoAfterPeerTrustRestart!=null){
								todoAfterPeerTrustRestart.execute(null);
							}
							
							if(userAlert!=null){
								userAlert.execute("Credential install:\n"+
												"name:"+resp.getName()+
												"Please retry the last url to see the effect");
							}
							
						}catch(Throwable th){
							th.printStackTrace();
							userAlert.execute("Credential not installed due to exception:\n"+
										"Message:"+th.getMessage());
						}
												
					}
				}
    		
    	};   
    	
    	return exe;
    }
    
    private CredentialDistributionClient makeCredentialDistributionClient(){
    	CredentialDistributionClient cdc=
    		new CredentialDistributionClient();
    	cdc.setup(
    			trustClient,
    			makeRestartTrustClientTask());
    	return cdc;
    }
    
    
    public void requestCredential(String name){
    	CredentialDistributionClient cdc=
    			makeCredentialDistributionClient();
    	cdc.requestCredential(name,localPeer,serverPeer);
    	
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
    
    public void restartTrustClient() throws ConfigurationException {
    	//clean up
    	cmdFIFO.clear();
    	comFac.destroy();
    	trustClient.destroy();
    	comFac=null;
    	trustClient=null;
    	
    	//restart
    	startPeerTrustClient();
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
//								StartPeerTrustNegoCmd negoCmd=
//											(StartPeerTrustNegoCmd)cmd;
//								resourceID=negoCmd.getResource();
//								negotiate(negoCmd.getPtQueryGoal());
//								//Answer.LAST_ANSWER
							}else if (cmd instanceof StartPasswordNegoCmd){
//								FinalResponse finalResponse=
//									new FinalResponse(	((StartPasswordNegoCmd)cmd).getPassword(),
//														((StartPasswordNegoCmd)cmd).getResource(),
//														getServiveURL(
//																((StartPasswordNegoCmd)cmd).getResource(),
//																((StartPasswordNegoCmd)cmd).getUserName(),
//																((StartPasswordNegoCmd)cmd).getPassword()));
								//objStreamToApplet.writeObject(finalResponse);								
//								newsListener.onNews(finalResponse);								
							}else if(cmd instanceof HttpSessionRegistrationRequest){
								//System.out.println("Sending "+cmd);
								PTCommunicationASPObject.send(
											comFac.createNetClient(),
											(HttpSessionRegistrationRequest)cmd,
											localPeer,//TODO check for source
											serverPeer);//comFac.getServerPeer("eLearn"));
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
	
	public void sendToHttpServer(Message mes){
		comFac.createNetClient().send(mes,serverPeer);
	}

	/**
	 * @return Returns the prologFile.
	 */
	public File getPrologFile() {
		return prologFile;
	}

	/**
	 * @param prologFile The prologFile to set.
	 */
	public void setPrologFile(File prologFile) {
		this.prologFile = prologFile;
	}

	public Executable getTodoAfterPeerTrustRestart() {
		return todoAfterPeerTrustRestart;
	}

	public void setTodoAfterPeerTrustRestart(Executable todoAfterPeerTrustRestart) {
		this.todoAfterPeerTrustRestart = todoAfterPeerTrustRestart;
	}
	
	
	
	
}
