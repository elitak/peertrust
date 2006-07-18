/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;

import org.apache.log4j.Logger;

import g4mfs.impl.gridpeertrust.wrappers.SendHelper;
import g4mfs.impl.gridpeertrust.wrappers.SendWrapper;
import g4mfs.impl.org.peertrust.net.Message;
import g4mfs.impl.org.peertrust.net.Peer;

import g4mfs.impl.org.peertrust.net.NetClient;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * Called by the MetaInterpreter in order to deliver a message to a grid peer. The GridNetClient object locates the SendWrapper associated
 * with a destination/port/address and calls the sendMessage function of the SendWrapper with the message to be delivered as parameter
 */
public class GridNetClient implements NetClient
{
	
	SendHelper sendHelper;  // holds the SendWrappers for the grid peers
	private static Logger logger = Logger.getLogger(GridNetClient.class.getName());
	
	
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
	
	
	/**
	 * delivers a message to a grid peer with the help of a SendWrapper
	 * @param message
	 * @param destination
	 */
	public void send(Message message, Peer destination)
	{
		SendWrapper sw = sendHelper.getSendWrapper(destination.getAddress());	
		// in case destination is a client for notifications getAddress would return a wrapper for the address of the notification topic for
		// which the Peer subscribed, otherwise it would return a wrapper for contacting the destination service
		
		logger.info("Send message from "+message.getSource()+" to "+message.getTarget());
		
		if(sw == null)
		{
			logger.info("Send message to a destiantion with no registered SendWrapper");
			logger.info("Message can not be delivered");
			logger.info("The message is: "+message);
			logger.info("The message destination is: "+destination);
			return;
		}
		sw.setMessage(message);
		sw.sendMessage();
	}

}
