/*
 * Created on Jun 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.wrappers;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * As the ways to interact with a peer may be unknown, this interface is used to abstract the process of message sending to another peer.
 * For each service contacted by the client a SendWrapper should be provided   
 */

public interface SendWrapper 
{
	
	
	/**
	 * @param peer of type org.peertrust.net.Peer should be transformed in the Peer type used by the
	 * destination port in order to travers the Grid
	 */
	public void setPeer(Object peer); 
	
	/**
	 * @param mesg of type org.peertrust.net.Message should be transformed in the message type 
	 * used by the destination port
	 */
	public void setMessage(Object mesg); 
	
	public Object getPeer();
	public Object getMessage();
	public void sendMessage();
}
