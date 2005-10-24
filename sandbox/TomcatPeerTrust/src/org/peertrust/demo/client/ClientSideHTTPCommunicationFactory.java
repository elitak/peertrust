/*
 * Created on 20.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClientSideHTTPCommunicationFactory implements AbstractFactory, Configurable {
	
	final private Peer httpServerPeer= new Peer("alias","addi",0);
	final private Peer localPeer= new Peer("_local_","_addi_",0);
	
	private String webAppURLPath=null;
	
	//private SecureRandom secRandom=null;
	private Logger logger=Logger.getLogger(this.getClass());
	
	private ClientSideNetClient ptClient;
	private ClientSideNetServer ptServer;
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#getServerPeer(java.lang.String)
	 */
	public Peer getServerPeer(String serverName) {
		localPeer.setAlias(serverName);
		return localPeer;
		
	}

	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetClient()
	 */
	public NetClient createNetClient() {
		return ptClient;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetServer()
	 */
	public NetServer createNetServer() {
		return ptServer;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {	
		//localPeerAlias="alice";
		//make unique client
		ptClient= 
			new ClientSideNetClient(webAppURLPath, /*localPeerAlias,*/logger);
		//ptClient.setHttpServer(serverPeer);//getServerPeer(randomAlias));
		//create unique net server
		ptServer=
			new ClientSideNetServer(localPeer,	
									httpServerPeer,//getServerPeer(randomAlias),
									webAppURLPath,
									logger);
	}
	

//	/**
//	 * @return Returns the randomServerName.
//	 */
//	public String getRandomAlias() {
//		return randomAlias;
//	}
//	/**
//	 * @param randomServerName The randomServerName to set.
//	 */
//	public void setRandomAlias(String randomAlias) {
//		this.randomAlias = randomAlias;
//	}
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
	 * @return Returns the _host
	 */
	public String getHost() {
		return localPeer.getAddress();
	}
	
	/**
	 * @param _host The _host to set.
	 */
	public void setHost(String _host) {
		//this._host = _host;
		if(_host!=null){
			localPeer.setAddress(_host);
		}
		
	}
	
	/**
	 * @return Returns the _port.
	 */
	public int getPort() {
		return localPeer.getPort();
	}
	
	/**
	 * @param _port The _port to set.
	 */
	public void setPort(int _port) {
		if(_port>0){
			localPeer.setPort(_port);
		}
	}
		
	public void destroy(){
		ptClient.destroy();
		ptServer.destroy();
	}
}
