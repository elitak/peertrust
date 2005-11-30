/*
 * Created on Jun 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4clifs.impl;

import g4mfs.impl.MathQNames;
import g4mfs.stubs.*;
import g4mfs.stubs.GetValueRP;
import g4mfs.stubs.MathPortType;
import g4mfs.stubs.service.MathFactoryServiceAddressingLocator;
import g4mfs.stubs.service.MathServiceAddressing;
import g4mfs.stubs.service.MathServiceAddressingLocator;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;

import javax.xml.namespace.QName;
import javax.xml.rpc.Stub;

import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.axis.util.Util;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.security.Constants;
import org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse;
import org.oasis.wsrf.properties.GetMultipleResourceProperties_Element;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import org.oasis.wsrf.properties.SetResourceProperties_Element;
import org.oasis.wsrf.properties.SetResourceProperties_PortType;
import org.oasis.wsrf.properties.UpdateType;
import org.oasis.wsrf.properties.WSResourcePropertiesServiceAddressingLocator;
import org.xml.sax.InputSource;

/**
 * @author ionut
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClientRP 
{
	
	static 
	{
	    Util.registerTransport();
	}
	
	public EndpointReferenceType createInstance(String args) 
	{
		
		
		MathFactoryServiceAddressingLocator factoryLocator = new MathFactoryServiceAddressingLocator();
		MathServiceAddressing instanceLocator = new MathServiceAddressingLocator();
		
		
		try
		{
			String factoryURI = args;
			String eprFilename;
		
			EndpointReferenceType factoryEPR, instanceEPR;
			MathFactoryPortType mathFactory;
			MathPortType math;
		
			factoryEPR = new EndpointReferenceType();
			factoryEPR.setAddress(new Address(factoryURI));
			mathFactory = factoryLocator.getMathFactoryPortTypePort(factoryEPR);
			((Stub) mathFactory)._setProperty(Constants.GSI_SEC_MSG,Constants.SIGNATURE);
			((Stub) mathFactory)._setProperty(Stub.SESSION_MAINTAIN_PROPERTY,new Boolean(true));
			
			CreateResourceResponse createResponse =null;
			createResponse = mathFactory.createResource(new CreateResource());
			
			instanceEPR = createResponse.getEndpointReference();
			String endpointString = ObjectSerializer.toString(instanceEPR,MathQNames.RESOURCE_REFERENCE);
			System.out.println(endpointString);
			
			FileWriter fileWriter = new FileWriter("epr.txt");
			BufferedWriter bfWriter = new BufferedWriter(fileWriter);
			bfWriter.write(endpointString);
			bfWriter.close();
			
			return instanceEPR;
			
		}
		catch(Exception e)
		{
			System.out.println("Am prins exceptie la createInstance");
			e.printStackTrace();
			return null;
		}
	
	
	}
	
	
	private void printMultipleResourceProperties(MathPortType math) throws Exception
	{
		GetMultipleResourceProperties_Element request;
		GetMultipleResourcePropertiesResponse response;
	
		QName[] resourceProperties = new QName[] {MathQNames.RP_VALUE,MathQNames.RP_LASTOP};
		request = new GetMultipleResourceProperties_Element(resourceProperties);
		response = math.getMultipleResourceProperties(request);
		
		for(int i=0;i<response.get_any().length;i++)
		{
			String name = response.get_any()[i].getLocalName();
			String value = response.get_any()[i].getValue();
			System.out.println("ClientRP pmrp"+name + " " + value);
		
		}
	}
	
	
	
	
	private void printResourceProperties(MathPortType math) throws Exception
	{
		GetResourcePropertyResponse valueRP, lastOpRP, lastLogRP;
		String value, lastOp, lastLog;
		
		valueRP = math.getResourceProperty(MathQNames.RP_VALUE);
		lastOpRP = math.getResourceProperty(MathQNames.RP_LASTOP);
		
		value = valueRP.get_any()[0].getValue();
		lastOp = lastOpRP.get_any()[0].getValue();
		
		System.out.println("ClientRP Value RP: "+value);
		System.out.println("ClientRP LastOp RP: "+lastOp);
	}
	
	
	
	private void updateRP(EndpointReferenceType epr, QName rpQName, Integer value) throws Exception
	{
		WSResourcePropertiesServiceAddressingLocator locator = new WSResourcePropertiesServiceAddressingLocator();
		SetResourceProperties_PortType port = locator.getSetResourcePropertiesPort(epr);
		
		UpdateType update = new UpdateType();
		MessageElement msg = new MessageElement(rpQName,value);
		
		update.set_any(new MessageElement[]{msg});
		
		SetResourceProperties_Element request = new SetResourceProperties_Element();
		request.setUpdate(update);
		
		port.setResourceProperties(request);
	}
	
	
	public void runClientRP(EndpointReferenceType args)
	{
		MathServiceAddressingLocator instanceLocator = new MathServiceAddressingLocator();
		
		
		EndpointReferenceType instanceEPR = args;
		try
		{
			MathPortType math = instanceLocator.getMathPortTypePort(instanceEPR);
			
			
			// Perform some operations and print out the resource properties
			printResourceProperties(math);
			math.add(10);
			printResourceProperties(math);
			System.out.println();
			//updateRP(instanceEPR, MathQNames.RP_VALUE, new Integer(100));
			//printResourceProperties(math);
			//System.out.println();
			//printMultipleResourceProperties(math);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	
	
	}
	public static void main(String[] args)
	{
		ClientRP clientRP = new ClientRP();
		
		EndpointReferenceType resourceEPR = clientRP.createInstance(args[0]);
		clientRP.runClientRP(resourceEPR);
	}
}
