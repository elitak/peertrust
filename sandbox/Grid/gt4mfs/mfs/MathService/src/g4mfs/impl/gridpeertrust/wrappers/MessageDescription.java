/*
 * Created on Jun 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.wrappers;


import g4mfs.impl.org.peertrust.net.Message;
import g4mfs.impl.org.peertrust.net.Peer;


/**
 * @author ionut
 * This class is used to abstract the PT message and the PT peer for identifying the destiantion 
 */
public class MessageDescription 
{
	Peer peer;
	Message message;
	
	public MessageDescription(Peer p, Message m) 
	{
		peer = p;
		message = m;
	}

	public void setPeer(Peer p)
	{
		peer = p;
	}
	
	public void setMessage(Message m)
	{
		message = m;
	}
	
	public Peer getPeer()
	{
		return peer;
	}
	
	public Message getMessage()
	{
		return message;
	}
	public String toString()
	{
		return "destination "+peer.toString()+" message "+message.toString();
	}	
}

