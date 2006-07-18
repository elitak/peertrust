
/*
 * Created on Aug 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;



import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageType;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * The AuthorizedClient class is used to check if the message sent to a client is the last message from a successful trust negotiation
 * 
 */
public class AuthorizedClient 
{

	private static Logger logger = Logger.getLogger(AuthorizedClient.class.getName());
	
	public AuthorizedClient()
	{
		
	}
	
	public String isAuthorized(TrustNegotiationNotificationMessageType mesg)
	{
	
		String str[] = mesg.getTrace();
		int len = str.length;
		
		if(str[len-1].indexOf("request")>=0)
		{
			logger.info("Authorized Client last request");
			StringTokenizer stok = new StringTokenizer(str[len-1],",()");
			stok.nextToken();
			String op = stok.nextToken();
			logger.info("Authorized Client operation "+op);
			return op;
		}
		return null;
	}
	
}
