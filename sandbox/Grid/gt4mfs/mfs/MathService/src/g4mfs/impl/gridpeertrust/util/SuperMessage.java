/*
 * Created on Jun 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;

import g4mfs.impl.org.peertrust.meta.Trace;
import g4mfs.impl.org.peertrust.net.Peer;



/**
 * @author ionut constandache ionut_con@yahoo.com
 * the SuperMessage class is used to hold the types of messages that could be returned during a negotiation
 * MESSAGE_TYPE, QUERY_TYPE, ANSWER_TYPE to check status of the negotiation. 
 */
public class SuperMessage
{
	
	public final static int MESSAGE_TYPE = 0;
	public final static int QUERY_TYPE = 1;
	public final static int ANSWER_TYPE = 2;
	
	public SuperMessage(){}
	
}
