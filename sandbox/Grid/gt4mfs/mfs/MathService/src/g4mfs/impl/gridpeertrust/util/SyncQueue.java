/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;

import java.util.ArrayList;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SyncQueue 
{
	private ArrayList queue;
	private boolean isAsleep = false;
	private boolean finish = false;
	
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
					System.out.println("\n\nSyncQueue adorm nu e nimic in coada\n\n");
					wait();
					System.out.println("\n\nSyncQueue am fost trezit\n\n");
					isAsleep = false;
					if(finish)
					{
						return null;
					}
										
					if(queue.size()>0)
					{
						System.out.println("\n\nSyncQueue fac return am ceva in coada\n\n");
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
	
	
	public synchronized void push(Object o)
	{
		
		System.out.println("\n\nSyncQueue push in coada\n\n");
		queue.add(o);
		if(isAsleep == true)
		{
			System.out.println("\n\nSyncQueue push fir adormit trimit notify\n\n");
			notify();
		}		
	}	
}
