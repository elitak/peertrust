/*
 * Created on Jun 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;


import g4mfs.impl.org.peertrust.meta.Trace;
import g4mfs.impl.org.peertrust.net.Answer;
import g4mfs.impl.org.peertrust.net.Message;
import g4mfs.impl.org.peertrust.net.Query;

import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.NegotiateTrust;
import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer;
import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageType;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * class used to convert to and from Peertrust peer/message representation to NegotiateTrust / Peer / TrustNegotiationNotificationMessageType 
 * which are the message/peer representations traversing the Grid 
 */
public class ConverterClass 
{
		
	public static Peer ptPeerToGridPeer(g4mfs.impl.org.peertrust.net.Peer p)
	{
		Peer gp = new Peer();
		gp.setAddress(p.getAddress());
		gp.setAlias(p.getAlias());
		gp.setPort(p.getPort());
		return gp;
	}


	public static g4mfs.impl.org.peertrust.net.Peer gridPeerToPtPeer(Peer p)
	{
		g4mfs.impl.org.peertrust.net.Peer pt = new g4mfs.impl.org.peertrust.net.Peer(p.getAlias(),p.getAddress(),p.getPort());
		return pt;	
	}

	public static NegotiateTrust ptMessageToNegotiatiateTrust(Message mesg)
	{
		
		Peer source = ptPeerToGridPeer(mesg.getSource());
		Peer target = ptPeerToGridPeer(mesg.getTarget());
		String[] trace = mesg.getTrace().getTrace();
		
		NegotiateTrust nt = new NegotiateTrust();
		nt.setSource(source);
		nt.setTarget(target);
		nt.setTrace(trace);
		
		if (mesg instanceof Answer)
		{
			Answer answer = (Answer) mesg;
			nt.setMessageType(SuperMessage.ANSWER_TYPE);
			nt.setGoal(answer.getGoal());
			nt.setProof(answer.getProof());
			nt.setStatus(answer.getStatus());
			nt.setReqQueryId(answer.getReqQueryId());
		}
		else
			if(mesg instanceof Query)
			{
				Query query = (Query) mesg;
				nt.setMessageType(SuperMessage.QUERY_TYPE);
				nt.setGoal(query.getGoal());
				nt.setReqQueryId(query.getReqQueryId());
			}
			else
			{
				nt.setMessageType(SuperMessage.MESSAGE_TYPE);
			}
		
		return nt;
	}
	
	public static Message negotiateTrustToPtMessage(NegotiateTrust nt)
	{
		g4mfs.impl.org.peertrust.net.Peer source = gridPeerToPtPeer(nt.getSource());
		g4mfs.impl.org.peertrust.net.Peer target = gridPeerToPtPeer(nt.getTarget());
		String[] trace = nt.getTrace();
	
		if(nt.getMessageType() == SuperMessage.ANSWER_TYPE)
		{
			Answer answer = new Answer(nt.getGoal(),nt.getProof(),nt.getStatus(),nt.getReqQueryId(),source,target,new Trace(trace));
			return answer;		
		}
		else
			if(nt.getMessageType() == SuperMessage.QUERY_TYPE)
			{
				Query query = new Query(nt.getGoal(),source,target,nt.getReqQueryId(),new Trace(trace));
				return query;
			}
		
		Message mesg = new Message(source,target,new Trace(trace));
		return mesg;	
	}
	
	public static TrustNegotiationNotificationMessageType ptMessageToTrustNegotiationNotificationMessage(Message mesg)
	{
		
		Peer source = ptPeerToGridPeer(mesg.getSource());
		Peer target = ptPeerToGridPeer(mesg.getTarget());
		String[] trace = mesg.getTrace().getTrace();
		
		
		TrustNegotiationNotificationMessageType tnnmt = new TrustNegotiationNotificationMessageType();
		tnnmt.setSource(source);
		tnnmt.setTarget(target);
		tnnmt.setTrace(trace);
		
		if (mesg instanceof Answer)
		{
			Answer answer = (Answer) mesg;
			tnnmt.setMessageType(SuperMessage.ANSWER_TYPE);
			tnnmt.setGoal(answer.getGoal());
			tnnmt.setProof(answer.getProof());
			tnnmt.setStatus(answer.getStatus());
			tnnmt.setReqQueryId(answer.getReqQueryId());
		}
		else
			if(mesg instanceof Query)
			{
				Query query = (Query) mesg;
				tnnmt.setMessageType(SuperMessage.QUERY_TYPE);
				tnnmt.setGoal(query.getGoal());
				tnnmt.setReqQueryId(query.getReqQueryId());
			}
			else
			{
				tnnmt.setMessageType(SuperMessage.MESSAGE_TYPE);
				
			}
		
		return tnnmt;
	}
	
	public static Message trustNegotiationNotificationMessageToPtMessage(TrustNegotiationNotificationMessageType mesg)
	{
		g4mfs.impl.org.peertrust.net.Peer source = gridPeerToPtPeer(mesg.getSource());
		g4mfs.impl.org.peertrust.net.Peer target = gridPeerToPtPeer(mesg.getTarget());
		String[] trace = mesg.getTrace();
		
		if(mesg.getMessageType() == SuperMessage.ANSWER_TYPE)
		{
			Answer answer = new Answer(mesg.getGoal(),mesg.getProof(),mesg.getStatus(),mesg.getReqQueryId(),source,target,new Trace(trace));
			return answer;		
		}
		else
			if(mesg.getMessageType() == SuperMessage.QUERY_TYPE)
			{
				Query query = new Query(mesg.getGoal(),source,target,mesg.getReqQueryId(),new Trace(trace));
				return query;
			}
		
		Message message = new Message(source,target,new Trace(trace));
		return message;
		
	}
}
