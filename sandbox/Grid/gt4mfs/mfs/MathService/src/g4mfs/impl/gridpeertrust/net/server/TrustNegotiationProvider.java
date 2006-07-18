 /*
 * Created on Jun 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;


import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.GetNotificationURI;
import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.GetNotificationURIResponse;
import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.NegotiateTrust;
import ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.NegotiateTrustResponse;

import g4mfs.impl.gridpeertrust.util.CertificateExtractor;
import g4mfs.impl.gridpeertrust.util.ConverterClass;
import g4mfs.impl.gridpeertrust.util.InitializationHolder;
import g4mfs.impl.gridpeertrust.util.InitializeNegotiationEngine;
import g4mfs.impl.gridpeertrust.util.LocalPeer;
import g4mfs.impl.gridpeertrust.util.SuperMessage;
import g4mfs.impl.gridpeertrust.util.SyncQueue;
import g4mfs.impl.gridpeertrust.util.TopicHelper;
import g4mfs.impl.gridpeertrust.wrappers.NotificationSendWrapper;
import g4mfs.impl.gridpeertrust.wrappers.SendHelper;
import g4mfs.impl.gridpeertrust.wrappers.SendWrapper;
import g4mfs.impl.org.peertrust.net.Message;
import g4mfs.impl.MathQNames;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;


import org.apache.log4j.Logger;
import org.globus.gsi.jaas.GlobusPrincipal;
import org.globus.gsi.jaas.JaasSubject;
import org.globus.wsrf.Constants;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.Topic;
import org.globus.wsrf.TopicList;
import org.globus.wsrf.TopicListAccessor;
import org.globus.wsrf.impl.SimpleTopic;
import org.globus.wsrf.impl.security.SecurityManagerImpl;
import org.globus.wsrf.impl.security.authentication.ContextCredential;
import org.globus.wsrf.impl.security.util.AuthUtil;
import org.globus.wsrf.security.SecurityManager;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * Provider for the trust negotiation. Implements the operations in TustNegotiation.wsdl
 * 
 */

public class TrustNegotiationProvider 
{
	
	Hashtable ht; 
	SyncQueue syncQueue = new SyncQueue(); // the messages received from the client are put in this queue. The message is poped out 
											// from the queue in GridNetServer and returned by the listen method
	private SendHelper sendHelper = new SendHelper(); // used to store the SendWrappers needed to communicate with peers over the Grid
	
	InitializeNegotiationEngine initializeNegotiationEngine;  //for initializing the negotiation engine
	ClientManager clientManager;
	private static Logger logger = Logger.getLogger(TrustNegotiationProvider.class.getName());
	private String serviceURI = "https://127.0.0.1:8443/wsrf/services/ionut/services/MathService"; // the TrustNegotiationProvider should be initialized with the serviceURI
																									// to which its functionality is plugged in our case MathService						
	
	private String serviceNamespace = MathQNames.NS; // the TrustNegotiationProvider should be initialized with the serviceNamespace
													// to which its functionality is plugged in our case MathQNames.NS
	
	
	public TrustNegotiationProvider()
	{
		ht = new Hashtable();
		logger.info("TrustNegotiationProvider initialized");	
		init();
	}

	
	// init the Peertrust Engine
	public void init()
	{
		 initializeNegotiationEngine = new InitializeNegotiationEngine(
				"hpclinuxcluster","PeertrustFiles/demoServer/entities.dat2",syncQueue,sendHelper,"demo",
				"PeertrustFiles/demoServer/","demoPolicies.eLearn1","minervagui.mca",false,false);
		 InitializationHolder.setInitializeNegotiationEngine(initializeNegotiationEngine);
		 
		 clientManager = new ClientManager();
		 InitializationHolder.setClientManager(clientManager);
	}
	
	/**
	 * operation called in order to push from the client to the service trust negotiation messages
	 * @param request
	 * @return
	 */
	
	public NegotiateTrustResponse negotiateTrust(NegotiateTrust request)
	{
		
		logger.info("negotiateTrust called");
		logger.info("received from source "+request.getSource().getAddress()+" "+request.getSource().getAlias()+" "+request.getSource().getPort()+
				" for target "+request.getTarget().getAddress()+" "+request.getTarget().getAlias()+" "+request.getTarget().getPort());
		logger.info("Goal: "+request.getGoal());
		
		String deliveredTrace = "Trace: ";
		String[] a = request.getTrace();		
		
		for(int i=0;i<a.length;i++)
			deliveredTrace = deliveredTrace + a[i];
		
		logger.info(deliveredTrace);
				
		if(request.getMessageType() == SuperMessage.ANSWER_TYPE)
		{
			logger.info("Proof: "+request.getProof()+" Status: "+request.getStatus());
		}
		
		
		Message mesg = ConverterClass.negotiateTrustToPtMessage(request);
		
		logger.info ("TrustNegotiationProvider the received message is: "+mesg);
		
		
		syncQueue.push(mesg); //put the received message in the gridNetServeQueue for being procesed by the negotiation engine
	
		NegotiateTrustResponse ntr = new NegotiateTrustResponse();
		return ntr;
	
	}
	
	/**
	 * return to the client the notification URI where to register for receiving trust negotiation messages from the service side
	 * @param u
	 * @return
	 */
	public GetNotificationURIResponse getNotificationURI(GetNotificationURI u)
	{
		QName response = null; 
		NotificationSendWrapper nsw = new NotificationSendWrapper(); // create the NotificationSendWrapper used to notify 
																	 // the client of the trust negotiation messages
		Topic negotiationTopic = null;
		TopicList topicList = null;
		CertificateExtractor certificateExtractor = null;  // used to obtain client DN, issuer DN and expiration date of the client
		
		try
		{
				
		 org.apache.axis.MessageContext messageContext = org.apache.axis.MessageContext.getCurrentContext();
		 // get Peer subject
		 Subject sub = (Subject) messageContext.getProperty(org.globus.wsrf.impl.security.authentication.Constants.PEER_SUBJECT);
			 
		 Set pc = sub.getPublicCredentials();
				
		 Iterator it = pc.iterator();
			 
		 String res;
		 
		 int i = 0;	 		 
		 while(it.hasNext())
		 {
			Object o = it.next();
				
			X509Certificate[] tab = (X509Certificate[]) o;
			
			if(i == 0 ) //should be only one PublicCredntial or the chain of certificates authenticating the client should be first 
			{
			  certificateExtractor = new CertificateExtractor(tab);
			}
			i++;
		  }
		 
		 }
		 catch(Exception e)
		 {
			System.out.println("Exception in getNotificationURI");
			e.printStackTrace();
		 }
			
		 String subjectDN = certificateExtractor.getSubjectDN();
		 String issuerDN = certificateExtractor.getIssuerDN();
		 Date expirationDate = certificateExtractor.getExpirationDate();
		 logger.info("getNotificationURI called by "+" subject DN "+subjectDN + " issuerDN " + issuerDN + " with certificate expiration date "+expirationDate);
			
		 
		 try
		 {
		 	// obtain the topic list of the current client
		 	Resource resource = ResourceContext.getResourceContext().getResource();
		 	topicList = ((TopicListAccessor)resource).getTopicList();  
		 }
		 catch(Exception ex)
		 {
		 	ex.printStackTrace();
		 }
		 
		 
		 // add a new topic for this client - each client will receive a QNAMR where to listen for notifications with 
		 // the trust negotiation messages
		 // compute the local part for a qname used by the new topic for the client
		 String localPart = TopicHelper.getUniqueLocal(subjectDN,issuerDN);
		  
		  // obtain the namespace of the service 
		 response = new QName(serviceNamespace,localPart);
		 negotiationTopic = new SimpleTopic(response); //new topic for client - the address of the topic
			
		 topicList.addTopic(negotiationTopic); //test if a topic for this client already exists
		  
		 //add subjectDN issuerDN topic date to ClientManager
		 clientManager.addTopicClient(subjectDN,issuerDN,negotiationTopic,expirationDate);
		 
		 // associate this sendwrapper with the notification topic URI
		 nsw.setNegotiationTopic(negotiationTopic);
		 sendHelper.putSendWrapper(response.getNamespaceURI()+response.getLocalPart(),nsw); 
		
		 // the service identifies to the remote peer with its address   
		 LocalPeer localPeer = initializeNegotiationEngine.getMetaInterpreter().getLocalPeer();
		 localPeer.add(response.getNamespaceURI()+response.getLocalPart(),serviceURI);
		 
		 String callerIdentity = SecurityManager.getManager().getCaller(); 
		 System.out.println("Caller identity "+callerIdentity);
		 
		 
		 GetNotificationURIResponse resp = new GetNotificationURIResponse();
		 resp.setLocalPart(response.getLocalPart());
		 resp.setNamespaceURI(response.getNamespaceURI());
		 return resp;  // return to the client the notificationURI where to listen for the status of the negotiation
	}
	
}//inchide clasa




//ii inregistrez DN si topicul si cand a reusit negocierea il mapez cu ok
/*
import org.globus.wsrf.security.SecurityManager;
Subject subject = JaasSubject.getCurrentSubject();
Set set = subject.getPrincipals();
Iterator it = set.iterator();
while(it.hasNext())
{
	System.out.println("Principal "+it.next());
}

String identity = SecurityManager.getManager().getCaller();
System.out.println("Identity "+identity);


return response;*/


/*Resource resource = ResourceContext.getResourceContext().getResource();

	Iterator it = ((TopicListAccessor)resource).getTopicList().topicIterator();
	// create the namespace for listening for this client return the namespace


	while(it.hasNext())
	{
		Topic t = (Topic) it.next();
		System.out.println("\n\nTNP Local Part "+t.getName().getLocalPart());
		System.out.println("TNP NamespaceURI "+t.getName().getNamespaceURI());
		System.out.println("TNP Prefix "+t.getName().getPrefix());
		if(t.getName().getLocalPart().indexOf("TrustNegotiation")>=0)
		{
			System.out.println("******TNP Am gasit NegotiationTopic******\n\n");
			negotiationTopic = t;
		}
	}*/







/* Subject subject = JaasSubject.getCurrentSubject();

Iterator it = subject.getPrincipals().iterator();

while(it.hasNext())
{
	while(it.hasNext())
	{
		GlobusPrincipal o = (GlobusPrincipal)it.next();
		System.out.println("Object "+o+" class "+o.getClass().getName());
		System.out.println("Name "+o.getName());
		
	}
}*/


	

//SecurityManagerImpl securityManagerImpl = SecurityManagerImpl.getManager();


/*GSSCredential gssCredential = ContextCredential.getCurrent();
if(gssCredential != null)
{
	try
	{
		System.out.println("gssCredential name "+gssCredential.getName().toString());
		System.out.println("gssCredential este din clasa "+gssCredential.getClass().getName());
	}
	catch(GSSException ex)
	{
		System.out.println("exceptie la GSSCredential");
		ex.printStackTrace();
	}
}
else
{
	System.out.println("gssCredential e null");
	MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();
	
	
	try
	{
		gssCredential = AuthUtil.getCredential(ctx);
		if(gssCredential != null)
		{
			try
			{
				System.out.println("II gssCredential name "+gssCredential.getName().toString());
		 		System.out.println("II gssCredential este din clasa "+gssCredential.getClass().getName());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			System.out.println("II gssCredential e null");
		}
		
	}
	catch(Exception ex)
	{
		
		System.out.println("exceptie la AuthUtil");
		ex.printStackTrace();
	}
	

}*/
