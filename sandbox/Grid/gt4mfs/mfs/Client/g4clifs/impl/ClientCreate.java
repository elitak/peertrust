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
import org.apache.log4j.Logger;


/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * Client for a MathFactoryService & a trust negotiation enabled MathService
 */


public class ClientCreate 
{
	
	private static Logger logger = Logger.getLogger(ClientCreate.class.getName());
	private static String query = "request(add,Session) @ hpclinuxcluster"; // query to send to the MathPort - request for the add operation
	
	
	static 
	{
	    Util.registerTransport();
	}


	/**
	 * Receives as command line parameter the MathFactoryService address
	 */

	
	public static void main(String[] args) 
	{
		MathFactoryServiceAddressingLocator factoryLocator = new MathFactoryServiceAddressingLocator();
		MathServiceAddressing instanceLocator = new MathServiceAddressingLocator();
		
		try
		{
			String factoryURI = args[0];  // get the factory URI
			
			EndpointReferenceType factoryEPR, instanceEPR;
			MathFactoryPortType mathFactory;
			MathPortType math;
			
			
			
			factoryEPR = new EndpointReferenceType();
			factoryEPR.setAddress(new Address(factoryURI));
			mathFactory = factoryLocator.getMathFactoryPortTypePort(factoryEPR);
			
			((Stub) mathFactory)._setProperty(Constants.GSI_TRANSPORT,Constants.SIGNATURE);
			((Stub) mathFactory)._setProperty(Stub.SESSION_MAINTAIN_PROPERTY,new Boolean(true));
			
			CreateResourceResponse createResponse =null;
					
			try
			{
				createResponse = mathFactory.createResource(new CreateResource()); //create the MathService resource
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("Exception caught at mathFactory.createResource");
				System.exit(1);
			}
			
			
			// obtain the address of the resource
			instanceEPR = createResponse.getEndpointReference();
			
			String endpointString = ObjectSerializer.toString(instanceEPR,MathQNames.RESOURCE_REFERENCE);
		
			math = instanceLocator.getMathPortTypePort(instanceEPR);
			((Stub) math)._setProperty(Constants.GSI_SEC_MSG,Constants.SIGNATURE);
			
			
			logger.info("MathService address: "+instanceEPR.getAddress().toString());
			
			String str = new String(((Stub) math).ENDPOINT_ADDRESS_PROPERTY);
			logger.info("From Math Stub the endpoint address is: "+str);
			
			
			String serviceURI = instanceEPR.getAddress().toString();
			logger.info("From Math Stub the service URI is: "+serviceURI);
						
			try
			{
				int a = 20;
				math.add(a);
				
				System.out.println("The access was already negotiated, the add operation succeeded");
			
			}
			catch(Exception e)
			{
				
				
				System.out.println("Exception caught while executing the add operation");
				
				
				// create a GridClientTrustNegotiation object responsible for initializing the trust negotiation process 
				GridClientTrustNegotiation gridClientTrustNegotiation = new GridClientTrustNegotiation();
				
				
				if(gridClientTrustNegotiation.checkPolicyException(e))
				{
						if(e instanceof AxisFault)
						{
				
							System.out.println("Start listening for negotiation notifications");
							
							SyncQueue sq = new SyncQueue();
							gridClientTrustNegotiation.setSyncQueue(sq);
							
							// find out the notification QName
							GetNotificationURIResponse notificationURI = math.getNotificationURI(new GetNotificationURI());
							
							
							logger.info("Listen for notifications at: "+notificationURI.getNamespaceURI()+" "+notificationURI.getLocalPart());
							
							gridClientTrustNegotiation.setNotificationQName(notificationURI.getNamespaceURI(),notificationURI.getLocalPart());							
							gridClientTrustNegotiation.startListeningEPR(instanceEPR);
							
							
							
							// 	send first message - messages to the peer port are sent using SendWrappers
							SendHelper sendHelper = new SendHelper();  // hold the SendWrappers that may be used to communicate with other grid peers
							MathServiceSendWrapper mathServiceSendWrapper = new MathServiceSendWrapper();  // the SendWrapper for communicating with the MathPort of type
																											// MathServiceSendWrapper
					
							// set the math port type used for sending messages - deliver negotiation messages through the negotiatiteTrust operation of the math port
							mathServiceSendWrapper.setMathPortType(math);
							// set the peer associated with the math port type (the peer name is hpcLinuxCluster and has an URI - serviceURI)
							mathServiceSendWrapper.setPeer(new g4mfs.impl.org.peertrust.net.Peer("hpclinuxcluster",serviceURI,0));
							// store the wrapper used to communictae with the math port 
							sendHelper.putSendWrapper(serviceURI,mathServiceSendWrapper);
							
							// initialize the negotiation engine							
							InitializeNegotiationEngine initializeNegotiationEngine = new InitializeNegotiationEngine(
									"alice","PeertrustFiles/demoClient/entities.dat1",sq,sendHelper,"demo",
									"PeertrustFiles/demoClient/","demoPolicies.alice1","minervagui.mca",false,true);
							
							
							// store the negotiation engine instantion and the gridClientTrustNegotiation object used for trust negotation
							InitializationHolder.setInitializeNegotiationEngine(initializeNegotiationEngine);
							InitializationHolder.setGridClientTrustNegotiation(gridClientTrustNegotiation);
							
							
							// LocalPeer is used to hold the local address - address with which the local peer is identified during the negation process
							// in our case it is the notification URI - it will act as the source address in Peertrust
							LocalPeer localPeer = initializeNegotiationEngine.getLocalPeer();
							String localURI = notificationURI.getNamespaceURI()+notificationURI.getLocalPart();
							localPeer.add(serviceURI,localURI);
							
							initializeNegotiationEngine.sendQuery(query);
							
							
							boolean flag = gridClientTrustNegotiation.negotiate();  // wait for the notifications/the trust negotiation process to finish
							if(flag)
							{
								System.out.println("Trust negociated "+flag);
								int a = 20;
								math.add(a);
								
							}
							else
							{
								System.out.println("Trust negotiated "+flag);								
							}
							
						}
						else
						{
							e.printStackTrace();
						}
					}
			}
			
		}
		
		catch(Exception e)
		{
			System.out.println("Exception thrown");
			e.printStackTrace();
		}
	
	}
}
