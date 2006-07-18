/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;

import org.apache.log4j.Logger;

import g4mfs.impl.gridpeertrust.util.SyncQueue;
import g4mfs.impl.org.peertrust.net.Message;

import g4mfs.impl.org.peertrust.net.NetServer;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * GridNetServer receives messages through a SyncQueue. The SyncQueue is loaded through the trustNegotiate call on the service side and
 * through the GridClientNotificationThread on the client side
 */
public class GridNetServer implements NetServer
{
	SyncQueue queue;
	private static Logger logger = Logger.getLogger(GridNetServer.class.getName());
	
	public GridNetServer()
	{

	}
	
	public GridNetServer(SyncQueue sq)
	{
		queue = sq;
	}
	
	
	public void setSyncQueue(SyncQueue q)
	{
		queue = q;
	}
	
	
	public Message listen()
	{
		// the service will listen for trustNegotiatie (TrustNegotiationProvider)/deliver (GridClientNotificationThread) calls - every time such a call is made a new Message is put in the SyncQueue
		// and it will be read this function
	
		logger.info("GridNetServer listening for messages / waiting on the SyncQueue");
		Message mesg = (Message) queue.pop();
		logger.info("GridNetServer retrieved the message "+mesg+" from the SyncQueue");
		return mesg;
	}
		
}
