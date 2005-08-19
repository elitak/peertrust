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
import org.peertrust.demo.client.applet.SessionRegistrationMessage;
import org.peertrust.demo.common.NewsEvent;
import org.peertrust.demo.common.NewsServer;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;


/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClientSideNetClient 	extends NewsServer
									implements NetClient {
	private String webAppURLPath="/myapp-0.1-dev/PeerTrustCommunicationServlet";
	
	StringBuffer strBuffer= new StringBuffer(128);
	HttpClient httpClient;
	String randomPeerAlias=null;
	Logger logger;
	public ClientSideNetClient(String webAppURLPath, String peerAlias, Logger logger){
		this.webAppURLPath=webAppURLPath;
		this.httpClient= new HttpClient();
		this.randomPeerAlias=peerAlias;
		this.logger=logger;
		return;
	}
	
	private Message resetPeerSourceAlias(Message mes){
		//Query(String goal, Peer origin, Peer target, long reqQueryId, Trace trace)
		Peer origin= mes.getSource();
//		IdentifiablePeer originWithID= 
//			new IdentifiablePeer(	origin.getAlias(),
//									origin.getAddress(),
//									origin.getPort(),
//									randomPeerAlias);
//		Peer originWithID= 
//			new Peer(	origin.getAlias(),//randomPeerAlias,
//						origin.getAddress(),
//						origin.getPort());
		if(mes instanceof Query){
			Peer originWithID= 
				new Peer(	origin.getAlias(),//randomPeerAlias,
							origin.getAddress(),
							origin.getPort());
			Query query= (Query)mes;
			mes= new Query(	query.getGoal(),
							originWithID,
							query.getTarget(),
							query.getReqQueryId(), 
							query.getTrace());
			return mes;
		}else if(mes instanceof Answer){
			Answer answer= (Answer)mes;
			//TODO answer trick fix
//			mes= new Answer(answer.getGoal(),
//							answer.getProof(),
//							answer.getStatus(),
//							answer.getReqQueryId(),
//							originWithID,
//							answer.getTarget(),
//							answer.getTrace());
			return mes;
		}else if(mes instanceof SessionRegistrationMessage){
			Peer source=new Peer("alice","dsdddd",124);
			Peer target=mes.getTarget();//new Peer("eLearn","defrd",23541);
			SessionRegistrationMessage mes1=
				new SessionRegistrationMessage(
						((SessionRegistrationMessage)mes).getSessionKey(),source,target);
			System.out.println("mes1"+mes1);
			return mes1;
		}else{
		
			return null;			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.NetClient#send(org.peertrust.net.Message, org.peertrust.net.Peer)
	 */
	public void send(Message mes, Peer server) {		
		PostMethod postMethod=null;
		//Message (Peer source, Peer target, Trace trace)
		mes= resetPeerSourceAlias(mes);
		
		
		try {
//			URL url = 
//				new URL(makeHTTPAdress(mes,server));
//			URLConnection conn = url.openConnection();
//				conn.setUseCaches(false);
//			conn.setRequestProperty("CONTENT_TYPE", "application/octetstream");
//			conn.setDoInput(true);
//			conn.setDoOutput(true);
//			conn.setUseCaches(false);
//			ByteArrayOutputStream byteStream=
//				new ByteArrayOutputStream();
//			ObjectOutputStream objOut = 
//				new ObjectOutputStream(byteStream);
//			objOut.writeObject(mes);
//			objOut.flush();
//			
//			conn.setRequestProperty("Content-Length", Integer.toString(byteStream.size()));
//			byteStream.writeTo(conn.getOutputStream());		
//			objOut.close();
//			return;
			postMethod=
				new PostMethod(makeHTTPAdress(mes,server));			
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
			fireNewsEvent(new NewsEvent(this,""+obj));			
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
	
	private String makeHTTPAdress(Message mes, Peer server){
		
		strBuffer.delete(0,strBuffer.length());
		
		strBuffer.append("http://");
		strBuffer.append(server.getAddress());
		strBuffer.append(":");
		strBuffer.append(server.getPort());
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
