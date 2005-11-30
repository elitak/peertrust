/*
 * Created on Aug 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;


import org.globus.wsrf.Topic;

/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ClientManager 
{
	Hashtable topicClient; //hashtopic-subjectDN-issuerDN-topic-proxyDate -> added in getNotificationURI initial mapping
		                   //removed once the negotiation has ended 
	Hashtable authorizedClient; //hash(subjectDN,issuerDN,operation)-subjectDN-issuerDN-operation-proxyDate
	
	public ClientManager()
	{
		topicClient = new Hashtable();
		authorizedClient = new Hashtable();
	}
	
	
	public synchronized void addTopicClient(String subjectDN, String issuerDN, Topic topic,Date date)
	{
		int hash = (topic.getName().getNamespaceURI()+topic.getName().getLocalPart()).hashCode();
		topicClient.put(new Integer(hash),new TopicClient(subjectDN,issuerDN,topic,date));
	}
	
	public synchronized void  addAuthorizedClient(String subjectDN, String issuerDN,String operation,Date date)
	{
		int hash = (subjectDN+issuerDN+operation).hashCode();
		authorizedClient.put(new Integer(hash), new ClientEntry(subjectDN,issuerDN,operation,date));	
	}
	
	public synchronized TopicClient getTopicClient(Topic topic)
	{
		Integer val  = new Integer((topic.getName().getNamespaceURI()+topic.getName().getLocalPart()).hashCode());
		return (TopicClient) topicClient.get(val);
	}
	
	public boolean isAuthorized(String subjectDN,String issuerDN,String operation)
	{
		Integer val = new Integer((subjectDN+issuerDN+operation).hashCode());
		Date date = new Date();
		
		if(authorizedClient.get(val)!= null)
		{
			ClientEntry clientEntry = (ClientEntry) authorizedClient.get(val);
			if(date.after(clientEntry.getExpirationDate()))
			{
				System.out.println("ClientManager Am gasit intrare client dar data a expirat"+subjectDN+" "+issuerDN+" "+operation);
				return false;
			}
			else
			{
				System.out.println("ClientManager Am gasit intrare client si e ok"+subjectDN+" "+issuerDN+" "+operation);
				return true;
			}
		}
		else
		{
			System.out.println("ClientManager Nu am mapare pentru client "+subjectDN+" "+issuerDN+" "+operation);
			return false;
		}
		
	}
	
	public synchronized void checkAuthorizedClient()
	{
		Collection col = authorizedClient.values();
		Iterator it = col.iterator();
		
		
		ClientEntry client;
		Date currentDate = new Date();
		Date clientDate;
		
		while(it.hasNext())
		{
			client = (ClientEntry) it.next();
			clientDate = client.getExpirationDate();
			if(currentDate.after(clientDate))
			{
				it.remove();
			}
		}		
	}
	
	
}
