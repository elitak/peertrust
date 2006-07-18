/*
 * Created on Jul 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.wrappers;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.axis.message.MessageElement;
import org.globus.axis.gsi.GSIConstants;
import org.globus.cas.CASPortType;
import org.globus.cas.faults.CasFault;
import org.globus.cas.impl.CasConstants;
import org.globus.cas.impl.client.CasClientException;
import org.globus.cas.impl.client.CasClientSetup;
import org.globus.cas.impl.client.CasProxyHelper;
import org.globus.cas.impl.client.ClientParams;
import org.globus.cas.types.ArrayOfSAMLAuthzQueryType;
import org.globus.cas.types.GetAssertionParam;
import org.globus.cas.types.SAMLAssertion;
import org.globus.cas.types.SAMLAuthzQueryType;
import org.globus.common.CoGProperties;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.X509Extension;
import org.globus.gsi.X509ExtensionSet;
import org.globus.gsi.bc.BouncyCastleCertProcessingFactory;
import org.globus.wsrf.impl.security.SecurityMessageElement;
import org.globus.wsrf.security.Constants;
import org.opensaml.SAMLAction;
import org.opensaml.SAMLAuthorizationDecisionQuery;
import org.opensaml.SAMLAuthorizationDecisionStatement;
import org.opensaml.SAMLException;
import org.opensaml.SAMLSubject;
import org.w3c.dom.Element;
/**
 * @author ionut constandache ionut_con@yahoo.com
 * use this class when an assertion is required from a CAS Service
 * the call will be made and the certificate assertion retrieved returned in a proxy certificate 
 */
public class CasWrapper 
{
	
	String instanceURL;
	String serverIdentity;
	
	
	public CasWrapper()
	{
		
	}
	
	public CasWrapper(String instanceURL, String serverIdentity)
	{
		this.instanceURL = instanceURL;
		this.serverIdentity = serverIdentity;
	}
	
	public void setInstanceURL(String instanceURL)
	{
		this.instanceURL = instanceURL;
	}
	
	public void setServerIdentity(String serverIdentity)
	{
		this.serverIdentity = serverIdentity;
	}
	
	
	
	
	
	public String retrieveProxy()
	{
		String path = null;
		CasClientSetup clientSetup = new CasClientSetup();
		CASPortType casPort = clientSetup.getCASPort(instanceURL,serverIdentity);
		ClientParams clientParams = new ClientParams(); // assertion lifetime default 24 hours, default proxy file used the proxy containing 
														// the embedded assertions is saved in a file having the same name with the default
														// proxy plus an extension .cas
		
		// TODO add the possibility of specifying the proxy used for for retrieving the assertion from the CAS server
	    // clientParams.setProxyFileName(proxyFilename);
		
		// the filename where the proxy would be saved can be specified 
		// clientParams.setCasProxyFileName(casProxyFilename);
		
		// extension to append to the original proxy filename -> default .cas
		// clientParams.setCasProxyTag(tag);
		
		
		// query for certain resource/actions for which assertion is requested
		clientParams.setSecurityType(Constants.GSI_TRANSPORT);
		
		try
		{
			CasProxyHelper casProxyHelper = new CasProxyHelper(instanceURL);
			String casProxyFilename = casProxyHelper.getCasProxy(clientParams);
			System.out.println("Cas proxy saved in "+casProxyFilename);
			return casProxyFilename;
		}
		catch(CasClientException ex)
		{
			System.out.println("CasWrapper CasClientException");
			ex.printStackTrace();
		}
		
		
		
		return path; 
	}
	
	public void retrieveSAMLAssertion()
	{
		
		String path = null;
		ClientParams clientParams = new ClientParams();
		
		 // set of all assertions
        
		 Vector confMethods = new Vector(1);
	     confMethods.add(CasConstants.X509_CONFIRMATION_METHOD);

	     GlobusCredential credential = null;
	     try
		 {
	     	credential = GlobusCredential.getDefaultCredential();
		 }
	     catch(Exception ex)
		 {
	     	ex.printStackTrace();
		 }
	     //	   Set subject dn and trust anchor DN from proxy used.
	     String clientDN = credential.getIdentity();
	     System.out.println("Client DN is " + clientDN);
	     String clientTADN = credential.getIssuer();
	     System.out.println("Client cert TA DN is " + clientTADN);
	     
	     
	     // SAML Subject
	     SAMLSubject samlSubject = null;
	     try 
		 {
	         samlSubject = new SAMLSubject(clientDN, clientTADN, CasConstants.X509_FORMAT,confMethods, null, null);
	     } 
	     catch (SAMLException exp) 
		 {
	         String err = "Cannot construct SAML Subject";
	         exp.printStackTrace();
		 }
			
		
		String resourceWildcard = CasConstants.RESOURCE_WILDCARD;
        Vector wildCardActions = new Vector(1);
        
        SAMLAction wildCardAction = null;
        try 
		{
           	wildCardAction = new SAMLAction(CasConstants.ACTION_NS_WILDCARD, CasConstants.ACTION_WILDCARD);
        } 
        catch (SAMLException samlExp) 
		{
        	String err = "Error constucting wildcard SAMLAction \n";
            samlExp.printStackTrace();
		}
        
        wildCardActions.add(wildCardAction);
        SAMLAuthorizationDecisionQuery samlAuthQuery = null;
        try 
		{
            samlAuthQuery = new SAMLAuthorizationDecisionQuery(samlSubject,resourceWildcard,wildCardActions,null);
        } catch (SAMLException samlExp) 
		{
            String err = "Error constucting SAML Authz decision query \n";
            samlExp.printStackTrace();
        }
       
        SAMLAuthzQueryType[] samlAnyQueries = null;
        samlAnyQueries = new SAMLAuthzQueryType[1];
        samlAnyQueries[0] = new SAMLAuthzQueryType();
        samlAnyQueries[0].set_any(new MessageElement[] {new SecurityMessageElement((Element)samlAuthQuery.toDOM())});
        
        CasClientSetup clientSetup = new CasClientSetup();
        //String securityType = Constants.GSI_TRANSPORT;
        CASPortType casPort = clientSetup.getCASPort(instanceURL,serverIdentity);
        
        SAMLAssertion assertion = null;
        
        try
		{
        	ArrayOfSAMLAuthzQueryType arrayOfSamlAuthz = new ArrayOfSAMLAuthzQueryType(samlAnyQueries);
        	GetAssertionParam getAssertion = new GetAssertionParam();
        	getAssertion.setSamlAuthzQuery(arrayOfSamlAuthz);
        	assertion = casPort.getAssertion(getAssertion);
		}
        catch(Exception ex)
		{
        	ex.printStackTrace();
		}        
        
        org.opensaml.SAMLAssertion samlAssertion = null;
        if (assertion != null)
        {
        	org.apache.xml.security.Init.init();
        	try
			{
        		MessageElement[] msgElement = assertion.get_any();
        		samlAssertion = new org.opensaml.SAMLAssertion(msgElement[0].getAsDOM());;
			}
        	catch(Exception ex)
			{
        		ex.printStackTrace();
			}
        }
        
        try
		{
        	samlAssertion.verify(false);
		}
        catch(SAMLException samlExp)
		{
        	samlExp.printStackTrace();
		}
        
        System.out.println("Issuer: "+samlAssertion.getIssuer());
        
        
        Iterator it = samlAssertion.getStatements();
        
        int i = 0;
        
        while(it.hasNext())
        {
        	i++;
        	SAMLAuthorizationDecisionStatement sads = (SAMLAuthorizationDecisionStatement) it.next();
        	System.out.println(i+" "+sads.getClass().getName());
        	System.out.println(sads);
        	System.out.println("decision: "+sads.getDecision()+" resource "+sads.getResource());
        	SAMLSubject ss = sads.getSubject();
        	System.out.println("Subject name: "+ss.getName());
        	
        	Iterator it2 = sads.getActions();
        	System.out.println("Actions: ");
        	while(it2.hasNext())
        	{        		
        		SAMLAction sa = (SAMLAction) it2.next();
        		System.out.println("namespace "+sa.getNamespace()+" data "+sa.getData());
        	}
        	Iterator it3 = sads.getEvidence();
        	System.out.println("Evidences ");
        	while(it3.hasNext())
        	{
        		System.out.println(it3.next());
        	}        	
        }
        
        //System.out.println("SAML ASSERTION IS: "+samlAssertion.toString());
        
        boolean critical = false;
        X509ExtensionSet extensionSet = new X509ExtensionSet();
        X509Extension extension = new X509Extension(CasConstants.OID,critical,samlAssertion.toString().getBytes());
        
        extensionSet.add(extension);
        
        
        BouncyCastleCertProcessingFactory certProcessingFactory = BouncyCastleCertProcessingFactory.getDefault();
        
        
        Long lifetime = new Long(credential.getTimeLeft());
        
        GlobusCredential newCredential = null;
        try
		{
        	newCredential = certProcessingFactory.createCredential( credential.getCertificateChain(),
        														    credential.getPrivateKey(),
																	512,
																	Integer.parseInt(lifetime.toString()),
																	GSIConstants.GSI_3_IMPERSONATION_PROXY,
																	extensionSet,
																	null );
		}
        catch(Exception ex)
		{
        	ex.printStackTrace();
		}
        
        String proxyFilename = CoGProperties.getDefault().getProxyFile();
        String casProxyFilename = proxyFilename + "." + CasConstants.CAS_PROXY_TAG;
        
        System.out.println("proxyFilename: "+proxyFilename+" casProxyFilename: "+casProxyFilename);
        
        try
		{
        	newCredential.save(new FileOutputStream(casProxyFilename));
		}
        catch(Exception ex)
		{
        	ex.printStackTrace();
		}
        
        /*
        String permissionCommand = "chmod 600 "+casProxyFilename;
        Runtime runtime = Runtime.getRuntime();
        boolean success = true;
        try
		{
        	Process p = runtime.exec(permissionCommand,null);
        	int returnCode = p.waitFor();
        	if(returnCode != 0)
        	{
        		success = false;
        	}
		}
        catch(Exception e)
		{
        	success = false;
        	e.printStackTrace();
		}
        
        if(!success)
        {
        	System.out.println("Check permission on proxy");
        }*/
        
        FileInputStream fin = null;
        
        try
		{
        	fin = new FileInputStream(casProxyFilename);
		}
        catch(Exception ex)
		{
        	ex.printStackTrace();
		}
        
        /*
        BouncyCastleCertProcessingFactory certProcessingFactory2 = BouncyCastleCertProcessingFactory.getDefault();
        
        X509Certificate loadCert = null;
        try
		{
        	loadCert = certProcessingFactory2.loadCertificate(fin);
		}
        catch(Exception ex)
		{
        	ex.printStackTrace();
		}
        
        Set set = loadCert.getNonCriticalExtensionOIDs();
        Iterator it4 = set.iterator();
        
        while(it4.hasNext())
        {
        	Object o = it4.next();
        	System.out.println("Class: "+o.getClass().getName()+" OID "+o);
        }
        
        byte[] b = loadCert.getExtensionValue(CasConstants.OID);
        for(int i1=0;i1<b.length;i1++)
        {
        	System.out.println(b[i1]);
        }*/
        
        GlobusCredential glCredential = null;
        try
		{
        	glCredential = new GlobusCredential(fin);
		}
        catch(Exception ex)
		{
        	ex.printStackTrace();	
		}
        
        System.out.println("loaded identity "+glCredential.getIdentity());
        X509Certificate[] certChain = glCredential.getCertificateChain();
        
        
        
        
        for(i=0;i<certChain.length;i++)
        {
        	try
			{
        		byte[] b = X509Extension.getExtensionValue(certChain[i],CasConstants.OID);
        		if(b != null)
        		{ 
        			
        			ByteArrayInputStream bais = new ByteArrayInputStream(b); 
        			String str = new String(b);
        			org.opensaml.SAMLAssertion samlAss = new org.opensaml.SAMLAssertion(bais);
        			
        			Iterator it5 = samlAssertion.getStatements();
        	        
        			while(it5.hasNext())
        	        {
        	           	SAMLAuthorizationDecisionStatement sads2 = (SAMLAuthorizationDecisionStatement) it5.next();
        	        	System.out.println("decision2: "+sads2.getDecision()+" resource2 "+sads2.getResource());
        	        	SAMLSubject ss2 = sads2.getSubject();
        	        	System.out.println("Subject name2: "+ss2.getName());
        	        }
        			
				}
			}
        	catch(Exception ex)
			{
        		ex.printStackTrace();
			}
        }
	
	}
}
