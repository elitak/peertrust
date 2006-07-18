/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.wrappers;


import org.apache.log4j.Logger;
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
 * the NotificationSendWrapper class is used to send negotiation messages to a client listening fot notifications
 * the NotificationSendWrapper has to receive the negotiationTopic of the client in order to know where to send the message
 */
public class NotificationSendWrapper implements SendWrapper
{
	Peer peer;
	TrustNegotiationNotificationMessageType message;
	Topic negotiationTopic;
	AuthorizedClient authorizedClient = new AuthorizedClient(); 
	ClientManager clientManager = InitializationHolder.clientManager;
	private static Logger logger = Logger.getLogger(NotificationSendWrapper.class.getName());
	
	
	/** 
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
		if(operation!=null) // if the operation is authorized - if the negotiation ended successfull then mark the client as authorized
		{
			logger.info("Client has been authorized for operation "+operation);
			TopicClient topicClient = clientManager.getTopicClient(negotiationTopic);
			clientManager.addAuthorizedClient(topicClient.getSubjectDN(),topicClient.getIssuerDN(),operation,topicClient.getExpirationDate());
			logger.info("Client registered as authorized with subjectDN "+topicClient.getSubjectDN()+" issuerDN "+topicClient.getIssuerDN()+" operation "+operation+" expirationDate "+topicClient.getExpirationDate());
		}
		
		
		TrustNegotiationNotificationMessageWrapperType msgWrapper = new TrustNegotiationNotificationMessageWrapperType(message);
		
		logger.info("Sending from source "+message.getSource().getAddress()+" "+message.getSource().getAlias()+" "+message.getSource().getPort()+
				" for target "+message.getTarget().getAddress()+" "+message.getTarget().getAlias()+" "+message.getTarget().getPort());
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
			// send the message to the client
			negotiationTopic.notify(msgWrapper);
			logger.info("Notification sent");	
		}
		catch(Exception e)
		{
			System.out.println("Exception caught at NotificationSendWrapper");
			e.printStackTrace();
		}
		
	}
}
