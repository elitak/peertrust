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
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SuperMessage
{
	
	public final static int MESSAGE_TYPE = 0;
	public final static int QUERY_TYPE = 1;
	public final static int ANSWER_TYPE = 2;

	private Peer source, target;
 	private Trace trace;
	
	private int messageType = -1;
	
	private String goal = null;
	private String proof = null;
	private int status = -1;
	private long reqQueryId = -1;
	
	
	
	
	public SuperMessage(Peer source, Peer target, Trace trace)
	{
		this.source = source ;
		this.target = target ;
		this.trace = trace;
	}
	
	public void setMessageType(int messageType)
	{
		if (messageType != MESSAGE_TYPE && messageType != QUERY_TYPE && messageType != ANSWER_TYPE)
		{
			System.out.println("SuperMessage setMessageType unknown");
			return;
		}
		
		this.messageType = messageType;
	}
	
	public int getMessageType()
	{
		return messageType;
	}
	
	public String getGoal()
	{
		return goal;
	}
	
	public void setGoal(String goal)
	{
		this.goal = goal;	
	}
	
	public long getReqQueryId()
	{
		return reqQueryId;
	}
	
	public void setReqQueryId(long reqQueryId)
	{
		this.reqQueryId = reqQueryId;	
	}
		
	public String getProof()
	{
		return proof;
	}
	
	public void setProof(String proof)
	{
		this.proof = proof;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public void setStatus(int status)
	{
		this.status = status;	
	}
}
