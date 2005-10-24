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

import org.peertrust.net.Message;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;


/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClientSideNetClient 	/*extends NewsServer*/
									implements NetClient {
	//private String webAppURLPath="/myapp-0.1-dev/PeerTrustCommunicationServlet";
	private String webAppURLPath="/demo/PeerTrustCommunicationServlet";
	
	private StringBuffer strBuffer= new StringBuffer(128);
	private HttpClient httpClient;
	private Logger logger;
	//private Peer httpServer;
	
	public ClientSideNetClient(	String webAppURLPath, 
								//String peerAlias, 
								Logger logger){
		this.webAppURLPath=webAppURLPath;
		this.httpClient= new HttpClient();
		//this.randomPeerAlias=peerAlias;
		this.logger=logger;
		return;
	}

	
	/* (non-Javadoc)
	 * @see org.peertrust.net.NetClient#send(org.peertrust.net.Message, org.peertrust.net.Peer)
	 */
	public void send(Message mes, Peer destination) {		
		System.out.println("\n-------------------------Sending: "+mes +
							"\n to "+destination);
		PostMethod postMethod=null;
		//Message (Peer source, Peer target, Trace trace)
		///mes= resetPeerSourceAlias(mes); //TODO not needed anymore
		
		
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
			System.out.println("\nrcv obj:"+obj+" in response to: "+mes);
			/*fireNewsEvent(new NewsEvent(this,""+obj));*/	
			
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
		
		//server.setAlias(server.getAlias());
		strBuffer.delete(0,strBuffer.length());
		
		strBuffer.append("http://");
		strBuffer.append(destination.getAddress());
		strBuffer.append(":");
		strBuffer.append(destination.getPort());
		strBuffer.append(webAppURLPath);
		strBuffer.append("?action=send");
				
		return strBuffer.toString();
	}
	
	
	
	public String getWebAppURLPath() {
		return webAppURLPath;
	}
	public void setWebAppURLPath(String webAppURLPath) {
		this.webAppURLPath = webAppURLPath;
	}
	
	
	
	public void destroy(){
		
	}

}
