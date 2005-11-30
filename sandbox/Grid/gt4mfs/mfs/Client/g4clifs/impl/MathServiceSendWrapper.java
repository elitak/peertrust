/*
 * Created on Jun 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4clifs.impl;


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
 * SendWrapper for MathPortType. The Peer attribute must contain the address of the service which is to be invocked through this SendWrapper
 * the message sent is of type NegotiateTrust and the mport inside is a MathPortType
 * 
 */
public class MathServiceSendWrapper implements SendWrapper
{
	Peer peer;
	NegotiateTrust message;
	MathPortType mport;
	
	
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
	
	/* (non-Javadoc)
	 * @see g4clifs.impl.net.SendWrapper#sendMessage()
	 * this function should be called only after the setMessage one. First the setMessage functions sets the message to be transmitted and then call sendMessage
	 * which using its attributes will send the message to the intended port
	 */
	public void sendMessage()
	{
		
		
		System.out.println("MathServiceSendWrapper trimit de la sursa "+message.getSource().getAddress()+" "+message.getSource().getAlias()+" "+message.getSource().getPort()+
				" pentru target "+message.getTarget().getAddress()+" "+message.getTarget().getAlias()+" "+message.getTarget().getPort());
		System.out.println("Goal: "+message.getGoal());
		System.out.println("Trace: ");
		String[] a = message.getTrace();
		for(int i=0;i<a.length;i++)
			System.out.println(a[i]);
		if(message.getMessageType() == SuperMessage.ANSWER_TYPE)
		{
			System.out.println("Proof: "+message.getProof()+" Statrus: "+message.getStatus());
		}
		System.out.println("\n\n");

		
		
		try
		{
			mport.negotiateTrust(message);
		}
		catch(Exception e)
		{
			System.out.println("MathServiceSendWrapper exceptie la negotiateTrust");
			e.printStackTrace();
		}
	}
	
}
