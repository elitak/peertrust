/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;

import g4mfs.impl.gridpeertrust.util.SyncQueue;
import g4mfs.impl.gridpeertrust.wrappers.SendHelper;
import g4mfs.impl.org.peertrust.net.AbstractFactory;
import g4mfs.impl.org.peertrust.net.Peer;

import g4mfs.impl.org.peertrust.net.NetClient;
import g4mfs.impl.org.peertrust.net.NetServer;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GridFactory implements AbstractFactory
{

	SyncQueue syncQueue;  //wiating queue used in listen call
	SendHelper sendHelper;
	
	public Peer getServerPeer (String alias) 
	{
		System.out.println("\n\nGridFactory intreb alias "+alias+"\n\n");
		//return new Peer(alias, "localhost", 0) ;
		return new Peer(alias,"https://127.0.0.1:8443/wsrf/services/ionut/services/MathService",0);
	}
	
	public NetClient createNetClient()
	{	
		GridNetClient gridNetClient = new GridNetClient();
		gridNetClient.setSendHelper(sendHelper);
		return gridNetClient;
	}
	
	public void init()
	{
		
	}
	
	public NetServer createNetServer()
	{		
		GridNetServer gridNetServer = new GridNetServer();
		gridNetServer.setSyncQueue(syncQueue);
		return gridNetServer;		
	}
	
	public void setSyncQueue(SyncQueue syncQueue)
	{
		this.syncQueue = syncQueue;
	}
	
	public void setSendHelper(SendHelper sendHelper)
	{
		this.sendHelper = sendHelper;
	}
}
