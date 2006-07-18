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
import org.apache.log4j.Logger;
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
 * Thread used to listen for notifications received from the service side
 */
public class GridClientNotificationThread extends Thread implements NotifyCallback
{
	
	String serviceURI;    // service URI
	EndpointReferenceType epr;   // service endPoint
	QName notificationURI;  // notification URI - where to listen for notifications
	SyncQueue queue;   // synchronization queue 
	GridClientTrustNegotiation gridClientTrustNegotiation;  // this object is used to start the thread
	boolean exitFlag = false;
	
	private static Logger logger = Logger.getLogger(GridClientNotificationThread.class.getName());
	
	public GridClientNotificationThread()
	{
		
	}
	
	public GridClientNotificationThread(GridClientTrustNegotiation gridClientTrustNegotiation)
	{
		this.gridClientTrustNegotiation = gridClientTrustNegotiation;
	}

	/**
	 * set the notification URI
	 */
	public void setNotificationURI(QName notificationURI)
	{
		this.notificationURI = notificationURI;
		logger.info("the notification URI is: "+notificationURI.getNamespaceURI()+" "+notificationURI.getLocalPart());
	}
	
	/**
	 * set the synchronization queue
	 */
	public void setSyncQueue(SyncQueue q)
	{
		queue = q;
	}
	
	
	
	public void setGridClientTrustNegotiation(GridClientTrustNegotiation gridClientTrustNegotiation)
	{
		this.gridClientTrustNegotiation = gridClientTrustNegotiation;	
	}

	/**
	 * called when notifications are delivered
	 * 
	 */
	
	public void deliver(List topicPath, EndpointReferenceType producer, Object message)
	{
		logger.info("deliver was called");
		try
		{
			TrustNegotiationNotificationMessageType notif;
			notif = (TrustNegotiationNotificationMessageType) ObjectDeserializer.toObject((Element) message,TrustNegotiationNotificationMessageType.class);
			if(notif != null)
			{
				
				
				
				logger.info("From source "+notif.getSource().getAddress()+" "+notif.getSource().getAlias()+" "+notif.getSource().getPort()+
						" for target "+notif.getTarget().getAddress()+" "+notif.getTarget().getAlias()+" "+notif.getTarget().getPort());
				logger.info("Goal: "+notif.getGoal());
				
				String deliveredTrace = "Trace: ";
				
				String[] a = notif.getTrace();
				for(int i=0;i<a.length;i++)
					deliveredTrace = deliveredTrace + a[i];
				
				logger.info(deliveredTrace);
					
				
				if(notif.getMessageType() == SuperMessage.ANSWER_TYPE)
				{
					logger.info("Proof: "+notif.getProof()+" Status: "+notif.getStatus());				
				}
				
								
				// use the ConverterClass to convert between  
				// g4mfs.impl.org.peertrust.net.Peer and org.peertrust.Peer
				
				Message mesg = ConverterClass.trustNegotiationNotificationMessageToPtMessage(notif);
				if (mesg instanceof Answer)
				{
					if (((Answer) mesg).getStatus() == Answer.LAST_ANSWER) // negotiation successful
					{
						logger.info("Negotiation successful");
						exitFlag = true; // we can exit
						gridClientTrustNegotiation.setSucces(true);
						
					}
					else
						if(((Answer) mesg).getStatus() == Answer.FAILURE) // negotiation failed
						{
							logger.info("Negociation failed");
							exitFlag = true;
							gridClientTrustNegotiation.setSucces(false);
						}
				}
				
				// push the message for further processing 
				logger.info("Message pushed in the queue");
				queue.push(mesg);
				
			}
			else
			{
				System.out.println("GridClientNotificationThread notification null in deliver");
			}
	
		}
		catch(Exception e)
		{
			System.out.println("GridClientNotificationThread exception in deliver");
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
		try
		{
					
			// the notifficationconsumer sets up an endpoint where notifications will be delivered
			NotificationConsumerManager consumer;
			consumer = NotificationConsumerManager.getInstance();
			consumer.startListening();
			EndpointReferenceType consumerEPR = consumer.createNotificationConsumer(this);
			
			
			//create the request to the remote Subscribe() call 
			Subscribe request = new Subscribe();
			
			//Must the notification be delivered using Notify opeation
			request.setUseNotify(Boolean.TRUE);
			
			//Indicate what the client's EPR is
			request.setConsumerReference(consumerEPR);
			
					
			//The TopicExpressionType specifies what topic we want to subscribe to
			TopicExpressionType topicExpression = new TopicExpressionType();
			topicExpression.setDialect(WSNConstants.SIMPLE_TOPIC_DIALECT);
			topicExpression.setValue(notificationURI);
			request.setTopicExpression(topicExpression);
			
				
			//Get a reference to the NotificationProducer portType
			WSBaseNotificationServiceAddressingLocator notifLocator = new WSBaseNotificationServiceAddressingLocator();
		
			
			NotificationProducer producerPort = notifLocator.getNotificationProducerPort(epr);
			logger.info("Notification Producer port "+producerPort.toString());
			
			if(producerPort == null)
			{
				logger.info("producerPort is null");
			}
			else
			{
				logger.info("producerPort ok");
			}
			
			producerPort.subscribe(request);
			
			logger.info("GridClientNotificationThread Waiting for notifications");
			
			while(true)
			{
				
				if(exitFlag) // negotiation is finished stop thread
 				{
					return;
				} 
				
				// when notifications are delivered the thread is automatically awaken
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
			System.out.println("GridClientNotificationThread exception in run");
			e.printStackTrace();
		}
	}

}
