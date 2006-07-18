package g4mfs.impl;

import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceIdentifier;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.Topic;
import org.globus.wsrf.TopicList;
import org.globus.wsrf.TopicListAccessor;
import org.globus.wsrf.impl.ResourcePropertyTopic;
import org.globus.wsrf.impl.SimpleResourceProperty;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.impl.SimpleTopic;
import org.globus.wsrf.impl.SimpleTopicList;

/**
 * 
 * @author ionut constandache ionut_con@yahoo.com
 * the WSRF resource of the MathService
 * it will expose a Topic the TrustNegotiationTopic used for pushing negotiation messages as notifications to the client
 * the client will have to register for notifications with the TrustNegotiation topic 
 */


public class MathResource implements Resource, ResourceIdentifier, ResourceProperties, TopicListAccessor 
{

	private ResourcePropertySet propSet;
	private Object key;
	
	private ResourceProperty valueRP;
	private ResourceProperty lastOpRP;
	private TopicList topicList;   //keep track of all the topics published by our resource

	
	public TopicList getTopicList()
	{
		return topicList;
	}
	
	public int getValue() 
	{
		Integer value_obj = (Integer) valueRP.get(0);
		int val = value_obj.intValue();
		return val;
	}

	public void setValue(int value) 
	{
		System.out.println("Setvalue called with "+value);
		Integer value_obj = new Integer(value);
		valueRP.set(0,value_obj);
	}

	public String getLastOp()
	{
		String lastOp_obj = (String) lastOpRP.get(0);
		return lastOp_obj;
	}	
	
	public void setLastOp(String lastOp)
	{
		lastOpRP.set(0,lastOp);
	}
	
	
	public Object create() throws Exception 
	{
		this.key = new Integer(hashCode());
		this.propSet = new SimpleResourcePropertySet(MathQNames.RESOURCE_PROPERTIES);

		try 
		{
			// in order to use notifications SimpleResourceProperty
			valueRP = new SimpleResourceProperty(MathQNames.RP_VALUE);
			valueRP.add(new Integer(0));
			
			lastOpRP = new SimpleResourceProperty(MathQNames.RP_LASTOP);
			lastOpRP.add("NONE");
			
			//trustNegotiationTopic = new SimpleTopic(MathQNames.RP_TRUST_NEGOTIATION);
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e.getMessage());
		}

		
		
		// add to the topicList the resource properties - they can also be topics of interest for the client
		this.topicList = new SimpleTopicList(this);
		
		valueRP = new ResourcePropertyTopic(valueRP);
		((ResourcePropertyTopic) valueRP).setSendOldValue(true);
		
		lastOpRP = new ResourcePropertyTopic(lastOpRP);
		((ResourcePropertyTopic) lastOpRP).setSendOldValue(true);
		
				
		this.topicList.addTopic((Topic) valueRP);
		this.topicList.addTopic((Topic) lastOpRP);
		//this.topicList.addTopic(trustNegotiationTopic);
		
		this.propSet.add(valueRP);
		this.propSet.add(lastOpRP);
		
		return key;
	}

	public ResourcePropertySet getResourcePropertySet() 
	{
		return this.propSet;
	}

	public Object getID() 
	{
		return this.key;
	}
}
