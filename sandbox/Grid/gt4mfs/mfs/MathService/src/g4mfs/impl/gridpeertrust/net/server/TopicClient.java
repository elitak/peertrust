/*
 * Created on Aug 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;

import java.util.Date;

import org.globus.wsrf.Topic;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TopicClient 
{
	String subjectDN;
	String issuerDN;
	Topic topic;
	Date expirationDate;
	
	public TopicClient(String subjectDN, String issuerDN, Topic topic, Date expirationDate)
	{
		this.subjectDN = subjectDN;
		this.issuerDN = issuerDN;
		this.topic = topic;
		this.expirationDate = expirationDate;
	}
	
	public String getSubjectDN()
	{
		return subjectDN;
	}

	public String getIssuerDN()
	{
		return issuerDN;
	}
	public Topic getTopic()
	{
		return topic;	
	}
	public Date getExpirationDate()
	{
		return expirationDate;
	}
}
