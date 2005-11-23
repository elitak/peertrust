/*
 * Created on 20.04.2005
 */
package org.peertrust.demo.servlet;

import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.demo.common.Messenger;
import org.peertrust.demo.common.QueueBasedMessenger;
import org.peertrust.demo.common.Utils;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.NetClient;
import org.peertrust.net.NetServer;
import org.peertrust.net.Peer;

/**
 * @author Patrice Congo (token77)
 */
public class ServletSideHTTPCommunicationFactory 
							implements AbstractFactory, Configurable {
	private Logger logger;
	private String httpServerIP;
	private int httpServerPort;
	
	private Messenger messenger;
	private ServletSideNetServer netServer;
	private ServletSideNetClient netClient;
	
	
	public ServletSideHTTPCommunicationFactory()
	{
		//nothing
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
		messenger= new QueueBasedMessenger();
		logger=Utils.getLogger(this.getClass().getName());
        createUniqueNetserver();
        createUniqueNetClient();
	}
	
	private void createUniqueNetserver(){
//		create the UNIQUE peertrust server that will be use during communication
		netServer=
			new ServletSideNetServer(
									logger,
									getServerPeer("elearn"),
									messenger);//TODO change hard coded
		return;
	}
	
	private void createUniqueNetClient(){
		netClient= 
			new ServletSideNetClient(logger,messenger);

	}
	
	
	/**
	 * @return Returns the httpServerIP.
	 */
	public String getHttpServerIP() {
		return httpServerIP;
	}
	/**
	 * @param httpServerIP The httpServerIP to set.
	 */
	public void setHttpServerIP(String httpServerIP) {
		this.httpServerIP = httpServerIP;
	}
	/**
	 * @return Returns the httpServerPort.
	 */
	public int getHttpServerPort() {
		return httpServerPort;
	}
	/**
	 * @param httpServerPort The httpServerPort to set.
	 */
	public void setHttpServerPort(int httpServerPort) {
		this.httpServerPort = httpServerPort;
	}
	
	/**
	 * @return Returns the _host
	 */
	public String getHost() {
		return httpServerIP;//_host;
	}
	/**
	 * @param _host The _host to set.
	 */
	public void setHost(String _host) {
		//this._host = _host;
		this.httpServerIP=_host;
	}
	
	/**
	 * @return Returns the _port.
	 */
	public int getPort() {
		return httpServerPort;//_port;
	}
	
	/**
	 * @param _port The _port to set.
	 */
	public void setPort(int _port) {
		//this._port = _port;
		this.httpServerPort=_port;
	}
	/**
	 * @return Returns the messenger.
	 */
	public Messenger getMessenger() {
		return messenger;
	}
	/**
	 * @param messenger The messenger to set.
	 */
	public void setMessenger(Messenger messenger) {
		this.messenger = messenger;
	}
		
	
	
}
