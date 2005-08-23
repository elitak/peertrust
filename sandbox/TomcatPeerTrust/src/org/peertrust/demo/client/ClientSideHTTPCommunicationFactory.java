/*
 * Created on 20.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client;

import java.security.SecureRandom;

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
	
	private String serverIP=null;
	private String randomAlias=null;
	private int serverPort=-1;
	private String webAppURLPath=null;
	
	private SecureRandom secRandom=null;
	private Logger logger=Logger.getLogger(this.getClass());
	
	private ClientSideNetClient ptClient;
	private ClientSideNetServer ptServer;
	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#getServerPeer(java.lang.String)
	 */
	public Peer getServerPeer(String serverName) {
		return new Peer(serverName,serverIP,serverPort);
	}

	/* (non-Javadoc)
	 * @see org.peertrust.net.AbstractFactory#createNetClient()
	 */
	public NetClient createNetClient() {
//		ClientSideNetClient client= 
//			new ClientSideNetClient(webAppURLPath, randomAlias);
//		
//		return client;
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
		//randomServerName= "RandomName"+(new Random()).nextLong();
		secRandom= new SecureRandom();
		//randomAlias=Long.toHexString(secRandom.nextLong());	
		randomAlias="alice";
		//make unique client
		ptClient= 
			new ClientSideNetClient(webAppURLPath, randomAlias,logger);
		ptClient.setHttpServer(getServerPeer(randomAlias));
		//create unique net server
		ptServer=
			new ClientSideNetServer(	
									getServerPeer(randomAlias),
									webAppURLPath,logger);
	}
	

	/**
	 * @return Returns the randomServerName.
	 */
	public String getRandomAlias() {
		return randomAlias;
	}
	/**
	 * @param randomServerName The randomServerName to set.
	 */
	public void setRandomAlias(String randomAlias) {
		this.randomAlias = randomAlias;
	}
	/**
	 * @return Returns the serverIP.
	 */
	public String getServerIP() {
		return serverIP;
	}
	/**
	 * @param serverIP The serverIP to set.
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
		ptServer.getServer().setAddress(serverIP);
		ptClient.getHttpServer().setAddress(serverIP);
	}
	/**
	 * @return Returns the serverPort.
	 */
	public int getServerPort() {
		return serverPort;
	}
	/**
	 * @param serverPort The serverPort to set.
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
		ptServer.getServer().setPort(serverPort);
		ptClient.getHttpServer().setPort(serverPort);
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
	
	public long getRandom(){
		return secRandom.nextLong();
	}
	
	public void destroy(){
		ptClient.destroy();
		ptServer.destroy();
	}
}
