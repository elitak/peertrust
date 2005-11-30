/*
 * Created on Jun 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.client;



import g4mfs.impl.gridpeertrust.net.server.GridNetClient;
import g4mfs.impl.gridpeertrust.net.server.GridNetServer;
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
public class GridClientFactory implements AbstractFactory
{
	
	GridClientTrustNegotiation gridClientTrustNegotiation;
	SyncQueue queue;
	Peer serverPeer;
	SendHelper sendHelper;
	
	public void setGridClientTrustNegotiation(GridClientTrustNegotiation gctn)
	{
		gridClientTrustNegotiation = gctn;
	}
	
	public void setSyncQueue(SyncQueue sq)
	{
		queue = sq;
	}
	
	
	public void setServerPeer(String alias,String address,int port)
	{
		serverPeer = new Peer(alias,address,port);
	}
	
	public void setSendHelper(SendHelper sendHelper)
	{
		this.sendHelper = sendHelper;
	}
	
	public Peer getServerPeer (String alias)
	{
		System.out.println("\n\nGridClientFactory intreb alias "+alias+"\n\n");
		//return new Peer(alias, "localhost", 0) ;
		return new Peer(alias,"http://www.globus.org/gt4ide/example/MathServiceTrustNegotiation",0);
		//return serverPeer;
	} 
	
	public NetClient createNetClient()
	{
		
		GridNetClient gridNetClient = new GridNetClient();
		gridNetClient.setSendHelper(sendHelper);
		
		return gridNetClient;
	}
	
	public NetServer createNetServer()
	{
		return new GridNetServer(queue);
	}
	public void init()
	{
		
	}
}
