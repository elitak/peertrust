
/*
 * Created on Aug 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;



import java.util.StringTokenizer;

import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageType;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AuthorizedClient 
{

	public AuthorizedClient()
	{
		
	}
	
	public String isAuthorized(TrustNegotiationNotificationMessageType mesg)
	{
	
		String str[] = mesg.getTrace();
		int len = str.length;
		
		if(str[len-1].indexOf("request")>=0)
		{
			System.out.println("AuthorizedClient last request");
			StringTokenizer stok = new StringTokenizer(str[len-1],",()");
			stok.nextToken();
			String op = stok.nextToken();
			System.out.println("AuthorizedClient operation "+op);
			return op;
		}
		return null;
	}
	
}
