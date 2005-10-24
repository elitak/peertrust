/*
 * Created on 15.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import java.io.File;
import java.io.Serializable;

import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.peertrust.PTEngine;
import org.peertrust.config.Vocabulary;
import org.peertrust.TrustClient;
import org.peertrust.demo.common.ConfigurationOption;
import org.peertrust.demo.common.RDFConfigFileUpdater;
import org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.demo.resourcemanagement.TrustManager;
import org.peertrust.demo.session_registration.HttpSessionRegistrationRequest;

import org.peertrust.event.PTEventListener;
import org.peertrust.exception.ConfigurationException;
//import org.peertrust.inference.InferenceEngine;

import org.peertrust.net.EntitiesTable;
import org.peertrust.net.Message;
import org.peertrust.net.Peer;
/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NegotiationObjects implements PeerTrustCommunicationListener, NegotiationObjectRepository{
	//private static final long serialVersionUID = 23487495895819393L;
	
	private ServletSideNetClient netClient=null;
	private ServletSideNetServer netServer=null;
	private ServletSideHTTPCommunicationFactory comFactory=null;
	
	private String configFilePath=null;
	private PTEngine engine;
	private Logger logger;
	private StringBuffer freePageList= new StringBuffer();
	//private InferenceEngine inferenceEngine;
	private Hashtable messagePool;
	private Hashtable sessionTable=new Hashtable();
	private TrustClient trustClient; 
	private TrustManager trustManager;
	//private CredentialDistributionServer credentialDistributionServer;
	
	//private String _ResourceClassifierSetupFile;
	//private String _ResourcePoliciesSetupFile;
	//private String _RequestServingMechanismPoolSetupFile;
	private String _TrustManagerConfigFile;
	private PTComASPMessageListener httpSessionRegisterer; 
	
	//private static Context demoCtx;
	//private static Context envCtx;
	private ServletConfig servletConfig;
	public NegotiationObjects(ServletConfig config){
		this(config.getServletContext());
		this.servletConfig=config;
	}
	
	public NegotiationObjects(ServletContext  context){
		try{
			messagePool= new Hashtable();
			 
	        logger=ConfigurationOption.getLogger(NegotiationObjects.class.getName());
	        String list= context.getInitParameter("freePages");
	        //peetrustConfigFileRelativePath
	        configFilePath=context.getRealPath(context.getInitParameter("peetrustFolderRelativePath"));
	        File file=new File(context.getRealPath(context.getInitParameter("serverPTInstallXML")));
	        System.out.println("\n**********************************************************");
	        System.out.println("launchPage:"+context.getInitParameter("launchPage"));
	        System.out.println("companyName:"+context.getInitParameter("companyName"));
	        System.out.println("**********************************************************");
	        
	        RDFConfigFileUpdater updater= 
	        	new RDFConfigFileUpdater(
	        			file.getName(),//context.getRealPath(context.getInitParameter("serverPTInstallXML")),
						file.getParent());//context.getRealPath(context.getInitParameter("peetrustFolderRelativePath")));
	        updater.update();
	        configFilePath=updater.getRDFConfigFile();//updater.onfigFile.toURI().toString();
	        if(list!=null){
	        	freePageList.append(list);
	        }
	        
	        _TrustManagerConfigFile=
	        	context.getRealPath(context.getInitParameter("TrustManagerConfigFile"));
	        
		}catch(Throwable th){
			th.printStackTrace();
			logger.error("-- error while constructing trust objects--",th);
		}
	}
	
	
	/**
	 * @return Returns the trustClient.
	 */
	public ServletSideNetClient getNetClient() {
		return netClient;
	}
	/**
	 * @param trustClient The trustClient to set.
	 */
	public void setNetClient(ServletSideNetClient netClient) {
		this.netClient = netClient;
	}
	/**
	 * @return Returns the trustServer.
	 */
	public ServletSideNetServer getNetServer() {
		return netServer;
	}
	/**
	 * @param trustServer The trustServer to set.
	 */
	public void setNetServer(ServletSideNetServer netServer) {
		this.netServer = netServer;
	}
	/**
	 * create and add a negotiationObjects if not already in context
	 * @param config
	 */
	static public NegotiationObjects createAndAddForAppContext(ServletConfig config){
		return createAndAddForAppContext(config.getServletContext());
	}
	
	static public NegotiationObjects createAndAddForAppContext(ServletContext context){
		if(context==null){
			System.out.println("Context ist null: nothing done!");
			return null;
		}
		NegotiationObjects negoObjects=null;
		
		try {
			Object obj=
				context.getAttribute(
						NegotiationObjects.class.getName());
			context.log("NegoObjectsClass:"+obj);
			
//			negoObjects= 
//				(NegotiationObjects)obj;/*context.getAttribute(
//										NegotiationObjects.class.getName());*/
			if(obj==null){
				negoObjects= 
						new NegotiationObjects(context);
				negoObjects.startTrustServer();
				negoObjects.makeTrustManager();
				negoObjects.makeHttpSessionRegisterer();
				context.setAttribute(	NegotiationObjects.class.getName(), 
										negoObjects);
				context.log("NegotiationObjects created\n");
				return negoObjects;
			}else{
				return (NegotiationObjects)obj;
			}
		} catch (Throwable th) {
			context.log("error creating nego objects", th);
	        return null;
		}
		//return negoObjects;
	}
	
	private void startTrustServer(){
		String PREFIX = "Servlet internal peertrust server app.: ";
		logger.info(PREFIX+"starting................");

		String defaultComponent = Vocabulary.PeertrustEngine.toString() ;
		
		String[] components = { defaultComponent } ;
		String[]configFiles={configFilePath};
		try {		
			//config.startApp(configFiles, components) ;
			trustClient= new TrustClient(configFiles,components);
			//config=trustClient.getPTConfigurator();
		} catch (ConfigurationException e) {
			logger.error("--starting peertrust server fail",e);
		}
		try {
			//engine = (PTEngine) config.getComponent(Vocabulary.PeertrustEngine);
			engine = (PTEngine) trustClient.getComponent(Vocabulary.PeertrustEngine);	
			comFactory=
				(ServletSideHTTPCommunicationFactory)
						trustClient.getComponent(
								Vocabulary.CommunicationChannelFactory);
			//comFactory.setHttpServerIP(servletConfig.getServletContext().getRequestDispatcher("/demo").);
			netClient=(ServletSideNetClient)comFactory.createNetClient();
			netServer=(ServletSideNetServer)comFactory.createNetServer();
			netClient.setPeerTrustCommunicationListener(this);
			
			//netClient.addPeerTrustCommunicationListener(this);
//			EventDispatcher dispatcher = engine.getEventDispatcher() ;
//			PTEventListener eventL= engine.getEventListener();
//			if(eventL instanceof PTCommunicationASP){
//				dispatcher.register(eventL, NewMessageEvent.class);
//			}else{
//				throw new Error("PTCommunicationASP but found "+eventL.getClass()+
//								" as PTEventListener	");
//			}
			logger.info(PREFIX + 
						"Started with this ptevent listener"+
						engine.getEventDispatcher()) ;
		} catch (Throwable th) {
			logger.error("--cannot start server --",th);
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#destroy()
	 */
	public void destroy(){
				
		try {			
			trustClient.destroy();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
	}
	
	private void makeTrustManager(){
		trustManager= 
			new TrustManager(
						trustClient,
						_TrustManagerConfigFile);		
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getTrustManager()
	 */
	public TrustManager getTrustManager(){
		return this.trustManager;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#addMessage(java.lang.Object)
	 */
	public void addMessage(Object mes) {
		comFactory.addMessage(mes);
	}
//	public void addPeerTrustCommunicationListener(String finalDestination,
//			PeerTrustCommunicationListener comHelper) {
//		comFactory.addPeerTrustCommunicationListener(finalDestination,
//				comHelper);
//	}
	public void removeAllPeerTrustCommunicationListener() {
		comFactory.removeAllPeerTrustCommunicationListener();
	}
//	public void removePeerTrustCommunicationListener(String finalDestination) {
//		comFactory.removePeerTrustCommunicationListener(finalDestination);
//	}
	
	
	public boolean getIsListening() {
		return comFactory.getIsListening();
	}
	
	static public boolean checkPassword(HttpServletRequest request,
										ServletContext context,String userNameKey){
		//String userName=request.getParameter("userName");
		if(userNameKey==null){
			return false;
		}else{
			String password= request.getParameter("password");
			String initPassword=context.getInitParameter(userNameKey);
			if(password==null ){
				return false;
			}else if(initPassword==null){			
				return false;
			}else{
				return password.equals(initPassword);
			}
		}
	}
	
	
	
	
	
	public String[] getAnswers(long queryId) {
		return trustClient.getAnswers(queryId);
	}

	public boolean isQueryFinished(long queryId) {
		return trustClient.isQueryFinished(queryId);
	}

	public Boolean isQuerySuccessful(long queryId) {
		return trustClient.isQuerySuccessful(queryId);
	}

	public void removeQuery(long id) {
		trustClient.removeQuery(id);
	}

	public long sendQuery(String query) {
		return trustClient.sendQuery(query);
	}

	public Boolean waitForQuery(long id) {
		return trustClient.waitForQuery(id);
	}

	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getMessageFIFO(org.peertrust.net.Peer)
	 */
	synchronized public BlockingQueue getMessageFIFO(Peer peer){
		//Object toSend=null;
		//negoObjects.addPeerTrustCommunicationListener((Peer)obj, this);
		String key=peer.getAlias();
		ArrayBlockingQueue messageFIFO=
			(ArrayBlockingQueue)messagePool.get(key);
		
		if(messageFIFO==null){
				//create message fifo
			System.out.println("\n======>getMessageFIFO for"+peer+"\n");
		        messageFIFO= new ArrayBlockingQueue(8);
				messagePool.put(key,messageFIFO);
				//this.addPeerTrustCommunicationListener(key, this);
				this.registerNewPeerEntity(peer);
		}
		
		return messageFIFO;
	}	
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#removeMessageFIFO(java.lang.String)
	 */
	synchronized public BlockingQueue removeMessageFIFO(String key){
		return (BlockingQueue)messagePool.remove(key);
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#removeMessageFIFO(org.peertrust.net.Peer)
	 */
	synchronized public BlockingQueue removeMessageFIFO(Peer key){
		if(key!=null){
			return removeMessageFIFO(key.getAlias());
		}else{
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#send(org.peertrust.net.Message, java.lang.String)
	 */
	public void send(Message mes,String finalDestination) {
		try {
			//((Answer)mes).
			System.out.println("------------------Adding:"+mes+" to queue for"+finalDestination);
			ArrayBlockingQueue queue=
				(ArrayBlockingQueue)messagePool.get(finalDestination);
			queue.offer(mes);
		} catch (Exception e) {
			logger.debug("NOT in messagePool:"+finalDestination,e);
		}
		return;
	}
	
	public boolean isHttpPTCommunicationReady(String finalDestination){
		return messagePool.contains(finalDestination);
	}
	
	private void registerNewPeerEntity(Peer newPeer){
		EntitiesTable entitiesTable = 
			(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
		entitiesTable.put(newPeer.getAlias(),newPeer);
		//netClient.addPeerTrustCommunicationListener(newPeer.getAlias(),this);
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#registerSession(java.lang.String, org.peertrust.net.Peer)
	 */
	public void registerSession(String key, Peer peer){
		sessionTable.put(key, peer);
		//netClient.addPeerTrustCommunicationListener(peer.getAlias(),this);
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getSessionPeer(java.lang.String)
	 */
	public Peer getSessionPeer(String key){
		return (Peer)sessionTable.get(key);
	}

	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getTrustClient()
	 */
	public TrustClient getTrustClient() {
		return trustClient;
	}
	
	public void makeHttpSessionRegisterer(){
		PTComASPMessageListener l= new PTComASPMessageListener(){

			public void PTMessageReceived(Serializable message,Peer source, Peer target) {
				//register session
				if(message instanceof HttpSessionRegistrationRequest){
					HttpSessionRegistrationRequest req=
						(HttpSessionRegistrationRequest)message;
					if(req.doMakeRegistration()){
						String sessionId=
							((HttpSessionRegistrationRequest)message).getSessionKey();
						sessionTable.put(sessionId, source);
						
						//register peer to entity table
						EntitiesTable entitiesTable = 
							(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
						entitiesTable.put(source.getAlias(),source);
						
						//Peer negotiatingPeer=((HttpSessionRegistrationRequest)message).getSource();
						
						
						System.out.println("*********************************Registerering**********************");
						System.out.println("sessionId:"+sessionId);
						System.out.println("Peer:"+source);
						System.out.println("*********************************Registerering END**********************");
					}else if(req.doRemoveRegistration()){
						sessionTable.remove(req.getSessionKey());
						EntitiesTable entitiesTable = 
							(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
						//TODO remove from entity table table 
					}
				}else{	
					System.out.println("Cannot handle "+message.getClass());
				}
			}
			
		};
		if(trustClient==null){
			throw 
				new RuntimeException("TrustClient not started therefore Registerer cannot be registered\n"+
								"to listen to HttpSessionRegistrationRequest");
		}else{
			PTEventListener eventL=
				(PTEventListener)trustClient.getComponent(Vocabulary.EventListener);
			if(eventL instanceof PTCommunicationASP){
				((PTCommunicationASP)eventL).registerPTComASPMessageListener(l,HttpSessionRegistrationRequest.class);
				this.httpSessionRegisterer = l;
			}else{
				throw new Error("Cannot  register HttpRegisterer since pt event listener is\n"+
								"not a PTCommunicationASP but a "+eventL.getClass());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getThis(org.peertrust.demo.servlet.NegotiationObjects[])
	 */
	public NegotiationObjects getThis() throws NullPointerException{
//		if(negoObjectsArray==null){
//			throw new NullPointerException("Parameter must not be null");
//		}
//		
//		if(negoObjectsArray.length!=1){
//			throw new IllegalArgumentException("negoObjectsArray length must be 1");
//		}
//		negoObjectsArray[0]=this;
		return this;
	}
}
