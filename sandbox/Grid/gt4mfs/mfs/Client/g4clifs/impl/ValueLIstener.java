/*
 * Created on Jun 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4clifs.impl;

import g4mfs.impl.MathQNames;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReference;
import org.apache.axis.message.addressing.EndpointReferenceType;

import java.io.FileInputStream;
import java.util.List;
import org.globus.wsrf.NotificationConsumerManager;
import org.globus.wsrf.NotifyCallback;
import org.globus.wsrf.WSNConstants;
import org.globus.wsrf.core.notification.ResourcePropertyValueChangeNotificationElementType;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.oasis.wsn.NotificationProducer;
import org.oasis.wsn.Subscribe;
import org.oasis.wsn.TopicExpressionType;
import org.oasis.wsn.WSBaseNotificationServiceAddressingLocator;
import org.oasis.wsrf.properties.ResourcePropertyValueChangeNotificationType;
import org.xml.sax.InputSource;



/**
 * @author ionut
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ValueLIstener implements NotifyCallback
{
	
	public void run(String serviceURI)
	{
		try
		{
			NotificationConsumerManager consumer;
			
			consumer = NotificationConsumerManager.getInstance();
			consumer.startListening();
			EndpointReferenceType consumerEPR = consumer.createNotificationConsumer(this);
			
			Subscribe request = new Subscribe();
			request.setUseNotify(Boolean.TRUE);
			request.setConsumerReference(consumerEPR);
			
			TopicExpressionType topicExpression = new TopicExpressionType();
			topicExpression.setDialect(WSNConstants.SIMPLE_TOPIC_DIALECT);
			topicExpression.setValue(MathQNames.RP_VALUE);
			request.setTopicExpression(topicExpression);
			
			WSBaseNotificationServiceAddressingLocator notifLocator = new WSBaseNotificationServiceAddressingLocator();
			
			
			FileInputStream fis = new FileInputStream("epr.txt");
			EndpointReferenceType instanceEPR = (EndpointReferenceType) ObjectDeserializer.deserialize(new InputSource(fis), EndpointReferenceType.class);
			fis.close();
			
			EndpointReferenceType endpoint = new EndpointReferenceType();
			endpoint.setAddress(new Address(serviceURI));
			NotificationProducer producerPort = notifLocator.getNotificationProducerPort(instanceEPR);
			
			producerPort.subscribe(request);
			
			System.out.println("Waiting for notification. Ctrl-C to end");

			
			while(true)
			{
				try
				{
					Thread.sleep(30000);
				}
				catch(Exception e)
				{
					System.out.println("INterrupted while sleeping");
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Exceptie in ValueListener");
			e.printStackTrace();
		}
	}
	
	public void deliver(List topicPath,EndpointReferenceType producer, Object message)
	{
		ResourcePropertyValueChangeNotificationElementType notif_elem;
		ResourcePropertyValueChangeNotificationType notif;
		
		notif_elem = (ResourcePropertyValueChangeNotificationElementType) message;
		notif = notif_elem.getResourcePropertyValueChangeNotification();
		
		if(notif != null)
		{
			System.out.println("A notification has been delivered");
			System.out.print("Old value: ");
			System.out.println(notif.getOldValue().get_any()[0].getValue());
			System.out.print("New value: ");
			System.out.println(notif.getNewValue().get_any()[0].getValue());
		}
		
	}
	
	public static void main(String[] args) 
	{
		ValueLIstener client = new ValueLIstener();
		client.run(args[0]);
	}
}
