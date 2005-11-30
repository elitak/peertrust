/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;

import g4mfs.impl.gridpeertrust.util.SyncQueue;
import g4mfs.impl.org.peertrust.net.Message;

import g4mfs.impl.org.peertrust.net.NetServer;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GridNetServer implements NetServer
{
	SyncQueue queue;
	
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
		// the service will listen for trustNegotiatie calls - every time such a call is made a new Message is put in the SyncQueue
		// and will be read from there by this function
	
		System.out.println("\n\nGridNetServer listen astept sa iau ceva din coada\n\n");
		Message mesg = (Message) queue.pop();
		System.out.println("\n\nGridNetServer am scos mesaj din coada "+mesg+"\n\n");
		return mesg;
	
	}
		
}
