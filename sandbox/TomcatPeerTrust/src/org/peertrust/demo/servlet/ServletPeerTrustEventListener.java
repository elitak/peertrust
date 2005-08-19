/*
 * Created on Apr 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;


import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.demo.client.applet.SessionRegistrationMessage;
import org.peertrust.demo.common.ConfigurationOption;
import org.peertrust.event.AnswerEvent;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.NewMessageEvent;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.meta.Trace;
import org.peertrust.net.Answer;
import org.peertrust.net.Query;



public class ServletPeerTrustEventListener implements PTEventListener, Configurable{
	
	//private ServerSocket serverSocket;
	private int serverPort=-1;
	private Hashtable negotiations;
	private String serverIP;
	private EventDispatcher _dispatcher;
	private Logger logger;
		
	public ServletPeerTrustEventListener()throws IOException{
		try{
			negotiations=new Hashtable();
			
		
		}catch(Throwable th){
			th.printStackTrace();
			serverPort=-1;
		}
	}
		
	public int getSServerPort(){
		return serverPort;
	}
	
	public String getServerIP(){
		return serverIP;
	}
	
	public boolean isNegotiationGoingOn(String id){
		Object negObject=negotiations.get(id);
		if(negObject==null){
			return false;
		}else{
			return ((NegotiationState)negObject).isNegotiationGoingOn();
		}
	}
	
	public boolean isNetgotiationSuccessfull(String id){
		if(id==null){
			return false;
		}
		Object negObject=negotiations.get(id);
		if(negObject==null){
			return false;
		}else{
			return ((NegotiationState)negObject).isNegotiationSuccessfull();
		}
	}
	
	public boolean hasNetgotiationFailed(String id){
		if(id==null){
			return false;
		}
		Object negObject=negotiations.get(id);
		if(negObject==null){
			return false;
		}else{
			return ((NegotiationState)negObject).hasFailed();
		}
	}
	
	public void addNegotiation(String negoID, String reqResource){
		NegotiationState state= 
			new NegotiationState(negoID,reqResource);
		synchronized(negotiations){
			negotiations.put(negoID,state);
			negotiations.notifyAll();
		}
		
		return;
	}
	
	public void event(PTEvent event) {
		if(event instanceof QueryEvent){
			processEvent((QueryEvent)event);
		}else if(event instanceof AnswerEvent){
			processEvent((AnswerEvent)event);
		}else if(event instanceof NewMessageEvent){
			try{
				SessionRegistrationMessage mes=
					(SessionRegistrationMessage)((NewMessageEvent)event).getMessage();
				System.out.println("mes:"+mes);
			}catch(ClassCastException e){
				e.printStackTrace();
			}
		}else{
		
			System.out.println("Bad Event:"+event);
		}
	}
	
	public void processEvent(QueryEvent qEvent){
		//TODO fix trick with trace length don t need it anymore
		Query query=qEvent.getQuery();
		Trace trace=query.getTrace();
		//String traceStrs[]= trace.getTrace();
		Vector traces= trace.getTrace();
		logger.info("!!!!!!!!!traceStrs.length:"+traces.size()+" "+query.getGoal());
		if(traces.size()==0){//
			String id=Long.toString(query.getReqQueryId());
			logger.info("++++++++++++++++++++new nego:"+id);
			addNegotiation(
					id,//query.getSource().getAlias(),
					query.getGoal());
		}
		
	}
	
	public void processEvent(AnswerEvent aEvent){
		try {
			Answer answer=aEvent.getAnswer();
			int status=answer.getStatus();
			String reqID=Long.toString(answer.getReqQueryId());
			logger.info("++++++++++++++reqID:"+reqID+ " "+answer.getGoal());
			if(negotiations.containsKey(reqID)){
				//it is the final answer
				//so cache this answer
				
				synchronized(negotiations){
					NegotiationState negoState=
						(NegotiationState)negotiations.get(reqID);
					negoState.setFinalAnswer(answer);
					logger.info("++++++++++++++reqID:"+reqID+
								" last answer");
					negotiations.notifyAll();				
				}
				
			}
		} catch (Exception e) {
			logger.error("+++++++++++++Error processing answer",e);
		}
//		if(status==Answer.FAILURE){
//			negotiations.get(answer.getTarget().getAlias());
//		}
	}
	public void init() throws ConfigurationException {
		String msg = null ;
		logger=ConfigurationOption.getLogger(this.getClass().getName());
        logger.info("******************************INIT SERVLET_PEETRUST_PT_EVENT_LISTENER");
		if (_dispatcher == null)
		{
			msg = "There not exist an event dispatcher" ;
			logger.error (msg) ;
			throw new ConfigurationException(msg) ;
		}
		
		_dispatcher.register(this) ;
		
		logger.debug("Event listener registered") ;
	}
	
	/**
	 * @param _dispatcher The _dispatcher to set.
	 */
	public void setEventDispatcher(EventDispatcher _dispatcher) {
		this._dispatcher = _dispatcher;
	}
	
	//not used anymore
	public void waitForResult(String id,int timeout){
		synchronized(negotiations){
			boolean leave=false;
			timeout=timeout*6;
			for(int i=0; 
				i<timeout &&
					!hasNetgotiationFailed(id) &&
					!isNetgotiationSuccessfull(id);
				i++){
				try {negotiations.wait(10000);} catch (InterruptedException e) {
					logger.error("error while waiting for nego:",e);
				}
			}
		}
		
	}
	
	
	
}