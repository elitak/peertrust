/*
 * Created on Jun 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.client;

import g4mfs.impl.gridpeertrust.util.SyncQueue;
import g4mfs.impl.gridpeertrust.wrappers.MessageDescription;
import g4mfs.impl.org.peertrust.net.Message;
import g4mfs.impl.org.peertrust.net.Peer;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.apache.axis.message.addressing.EndpointReferenceType;



/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GridClientTrustNegotiation 
{
	
	boolean finish = false;   // if the negotiation process has finished
	boolean succeeded = false; // if the negotiation process has succeeded
	boolean isSleeping = false; // if the thread using this object is sleeping
	GridClientNotificationThread notificationThread = new GridClientNotificationThread();
	SyncQueue queue;
	QName notificationQName;
	
	public GridClientTrustNegotiation()
	{
		
	}
	
	
	public synchronized boolean isFinished()
	{
		return finish;
	}
	
	public synchronized void setFinished()
	{
		finish = true;
		if(isSleeping)
		{
			System.out.println("dormea si l-am trezit");
			notify(); // in case the thread is sleeping
		}
		else
		{
			System.out.println("e trezit");	
		}
	}
	
	
	public synchronized boolean isSuccess()
	{
		return succeeded;
	}
	
	public synchronized void setSucces(boolean flag) 
	{
		succeeded = flag;
		setFinished(); // it is also finished
		System.out.println("am setat finished");
	}
	
	public void setNotificationQName(String namespaceURI, String localPart)
	{
		notificationQName = new QName(namespaceURI,localPart);	
	}
	
	public void setSyncQueue(SyncQueue sq)
	{
		queue = sq;
	}
	
	public boolean checkPolicyException(Exception e)
	{
		String str = e.getMessage();
		if(str.indexOf("e clar ceva in neregula")>-1)
		{
			return true;
		} 
		else return false;
	}
	
	public void startListening(String serviceURI)
	{
		notificationThread.setServiceURI(serviceURI);
		notificationThread.setNotificationURI(notificationQName);
		notificationThread.setSyncQueue(queue);
		//be sure that the other thread has registered for notifications sleeping
		//????????????????????????????????????????????????
		notificationThread.start();
	}
	
	public void startListeningEPR(EndpointReferenceType epr)
	{
		notificationThread.setServiceEPR(epr);
		notificationThread.setNotificationURI(notificationQName);
		notificationThread.setSyncQueue(queue);
		//be sure that the other thread has registered for notifications sleeping
		//????????????????????????????????????????????????
		notificationThread.start();
	}
	
	
	
	//?????????????????????? kill or not the GridCLientNotification Thread
	
	
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
					System.out.println("GridClientTrustNegotiation sleeping");
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
					System.out.println("GridClientNotification Am prins interrupted exception");
					ex.printStackTrace();
				}
			}
		}
	}	
	
}		

