/*
 * Created on Jun 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.wrappers;

/**
 * @author ionut
 * this interface is used to abstract the process of message sending to another peer
 * as the ways to interact with a peer may be unknown
 * The intent use is to provide a SendWrapper for each service that might be contacted by the client   
 */
/**
 * @author ionut
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface SendWrapper 
{
	
	
	/**
	 * @param peer peer is going to be of org.peertrust.net.Peer type and it would be transformed in the Peer type used by the
	 * destination port
	 */
	public void setPeer(Object peer); 
	/**
	 * @param mesg mesg is going to be of org.peertrust.net.Message type and it would be transformed in the message type 
	 * used by the destination port
	 */
	public void setMessage(Object mesg); 
	public Object getPeer();
	public Object getMessage();
	public void sendMessage();
}
