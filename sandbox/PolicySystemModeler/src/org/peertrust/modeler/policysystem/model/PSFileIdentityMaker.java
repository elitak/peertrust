/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.io.File;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker;

/**
 * Provides the algorithm to amke an identity key from a file resource
 * 
 * @author Patrice Congo
 */
public class PSFileIdentityMaker implements PSResourceIdentityMaker 
{
	/**
	 * Logger for the PSFileIdentityMaker class
	 */
	static private final Logger logger= 
			Logger.getLogger(PSFileIdentityMaker.class);
	
	public PSFileIdentityMaker() 
	{
		super();
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker#makeIdentity(java.lang.Object)
	 */
	public String makeIdentity(Object fileResource)
	{
		if(fileResource==null)
		{
			return null;
		}
		else if(fileResource instanceof File)
		{
			return ((File)fileResource).getAbsolutePath();
		}
		else
		{
			return null;
		}
			
	}

	public String makeLabel(Object resource) 
	{
		if(resource==null)
		{
			return null;
		}
		else if(resource instanceof File)
		{
			return ((File)resource).getName();
		}
		else
		{
			return null;
		}
	}
}
