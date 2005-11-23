/*
 * Created on 20.04.2005
 */
package org.peertrust.demo.client;

import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.NetClient;
import org.peertrust.net.NetServer;
import org.peertrust.net.Peer;

/**
 *This class provide an concrete implementation of the AbstractFactory.
 *It is based on ClientSideNetClient and ClientSideNetserver. 
 *They provide mechanisms to do peertrust communication thow an http connection.
 *This "http-tunneling" communication required the adequate http based partner peer.
 *  
 * 
 *@author Patrice Congo
 *@see org.peertrusr.demo.client.ClientSideNetClient
 *@see org.peertrusr.demo.client.ClientSideNetServer
 * 
 */
public class ClientSideHTTPCommunicationFactory implements AbstractFactory, Configurable {
	
	/** represents the server peer in the http server*/
	final private Peer httpServerPeer= new Peer("alias","addi",0);
	/** represents the local (in the browser applet) peer*/
	final private Peer localPeer= new Peer("_local_","_addi_",0);
	/** represents the url of the web application in charge of peertrust the communication*/
	private String webAppURLPath=null;
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	/**represents the applet peer net client which sends 
	 * its messages throw http post*/
	private ClientSideNetClient ptClient;
	
	/** represents the appler peer net server which 
	 * listen to incoming message using http get request.*/ 
	private ClientSideNetServer ptServer;
	
	
	/** 
	 * @see org.peertrust.net.AbstractFactory#getServerPeer(java.lang.String)
	 */
	public Peer getServerPeer(String serverName) {
		localPeer.setAlias(serverName);
		return localPeer;
		
	}

	/**
	 * @see org.peertrust.net.AbstractFactory#createNetClient()
	 */
	public NetClient createNetClient() {
		return ptClient;
	}

	/**
	 * @see org.peertrust.net.AbstractFactory#createNetServer()
	 */
	public NetServer createNetServer() {
		return ptServer;
	}

	/**
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {	
		ptClient= 
			new ClientSideNetClient(webAppURLPath, 
									httpServerPeer,
									/*localPeerAlias,*/
									logger);
		ptServer=
			new ClientSideNetServer(localPeer,	
									httpServerPeer,//getServerPeer(randomAlias),
									webAppURLPath,
									logger);
	}
	
	/**
	 * @return Returns the serverIP.
	 */
	public String getHttpServerIP() {
		return httpServerPeer.getAddress();
	}
	
	/**
	 * @param serverIP The serverIP to set.
	 */
	public void setHttpServerIP(String serverIP) {
		this.httpServerPeer.setAddress(serverIP);
	}
	
	/**
	 * @return Returns the serverPort.
	 */
	public int getHttpServerPort() {
		return httpServerPeer.getPort();//Port;
	}
	
	/**
	 * @param serverPort The serverPort to set.
	 */
	public void setServerPort(int serverPort) {
		this.httpServerPeer.setPort(serverPort);
	}
	
	/**
	 * @return Returns the webAppURLPath.
	 */
	public String getWebAppURLPath() {
		return webAppURLPath;
	}
	/**
	 * @param webAppURLPath The webAppURLPath to set.
	 */
	public void setWebAppURLPath(String webAppURLPath) {
		this.webAppURLPath = webAppURLPath;
		ptClient.setWebAppURLPath(webAppURLPath);
		ptServer.setWebAppURLPath(webAppURLPath);
		return;
	}
	
	/**
	 * @return Returns the serverAlias.
	 */
	public String getHttpServerAlias() {
		return this.httpServerPeer.getAlias();//httpServerAlias;
	}

	/**
	 * @param serverAlias The serverAlias to set.
	 */
	public void setHttpServerAlias(String serverAlias) {
		//this.httpServerAlias = serverAlias;
		this.httpServerPeer.setAlias(serverAlias);
	}

	
	/**
	 * @return Returns the address of the local peer.
	 */
	public String getHost() {
		return localPeer.getAddress();
	}
	
	/**
	 * Sets the address of the local peer.
	 * E.g. use in automatic initialization with .rdf config file.
	 * @param the  new address for the local peer.
	 */
	public void setHost(String _host) {
		//this._host = _host;
		if(_host!=null){
			localPeer.setAddress(_host);
		}
		
	}
	
	/**
	 * @return Returns the port of the local peer.
	 */
	public int getPort() {
		return localPeer.getPort();
	}
	
	/**
	 * Sets the port for the local peer.
	 * @param _port the new integer value for the local peer 
	 * (e.g. use in automatic initialization with .rdf config file).
	 */
	public void setPort(int _port) {
		if(_port>0){
			localPeer.setPort(_port);
		}
	}
		
	/** 
	 * Destroy the communucation factory.
	 * Done by delegation to the destroy methods of the peer trust client and server instances
	 *
	 */
	public void destroy(){
		ptClient.destroy();
		ptServer.destroy();
	}
}
