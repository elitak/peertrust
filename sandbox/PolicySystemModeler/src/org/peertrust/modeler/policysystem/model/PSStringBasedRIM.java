/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker;

public class PSStringBasedRIM implements PSResourceIdentityMaker 
{
	/**
	 * Logger for the <code>PSStringBasedRIM</code>
	 */
	final static private Logger logger= 
					Logger.getLogger(PSStringBasedRIM.class);
	
	public URI makeIdentity(Object resource) 
	{
		logger.debug("\n\tmakeIdentity:"+resource);
		if(resource instanceof String)
		{
			try {
				return new URI((String)resource);
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(
						"String is not an uri:"+resource,e);
			}
		}
		else if(resource==null)
		{
			throw new IllegalArgumentException(
					"Parameter resource must not be null");
		}
		else
		{
			throw new IllegalArgumentException(
					"Can only handle string resource: class="+resource.getClass());
		}
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker#makeLabel(java.lang.Object)
	 */
	public String makeLabel(Object resource) 
	{
		logger.debug("\n\tmakeLabel:"+resource);
		if(resource==null)
		{
			throw new IllegalArgumentException(
							"resource must not be null");
		}
		else if(resource instanceof String)
		{
			return new File((String)resource).getName();
		}
		else
		{
			throw new IllegalArgumentException(
						"Can only a handle String: class= "+resource.getClass());
		}
	}

	public boolean canHaveChild(Object resource) 
	{
		logger.debug("\n\tcanHaveChilden:"+resource);
		throw new RuntimeException("Cannot get this property from string value");
		//return false;
	}

	public boolean isRoot(Object resource) 
	{
		logger.debug("\n\tisRoot:"+resource);
		if(resource==null)
		{
			throw new IllegalArgumentException(
					"Argument resource must not be null");
		}
		else if(resource instanceof String)
		{
			URI uri=null;
			try {
				uri=new URI((String)resource);
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(
						"Bad uri syntax:"+resource,
						e);
			}
			return ProjectConfig.getInstance().isRoot(uri);
		}
		else
		{
			throw new IllegalArgumentException(
					"Can only handle strig instances: actuak class="+resource.getClass());
		}
		
	}

	public URI toURI(Object resource) {
		logger.debug("\n\ttoURI:"+resource);
		if(resource==null)
		{
			throw new IllegalArgumentException(
							"resource must not be null");
		}
		else if(resource instanceof String)
		{
			try {
				return new URI((String)resource);
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(
						"bad url syntax="+resource);
			}
		}
		else
		{
			throw new IllegalArgumentException(
						"Can only a handle String: class= "+resource.getClass());
		}
	}

	public Object getParent(Object resource) {
		if(resource==null)
		{
			throw new IllegalArgumentException(
					"Argument resource must not be null");
		}
		else if(resource instanceof String)
		{
			try
			{
				File file= new File((String)resource);
				return file.getParent();
			}
			catch (Throwable th)
			{
				throw new IllegalArgumentException(
						"\nCould not construct resource parent throw file.getParent()"+
						"\n\tresource="+resource);
			}
		}
		else
		{
			throw new IllegalArgumentException(
						"Can only a handle String: class= "+resource.getClass());
		}
		
	}
	
}