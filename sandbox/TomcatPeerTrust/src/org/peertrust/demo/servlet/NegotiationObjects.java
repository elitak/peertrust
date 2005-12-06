/*
 * Created on 15.04.2005
 */
package org.peertrust.demo.servlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.*;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.StartLogFactor5;
import org.peertrust.PTEngine;
import org.peertrust.config.PTConfigurator;
import org.peertrust.config.Vocabulary;
import org.peertrust.TrustClient;
import org.peertrust.demo.common.Messenger;
import org.peertrust.demo.common.RDFConfigFileUpdater;
import org.peertrust.demo.common.Utils;
import org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.demo.resourcemanagement.TrustManager;
import org.peertrust.demo.session_registration.HttpSessionRegistrationRequest;
import org.peertrust.event.PTEventListener;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.EntitiesTable;
import org.peertrust.net.Peer;

/**
 * NegotiationObjects represent a collection of objects
 * participating in the trust negotiation. 
 * 
 * @author Patrice Congo (token77)
 */
public class NegotiationObjects  
				implements 	/*PeerTrustCommunicationListener,*/ 
							NegotiationObjectRepository{
	static private NegotiationObjects negotiationObjects;
	
	/**
	 * The pt configurator used to create the trust objects
	 */
	PTConfigurator config;
	/** 
	 * the local peertrust client
	 */
	private TrustClient trustClient;
	
	/** the trustclient communication factory*/
	private ServletSideHTTPCommunicationFactory comFactory=null;
	
	/** the path of the peertrust rdf config file*/
	private String configFilePath=null;
	
	/** mesage logger*/
	private Logger logger;
	
//	/** 
//	 * contained registered sessions; A key is the peer
//	 * peer for which the session is registered.
//	 */
//	private Hashtable sessionTable=new Hashtable();
	
	/**
	 * the manager for the trust negotiation
	 */
	private TrustManager trustManager;
	
	/**
	 *The path to the trust manager config file 
	 */
	private String _TrustManagerConfigFile;
	
	/**
	 * A registerer for peertrust http session
	 */
	private PTComASPMessageListener httpSessionRegisterer; 
	
	/**
	 * Creates a new NegotiationObjects object from a ServletConfig
	 * The ServletConfig contains throws its ServlerContext init-parameters
	 * to build the negotiation objects
	 *  
	 * @param config -- the servlet config used for creation
	 */
	public NegotiationObjects(ServletConfig config)
	{
		this(config.getServletContext());
	}
	
	/**
	 * Creates a new NegotiationObjects object using init-parameters
	 * stored in a servlet context.
	 * The context contains these initials parameter:
	 * <ul>
	 * 		<li/>peetrustFolderRelativePath = the web application contex 
	 * 		absolut url of the directory containing the peertrust files. 
	 * 		The current implementation expects the folder to be in 
	 * 		the tomcat application directory.		
	 * 		<li/>serverPTInstallXML = the web application contex absolut 
	 * 		url to the server xml installation specification.
	 * 		<li/>TrustManagerConfigFile =  the web application contex 
	 * 		absolut url to the peertrust .rdf config file
	 * </ul>
	 * All these parameters a defined in the application web.xml.
	 * 
	 * @param 	context -- the context of the servlet that is creating the
	 * 			new NegoatiationObjects instance 
	 */
	public NegotiationObjects(ServletContext  context)
	{
		try{
	        logger=Utils.getLogger(NegotiationObjects.class.getName());
	        configFilePath=
	        	context.getRealPath(
	        			context.getInitParameter("peetrustFolderRelativePath"));
	        File file=
	        	new File(
	        			context.getRealPath(
	        					context.getInitParameter("serverPTInstallXML")));
	        
	        RDFConfigFileUpdater updater= 
	        	new RDFConfigFileUpdater(
	        			file.getName(),//context.getRealPath(context.getInitParameter("serverPTInstallXML")),
						file.getParent());//context.getRealPath(context.getInitParameter("peetrustFolderRelativePath")));
	        updater.update();
	        configFilePath=updater.getRDFConfigFile();//updater.onfigFile.toURI().toString();
	        _TrustManagerConfigFile=
	        	context.getRealPath(context.getInitParameter("TrustManagerConfigFile"));
	        
		}catch(Throwable th){
			logger.error("-- error while constructing trust objects--",th);
		}
	}
	
	
	/**
	 * Creates and adds a negotiationObjects to the context 
	 * if it does not already exist in that servlet context from
	 * a servlet config.
	 * 
	 * @param config -- the servlet config of the creating servlet.
	 * @return 	a new Negotiation object of the negotiation object
	 * 			cached in the contex if one exists
	 */
	static public NegotiationObjects createAndAddForAppContext(
													ServletConfig config)
	{
		return createAndAddForAppContext(config.getServletContext());
	}
	
	/**
	 * Creates and add negotiationObjects to the context 
	 * if it does not already exist in that servlet context.
	 * 
	 * @param context -- the creation context.
	 * @return 	a new Negotiation object of the negotiation object
	 * 			cached in the contex if one exists
	 */
	static synchronized 
			public NegotiationObjects createAndAddForAppContext(
													ServletContext context)
	{
		if(negotiationObjects!=null){
			return negotiationObjects;
		}
		
		if(context==null){
			return null;
		}
		
		try {
			Object obj=
				context.getAttribute(
						NegotiationObjects.class.getName());
			context.log("NegoObjectsClass:"+obj);
			negotiationObjects= 
					new NegotiationObjects(context);
			NegotiationObjects.startTrustServer(
								negotiationObjects.configFilePath,
								negotiationObjects.logger,
								negotiationObjects);
//			negotiationObjects.startTrustServer();
//			negotiationObjects.makeTrustManager();
//			negotiationObjects.makeHttpSessionRegisterer();
////			negotiationObjects.setAttribute(	NegotiationObjects.class.getName(), 
////										negoObjects);
			negotiationObjects.logger.info("NegotiationObjects created\n");
			return negotiationObjects;
		
		} catch (Throwable th) {
			context.log("error creating nego objects", th);
	        return null;
		}
		
	}
	
	/**
	 * Starts the peertrust client of the server.
	 */
	private void startTrustServer(){
		String PREFIX = "Servlet internal peertrust server app.: ";
		logger.info(PREFIX+"starting................");

		String defaultComponent = Vocabulary.PeertrustEngine.toString() ;
		
		String[] components = { defaultComponent } ;
		String[]configFiles={configFilePath};
		try {		
			trustClient= new TrustClient(configFiles,components);
		} catch (ConfigurationException e) {
			logger.error("--starting peertrust server fail",e);
			throw new Error("--starting peertrust server fail",e);
		}
		try {
			PTEngine engine = 
				(PTEngine) trustClient.getComponent(Vocabulary.PeertrustEngine);	
			comFactory=
				(ServletSideHTTPCommunicationFactory)
						trustClient.getComponent(
								Vocabulary.CommunicationChannelFactory);
			PTEventListener el= (PTEventListener)trustClient.getComponent(Vocabulary.EventListener);
			if(el instanceof PTCommunicationASP){
				((PTCommunicationASP)el).setCommunicationChannelFactory(comFactory);
			}else{
				throw new ConfigurationException(
						"A PTCommunicationASP is expected as pt listener but"+
						" found "+el);
			}
			
			logger.info(PREFIX + 
						"Started with this ptevent listener"+
						engine.getEventDispatcher()) ;
		} catch (Throwable th) {
			logger.error("--cannot start server --",th);
		}
	}
	
	
	
	/**
	 * Destroys the NegotiationObjects and the objects it contains.
	 * 
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#destroy()
	 */
	public void destroy(){
				
		try {			
			trustClient.destroy();
		} catch (RuntimeException e) {
			logger.debug("Error while destroying trustClient",e);
		}
		
	}
	
	/**
	 * Makes a new trust manager.
	 */
	private void makeTrustManager(){
		throw new Error("makeTrustManager() is not in use anymore");
////		trustManager= 
////			new TrustManager(
////						trustClient,
////						_TrustManagerConfigFile);
//		trustManager= 
//			new TrustManager();
//		trustManager.setTrustClient(trustClient);
//		trustManager.setSetupFilePath(_TrustManagerConfigFile);
//		try {
//			trustManager.init();
//		} catch (ConfigurationException e) {
//			throw new Error("Coud not create and init TrustManager");
//		}
	}
	
	/**
	 * @return Returns the trust manager
	 *  
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getTrustManager()
	 */
	public TrustManager getTrustManager(){
		return this.trustManager;
	}
	
	
//	/**
//	 * 
//	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#registerSession(java.lang.String, org.peertrust.net.Peer)
//	 */
//	public void registerSession(String key, Peer peer){
//		sessionTable.put(key, peer);
//	}
	
	/**
	 * To get the peer registern for the spefied session
	 * @param key -- the registered session key
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getSessionPeer(java.lang.String)
	 */
	public Peer getSessionPeer(String key){
		if(key==null){
			return null;
		}
		//return (Peer)sessionTable.get(key);
		return trustManager.getSessionRegisterer().getSessionPeer(key);
	}

	/**
	 * @return Returns the trust client.
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getTrustClient()
	 */
	public TrustClient getTrustClient() {
		return trustClient;
	}
	
	/**
	 * Constructs the http session registerer.
	 * It listens to registration requests and does 
	 * the registration steps.
	 *
	 */
	public void makeHttpSessionRegisterer(){
		throw new Error("makeHttpSessionRegisterer() is no more in use");
//		PTComASPMessageListener l= new PTComASPMessageListener(){
//
//			public void PTMessageReceived(	
//										Serializable message,
//										Peer source, 
//										Peer target) 
//			{
//				logger.info(
//						"************************Registerering log**********************\n"+
//						message+	
//						"\nSource Peer:"+source+
//						"\n************Registerering log END**********************");
//				//register session
//				if(message instanceof HttpSessionRegistrationRequest){
//					
//					HttpSessionRegistrationRequest req=
//						(HttpSessionRegistrationRequest)message;
//					if(req.doMakeRegistration()){
//						String sessionId=
//							((HttpSessionRegistrationRequest)message).getSessionKey();
//						sessionTable.put(sessionId, source);
//						
//						//register peer to entity table
//						EntitiesTable entitiesTable = 
//							(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
//						entitiesTable.put(source.getAlias(),source);
//					}else if(req.doRemoveRegistration()){
//						sessionTable.remove(req.getSessionKey());
//						EntitiesTable entitiesTable = 
//							(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
//						entitiesTable.remove(source.getAlias());
//						try {
//							getMessenger().closeChannel(source);
//						} catch (IOException e) {
//							logger.debug(	
//									"Unregistering;Could not close channel "+
//											"for "+source,
//									e);
//						}
//					}
//				}else{	
//					logger.debug("Cannot handle "+message.getClass());
//				}
//			}
//			
//		};
//		
//		if(trustClient==null){
//			throw 
//				new RuntimeException("TrustClient not started therefore Registerer cannot be registered\n"+
//								"to listen to HttpSessionRegistrationRequest");
//		}else{
//			PTEventListener eventL=
//				(PTEventListener)trustClient.getComponent(Vocabulary.EventListener);
//			if(eventL instanceof PTCommunicationASP){
//				this.httpSessionRegisterer = l;
//				((PTCommunicationASP)eventL).registerPTComASPMessageListener(
//													this.httpSessionRegisterer,
//													HttpSessionRegistrationRequest.class);
//				
//			}else{
//				throw new Error("Cannot  register HttpRegisterer since pt event listener is\n"+
//								"not a PTCommunicationASP but a "+eventL.getClass());
//			}
//		}
	}

	/**
	 * @return Returns the messenger of the comfactory.
	 * 
	 * @see org.peertrust.demo.servlet.ServletSideHTTPCommunicationFactory#getMessenger()
	 */
	public Messenger getMessenger() {
		return comFactory.getMessenger();
	}
	
	
	/**
	 * Removed all session entries linked to the specified 
	 * peer;
	 * @param 	leavingPeer -- the peer which entries will be
	 * 			removed.
	 */
	public void  removePeertrustSessionEntries(Peer leavingPeer){
		trustManager.getSessionRegisterer().removePeertrustSessionEntries(leavingPeer);
//		if(leavingPeer==null){
//			return;
//		}
//		
//		String leavingPeerAlias=leavingPeer.getAlias();
//		
//		if(leavingPeerAlias==null){
//			return;
//		}
//		
//		for(	Iterator it=sessionTable.entrySet().iterator(); 
//				it.hasNext();){
//			Map.Entry entry=(Map.Entry)it.next();
//			
//			if(leavingPeerAlias.equals((String)entry.getValue())){
//				sessionTable.remove(entry.getClass());
//				
//			}
//			
//		}
//		
//		EntitiesTable entitiesTable = 
//			(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
//		entitiesTable.remove(leavingPeerAlias);
//		try {
//			getMessenger().closeChannel(leavingPeer);
//		} catch (IOException e) {
//			logger.debug(	
//					"Unregistering;Could not close channel "+
//							"for "+leavingPeer,
//					e);
//		}
		
	}
	
	
	static private PTConfigurator startTrustServer(
										String configFilePath,
										Logger logger,
										NegotiationObjects negoObjects)
										throws Exception
	{
		String PREFIX = "Servlet internal peertrust server app.: ";
		logger.info(PREFIX+"starting................");

		//String defaultComponent = Vocabulary.PeertrustEngine.toString() ;
		String defaultComponent = Vocabulary.TrustManager.toString() ;
		String[] components = { defaultComponent } ;
		String[]configFiles={configFilePath};
		
		PTConfigurator config = new PTConfigurator() ;

		config.startApp(configFiles, components) ;
		
		if(negoObjects!=null){
			negoObjects.comFactory= 
				(ServletSideHTTPCommunicationFactory)
					config.getComponent(Vocabulary.CommunicationChannelFactory);
			negoObjects.trustClient=
				(TrustClient)config.getComponent(Vocabulary.TrustClient);
			negoObjects.trustManager=
				(TrustManager)config.getComponent(Vocabulary.TrustManager);
			negoObjects.config=config;
			
		}
		
		return config;
		
//		try {		
//			trustClient= new TrustClient(configFiles,components);
//		} catch (ConfigurationException e) {
//			logger.error("--starting peertrust server fail",e);
//			throw new Error("--starting peertrust server fail",e);
//		}
//		try {
//			PTEngine engine = 
//				(PTEngine) trustClient.getComponent(Vocabulary.PeertrustEngine);	
//			comFactory=
//				(ServletSideHTTPCommunicationFactory)
//						trustClient.getComponent(
//								Vocabulary.CommunicationChannelFactory);
//			PTEventListener el= (PTEventListener)trustClient.getComponent(Vocabulary.EventListener);
//			if(el instanceof PTCommunicationASP){
//				((PTCommunicationASP)el).setCommunicationChannelFactory(comFactory);
//			}else{
//				throw new ConfigurationException(
//						"A PTCommunicationASP is expected as pt listener but"+
//						" found "+el);
//			}
//			
//			logger.info(PREFIX + 
//						"Started with this ptevent listener"+
//						engine.getEventDispatcher()) ;
//		} catch (Throwable th) {
//			logger.error("--cannot start server --",th);
//		}
	}
	
	static public void main(String[] args)throws Exception{
		String rdf_config_file=
			"file:///home/congo/eclipse_base/workspace_3_1/TomcatPeerTrust/web/PeerTrustConfig/demoPeertrustConfig.server.rdf";
		Logger logger=Logger.getLogger(NegotiationObjects.class);
		PTConfigurator config=
			startTrustServer(
						rdf_config_file,
						logger,
						null);
		System.out.println("\n*********************************************************");
		System.out.println(
				"#PeertrustClient:"+
				config.getComponent(Vocabulary.TrustClient));
		System.out.println(
				"TrustManager:"+
				config.getComponent(Vocabulary.TrustManager));
		System.out.println(
				"PolicyEvaluator:"+
				config.getComponent(Vocabulary.PolicyEvaluator));
		System.out.println(
				"PolicySystem"+
				config.getComponent(Vocabulary.PolicySystem));
		System.out.println(
				"ResourceClassifier:"+
				config.getComponent(Vocabulary.ResourceClassifier));
		System.out.println(
				"SessionRegisterer:"+
				config.getComponent(Vocabulary.SessionRegisterer));
		System.out.println(
				"Messenger:"+
				config.getComponent(Vocabulary.Messenger));
	}
}
