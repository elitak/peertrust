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
import org.peertrust.demo.common.Executable;
import org.peertrust.demo.common.PrologFileManipulator;
import org.peertrust.demo.common.Utils;
import org.peertrust.demo.credential_distribution.CredentialDistributionClient;
import org.peertrust.demo.credential_distribution.CredentialResponse;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.event.PTEventListener;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.meta.MetaInterpreter;
import org.peertrust.net.EntitiesTable;
import org.peertrust.net.Message;
import org.peertrust.net.Peer;
import java.io.File;
import java.util.*;

/**
 * PeerTrustClient mainly wrapped TrustClient. thus allowing an
 * easier setup of the wrapped TrustClient with runtime data about
 * the peer on the http server side.
 *  
 * @author Patrice Congo (token77)
 */
public class PeerTrustClient   /*implements PTEventListener*/{
	/** time out for peertrust client*/
	final static public int TIMEOUT = 30*000 ;
	
	/** sleep interval duration for peertrust client*/
	final static public int SLEEP_INTERVAL = 100 ;
	
	/**context path of the web application responsible for peertrust communication */
	private String appContextStr;
	
	/** the ip of the http server*/
	private String serverPeerIP;
	/** the port at which the htp server listen for connections*/
	private int serverPeerPort;
	
	/** the name of the remote peer in the http server*/
	private String serverPeerName;
	
	/**represents the local peer*/
	private Peer localPeer;
	
	/** Represents the remote peer e.g. in the http server*/
	private Peer serverPeer;
	
	/** applet logger*/
	private Logger logger;
	
	/** the name of the configuration file (.rdf file)*/
	private String configFileName;
	
	/** the trust client for peertrust negotiation*/
	private TrustClient trustClient;
	
	
	/** the alias of the local peer*/
	private String localPeerName;
	
	/** the client prolog file, which contains rules policies and credential*/
	private File prologFile;
	
	/**Executable used to provide the objects with the 
	 * capability to show info to the user*/
	private Executable userAlert;
	/** Executable to execute after a restart of the peer trust client*/
	private Executable todoAfterPeerTrustRestart;
	
	/** the communication factory, which need to be initialised with 
	 * http server data
	 */
	private ClientSideHTTPCommunicationFactory comFac;
	
	/**
	 * Create a PeertrustClient using hhtp server info passed as properties
	 * object. The construction is delegated to #construct();
	 * 
	 * @param props
	 * @param configFile
	 * @param prologFile
	 * @param userAlert
	 * @throws Exception
	 */
    public PeerTrustClient(
    			Properties props, 
				String configFile,
				File prologFile,
				Executable userAlert) throws Exception
	{
    	this.configFileName=configFile;
    	this.prologFile=prologFile;
    	this.userAlert=userAlert;
    	construct(props);
    	
        return;
    }
    
    /**
     * Does construct a peertruct client.
     * 
     * @param props
     * @throws Exception
     */
    private void construct(Properties props) throws Exception
    {
			//AllPermission ap= new AllPermission();
			//makeCmdWorker();
			
			logger=Utils.getLogger(this.getClass().getName());
			
			setParameters(props);
						
			startPeerTrustClient();
			
		}
    
    /**
     * Sets local parameters with runtime info from the http server  
     * @param params -- a Properties object containig the server runtime info
     */
    private void setParameters(Properties params)
    {
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
    
    /**
     * Logs a message.
     * @param mes
     */
    private void showMessage(String mes)
    {
    	if(logger!=null){
    		logger.info(mes);
    	}
    	return;
    }
    
    /**
     * Log an Execption.
     * @param th -- the exception to log
     * @param mes -- the message to log with
     */
    private void showMessage(Throwable th, String mes)
    {
    	if(logger!=null){
    		logger.error(mes,th);
    	}
    	return;
    }
    

    
    /**
     * Start the Peertrust client
     * @throws ConfigurationException
     */
    private void startPeerTrustClient() throws ConfigurationException
    {
    	showMessage("startPeerTrustClient.............................");
    	
    	String defaultConfigFile = null;

		if(configFileName==null){
//			defaultConfigFile=
//				ConfigurationOption.getPTClientConfigFilePath();
			throw new ConfigurationException("configFileName not set before");
		}else{
			defaultConfigFile=configFileName;
		}
		
		showMessage("defaulConfigFile:"+defaultConfigFile);

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
			ptServer.setConfigNotEnded(false);
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
			PTEventListener el= (PTEventListener)trustClient.getComponent(Vocabulary.EventListener);
			if(el instanceof PTCommunicationASP){
				((PTCommunicationASP)el).setCommunicationChannelFactory(comFac);
			}else{
				throw new ConfigurationException(
						"A PTCommunicationASP is expected as pt listener but"+
						" found "+el);
			}
			
			trustClient.setTimeout(TIMEOUT);
			trustClient.setSleepInterval(SLEEP_INTERVAL);
			showMessage("comfac:"+comFac);			
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
			showMessage(e,"Problem staring pt client");
			throw(e);
		} catch(Throwable th){
			showMessage(th,"Unexpected Exception while starting pt client");
		}
		return;
    }
    /**
     * Specifies the tasks to execute to restart the TrustClient.
     * E.g. use when restart is necessary after policy file has 
     * been modified 
     * @return an Executable which can be use to restart the TrustClient
     * 			by calling it execute() method
     */
    private Executable makeRestartTrustClientTask()
    {
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
							showMessage(th,"Credential not installed due to exception");
							userAlert.execute("Credential not installed due to exception:\n"+
										"Message:"+th.getMessage());
						}												
					}
				}    		
    	};   
    	
    	return exe;
    }
    
    /**
     * Creates a credebtial distribution client. 
     * Such a client is use to request credentials from the http server.
     *  
     * @return a CredentialDistributionClient
     */
    private CredentialDistributionClient makeCredentialDistributionClient()
    {
    	CredentialDistributionClient cdc=
    		new CredentialDistributionClient();
    	cdc.setup(
    			trustClient,
    			makeRestartTrustClientTask());
    	return cdc;
    }
    
    /**
     * To request a named credential.
     * The task is delegated to a CredentialDistributionClient.
     * @param name -- the name of the credential.
     */
    public void requestCredential(String name)
    {
    	CredentialDistributionClient cdc=
    			makeCredentialDistributionClient();
    	cdc.requestCredential(name,localPeer,serverPeer);
    	
    }
    
	/**
	 * Destroys the PeerTrustClient.
	 */
    public void destroy()
    { 
    	stopTrustClient(); 	
    }
    
    /**
     * Stops the trust client.
     *
     */
    public void stopTrustClient()
    { 
    	comFac.destroy();
    	trustClient.destroy();
    	comFac=null;
    	trustClient=null;
    }
    
    /**
     * To restart the trust client.
     * @throws ConfigurationException
     */
    public void restartTrustClient() throws ConfigurationException 
    {
    	stopTrustClient();
//    	comFac.destroy();
//    	trustClient.destroy();
//    	comFac=null;
//    	trustClient=null;
//    	
    	//restart
    	startPeerTrustClient();
    }

	/**
	 * @return Returns the trustClient.
	 */
	public TrustClient getTrustClient() 
	{
		return trustClient;
	}


	/**
	 * @return Returns the localPeer.
	 */
	public Peer getLocalPeer() 
	{
		return localPeer;
	}


	/**
	 * @return Returns the serverPeer.
	 */
	public Peer getServerPeer() 
	{
		return serverPeer;
	}     
	
	public void sendToHttpServer(Message mes)
	{
		comFac.createNetClient().send(mes,serverPeer);
	}

	/**
	 * @return Returns the prologFile.
	 */
	public File getPrologFile() 
	{
		return prologFile;
	}

	/**
	 * Sets the prolog file
	 * @param prologFile The prologFile to set.
	 */
	public void setPrologFile(File prologFile) 
	{
		this.prologFile = prologFile;
	}
	/**
	 * Returns the executable that is invoque 
	 * after TrustClient have been restarted
	 * @return todoAfterPeerTrustRestart
	 */
	public Executable getTodoAfterPeerTrustRestart() 
	{
		return todoAfterPeerTrustRestart;
	}

	public void setTodoAfterPeerTrustRestart(
				Executable todoAfterPeerTrustRestart) 
	{
		this.todoAfterPeerTrustRestart = todoAfterPeerTrustRestart;
	}
	
}
