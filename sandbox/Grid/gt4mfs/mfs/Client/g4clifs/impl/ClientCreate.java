/*
 * Created on May 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4clifs.impl;

import javax.xml.rpc.Stub;

import org.globus.axis.util.Util;

import g4mfs.stubs.*;
import g4mfs.impl.MathQNames;
import g4mfs.impl.gridpeertrust.net.client.GridClientTrustNegotiation;
import g4mfs.impl.gridpeertrust.util.InitializationHolder;
import g4mfs.impl.gridpeertrust.util.InitializeNegotiationEngine;
import g4mfs.impl.gridpeertrust.util.LocalPeer;
import g4mfs.impl.gridpeertrust.util.SyncQueue;
import g4mfs.impl.gridpeertrust.wrappers.SendHelper;
import g4mfs.impl.gridpeertrust.wrappers.SendWrapper;
import g4mfs.stubs.CreateResource;
import g4mfs.stubs.CreateResourceResponse;
import g4mfs.stubs.MathFactoryPortType;
import g4mfs.stubs.MathPortType;
import g4mfs.stubs.service.MathFactoryServiceAddressingLocator;
import g4mfs.stubs.service.MathServiceAddressing;
import g4mfs.stubs.service.MathServiceAddressingLocator;

import org.apache.axis.AxisFault;
import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.security.Constants;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;

import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.GetNotificationURI;
import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.GetNotificationURIResponse;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClientCreate 
{
	static final String EPR_FILENAME = "epr.txt";
	public static final String PREFIX = "Client app.: " ;
	public final String ALIAS = "Client" ;
	//EventDispatcher _ed;
	
	private static final String LOG_CONFIG_FILE = "/home/ionut/PeertrustFiles/demoClient/.logconfig" ;
	
	
	
	static 
	{
	    Util.registerTransport();
	}
			
	public static void main(String[] args) 
	{
		MathFactoryServiceAddressingLocator factoryLocator = new MathFactoryServiceAddressingLocator();
		MathServiceAddressing instanceLocator = new MathServiceAddressingLocator();
		
		try
		{
			String factoryURI = args[0];
			String eprFilename;
			if (args.length == 2)
			{
				eprFilename = args[1];
			}
			else
			{
				eprFilename = EPR_FILENAME;
			}
			
			EndpointReferenceType factoryEPR, instanceEPR;
			MathFactoryPortType mathFactory;
			MathPortType math;
			
			
			
			factoryEPR = new EndpointReferenceType();
			factoryEPR.setAddress(new Address(factoryURI));
			mathFactory = factoryLocator.getMathFactoryPortTypePort(factoryEPR);
			//((Stub) mathFactory)._setProperty(Constants.GSI_SEC_MSG,Constants.SIGNATURE);
			((Stub) mathFactory)._setProperty(Constants.GSI_TRANSPORT,Constants.SIGNATURE);
			((Stub) mathFactory)._setProperty(Stub.SESSION_MAINTAIN_PROPERTY,new Boolean(true));
			
			CreateResourceResponse createResponse =null;
					
			try
			{
				createResponse = mathFactory.createResource(new CreateResource());
			}
			catch(Exception e)
			{
				System.out.println("Am prins exceptia si ultima oara");
				System.exit(1);
			}
			
			instanceEPR = createResponse.getEndpointReference();
			
			String endpointString = ObjectSerializer.toString(instanceEPR,MathQNames.RESOURCE_REFERENCE);
		
			math = instanceLocator.getMathPortTypePort(instanceEPR);
			((Stub) math)._setProperty(Constants.GSI_SEC_MSG,Constants.SIGNATURE);
			
			System.out.println("Adresa Math Service "+instanceEPR.getAddress().toString());
			//System.out.println("Valoarea este "+value);
		
			String str = new String(((Stub) math).ENDPOINT_ADDRESS_PROPERTY);
			System.out.println("Din stub am obtinut str "+str);

			
			String serviceURI = instanceEPR.getAddress().toString();
			System.out.println("Din stub am obtinut serviceURI "+serviceURI);

			/*GetResourcePropertyResponse valueRP = math.getResourceProperty(MathQNames.RP_VALUE);
			System.out.println("valueRP este "+valueRP.get_any()[0].getValue());
			
			GetResourcePropertyResponse lastOP = math.getResourceProperty(MathQNames.RP_LASTOP);
			System.out.println("lastOP este "+lastOP.get_any()[0].getValue());
			System.exit(1);*/
			
			
			try
			{
				int a = 20;
				math.add(a);
				
				System.out.println("Accesul a fost negociat");
				//math.add(value);
				//System.out.println("ClientADD Current value: "+math.getValueRP(new GetValueRP()));
			}
			catch(Exception e)
			{
				System.out.println("Am prins exceptie la add");
				//e.printStackTrace();
				GridClientTrustNegotiation gridClientTrustNegotiation = new GridClientTrustNegotiation();
				
				
				if(gridClientTrustNegotiation.checkPolicyException(e))
				{
						if(e instanceof AxisFault)
						{
				
							System.out.println("cli Am prins exceptie politica");
							
							System.out.println("cli Incep sa ascult");
							
														//initializePTClient(gridClientTrustNegotiation,sq);
							
							SyncQueue sq = new SyncQueue();
							gridClientTrustNegotiation.setSyncQueue(sq);
							
							// find out the notification QName
							
							GetNotificationURIResponse notificationURI = math.getNotificationURI(new GetNotificationURI());
							
							System.out.println("\n\nClientCreate voi ascult notificati la "+notificationURI.getNamespaceURI()+" "+notificationURI.getLocalPart()+"\n\n");
							
							//System.exit(1);
							
							gridClientTrustNegotiation.setNotificationQName(notificationURI.getNamespaceURI(),notificationURI.getLocalPart());							
						
							//initializePTClient(gridClientTrustNegotiation,sq);
							
							gridClientTrustNegotiation.startListeningEPR(instanceEPR);
							
							
							
							// 	send first message
							
							

							SendHelper sendHelper = new SendHelper();
							MathServiceSendWrapper mathServiceSendWrapper = new MathServiceSendWrapper();
					
							// set the math port type used for sending messages
							mathServiceSendWrapper.setMathPortType(math);
							// set the peer associated with the math port type
							// mathServiceSendWrapper.setPeer(new g4mfs.impl.org.peertrust.net.Peer("eLearn",serviceURI,0));
							mathServiceSendWrapper.setPeer(new g4mfs.impl.org.peertrust.net.Peer("hpclinuxcluster",serviceURI,0));
							// save the wrapper used for the serviceURI address
							sendHelper.putSendWrapper(serviceURI,mathServiceSendWrapper);
							
							// initialize the negotiation engine							
							InitializeNegotiationEngine initializeNegotiationEngine = new InitializeNegotiationEngine(
									"alice","/home/ionut/PeertrustFiles/demoClient/entities.dat1",sq,sendHelper,"demo",
									"/home/ionut/PeertrustFiles/demoClient/","demoPolicies.alice1","minervagui.mca",false,true);
							
							InitializationHolder.setInitializeNegotiationEngine(initializeNegotiationEngine);
							InitializationHolder.setGridClientTrustNegotiation(gridClientTrustNegotiation);
							
							
							
							LocalPeer localPeer = initializeNegotiationEngine.getLocalPeer();
							
							String localURI = notificationURI.getNamespaceURI()+notificationURI.getLocalPart();
							localPeer.add(serviceURI,localURI);
							
							initializeNegotiationEngine.sendQuery("request(add,Session) @ hpclinuxcluster");
							//Thread.sleep(3000);
							
							boolean flag = gridClientTrustNegotiation.negotiate();  // wait for the notification
							if(flag)
							{
								System.out.println("am negociat trust "+flag);
								int a = 20;
								math.add(a);
								
							}
							else
							{
								System.out.println("am negociat trust "+flag);								
							}
							
						}
						else
						{
							System.out.println("Exceptia nu e Axis Fault prins");
							e.printStackTrace();
						}
					}
			}
			
		}
		
		catch(Exception e)
		{
			System.out.println("Am prins a doua exceptie");
			e.printStackTrace();
		}
	
	}
}
