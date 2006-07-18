/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * SyncQueue implements a synchronization queue. Messages delivered from peers are placed in a SyncQueue from where they are processed by the
 * MetaInterpreterListener. The MetaInterpreterListener sleeps on the queue in case there is no message for processing. On the client side
 * a SyncQueue is loaded by the GridClientNotificationThread. On the service side the a SyncQueue is loaded by the TrustNegotiationProvider
  */
public class SyncQueue 
{
	private ArrayList queue;
	private boolean isAsleep = false;  //if there is a thread (MetaInterpreterListener) asleep on the queue
	private boolean finish = false;
	private static Logger logger = Logger.getLogger(SyncQueue.class.getName());
	
	
	public SyncQueue()
	{
		queue = new ArrayList();
		
	}
	
	
	public synchronized void finish()
	{
		finish = true;
		if (isAsleep == true)
		{
			notify();
		}
	}
	
	/**
	 * retrieve a message for processing by the PeertrustEngine, if there is no message in the queue wait until it is loaded
	 * @return
	 */
	
	public synchronized Object pop()
	{
		
		if(finish == true)
		{
			return null;
		}
		
		if(queue.size()>0)
		{
			isAsleep = false;
			return queue.remove(0);
		}
		else
		{
			
			while(true)
			{
				try
				{
					isAsleep = true;
					logger.info("Sleep in SyncQueue, there are no messages to process");
					wait();
					logger.info("Awoke from SyncQueue");
					isAsleep = false;
					if(finish) // check if the processing is done - exit
					{
						return null;
					}
										
					if(queue.size()>0)
					{
						logger.info("Return one message from the SyncQueue");
						return queue.remove(0);
					}
				}
				catch(InterruptedException e)
				{
					System.out.println("SyncQueue InterruptedException");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * messages from grid peers are pushed in the queue either by the GridClientNotificationThread (client side) or TrustNegotiationProvider (server side)
	 * @param o
	 */
	
	public synchronized void push(Object o)
	{
		
		logger.info("Message pushed in the SyncQueue");
		queue.add(o);
		if(isAsleep == true)
		{
			logger.info("There is a thread sleeping on the SyncQueue, notify it");
			notify();
		}		
	}	
}
