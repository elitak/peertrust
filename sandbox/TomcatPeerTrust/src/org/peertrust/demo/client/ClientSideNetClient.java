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
//import org.peertrust.demo.common.HttpSessionRegistrationRequest;
import org.peertrust.demo.common.NewsEvent;
import org.peertrust.demo.common.NewsServer;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASPObject;
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
	//private String webAppURLPath="/myapp-0.1-dev/PeerTrustCommunicationServlet";
	private String webAppURLPath="/demo/PeerTrustCommunicationServlet";
	
	private StringBuffer strBuffer= new StringBuffer(128);
	private HttpClient httpClient;
	//private String randomPeerAlias=null;
	private Logger logger;
	//private Peer httpServer;
	
	public ClientSideNetClient(String webAppURLPath, String peerAlias, Logger logger){
		this.webAppURLPath=webAppURLPath;
		this.httpClient= new HttpClient();
		//this.randomPeerAlias=peerAlias;
		this.logger=logger;
		return;
	}
	
	private Message resetPeerSourceAlias(Message mes){
		//Query(String goal, Peer origin, Peer target, long reqQueryId, Trace trace)
		
		Peer source=mes.getSource();//new Peer("alice","dsdddd",124);
		Peer target=mes.getTarget();//new Peer("eLearn","defrd",23541);
		boolean doRebuild=false;
		if(source==null){
			doRebuild=true;
			source=new Peer("alice","_no_need_for_addi_",124);//TODO change to load automaticaly
			System.out.println("!!!! hardcoded peer");
		}else if(source.getAddress()==null){			
			source.setAddress("_no_need_for_addi_");
		}
		
		if(target==null){
			doRebuild=true;
			target=new Peer("elearn","_no_need_for_addi_X",23541);;//TODO change to load automaticaly
			System.out.println("!!!! hardcoded peer target");
		}else if(target.getAddress()==null){			
			target.setAddress("_no_need_for_addi_");
		}
		
		if(mes instanceof Query){
			if(doRebuild){
//				mes= new Query(	((Query)mes).getGoal(),
//								source,
//								target	,
//								((Query)mes).getReqQueryId(),
//								((Query)mes).getTrace());
				System.out.println("--------------------------rebuild previous bad source or target");
			}
			return mes;
		}else if(mes instanceof Answer){
			if(doRebuild){
				//public Answer(String goal, long negotiationId, Proof proof, 
				//int status, long reqQueryId, Peer source, Peer target, Trace trace) {
				mes= new Answer(
						((Answer)mes).getGoal(),
						((Answer)mes).getNegotiationId(),
						((Answer)mes).getProof(),
						((Answer)mes).getStatus(),
						((Answer)mes).getReqQueryId(),
						source,
						target,
						((Answer)mes).getTrace());
			}
			return mes;
		}else if(mes instanceof PTCommunicationASPObject){//HttpSessionRegistrationRequest){
//			HttpSessionRegistrationRequest mes1=
//				new HttpSessionRegistrationRequest(
//						((HttpSessionRegistrationRequest)mes).getSessionKey(),source,target);
//			System.out.println("mes1"+mes1);
			PTCommunicationASPObject mes1=
				new PTCommunicationASPObject(source,target,((PTCommunicationASPObject)mes).getPeggyBackedMessage());
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
		System.out.println("\n-------------------------Sending: "+mes +
							"\n to "+server);
		PostMethod postMethod=null;
		//Message (Peer source, Peer target, Trace trace)
		mes= resetPeerSourceAlias(mes);
		
		
		try {
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
		
		server.setAlias(server.getAlias());
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
	
	
	
//	public Peer getHttpServer() {
//		return httpServer;
//	}
//	public void setHttpServer(Peer httpServer) {
//		this.httpServer = httpServer;
//	}
}
