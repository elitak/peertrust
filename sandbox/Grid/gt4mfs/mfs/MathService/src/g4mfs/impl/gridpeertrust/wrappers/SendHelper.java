/*
 * Created on Jun 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.wrappers;



import java.util.HashMap;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * Holds all the known SendWrappers associated with their addresses (SendWrappers are used for sending data to a peer over the Grid)
 * In order to deliver a Peertrust message to a peer, the peer associated SendWrapper is retrieved using the peer(destination) address 
 */

public class SendHelper 
{
	private HashMap portTypesHashMap = new HashMap();  //holds pairs of (address, SendWrapper)
	
	
	public SendHelper()
	{
		
	}
	
	public void putSendWrapper(String address,SendWrapper sw)
	{
		portTypesHashMap.put(address,sw);
	}
	
	public SendWrapper getSendWrapper(String address)
	{
		return (SendWrapper) portTypesHashMap.get(address);
	}
	
}
