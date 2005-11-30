package g4mfs.impl;

import java.rmi.RemoteException;

import javax.xml.soap.SOAPMessage;

import org.apache.axis.MessageContext;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.TopicList;
import org.globus.wsrf.TopicListAccessor;

import g4mfs.stubs.AddResponse;
import g4mfs.stubs.GetValueRP;
import g4mfs.stubs.SubstractResponse;

public class MathService //extends GridServiceTrustNegotiation 
{

	private MathResource getResource() throws RemoteException 
	{
		MathResource resource = null;
		try 
		{
			resource = (MathResource) ResourceContext.getResourceContext().getResource();
		} 
		catch (Exception e) 
		
		{
			throw new RemoteException("", e);
		}
		
		return resource;
	}
	
	public TopicList getTopicList() 
	{
		try
		{
			MathResource mathResource = getResource();
			TopicList topicList = mathResource.getTopicList();
			return topicList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public AddResponse add (int a) throws RemoteException
	{
		
		MessageContext msgContext = MessageContext.getCurrentContext();
		
		SOAPMessage soap = msgContext.getMessage();
		
		String str = soap.toString();
		
		
		
		System.out.println("valoarea sosita este "+a);
		MathResource mathResource = getResource();
		
		int valInit = mathResource.getValue();
		//System.out.println("valoarea initiala "+valInit);
		mathResource.setValue(mathResource.getValue()+a);
		mathResource.setLastOp("ADDITION");
		
		return new AddResponse();
	}

	public SubstractResponse substract(int a) throws RemoteException 
	{
	
		MathResource mathResource = getResource();
		mathResource.setValue(mathResource.getValue()-a);
		mathResource.setLastOp("SUBSTRACT");
		
		return new SubstractResponse();
	}
	
	public int getValueRP(GetValueRP params) throws RemoteException
	{
		MathResource mathResource = getResource();
		return mathResource.getValue();
	}


}
