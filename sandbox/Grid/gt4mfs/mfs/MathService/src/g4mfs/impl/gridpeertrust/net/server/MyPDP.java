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

import org.globus.gsi.jaas.GlobusPrincipal;
import org.globus.gsi.jaas.SimplePrincipal;
import org.globus.wsrf.impl.security.authorization.exceptions.CloseException;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import org.globus.wsrf.impl.security.authorization.exceptions.InvalidPolicyException;
import org.globus.wsrf.security.authorization.PDP;
import org.globus.wsrf.security.authorization.PDPConfig;
import org.w3c.dom.Node;

import java.util.StringTokenizer;


/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyPDP implements PDP
{

	Vector permittedOperations = new Vector();
	InitializeNegotiationEngine initializeNegotiationEngine = InitializationHolder.initializeNegotiationEngine;
	ClientManager clientManager = InitializationHolder.clientManager;
	
	
	
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

	public boolean isPermitted(Subject peerSubject, MessageContext context, QName operation) throws MyAuthorizationException 
	{
		
		if(clientManager == null)
		{
			if(InitializationHolder.clientManager != null)
			{
				clientManager = InitializationHolder.clientManager;
				System.out.println("MyPDP am setat clientManager");
			}
		}
		
		System.out.println("\n\n ********** in isPermitted " +operation +" **********\n\n");
		String subjectDN = null;
		String issuerDN = null;
		String clientOperation = operation.getLocalPart();
		
		
		
		if(operation.toString().endsWith("negotiateTrust") || operation.toString().endsWith("getNotificationURI"))
		{
			System.out.println("\n\n***isPemited am intors true la "+operation+"\n\n");
			
			
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
				System.out.println("ObjectPublicCredentials "+o+" class "+o.getClass().getName());
			
				if(i == 0)
				{
					X509Certificate[] tab = (X509Certificate[]) o;
					
					subjectDN = tab[tab.length-1].getSubjectDN().getName();
					issuerDN = tab[tab.length-1].getIssuerDN().getName();
					Date expirationDate = tab[0].getNotAfter();
					
					
					System.out.println("MyPDP userDN "+subjectDN);
					System.out.println("MyPDP issuerDN "+issuerDN);
					System.out.println("MyPDP date "+expirationDate);
					
				}
				i++;
			}
			
			
			if(clientManager != null)
			{
				System.out.println("MyPDP verific daca client este autorizat "+subjectDN+" "+issuerDN+" "+clientOperation);
				if(clientManager.isAuthorized(subjectDN, issuerDN,clientOperation))
				{
					System.out.println("MyPDP client este autorizat "+subjectDN+" "+issuerDN+" "+clientOperation);
					return true;
				}
				else
				{
					MyAuthorizationException e = new MyAuthorizationException("e clar ceva in neregula");
					System.out.println("\n\n********* isPermitted arunc exceptie s-a apelat "+operation.toString()+" *******");
					e.setReason("caca");
					throw e;
				}
			}
			else
			{
				System.out.println("MyPDP clientManager e null");
				
				MyAuthorizationException e = new MyAuthorizationException("e clar ceva in neregula");
				System.out.println("\n\n********* isPermitted arunc exceptie s-a apelat "+operation.toString()+" *******");
				e.setReason("caca");
				throw e;
			}
		}
		
		//return false;
	}

	public void initialize(PDPConfig config, String name, String id) throws InitializeException 
	{
		System.out.println("\n\n***************MyPDP Am fost initializat **********************\n\n");
		String value = (String) config.getProperty(name,"operations");
		
		System.out.println("\n\n******MyPDP am citit valoarea "+ value+" *******n\n");
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