package g4mfs.impl;

import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.ResourceHomeImpl;
import org.globus.wsrf.impl.SimpleResourceKey;


/**
 * 
 * @author ionut constandache ionut_con@yahoo.com
 * the MathResourceHome implements the creation of a key to identify each Math Resource 
 */


public class MathResourceHome extends ResourceHomeImpl 
{
	public ResourceKey create() throws Exception 
	{
		MathResource resource = (MathResource) createNewInstance();
		resource.create();
		ResourceKey key = new SimpleResourceKey(keyTypeName, resource.getID());
		add(key, resource);
		return key;
	}
}
