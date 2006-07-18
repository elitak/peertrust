package g4mfs.impl;

import java.rmi.RemoteException;

import javax.xml.soap.SOAPMessage;

import org.apache.axis.MessageContext;
import org.apache.log4j.Logger;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.TopicList;
import org.globus.wsrf.TopicListAccessor;

import g4mfs.stubs.AddResponse;
import g4mfs.stubs.GetValueRP;
import g4mfs.stubs.SubstractResponse;

/**
 * 
 * @author ionut constandache ionut_con@yahoo.com
 * Math Service implements the logic of the MathService
 */


public class MathService  
{
	
	private static Logger logger = Logger.getLogger(MathService.class.getName());

	/**
	 * obtain the resource associated with this client
	 * @return
	 * @throws RemoteException
	 */
	
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
	
	/**
	 * add operation implemented by the service
	 * @param a
	 * @return
	 * @throws RemoteException
	 */
	public AddResponse add (int a) throws RemoteException
	{
		
		MessageContext msgContext = MessageContext.getCurrentContext();
		SOAPMessage soap = msgContext.getMessage();
		String str = soap.toString();
		
		logger.info("add operation invoked with parameter "+a);
		MathResource mathResource = getResource();
		
		int valInit = mathResource.getValue();
		mathResource.setValue(mathResource.getValue()+a);
		mathResource.setLastOp("ADDITION");
		
		return new AddResponse();
	}

	/**
	 * substract operation implemented by the service
	 * @param a
	 * @return
	 * @throws RemoteException
	 */
	
	public SubstractResponse substract(int a) throws RemoteException 
	{
	
		MathResource mathResource = getResource();
		mathResource.setValue(mathResource.getValue()-a);
		mathResource.setLastOp("SUBSTRACT");
		
		return new SubstractResponse();
	}
	
	/**
	 * getValueRP operation implemented by the service
	 * @param params
	 * @return
	 * @throws RemoteException
	 */
	
	public int getValueRP(GetValueRP params) throws RemoteException
	{
		MathResource mathResource = getResource();
		return mathResource.getValue();
	}


}
