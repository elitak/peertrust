/*
 * Created on May 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4clifs.impl;

import java.io.FileInputStream;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.axis.util.Util;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;

import g4mfs.stubs.GetValueRP;
import g4mfs.stubs.MathPortType;
import g4mfs.stubs.service.MathServiceAddressingLocator;

/**
 * @author ionut
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClientAdd 
{
	
	static 
	{
	    Util.registerTransport();
	}
	
	public static void main(String[] args)
	{
		MathServiceAddressingLocator instanceLocator = new MathServiceAddressingLocator();
		try
		{
			int value = Integer.parseInt(args[1]);
			EndpointReferenceType instanceEPR;
			if(args[0].startsWith("http")) 
			{
				String serviceURI = args[0];
				instanceEPR = new EndpointReferenceType();
				instanceEPR.setAddress(new Address(serviceURI));				
			}
			else
			{
				String eprFile = args[0];
				FileInputStream fis = new FileInputStream(eprFile);
				instanceEPR = (EndpointReferenceType) ObjectDeserializer.deserialize(new InputSource(fis), EndpointReferenceType.class);
				fis.close();
			}
			
			
			System.out.println(instanceEPR);
			
			MathPortType math = instanceLocator.getMathPortTypePort(instanceEPR);
			//System.out.println("Valoarea este "+value);
			int a = 20;
			math.add(a);
			//math.add(value);
		    System.out.println("ClientADD Current value: "+math.getValueRP(new GetValueRP()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}
}
