/*
 * Created on May 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4clifs.impl;

import g4mfs.stubs.CreateResource;
import g4mfs.stubs.CreateResourceResponse;
import g4mfs.stubs.GetValueRP;
import g4mfs.stubs.MathFactoryPortType;
import g4mfs.stubs.MathPortType;
import g4mfs.stubs.service.MathFactoryServiceAddressingLocator;
import g4mfs.stubs.service.MathServiceAddressingLocator;

import org.globus.exec.service.factory.FactoryServiceConfiguration;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
/**
 * @author ionut
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Client 
{
	public static void main(String[] args)
	{
		MathFactoryServiceAddressingLocator factoryLocator = new MathFactoryServiceAddressingLocator();
		MathServiceAddressingLocator instanceLocator = new MathServiceAddressingLocator();
	
		try
		{
			String factoryURI = args[0];
			EndpointReferenceType factoryEPR, instanceEPR;
			MathFactoryPortType mathFactory;
			MathPortType math;
		
			factoryEPR = new EndpointReferenceType();
			factoryEPR.setAddress(new Address(factoryURI));
			mathFactory = factoryLocator.getMathFactoryPortTypePort(factoryEPR);
			
			CreateResourceResponse createResponse = mathFactory.createResource(new CreateResource());
			instanceEPR = createResponse.getEndpointReference();
			
			math = instanceLocator.getMathPortTypePort(instanceEPR);
			System.out.println("client: Created instance");
			
			math.add(10);
			math.add(5);
			System.out.println("client: Current Value:"+math.getValueRP(new GetValueRP()));
			
			math.substract(5);
			System.out.println("client: Current Value:"+math.getValueRP(new GetValueRP()));
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
