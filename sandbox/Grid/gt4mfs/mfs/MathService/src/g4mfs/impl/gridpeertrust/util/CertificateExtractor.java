/*
 * Created on Aug 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;

import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * extracts from a chain of X509 Certificates the Subject DN, Issuer(CA) DN (first certificate) and expiration date of last certificate (proxy)  
 */
public class CertificateExtractor 
{
	X509Certificate[] chain;
	String subjectDN;
	String issuerDN;
	Date expirationDate;
	
	public CertificateExtractor(X509Certificate[] tab)
	{
		chain = tab;
		expirationDate = tab[0].getNotAfter();
		subjectDN = tab[tab.length-1].getSubjectDN().getName();
		issuerDN = tab[tab.length-1].getIssuerDN().getName();
	}
	
	public String getSubjectDN()
	{
		return subjectDN;
	}

	public String getIssuerDN()
	{
		return issuerDN;
	}

	public Date getExpirationDate()
	{
		return expirationDate;
	}

}
