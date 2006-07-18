/*
 * Created on Jun 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.client;

import g4mfs.impl.gridpeertrust.util.SyncQueue;
import g4mfs.impl.org.peertrust.net.Message;
import g4mfs.impl.org.peertrust.net.Peer;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;



/**
 * @author ionut constandache ionut_con@yahoo.com
 * Initializes a trust negotiation process
 */
public class GridClientTrustNegotiation 
{
	
	boolean finish = false;     // if the negotiation process has finished
	boolean succeeded = false;  // if the negotiation process has succeeded
	boolean isSleeping = false; // if the thread using this object is sleeping
	GridClientNotificationThread notificationThread = new GridClientNotificationThread();
	SyncQueue queue;
	QName notificationQName;   // the address used to register for notifications regarding the trust negotiation process
							   // notifications are pushed from the service side to the client side 
	private static Logger logger = Logger.getLogger(GridClientTrustNegotiation.class.getName());
	
	
	public GridClientTrustNegotiation()
	{
		
	}
	
	
	/**
	 * check if the negotiation process has finished
	 */
	public synchronized boolean isFinished()
	{
		return finish;
	}
	
	/**
	 * set negotiation process finished
	*/
	public synchronized void setFinished()
	{
		finish = true;
		if(isSleeping)
		{
			notify(); // in case the thread is sleeping
		}
		else
		{
			logger.info("thread awake");	
		}
	}
	
	/**
	 * check if the negotiation process was successful
	*/
	public synchronized boolean isSuccess()
	{
		return succeeded;
	}
	
	/**
	 * set success status
	 */
	
	public synchronized void setSucces(boolean flag) 
	{
		succeeded = flag;
		setFinished(); // it is also finished
		logger.info("finished set");
	}
	
	
	/**
	 * set the notification address - where to listen for service notifications
	*/
	public void setNotificationQName(String namespaceURI, String localPart)
	{
		notificationQName = new QName(namespaceURI,localPart);	
	}
	
	
	public void setSyncQueue(SyncQueue sq)
	{
		queue = sq;
	}
	
	/**
	 * checks whether the exception reaised by the service was caused by the lack of a trust negotiation process  
	 */
	
	public boolean checkPolicyException(Exception e)
	{
		String str = e.getMessage();
		if(str.indexOf("Authorization not allowed! Trust Negotiation required!")>-1)  // checks for the string in the message returned from the service 
		{
			return true;
		} 
		else return false;
	}
	
	/**
	 * creates a listening thread to process service notifications 
	 */
	
	public void startListening(String serviceURI)
	{
		notificationThread.setServiceURI(serviceURI);
		notificationThread.setNotificationURI(notificationQName);
		notificationThread.setSyncQueue(queue);
		//be sure that the other thread has registered for notifications sleeping
		
		notificationThread.start();
	}
	
	/**
	 * creates a listening thread to process service notifications 
	 */
	
	
	public void startListeningEPR(EndpointReferenceType epr)
	{
		notificationThread.setServiceEPR(epr);
		notificationThread.setNotificationURI(notificationQName);
		notificationThread.setSyncQueue(queue);
		//be sure that the other thread has registered for notifications sleeping
		
		notificationThread.start();
	}
	
	
	
	
	public  synchronized boolean negotiate()
	{
		while(true)
		{
			if(isFinished())
			{
				isSleeping = false;
				return isSuccess();
			}	
			else
			{
				try
				{
					logger.info("GridClientTrustNegotiation about to sleep");
					isSleeping = true;
					wait();
					isSleeping = false;
					if(isFinished() || isSuccess())
					{
						return isSuccess();
					}
				}
				catch(InterruptedException ex)
				{
					System.out.println("GridClientNotification caught interrupted exception");
					ex.printStackTrace();
				}
			}
		}
	}	
	
}		

