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
 * Provides the functionality of Trust Negotiation if a provider for a service
 * 
 */

public class TrustNegotiationProvider 
{
	
	Hashtable ht; 
	  
	private static final String LOG_CONFIG_FILE = "/home/ionut/PeertrustFiles/demoClient/.logconfig" ;
	
	SyncQueue syncQueue = new SyncQueue();
	// the messages received from the client are put in this queue. The message is poped out 
	// from the queue in GridNetServer and returned by the listen method
	private SendHelper sendHelper = new SendHelper();
	
	

	InitializeNegotiationEngine initializeNegotiationEngine;  //for initializing the negotiation engine
	ClientManager clientManager;
	
	
	public TrustNegotiationProvider()
	{
		ht = new Hashtable();
		System.out.println("\n\n********am fost apelat constructor TNP******\n\n");	
		init();
	}

	
	// init the whole Trust Negotiation Engine
	public void init()
	{
		 initializeNegotiationEngine = new InitializeNegotiationEngine(
				"hpclinuxcluster","/home/globus/PeertrustFiles/demoServer/entities.dat2",syncQueue,sendHelper,"demo",
				"/home/globus/PeertrustFiles/demoServer/","demoPolicies.eLearn1","minervagui.mca",false,false);
		 InitializationHolder.setInitializeNegotiationEngine(initializeNegotiationEngine);
		 
		 clientManager = new ClientManager();
		 InitializationHolder.setClientManager(clientManager);
	}
	
	public NegotiateTrustResponse negotiateTrust(NegotiateTrust request)
	{
		System.out.println("\n\nTrust Negotiation Provider negotiateTrust am primit mesaj");
		
		
		
		System.out.println("TrustNegotiationProvider primit de la sursa "+request.getSource().getAddress()+" "+request.getSource().getAlias()+" "+request.getSource().getPort()+
				" pentru target "+request.getTarget().getAddress()+" "+request.getTarget().getAlias()+" "+request.getTarget().getPort());
		System.out.println("Goal: "+request.getGoal());
		System.out.println("Trace: ");
		String[] a = request.getTrace();		
		for(int i=0;i<a.length;i++)
			System.out.println(a[i]);		
		if(request.getMessageType() == SuperMessage.ANSWER_TYPE)
		{
			System.out.println("Proof: "+request.getProof()+" Statrus: "+request.getStatus());
		}
		System.out.println("\n\n");
		
		
		
		Message mesg = ConverterClass.negotiateTrustToPtMessage(request);
		
		System.out.println("TrustNegotiationProvider mesajul este:");
		System.out.println(mesg);
		System.out.println("\n\n");
		
		syncQueue.push(mesg); //put the received message in the gridNetServeQueue for being procesed by the negotiation engine
	
		NegotiateTrustResponse ntr = new NegotiateTrustResponse();
		return ntr;
	
	}
	
	
	public GetNotificationURIResponse getNotificationURI(GetNotificationURI u)
	{
		QName response = null; // = MathQNames.RP_TRUST_NEGOTIATION;
		NotificationSendWrapper nsw = new NotificationSendWrapper();
		Topic negotiationTopic = null;
		TopicList topicList = null;
		CertificateExtractor certificateExtractor = null;
		
		try
		{
				
		 org.apache.axis.MessageContext messageContext = org.apache.axis.MessageContext.getCurrentContext();
		 Subject sub = (Subject) messageContext.getProperty(org.globus.wsrf.impl.security.authentication.Constants.PEER_SUBJECT);
			 
		 Set pc = sub.getPublicCredentials();
				
		 Iterator it = pc.iterator();
			 
		 String res;
		 
		 int i = 0;	 		 
		 while(it.hasNext())
		 {
			Object o = it.next();
			System.out.println("ObjectPublicCredentials "+o+" class "+o.getClass().getName());
				
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
		 System.out.println("in getNotificationURI "+" subject DN "+subjectDN + " issuerDN " + issuerDN + " date "+expirationDate);
			
		 
		 try
		 {
		 	Resource resource = ResourceContext.getResourceContext().getResource();
		 
		 	topicList = ((TopicListAccessor)resource).getTopicList();  //this section should be synchronized
		 }
		 catch(Exception ex)
		 {
		 	ex.printStackTrace();
		 }
		  //add a new topic for this client
		  
		  //compute the local part for a qname used by the new topic for the client
		 String localPart = TopicHelper.getUniqueLocal(subjectDN,issuerDN);
		  
		  // obtain the namespace of the service -> this should be set through the initiailize negotiation
		 response = new QName(MathQNames.NS,localPart);
		 negotiationTopic = new SimpleTopic(response); //new topic for client
			
		 topicList.addTopic(negotiationTopic); //test if a topic for this client already exists
		  
		 
		 //add subjectDN issuerDN topic date to CientManager
		 clientManager.addTopicClient(subjectDN,issuerDN,negotiationTopic,expirationDate);
		 
		 
		 nsw.setNegotiationTopic(negotiationTopic);
		 // associate this sendwrapper with the local part of the notification topic URI
		 sendHelper.putSendWrapper(response.getNamespaceURI()+response.getLocalPart(),nsw); 
		
		 
		 LocalPeer localPeer = initializeNegotiationEngine.getMetaInterpreter().getLocalPeer();
		 
		 
		 //
		 //System.out.println("in getNotificationURI "+response.getNamespaceURI()+"____"+response.getLocalPart());
		 localPeer.add(response.getNamespaceURI()+response.getLocalPart(),"https://127.0.0.1:8443/wsrf/services/ionut/services/MathService");
		 
		 
		 
		 
		 
		 String callerIdentity = SecurityManager.getManager().getCaller(); 
		 System.out.println("Caller identity "+callerIdentity);
		 
		 
		 GetNotificationURIResponse resp = new GetNotificationURIResponse();
		 resp.setLocalPart(response.getLocalPart());
		 resp.setNamespaceURI(response.getNamespaceURI());
		 return resp;
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
