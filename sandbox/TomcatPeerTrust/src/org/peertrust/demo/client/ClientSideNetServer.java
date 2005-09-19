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
import org.peertrust.demo.common.ConfigurationOption;
import org.peertrust.demo.common.NewsEvent;
import org.peertrust.demo.common.NewsServer;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.NetServer;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClientSideNetServer 	extends NewsServer
									implements NetServer {
	private StringBuffer strBuffer;
	//private String webAppURLPath="/myapp-0.1-dev/PeerTrustCommunicationServlet";
	private String webAppURLPath="/demo/PeerTrustCommunicationServlet";
	private String webHTTPURL=null;
	private Peer server=null;
	//private HttpURLConnection urlConnection;
	private HttpClient httpClient;
	private boolean configNotEnded=true;
	private Logger logger;//= Logger.getLogger(this.getClass());
	
	public ClientSideNetServer(Peer server,String webAppURL, Logger logger){
		this.httpClient=new HttpClient();
		this.server=server;
		this.webAppURLPath=webAppURL;
		this.strBuffer= new StringBuffer(128);
		this.webHTTPURL=makeHTTPAdress(server);
		this.logger=logger;		
		return;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.NetServer#listen()
	 */
	public Message listen() {
		logger.info("****************client ist listening*****************");
		ensureRightConfig();
		Object rcvObj=getUsingHttp(server);
		logger.info("rcvObject:"+rcvObj);
		if(rcvObj instanceof Answer){
			logger.info("returning answer:"+ConfigurationOption.getMessageAsString(rcvObj));
			return (Answer)rcvObj;
		}else if(rcvObj instanceof Query){
			return (Query)rcvObj;
		}else if(rcvObj instanceof Message){		
			return (Message)rcvObj;
		}else{
			return null;
		}
		
	}

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
				webHTTPURL=makeHTTPAdress(server);	
			}
	}
	
	
	
	public Object getUsingHttp(Peer server) {		
		PostMethod postMethod=null;
		try {
			//SocketPermission sp= new SocketPermission(postMethod.getURI().getHost()+":"+postMethod.getURI().getPort(),"connect");
			//logger.info("sp:"+sp.toString()+" url:"+webHTTPURL);
			logger.info("webHTTPUrl:"+webHTTPURL);
			postMethod=
				new PostMethod(webHTTPURL);
			
			//postMethod.setHttp11(true);
			//postMethod.setParameter();
			
			//put object into message
			ByteArrayOutputStream byteStream=
				new ByteArrayOutputStream();
			ObjectOutputStream objOut = 
				new ObjectOutputStream(byteStream);
			objOut.writeObject(server);
			objOut.flush();
			RequestEntity reqEntity=
				new ByteArrayRequestEntity(byteStream.toByteArray());
//			urlConnection.setRequestProperty("Content-Length", Integer.toString(byteStream.size()));
//			byteStream.writeTo(urlConnection.getOutputStream());	
			postMethod.setRequestEntity(reqEntity);
			
//			RuntimePermission pern= new RuntimePermission();
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(1000*60*60);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(1000*60*60);
			httpClient.executeMethod(postMethod);
			
			//read response Object
			ObjectInputStream objIn= 
				new ObjectInputStream(postMethod.getResponseBodyAsStream());
			Object obj= objIn.readObject();
//			String objSTR=postMethod.getResponseBodyAsString();
//			System.out.println("rcvSTR:"+objSTR);
//			ObjectInputStream objIn= 
//				new ObjectInputStream(new ByteArrayInputStream(objSTR.getBytes()));
//			Object obj= objIn.readObject();
			
			fireNewsEvent(new NewsEvent(this,""+postMethod.getStatusText()));
			if(obj instanceof Throwable){
				fireNewsEvent("RemoteServer:"+postMethod.getURI(),(Throwable)obj);
			}else{
				fireNewsEvent(
						new NewsEvent("RemoteServer:"+postMethod.getURI(),
									"news:"+ConfigurationOption.getMessageAsString(obj) ) );
			}
			return obj;
							
		} catch (MalformedURLException e) {
			e.printStackTrace();fireNewsEvent(this,e);return null;
		} catch (IOException e) {
			e.printStackTrace();fireNewsEvent(this,e);return null;
		}catch(Throwable th){
			th.printStackTrace();fireNewsEvent(this,th);return null;
		}
//		finally{
//			postMethod.releaseConnection();
//		}
		
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
	public Peer getServer() {
		return server;
	}
	
	/**
	 * @param server The server to set.
	 */
	public void setServer(Peer server) {
		this.server = server;
		webHTTPURL=makeHTTPAdress(server);
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
		this.webHTTPURL=makeHTTPAdress(server);
	}
	synchronized public void setConfigNotEnded(boolean configNotEnded) {
		this.configNotEnded = configNotEnded;
		notifyAll();
	}
	
	public void destroy(){
		httpClient.getHttpConnectionManager().closeIdleConnections(1);
	}
}
