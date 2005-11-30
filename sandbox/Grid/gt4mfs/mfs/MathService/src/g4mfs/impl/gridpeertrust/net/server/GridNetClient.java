/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;

import g4mfs.impl.gridpeertrust.wrappers.SendHelper;
import g4mfs.impl.gridpeertrust.wrappers.SendWrapper;
import g4mfs.impl.org.peertrust.net.Message;
import g4mfs.impl.org.peertrust.net.Peer;

import g4mfs.impl.org.peertrust.net.NetClient;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GridNetClient implements NetClient
{
	
	SendHelper sendHelper;
	
	public GridNetClient()
	{
		
	}
	
	public SendHelper getSendHelper()
	{
		return sendHelper;
	}
	
	public void setSendHelper(SendHelper sh)
	{
		sendHelper = sh;
	}
	
	
	public void send(Message message, Peer destination)
	{
		SendWrapper sw = sendHelper.getSendWrapper(destination.getAddress());	
		// in case destination is a Client for notifications getAddress would return the address of the notification topic for
		// which the Peer subscribed
		// otherwise would return a wrapper for contacting the destination service
		
		System.out.println("GridNetClient trimit mesaj "+message.getSource()+" "+message.getTarget());
		
		if(sw == null)
		{
			System.out.println("\n\nGridNetClient send message destination sw e null\n\n");
			System.out.println("GridNetClient mesaj si destinatie");
			System.out.println(message);
			System.out.println(destination);
			System.out.println("\n\n");
		}
		sw.setMessage(message);
		sw.sendMessage();
	}

}
