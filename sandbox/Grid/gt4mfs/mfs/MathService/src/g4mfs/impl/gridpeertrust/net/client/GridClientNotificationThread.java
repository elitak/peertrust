/*
 * Created on Jun 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.client;


import g4mfs.impl.gridpeertrust.util.ConverterClass;
import g4mfs.impl.gridpeertrust.util.SuperMessage;
import g4mfs.impl.gridpeertrust.util.SyncQueue;
import g4mfs.impl.org.peertrust.net.Answer;
import g4mfs.impl.org.peertrust.net.Message;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.NotificationConsumerManager;
import org.globus.wsrf.NotifyCallback;
import org.globus.wsrf.WSNConstants;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.oasis.wsn.NotificationProducer;
import org.oasis.wsn.Subscribe;
import org.oasis.wsn.TopicExpressionType;
import org.oasis.wsn.WSBaseNotificationServiceAddressingLocator;
import org.w3c.dom.Element;

import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageType;


/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GridClientNotificationThread extends Thread implements NotifyCallback
{
	
	String serviceURI;
	EndpointReferenceType epr;
	QName notificationURI;
	SyncQueue queue;
	GridClientTrustNegotiation gridClientTrustNegotiation;
	boolean exitFlag = false;
	
	
	public GridClientNotificationThread()
	{
		
	}
	
	public GridClientNotificationThread(GridClientTrustNegotiation gridClientTrustNegotiation)
	{
		this.gridClientTrustNegotiation = gridClientTrustNegotiation;
	}

	public void setNotificationURI(QName notificationURI)
	{
		this.notificationURI = notificationURI;
		System.out.println("\n\nGridClientNotificationThread setNotificationURI am setat urmatorul notificationURI "+notificationURI.getNamespaceURI()+" "+notificationURI.getLocalPart()+" \n\n");
	}
	
	public void setSyncQueue(SyncQueue q)
	{
		queue = q;
	}
	
	public void setGridClientTrustNegotiation(GridClientTrustNegotiation gridClientTrustNegotiation)
	{
		this.gridClientTrustNegotiation = gridClientTrustNegotiation;	
	}

	public void deliver(List topicPath, EndpointReferenceType producer, Object message)
	{
		System.out.println("\n\nGridClientNotificationThread s-a apelat deliver");
		try
		{
			TrustNegotiationNotificationMessageType notif;
			notif = (TrustNegotiationNotificationMessageType) ObjectDeserializer.toObject((Element) message,TrustNegotiationNotificationMessageType.class);
			if(notif != null)
			{
				
				
				
				System.out.println("GridClientNotificationThread s-a primit de la sursa "+notif.getSource().getAddress()+" "+notif.getSource().getAlias()+" "+notif.getSource().getPort()+
						" pentru target "+notif.getTarget().getAddress()+" "+notif.getTarget().getAlias()+" "+notif.getTarget().getPort());
				System.out.println("Goal: "+notif.getGoal());
				System.out.println("Trace: ");
				String[] a = notif.getTrace();
				for(int i=0;i<a.length;i++)
					System.out.println(a[i]);
				
				
				if(notif.getMessageType() == SuperMessage.ANSWER_TYPE)
				{
					System.out.println("Proof: "+notif.getProof()+" Statrus: "+notif.getStatus());				
				}
				System.out.println("\n\n");
				
				
				//System.out.println("GridClientNotificationThread asta a fost in deliver");
				
				
				
				Message mesg = ConverterClass.trustNegotiationNotificationMessageToPtMessage(notif);
				if (mesg instanceof Answer)
				{
					if (((Answer) mesg).getStatus() == Answer.LAST_ANSWER) // negotiation is finished with success
					{
						System.out.println("GridClientNotificationThread negocierea s-a incheiat cu succes");
						exitFlag = true;
						gridClientTrustNegotiation.setSucces(true);
						
					}
					else
						if(((Answer) mesg).getStatus() == Answer.FAILURE) // negotiation is finished without success
						{
							System.out.println("GridClientNotificationThread negocierea s-a incheiat cu insucces");
							exitFlag = true;
							gridClientTrustNegotiation.setSucces(false);
						}
				}
				/*if(queue == null)
				{
					System.out.println("GridClientNotificationThread queue este null in deliver");
				}*/
				
				
				System.out.println("GridClientNotificationThread am facut push la mesaj");
				queue.push(mesg);
				
			}
			else
			{
				System.out.println("GridClientNotificationThread notif null in deliver");
			}
	
		}
		catch(Exception e)
		{
			System.out.println("GridClientNotificationThread exceptie in deliver");
			e.printStackTrace();
		}
	
	}
	
	public void setServiceURI(String s)
	{
		serviceURI = s;
	}
	
	public void setServiceEPR(EndpointReferenceType s)
	{
		epr = s;
	}
	
	public void run()
	{
		System.out.println("Client GridClientNotificationThread started of course !!!!!");
		try
		{
			System.out.println("\n\n************aici0******************\n\n");
			
			
			// the notifficationconsumer sets up an endpoint where notifications will be delivered
			NotificationConsumerManager consumer;
			consumer = NotificationConsumerManager.getInstance();
			consumer.startListening();
			EndpointReferenceType consumerEPR = consumer.createNotificationConsumer(this);
			
			System.out.println("aici1");
			
			//create the request to the remote Subscribe() call 
			Subscribe request = new Subscribe();
			
			//Must the notification be delivered using Notify opeation
			request.setUseNotify(Boolean.TRUE);
			
			//Indicate what the client's EPR is
			request.setConsumerReference(consumerEPR);
			
			System.out.println("aici2");
			
			
			//The TopicExpressionType specifies what topic we want to subscribe to
			TopicExpressionType topicExpression = new TopicExpressionType();
			topicExpression.setDialect(WSNConstants.SIMPLE_TOPIC_DIALECT);
			topicExpression.setValue(notificationURI);
			request.setTopicExpression(topicExpression);
			
			System.out.println("GridClientNotificationThread am setat notificationURI "+notificationURI);
			
			
			//Get a reference to the NotificationProducer portType
			WSBaseNotificationServiceAddressingLocator notifLocator = new WSBaseNotificationServiceAddressingLocator();
			//EndpointReferenceType endpoint = new EndpointReferenceType();
			//endpoint.setAddress(new Address(serviceURI));
			//NotificationProducer producerPort = notifLocator.getNotificationProducerPort(endpoint);
			
			NotificationProducer producerPort = notifLocator.getNotificationProducerPort(epr);
			System.out.println("Notification Producer port "+producerPort.toString());
			
			// 
			if(producerPort == null)
			{
				System.out.println("producerPort e null");
			}
			else
			{
				System.out.println("producerPort e ok");
			}
			
			producerPort.subscribe(request);
			
			System.out.println("GridClientNotificationThread a intors subscribe");
			
			
			
			System.out.println("GridClientNotificationThread Waiting for notifications");
			
			while(true)
			{
				
				if(exitFlag) // negotiation is finished stop thread
 				{
					return;
				} 
				
				try
				{
					Thread.sleep(30000);
				}
				catch(Exception e)
				{
					System.out.println("GridClientNotificationThread interrupted while sleeping");
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("GridClientNotificationThread exceptie in run");
			e.printStackTrace();
		}
	}

}
