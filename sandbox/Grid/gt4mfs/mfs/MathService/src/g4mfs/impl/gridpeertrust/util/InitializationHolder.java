/*
 * Created on Aug 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;

import g4mfs.impl.gridpeertrust.net.client.GridClientTrustNegotiation;
import g4mfs.impl.gridpeertrust.net.server.ClientManager;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InitializationHolder 
{
	public static InitializeNegotiationEngine initializeNegotiationEngine = null;
	public static ClientManager clientManager = null;
	public static GridClientTrustNegotiation gridClientTrustNegotiation = null;
	
	public static void setInitializeNegotiationEngine(InitializeNegotiationEngine eng)
	{
		initializeNegotiationEngine = eng;
	}
	
	//used by the server to store the clientManager
	public static void setClientManager(ClientManager man)
	{
		clientManager = man;
	}
	
	// used by the client to store the gridClientTrustNegotiation
	public static void setGridClientTrustNegotiation(GridClientTrustNegotiation gctn)
	{
		gridClientTrustNegotiation = gctn;
	}
}
