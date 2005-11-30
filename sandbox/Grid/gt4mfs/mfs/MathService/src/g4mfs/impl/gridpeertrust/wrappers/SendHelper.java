/*
 * Created on Jun 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.wrappers;



import java.util.HashMap;

/**
 * @author ionut
 * Holds all the known SendWrappers associated with their address
 * When a message is delivered to the cliend for sending the client would look here to see for stored SendWrapper pick the one 
 * associated with the destination address and send the data
 */
public class SendHelper 
{
	private HashMap portTypesHashMap = new HashMap();
	
	
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
