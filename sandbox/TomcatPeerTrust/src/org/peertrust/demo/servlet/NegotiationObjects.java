/*
 * Created on 15.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import java.io.File;
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
import org.peertrust.demo.common.HttpSessionRegistrationRequest;
import org.peertrust.demo.common.RDFConfigFileUpdater;
import org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.demo.resourcemanagement.TrustManager;
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
public class NegotiationObjects implements PeerTrustCommunicationListener{
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
	
	
	//private String _ResourceClassifierSetupFile;
	//private String _ResourcePoliciesSetupFile;
	//private String _RequestServingMechanismPoolSetupFile;
	private String _TrustManagerConfigFile;
	private PTComASPMessageListener httpSessionRegisterer; 
	
	public NegotiationObjects(ServletConfig config){
		this(config.getServletContext());
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
	        
//	        _ResourceClassifierSetupFile=
//	        	context.getRealPath(context.getInitParameter("ResourceClassifierSetupFile"));
//	        _ResourcePoliciesSetupFile=
//	        	context.getRealPath(context.getInitParameter("ResourcePoliciesSetupFile"));
//	        _RequestServingMechanismPoolSetupFile=
//	        	context.getRealPath(context.getInitParameter("RequestServingMechanismPoolSetupFile"));
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
		NegotiationObjects negoObjects=null;
		
		try {
			
			negoObjects= 
				(NegotiationObjects)context.getAttribute(
										NegotiationObjects.class.getName());
			if(negoObjects==null){
				negoObjects= 
						new NegotiationObjects(context);
				negoObjects.startTrustServer();
				negoObjects.makeTrustManager();
				negoObjects.makeHttpSessionRegisterer();
				context.setAttribute(	NegotiationObjects.class.getName(), 
										negoObjects);
				negoObjects.logger.info("NegotiationObjects created");
				
			}
		} catch (Throwable th) {
			th.printStackTrace();
			Logger log=ConfigurationOption.getLogger(NegotiationObjects.class.getName());
			log.error("error creating nego objects", th);
	        
		}
		return negoObjects;
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
			netClient=(ServletSideNetClient)comFactory.createNetClient();
			netServer=(ServletSideNetServer)comFactory.createNetServer();
			
//			EventDispatcher dispatcher = engine.getEventDispatcher() ;

			logger.info(PREFIX + 
						"Started with this ptevent listener"+
						engine.getEventDispatcher()) ;
		} catch (Throwable th) {
			logger.error("--cannot start server --",th);
		}
	}
	
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
//						_ResourceClassifierSetupFile,//"classifierXML",
//						_ResourcePoliciesSetupFile,//"policySystemXML",
//						"EvaluatorXML",
//						_RequestServingMechanismPoolSetupFile);
		
	}
	public TrustManager getTrustManager(){
		return this.trustManager;
	}
	
	public void addMessage(Object mes) {
		comFactory.addMessage(mes);
	}
	public void addPeerTrustCommunicationListener(String finalDestination,
			PeerTrustCommunicationListener comHelper) {
		comFactory.addPeerTrustCommunicationListener(finalDestination,
				comHelper);
	}
	public void removeAllPeerTrustCommunicationListener() {
		comFactory.removeAllPeerTrustCommunicationListener();
	}
	public void removePeerTrustCommunicationListener(String finalDestination) {
		comFactory.removePeerTrustCommunicationListener(finalDestination);
	}
	
	
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

	synchronized public BlockingQueue getMessageFIFO(Peer peer){
		//Object toSend=null;
		//negoObjects.addPeerTrustCommunicationListener((Peer)obj, this);
		String key=peer.getAlias();
		ArrayBlockingQueue messageFIFO=
			(ArrayBlockingQueue)messagePool.get(key);
		
		if(messageFIFO==null){
				//create message fifo
		        messageFIFO= new ArrayBlockingQueue(8);
				messagePool.put(key,messageFIFO);
				this.addPeerTrustCommunicationListener(key, this);
				this.registerNewPeerEntity(peer);
		}
		
		return messageFIFO;
	}	
	
	synchronized public BlockingQueue removeMessageFIFO(String key){
		return (BlockingQueue)messagePool.remove(key);
	}
	
	synchronized public BlockingQueue removeMessageFIFO(Peer key){
		if(key!=null){
			return removeMessageFIFO(key.getAlias());
		}else{
			return null;
		}
	}
	
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
	}
	
	public void registerSession(String key, Peer peer){
		sessionTable.put(key, peer);
	}
	
	public Peer getSessionPeer(String key){
		return (Peer)sessionTable.get(key);
	}

	/**
	 * @return Returns the trustClient.
	 */
	public TrustClient getTrustClient() {
		return trustClient;
	}
	
	public void makeHttpSessionRegisterer(){
		PTComASPMessageListener l= new PTComASPMessageListener(){

			public void PTMessageReceived(Object message) {
				String sessionId=
					((HttpSessionRegistrationRequest)message).getSessionKey();
				Peer negotiatingPeer=((HttpSessionRegistrationRequest)message).getSource();
				sessionTable.put(sessionId, negotiatingPeer);
				System.out.println("*********************************Registerering**********************");
				System.out.println("sessionId:"+sessionId);
				System.out.println("Peer:"+negotiatingPeer);
				System.out.println("*********************************Registerering END**********************");
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
}
