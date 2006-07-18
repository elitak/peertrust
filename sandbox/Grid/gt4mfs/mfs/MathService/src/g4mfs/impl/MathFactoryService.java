package g4mfs.impl;




import g4mfs.stubs.CreateResource;
import g4mfs.stubs.CreateResourceResponse;
import g4mfs.stubs.*;


import java.rmi.RemoteException;
import java.net.URL;

import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.ResourceProperties;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.MessageContext;
import org.apache.log4j.Logger;
import org.globus.wsrf.utils.AddressingUtils;
import org.globus.wsrf.container.ServiceHost;


/**
 * 
 * @author ionut constandache ionut_con@yahoo.com
 *
 * Math Factory Service responsible for creating WSRF resources for the Math Service
  */


public class MathFactoryService implements MathQNames  {

	private static Logger logger = Logger.getLogger(MathFactoryService.class.getName()); 
	
	
	public MathFactoryService()
	{
		logger.info("MathFactoryService initialized");	
	}
	
	
	/**
	 * function called by a client to the MathFactoryService in order to create a resource
	 * @param request
	 * @return
	 * @throws RemoteException
	 */
	public CreateResourceResponse createResource(CreateResource request) throws RemoteException 
	{
		ResourceContext ctx = null;
		MathResourceHome home = null;
		ResourceKey key = null;
		
		try 
		{
			ctx = ResourceContext.getResourceContext();
			home = (MathResourceHome) ctx.getResourceHome();
			key = home.create();  //the resource is identified by a key
		} 
		catch (Exception e) 
		{
			System.out.println("Exception thrown at create Resource");
			throw new RemoteException("", e);
		}
		
		EndpointReferenceType epr = null;

		/* Get instance endpoint */
		try 
		{
			URL baseURL = ServiceHost.getBaseURL();
			String instanceService = (String) MessageContext.getCurrentContext().getService().getOption("instance");
			String instanceURI = baseURL.toString() + instanceService;
			epr = AddressingUtils.createEndpointReference(instanceURI, key);
		} 
		catch (Exception e) 
		{
			System.out.println("Exception thrown while obtaining the service instance");
			throw new RemoteException("", e);
		}
		CreateResourceResponse response = new CreateResourceResponse();
		response.setEndpointReference(epr);
		return response;
	}
	
	
}
