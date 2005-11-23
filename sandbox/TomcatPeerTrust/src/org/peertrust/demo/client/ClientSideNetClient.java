/*
 * Created on 15.04.2005
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

import org.peertrust.net.Message;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;


/**
 * ClientSideNetClient is a concrete implementation of NetClient.
 * Its provided therefore means to send messages for the peertrust communication.
 * The acual sending is done using the http post API provided by HttpClient.
 * @see org.peertrust.net.NetClient
 * @see org.apache.commons.httpclient.HttpClient
 * @author Patrice Congo (token77)
 */
public class ClientSideNetClient implements NetClient 
{
	/** represents the http url of the web application reponsible for the peertrust http communication*/
	private String webAppURLPath="/demo/PeerTrustCommunicationServlet";
	
	/** work buffer*/
	private StringBuffer strBuffer= new StringBuffer(128);
	
	/** http client that provide http post*/
	private HttpClient httpClient;
	
	private Logger logger;
	/** Represents the peer on the hhtp server side.*/
	private Peer httpServer;
	
	/**
	 * Creates a ClientSideNetClient.  
	 * @param webAppURLPath -- the http url of the application responsible for peertrust communication
	 * @param httpServer -- a peer representing the http server peer
	 * @param logger
	 */
	public ClientSideNetClient(	String webAppURLPath, 
								Peer httpServer,//String peerAlias, 
								Logger logger){
		this.webAppURLPath=webAppURLPath;
		this.httpClient= new HttpClient();
		this.httpServer=httpServer;
		this.logger=logger;
		return;
	}

	
	/**
	 * Sends a message to the destinatiom using a http post request.
	 * @param mes -- the message to send
	 * @param destination -- the destination of the message
	 * 
	 * @see org.peertrust.net.NetClient#send(org.peertrust.net.Message, org.peertrust.net.Peer)
	 */
	public void send(Message mes, Peer destination) {		
		logger.info("\n-------------------------Sending: "+mes +
					"\n to "+destination);
		PostMethod postMethod=null;
				
		try {
			postMethod=
				new PostMethod(makeHTTPAdress(destination));			
			//put message into body 
			ByteArrayOutputStream byteStream=
				new ByteArrayOutputStream();
			ObjectOutputStream objOut = 
				new ObjectOutputStream(byteStream);
			objOut.writeObject(mes);
			objOut.flush();
			RequestEntity reqEntity=
				new ByteArrayRequestEntity(byteStream.toByteArray());
			postMethod.setRequestEntity(reqEntity);
			httpClient.executeMethod(postMethod);
			
			//read response Object
			ObjectInputStream objIn= 
				new ObjectInputStream(postMethod.getResponseBodyAsStream());
			Object obj= objIn.readObject();
			logger.info("\nrcv obj:"+obj+" in response to: "+mes);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Throwable th){
			th.printStackTrace();
		}finally{
			postMethod.releaseConnection();
		}
	}
	
	private String makeHTTPAdress(Peer destination){
		//TODO check usage of internal httpServer vs. destination
		//server.setAlias(server.getAlias());
		strBuffer.delete(0,strBuffer.length());
		
		strBuffer.append("http://");
		//strBuffer.append(destination.getAddress());
		strBuffer.append(httpServer.getAddress());
		strBuffer.append(":");
		//strBuffer.append(destination.getPort());
		strBuffer.append(httpServer.getPort()); 
		strBuffer.append(webAppURLPath);
		strBuffer.append("?action=send");
				
		return strBuffer.toString();
	}
	
	
	/**
	 * @return the web appication url path
	 */
	public String getWebAppURLPath() {
		return webAppURLPath;
	}
	
	/**
	 * Sets the web application url 
	 * @param webAppURLPath
	 */
	public void setWebAppURLPath(String webAppURLPath) {
		this.webAppURLPath = webAppURLPath;
	}
	
	
	/** 
	 * Destroy net client.
	 *
	 */
	public void destroy(){
		logger.info("destroying ClientSideNetClient");
		httpClient=null;
		strBuffer.setLength(0);
		strBuffer=null;
		logger=null;
		webAppURLPath=null;
		httpServer=null;
	}

}
