/*
 * Created on 15.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.log4j.Logger;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.NetServer;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;

/**
 * ClientSideNetServer provide an http based implementation of NetServer.
 * 
 * 
 * @author Patrice Congo (token77)
 */
public class ClientSideNetServer	implements NetServer {
	/** work buffer*/
	private StringBuffer strBuffer;
	
	/** the http url of the web application responsible for peertrust communication*/
	private String webAppURLPath="/demo/PeerTrustCommunicationServlet";
	
	/** 
	 * Represents the full http uri to the web application responsible
	 * for peertrust communication. It include the query command.
	 */
	private String webHTTPURL=null;
	
	/** represents the peer in the http server**/
	private Peer remotePeer=null;
	
	/** Represents the local peer e.g. in the browser*/
	private Peer localPeer=null;
	
	/** The http client used for connection to the hhtp server*/
	private HttpClient httpClient;
	
	/** flag to log configuration end*/ 
	private boolean configNotEnded=true;
	
	/** logging engine*/
	private Logger logger;//=Logger.getLogger(this.getClass());
	
	
	/**
	 * To create a new net server.
	 * 
	 * @param localPeer -- a peer object holding data of the local peer
	 * @param remotePeer -- a peer object holding data of the local peer
	 * @param webAppURL -- the url of the web application
	 *   
	 */
	public ClientSideNetServer(	
								Peer localPeer,
								Peer remotePeer,
								String webAppURL, 
								Logger logger
								){
		this.httpClient=new HttpClient();
		this.remotePeer=remotePeer;
		this.webAppURLPath=webAppURL;
		this.strBuffer= new StringBuffer(128);
		this.webHTTPURL=makeHTTPAdress(remotePeer);
		this.logger=logger;
		this.localPeer=localPeer;
		return;
	}
	
	/**
	 * @see org.peertrust.net.NetServer#listen()
	 */
	public Message listen() {
		logger.info("\n****************client ist listening*****************");
		ensureRightConfig();
		Object rcvObj=getUsingHttp(/*remotePeer*/);
		logger.info("\nrcvObject:"+rcvObj);
		if(rcvObj instanceof Answer){
			//logger.info("\nreturning answer:"+ConfigurationOption.getMessageAsString(rcvObj));
			return (Answer)rcvObj;
		}else if(rcvObj instanceof Query){
			return (Query)rcvObj;
		}else if(rcvObj instanceof Message){		
			return (Message)rcvObj;
		}else{
			return (Message)rcvObj;
		}
		
	}

	/** 
	 *whait for the communication factory to become fully initialized 
	 */
	private synchronized void ensureRightConfig(){		
			if(configNotEnded){
				logger.info("----------waiting for final config--------");
				//wait for final config
				while(configNotEnded){
					try {
						wait(60000);
					}catch(InterruptedException e) {
						logger.error("waiting for config interupted",e);
					}
				}
				webHTTPURL=makeHTTPAdress(remotePeer);	
			}
	}
	
	
	/** 
	 * Helper method that carray out the listening, 
	 * by issuing a blocking http post request: a post request is made
	 * with the local peer object as post data. The responding web application
	 * return any message available for this peer or block until a message is 
	 * available. 
	 * 
	 *  
	 * @return the message got from the http post request.
	 */
	public Object getUsingHttp(/*Peer server*/) {		
		PostMethod postMethod=null;
		try {
			logger.info("webHTTPUrl:"+webHTTPURL);
			postMethod=
				new PostMethod(webHTTPURL);
			
			//put object into message
			ByteArrayOutputStream byteStream=
				new ByteArrayOutputStream();
			ObjectOutputStream objOut = 
				new ObjectOutputStream(byteStream);
			objOut.writeObject(localPeer);
			objOut.flush();
			RequestEntity reqEntity=
				new ByteArrayRequestEntity(byteStream.toByteArray());
			postMethod.setRequestEntity(reqEntity);
			
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(1000*60*60);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(1000*60*60);
			httpClient.executeMethod(postMethod);
			
			//read response Object
			ObjectInputStream objIn= 
				new ObjectInputStream(postMethod.getResponseBodyAsStream());
			Object obj= objIn.readObject();
			
			return obj;
							
		} catch (MalformedURLException e) {
			logger.debug("Exeption while listening for message",e);return null;
		} catch (IOException e) {
			logger.debug("Exeption while listening for message",e);return null;
		}catch(Throwable th){
			logger.debug("Exeption while listening for message",th);return null;
		}
		
	}
	
	private String makeHTTPAdress(Peer server){		
		strBuffer.delete(0,strBuffer.length());
		
		strBuffer.append("http://");
		strBuffer.append(server.getAddress());
		strBuffer.append(":");
		strBuffer.append(server.getPort());
		strBuffer.append(webAppURLPath);
		strBuffer.append("?action=receive");
				
		return strBuffer.toString();
	}
	
	
	
	/**
	 * @return Returns the server.
	 */
	public Peer getRemotePeer() {
		return remotePeer;
	}
	
	/**
	 * @param server The server to set.
	 */
	public void setRemotePeer(Peer remotePeer) {
		this.remotePeer = remotePeer;
		webHTTPURL=makeHTTPAdress(remotePeer);
	}
	


	
	/**
	 * @return Returns the comServletURL.
	 */
	public String getWebAppURLPath() {
		return webAppURLPath;
	}
	/**
	 * @param comServletURL The comServletURL to set.
	 */
	public void setWebAppURLPath(String comServletURL) {
		this.webAppURLPath = comServletURL;
		this.webHTTPURL=makeHTTPAdress(remotePeer);
	}
	
	/**
	 * Sets the configNotEnded flag
	 * @param configNotEnded
	 */
	synchronized public void setConfigNotEnded(boolean configNotEnded) {
		this.configNotEnded = configNotEnded;//TODO test reason why this exists
		notifyAll();
	}
	
	/**
	 * To destroy this net server.
	 */
	public void destroy(){
		httpClient.getHttpConnectionManager().closeIdleConnections(1);
	}
	
	/**
	 * Sets the localPeerName, the alias of the local client peer
	 * @param localPeerName
	 */
	public void setLocalPeerName(String localPeerName){
		this.localPeer.setAlias(localPeerName);
	}
}
