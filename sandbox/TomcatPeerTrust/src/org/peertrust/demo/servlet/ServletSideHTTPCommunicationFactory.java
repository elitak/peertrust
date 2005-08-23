/*
 * Created on 20.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.demo.common.ConfigurationOption;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.NetClient;
import org.peertrust.net.NetServer;
import org.peertrust.net.Peer;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServletSideHTTPCommunicationFactory 
							implements AbstractFactory, Configurable {
	private Logger logger;
	private String httpServerIP;
	private int httpServerPort;
	
	private ServletSideNetServer netServer;
	private ServletSideNetClient netClient;
	
	
	public ServletSideHTTPCommunicationFactory(){
	
	}
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#getServerPeer(java.lang.String)
	 */
	public Peer getServerPeer(String alias) {		
		return new Peer(alias, httpServerIP,httpServerPort);
	}

	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetClient()
	 */
	public NetClient createNetClient() {
		logger.info("getting ServletSideNetClient............");
		return netClient;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetServer()
	 */
	public NetServer createNetServer() {
		logger.info("getting ServletSideNetServer............");
		return netServer;
	}

	
	public void init() throws ConfigurationException {
		logger=ConfigurationOption.getLogger(this.getClass().getName());
        createUniqueNetserver();
        createUniqueNetClient();
        //create peer
        
	}
	
	private void createUniqueNetserver(){
//		create the UNIQUE peertrust server that will be use during communication
		netServer=
			new ServletSideNetServer(logger);
		return;
	}
	
	private void createUniqueNetClient(){
		netClient= 
			new ServletSideNetClient(logger);

	}
	
	
	public void addPeerTrustCommunicationListener(
								String finalDestination,
								PeerTrustCommunicationListener comHelper) {
		netClient.addPeerTrustCommunicationListener(
											finalDestination, 
											comHelper);
	}
	
	public void removeAllPeerTrustCommunicationListener() {
		netClient.removeAllPeerTrustCommunicationListener();
	}
	
	public void removePeerTrustCommunicationListener(String finalDestination) {
		netClient.removePeerTrustCommunicationListener(finalDestination);
	}
	
	public void addMessage(Object mes) {
		netServer.addMessage(mes);
	}
	
	
	public boolean getIsListening() {
		return netServer.getIsListening();
	}
}