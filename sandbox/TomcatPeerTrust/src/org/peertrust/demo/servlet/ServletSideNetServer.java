/*
 * Created on 14.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import org.apache.log4j.Logger;
import org.peertrust.net.Message;
import org.peertrust.net.NetServer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServletSideNetServer implements NetServer {

	//Vector messageFIFO= new Vector();
	private BlockingQueue messageFIFO=
		new ArrayBlockingQueue(16);
	boolean isListening= false;
	Logger logger;
	
	public ServletSideNetServer(Logger logger){
		this.logger=logger;
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.net.NetServer#listen()
	 */
	public Message listen() {
		setIsListening(true);
		Object obj=null;
		logger.info("Servlet PT server listening");
//		while(messageFIFO.size()==0){
//			try {wait();}catch(InterruptedException e) {e.printStackTrace();}			
//		}
//		Object obj= messageFIFO.firstElement();
//		messageFIFO.remove(obj);
		
		try{
		       obj= messageFIFO.take();
		}catch (InterruptedException ex) { 
			logger.error("Problem listening",ex);			
		}		
		setIsListening(false);		
		return (Message)obj;
	}
	
	public void addMessage(Object mes){
		logger.info("Servlet PT server added message:"+mes);
		if(mes instanceof Message){
			try {
				//messageFIFO.add(mes);
				messageFIFO.put(mes);
			} catch (InterruptedException e) {
				logger.error("could not add message:",e);
			}
		}else {
			logger.debug("Not a message:"+mes);
		}
		//notifyAll();
	}
	
	synchronized public boolean getIsListening(){
		return isListening;
	}
	
	synchronized public void setIsListening(boolean isL){
		this.isListening=isL;
		return;
	}
	
	
}
