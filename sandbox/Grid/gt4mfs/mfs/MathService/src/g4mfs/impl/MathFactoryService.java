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
import org.globus.wsrf.utils.AddressingUtils;
import org.globus.wsrf.container.ServiceHost;

public class MathFactoryService implements MathQNames  {

	String yo = "mama";
	//GridServiceTrustNegotiation tn = new GridServiceTrustNegotiation();
	
	
	public MathFactoryService()
	{
		
		//TrustNegotiation tn = new TrustNegotiation();
		
		super();
		MessageContext messageContext = MessageContext.getCurrentContext();
		messageContext.setProperty("Mama","Tata nu e aici");
		System.out.println("\n\n constructor MathFactoryService am fost initializat "+yo+"\n\n");
		
	}
	
	public CreateResourceResponse createResource(CreateResource request) throws RemoteException 
	{
		ResourceContext ctx = null;
		MathResourceHome home = null;
		ResourceKey key = null;
		/* Create resource */
		try 
		{
			ctx = ResourceContext.getResourceContext();
			home = (MathResourceHome) ctx.getResourceHome();
			key = home.create();
		} 
		catch (Exception e) 
		{
			System.out.println("\n\n****** Arunc exceptie ******\n\n");
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
			System.out.println("\n\n****** Arunc exceptie 2 ******\n\n");
			throw new RemoteException("", e);
		}
		CreateResourceResponse response = new CreateResourceResponse();
		response.setEndpointReference(epr);
		return response;
	}
	
	
}
