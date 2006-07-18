/*
 * Created on May 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;





import g4mfs.impl.gridpeertrust.util.InitializationHolder;
import g4mfs.impl.gridpeertrust.util.InitializeNegotiationEngine;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;

import org.apache.log4j.Logger;
import org.globus.gsi.jaas.GlobusPrincipal;
import org.globus.gsi.jaas.SimplePrincipal;
import org.globus.wsrf.impl.security.authorization.exceptions.CloseException;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import org.globus.wsrf.impl.security.authorization.exceptions.InvalidPolicyException;
import org.globus.wsrf.security.authorization.PDP;
import org.globus.wsrf.security.authorization.PDPConfig;
import org.w3c.dom.Node;
import org.globus.wsrf.impl.security.authorization.exceptions.AuthorizationException;

import java.util.StringTokenizer;


/**
 * @author ionut constandache ionut_con@yahoo.com
 * MyPDP protects the MathService from unauthorized calls (it requires for a successful trust negotiation process in order to allow
 * operation invocation). In case the invocation requires a trust negotiation it throws an exception indicating that a trust negotiation 
 * is required  
 */

public class MyPDP implements PDP
{

	Vector permittedOperations = new Vector();  // which operations are permite
	InitializeNegotiationEngine initializeNegotiationEngine = InitializationHolder.initializeNegotiationEngine; //intializationEngine
	ClientManager clientManager = InitializationHolder.clientManager; // the ClientManager holds information about previous successful trust negotiation
	                                                                  // in order to implement the caching mechanism

	private static Logger logger = Logger.getLogger(MyPDP.class.getName());
	
	
	public String[] getPolicyNames()
	{
		return new String[0];
	}
	
	public Node getPolicy(Node query) throws InvalidPolicyException
	{
		return null;
	}
	
	public Node setPolicy(Node query) throws InvalidPolicyException
	{
		return null;
	}

	public boolean isPermitted(Subject peerSubject, MessageContext context, QName operation) throws AuthorizationException 
	{
		
		if(clientManager == null)
		{
			if(InitializationHolder.clientManager != null)
			{
				clientManager = InitializationHolder.clientManager;
				logger.info("MyPDP sets the clientManager");
			}
		}
		
		logger.info("isPermitted called for operation " +operation);
		String subjectDN = null;
		String issuerDN = null;
		String clientOperation = operation.getLocalPart();
		
		
		// the negotiateTrust operation is permitted as it allows the client to send his trust negotiation messages to the service
		// the getNotificationURI operation is permitted as it allows the client to find out the notification URI where to listen for 
		// trust negotiation messages from the service
		if(operation.toString().endsWith("negotiateTrust") || operation.toString().endsWith("getNotificationURI"))
		{
			logger.info("isPemited returned true for operation "+operation);
			return true;
		}
		else
		{
			Set pc = peerSubject.getPublicCredentials();
			Iterator it = pc.iterator();
			int i = 0;
			while(it.hasNext()) // it should be only one entry
			{
				Object o = it.next();
				
				if(i == 0)
				{
					X509Certificate[] tab = (X509Certificate[]) o;
					
					subjectDN = tab[tab.length-1].getSubjectDN().getName();
					issuerDN = tab[tab.length-1].getIssuerDN().getName();
					Date expirationDate = tab[0].getNotAfter();
					
					logger.info("isPermitted invoked by client DN "+subjectDN+" issuer DN "+issuerDN+" expiration date "+expirationDate);
										
				}
				i++;
			}
			
			
			if(clientManager != null)
			{
				logger.info("checking if the client DN "+subjectDN+" issuer DN "+issuerDN+" operation "+clientOperation+" is authorized");
				if(clientManager.isAuthorized(subjectDN, issuerDN,clientOperation))
				{
					logger.info("Client DN "+subjectDN+" issuer DN "+issuerDN+" operation "+clientOperation+" is authorized");
					return true;
				}
				else
				{
					AuthorizationException e = new AuthorizationException("Authorization not allowed! Trust Negotiation required!");
					logger.info("isPermitted throws exceptions as operation "+operation.toString()+" attempted");
					throw e;
				}
			}
			else
			{
				logger.info("clientManager is null");
				
				AuthorizationException e = new AuthorizationException("Authorization not allowed! Trust Negotiation required!");
				logger.info("isPermitted throws exception as operation "+operation.toString()+" attempted");
				throw e;
			}
		}
		
		//return false;
	}

	public void initialize(PDPConfig config, String name, String id) throws InitializeException 
	{
		logger.info("MyPDP initialized");
		// get the property operations from deploy-server.wsdd
		// by setting properties in the  deploy-server.wsdd service operation that are allowed/or not to be invoked can be set
		// the default allowed operations (trustNegotiate/getNotificationURI are hardcoded as they are the enter points for the trust negotation
		// messages)
		String value = (String) config.getProperty(name,"operations");
		
		logger.info("read for property operations the following values "+ value);
		StringTokenizer st = new StringTokenizer(value," ");
		String s;
		while(st.hasMoreTokens())
		{
			s = st.nextToken();
			permittedOperations.add(s);
		}
		
		return;
	}

	public void close() throws CloseException 
	{
		return;
	}
}


/*Set set = peerSubject.getPrincipals();


Iterator it = set.iterator();

while(it.hasNext())
{
	GlobusPrincipal o = (GlobusPrincipal)it.next();
	System.out.println("Object "+o+" class "+o.getClass().getName());
	System.out.println("Name "+o.getName());
	
}*/







/*try
{
	Set pcc = peerSubject.getPublicCredentials(Class.forName("java.security.cert.X509Certificate"));
	
	System.out.println("Dimensiune pcc "+ pcc.size());
	
	if(pcc == null)
	{
		System.out.println("pcc e null");
	} 
	
	it = pcc.iterator();
	
	if(it == null)
	{
		System.out.println("it e null");
	}
	System.out.println("inainte de X509");
	while(true)
	{
		boolean b = it.hasNext();
		
		System.out.println("b este "+b);
		
		if(b == false)
		{
			break;
		}
		Object o = it.next();
		System.out.println("X509 "+o+" class "+o.getClass().getName());
	}
}
catch(Exception ex)
{
	System.out.println("EXCEPTIE la MyPDP");
	ex.printStackTrace();
}*/