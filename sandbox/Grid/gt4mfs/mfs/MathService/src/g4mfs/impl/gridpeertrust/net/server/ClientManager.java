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


import org.apache.log4j.Logger;
import org.globus.wsrf.Topic;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * the ClientManager class is used to check if a client is already authorized or if it has already an notificationURI through which 
 * it can negotiate trust
 * 
 */
public class ClientManager 
{
	Hashtable topicClient; //hash(topic) - TopicClient(subjectDN,issuerDN,topic,date)-> added in getNotificationURI initial mapping
		                   //removed once the negotiation has ended 
	Hashtable authorizedClient; //hash(subjectDN,issuerDN,operation)-subjectDN-issuerDN-operation-proxyDate
	private static Logger logger = Logger.getLogger(ClientManager.class.getName());
	
	
	
	public ClientManager()
	{
		topicClient = new Hashtable();
		authorizedClient = new Hashtable();
	}
	
	/**
	 * when a client requests a topic for negotiating trust store the topic and client identification in the ClientManager 
	 * @param subjectDN
	 * @param issuerDN
	 * @param topic
	 * @param date
	 */
	public synchronized void addTopicClient(String subjectDN, String issuerDN, Topic topic,Date date)
	{
		int hash = (topic.getName().getNamespaceURI()+topic.getName().getLocalPart()).hashCode();
		topicClient.put(new Integer(hash),new TopicClient(subjectDN,issuerDN,topic,date));
	}
	
	/**
	 * when a client is authorized store the client identification and its proxy certificate expiration date for caching the authorization
	 * decision 
	 * @param subjectDN
	 * @param issuerDN
	 * @param operation
	 * @param date
	 */
	public synchronized void  addAuthorizedClient(String subjectDN, String issuerDN,String operation,Date date)
	{
		int hash = (subjectDN+issuerDN+operation).hashCode();
		authorizedClient.put(new Integer(hash), new ClientEntry(subjectDN,issuerDN,operation,date));	
	}
	
	/**
	 * in case a client is authorized the TopicClient object associated with the client topic holds the client identification information
	 * subjectDN, issuerDN, expirationDate of his proxy certificate
	 * @param topic
	 * @return
	 */
	
	public synchronized TopicClient getTopicClient(Topic topic)
	{
		Integer val  = new Integer((topic.getName().getNamespaceURI()+topic.getName().getLocalPart()).hashCode());
		return (TopicClient) topicClient.get(val);
	}
	
	/**
	 * check if a client is Authorized
	 * @param subjectDN
	 * @param issuerDN
	 * @param operation
	 * @return
	 */
	public boolean isAuthorized(String subjectDN,String issuerDN,String operation)
	{
		Integer val = new Integer((subjectDN+issuerDN+operation).hashCode());
		Date date = new Date();
		
		if(authorizedClient.get(val)!= null)
		{
			ClientEntry clientEntry = (ClientEntry) authorizedClient.get(val);
			if(date.after(clientEntry.getExpirationDate()))
			{
				logger.info("ClientManager found client but certificate expired "+subjectDN+" "+issuerDN+" "+operation);
				return false;
			}
			else
			{
				logger.info("ClientManager found client and certificate ok "+subjectDN+" "+issuerDN+" "+operation);
				return true;
			}
		}
		else
		{
			logger.info("ClientManager client does not exist "+subjectDN+" "+issuerDN+" "+operation);
			return false;
		}
		
	}
	
	
	
	/**
	 * the checkAuthorizedClient function should be run perriodically in order to remove client entries with expired certificates
	 */
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
