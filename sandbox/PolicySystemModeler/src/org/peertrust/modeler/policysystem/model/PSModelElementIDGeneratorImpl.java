/**
 * 
 */
package org.peertrust.modeler.policysystem.model;


import java.util.Hashtable;

import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelElementIDGenerator;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;

/**
 * @author Patrice Congo
 *
 */
public class PSModelElementIDGeneratorImpl implements PSModelElementIDGenerator {

	private Hashtable prefixes;
	/**
	 * 
	 */
	public PSModelElementIDGeneratorImpl() 
	{
		super();
		makePrefixes();
	}
	
	private void makePrefixes()
	{
		prefixes= new Hashtable();
		prefixes.put(
				PSResource.class,
				PolicySystemRDFModel.NS_KB_DATA+"PSResource/");
		prefixes.put(
				PSFilter.class,
				PolicySystemRDFModel.NS_KB_DATA+"PSFilter/");
		prefixes.put(
				PSPolicy.class,
				PolicySystemRDFModel.NS_KB_DATA+"PSPolicy/");
		prefixes.put(
				PSOverridingRule.class,
				PolicySystemRDFModel.NS_KB_DATA+"PSOverridingRule/");
		prefixes.put(
				PSFilter.class,
				PolicySystemRDFModel.NS_KB_DATA+"PSFilter/");
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelElementIDGenerator#generateID(java.lang.Class, java.lang.String)
	 */
	public String generateID(Class type, String label) 
	{
		return generateTimeBasedID(type,label);
	}
	
	/**
	 * Generates an id build withthe current time in milli
	 * @param type -- the type of the model element
	 * @param label -- the label of the model element
	 * @return
	 */
	private String generateTimeBasedID(Class type,String label)
	{
		if(type==null || label== null)
		{
			return null;
		}
		
		String prefix=(String)prefixes.get(type);
	
		if(prefix==null)
		{
			prefix=type.getName()+"/";
		}
		
		String id=prefix+System.currentTimeMillis()+"/"+label;
		return id;
	}

}
