/*
 * Created on Jun 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4clifs.impl;


import org.apache.log4j.Logger;

import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.NegotiateTrust;
import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer;
import g4mfs.impl.gridpeertrust.util.ConverterClass;
import g4mfs.impl.gridpeertrust.util.SuperMessage;
import g4mfs.impl.gridpeertrust.wrappers.SendWrapper;
import g4mfs.impl.org.peertrust.net.Message;
import g4mfs.stubs.MathPortType;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * SendWrapper used to communicate with a port of type MathPortType. The Peer attribute must contain the address of the service which is to be invocked through this SendWrapper
 * the message sent is of type NegotiateTrust and the mport attribute is a MathPortType
 * 
 */
public class MathServiceSendWrapper implements SendWrapper
{
	Peer peer;
	NegotiateTrust message;
	MathPortType mport;
	private static Logger logger = Logger.getLogger(MathServiceSendWrapper.class.getName());
	
	public void setPeer(Object peer)
	{
		this.peer = ConverterClass.ptPeerToGridPeer((g4mfs.impl.org.peertrust.net.Peer) peer); 
	}

	public void setMessage(Object mesg)
	{
		this.message = ConverterClass.ptMessageToNegotiatiateTrust((Message) mesg);
	}
	
	public Object getPeer()
	{
		return peer;
	}
	
	public Object getMessage()
	{
		return message;
	}
	
	public void setMathPortType(MathPortType mport)
	{
		this.mport = mport;
	}
	
	/** 
	 * @see g4clifs.impl.net.SendWrapper#sendMessage()
	 * this function should be called only after message has been set (the setMessage function called). 
	 * First the setMessage functions sets the message to be transmitted and thenthe sendMessage function
	 * using its attributes can deliver the message to the MathPort 
	 */
	public void sendMessage()
	{
		
		
		logger.info("Send from source "+message.getSource().getAddress()+" "+message.getSource().getAlias()+" "+message.getSource().getPort()+
				" to target "+message.getTarget().getAddress()+" "+message.getTarget().getAlias()+" "+message.getTarget().getPort());
		logger.info("Goal: "+message.getGoal());
		
		String deliveredTrace = "Trace: ";
		
		
		String[] a = message.getTrace();
		for(int i=0;i<a.length;i++)
			deliveredTrace = deliveredTrace + a[i];
		
		logger.info(deliveredTrace);
		
		if(message.getMessageType() == SuperMessage.ANSWER_TYPE)
		{
			logger.info("Proof: "+message.getProof()+" Status: "+message.getStatus());
		}
			
		
		try
		{
			mport.negotiateTrust(message);
		}
		catch(Exception e)
		{
			System.out.println("MathServiceSendWrapper Exception at negotiateTrust");
			e.printStackTrace();
		}
	}
	
}
