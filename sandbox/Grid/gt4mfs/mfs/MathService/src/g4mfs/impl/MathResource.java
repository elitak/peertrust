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

public class MathResource implements Resource, ResourceIdentifier, ResourceProperties, TopicListAccessor 
{

	private ResourcePropertySet propSet;
	private Object key;

	/* Insert resource properties here. For example: */
	/*private int value; //Resource property
	private String lastOp;  //Reosurce property*/
	
	// in order to use Notifications SimpleResourceProperty
	
	private ResourceProperty valueRP;
	private ResourceProperty lastOpRP;
	private TopicList topicList;   //keep track of all the topics published by our resource

	//private SimpleTopic trustNegotiationTopic;
	
	
	public TopicList getTopicList()
	{
		return topicList;
	}
	
	public int getValue() 
	{
		//System.out.println("\n\n*******A fot apelat getvalue \n\n");
		//if (valu
		Integer value_obj = (Integer) valueRP.get(0);
		int val = value_obj.intValue();
		//System.out.println("getValue val este "+val);
		//return value_obj.intValue();
		//return 10;
		return val;
	}

	public void setValue(int value) 
	{
		System.out.println("\n\n*******A fot apelat setvalue "+value+"\n\n");
		
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
			/*ResourceProperty valueRP = new ReflectionResourceProperty(MathQNames.RP_VALUE,"Value",this);
			this.propSet.add(valueRP);
			setValue(0);
			ResourceProperty lastOpRP = new ReflectionResourceProperty(MathQNames.RP_LASTOP,"LastOp",this);
			this.propSet.add(lastOpRP);
			setLastOp("NONE");*/
			
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
