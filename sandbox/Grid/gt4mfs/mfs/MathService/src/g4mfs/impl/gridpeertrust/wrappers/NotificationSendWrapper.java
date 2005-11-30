/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.wrappers;


import org.globus.wsrf.Topic;

import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer;
import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageType;
import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageWrapperType;
import g4mfs.impl.gridpeertrust.net.server.AuthorizedClient;
import g4mfs.impl.gridpeertrust.net.server.ClientManager;
import g4mfs.impl.gridpeertrust.net.server.TopicClient;
import g4mfs.impl.gridpeertrust.util.ConverterClass;
import g4mfs.impl.gridpeertrust.util.InitializationHolder;
import g4mfs.impl.gridpeertrust.util.SuperMessage;
import g4mfs.impl.org.peertrust.net.Message;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NotificationSendWrapper implements SendWrapper
{
	Peer peer;
	TrustNegotiationNotificationMessageType message;
	Topic negotiationTopic;
	AuthorizedClient authorizedClient = new AuthorizedClient(); 
	ClientManager clientManager = InitializationHolder.clientManager;
	
	/* 
	 * receive a peer argument of type org.peetrust.net.Peer and convert it to a gridPeer
	 */
	public void setPeer(Object peer)
	{
		this.peer = ConverterClass.ptPeerToGridPeer((g4mfs.impl.org.peertrust.net.Peer)peer);
	} 
	
	
	/**
	 * @param mesg mesg is going to be of org.peertrust.net.Message type and it would be transformed in the message type 
	 * used by the destination port
	 */
	public void setMessage(Object mesg)
	{
		this.message = ConverterClass.ptMessageToTrustNegotiationNotificationMessage((Message)mesg);	
	} 
	
	
	public Object getPeer()
	{
		return peer;
	}
	public Object getMessage()
	{
		return message;
	}
	
	
	public void setNegotiationTopic(Topic t)
	{
		negotiationTopic = t;
	}
	
	public void sendMessage()
	{
		
		String operation = authorizedClient.isAuthorized(message);
		if(operation!=null)
		{
			System.out.println("NotificationSendWrapper  Client autorizat "+operation);
			TopicClient topicClient = clientManager.getTopicClient(negotiationTopic);
			clientManager.addAuthorizedClient(topicClient.getSubjectDN(),topicClient.getIssuerDN(),operation,topicClient.getExpirationDate());
			System.out.println("NotificationSendWrapper  Client autorizat: ");
			System.out.println(topicClient.getSubjectDN()+" "+topicClient.getIssuerDN()+" "+operation+" "+topicClient.getExpirationDate());
		}
		
		
		TrustNegotiationNotificationMessageWrapperType msgWrapper = new TrustNegotiationNotificationMessageWrapperType(message);
		
		
		
		System.out.println("NotificationSendWrapper trimit de la sursa "+message.getSource().getAddress()+" "+message.getSource().getAlias()+" "+message.getSource().getPort()+
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
			negotiationTopic.notify(msgWrapper);
			System.out.println("\n\n******TNP am trimis notificare******\n\n");	
		}
		catch(Exception e)
		{
			System.out.println("Am prins exceptie la notify NotificationSendWrapper");
			e.printStackTrace();
		}
		
	}
}
