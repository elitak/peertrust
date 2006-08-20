package org.peertrust.modeler.policysystem.model;

import java.io.File;
import java.net.URI;

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
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker#relativeURI(java.lang.Object)
	 */
	public URI relativeURI(Object fileResource)
	{
		logger.debug("\n\tmakeIdentity:"+fileResource);
		if(fileResource==null)
		{
			return null;
		}
		else if(fileResource instanceof File)
		{
			URI fileUri=((File)fileResource).toURI();
			URI root=ProjectConfig.getInstance().getRootFor(fileUri);
			if(root==null)
			{
				throw new IllegalArgumentException(
						"Not root available for the resource: "+fileResource);
			}
			URI relUri=root.relativize(fileUri);
			if(!relUri.isAbsolute())
			{
				throw new IllegalArgumentException(
						"Could not relativize the uri to get the identity");
			}
			return relUri;
		}
		else
		{
			return null;
		}
			
	}
	
	public boolean isRoot(Object resource)
	{
		logger.debug("\n\tisRoot?:"+resource);
		if(resource==null)
		{
			throw new IllegalArgumentException(
					"Argument resource must not be null");
		}
		else if(resource instanceof File)
		{
			return ProjectConfig.getInstance().isRoot(((File)resource).toURI());
		}
		else
		{
			throw new IllegalArgumentException(
					"Can only handle file instance: current class="+resource.getClass());
		}
		
	}

	public String getLabel(Object resource) throws IllegalArgumentException 
	{
		logger.debug("\n\tmake label:"+resource);
		if(resource==null)
		{
			throw new IllegalArgumentException(
					"argument resource must not be null");
		}
		else if(resource instanceof File)
		{
			return ((File)resource).getName();
		}
		else
		{
			throw new IllegalArgumentException(
					"Can only comput label for File instances"+
					"\n\tactual resource class="+resource.getClass());
		}
	}

	public boolean canHaveChild(Object resource) {
		logger.debug("\n\tcanHaveChild:"+resource);
		if(resource==null)
		{
			throw new IllegalArgumentException(
					"Argument resource must not be null");
		}
		else if(resource instanceof File)
		{
			return ((File)resource).isDirectory();
		}
		else
		{
			throw new IllegalArgumentException(
					"Can only handle file instances: actual class="+resource.getClass());
		}
	}

	

	public URI getAbsoluteURI(Object resource) {
		if(resource==null)
		{
			throw new IllegalArgumentException(
					"resource must not be null");
		}
		else if(resource instanceof File)
		{
			return ((File)resource).toURI();
		}
		else
		{
			throw new IllegalArgumentException(
					"Can only handle File instances: actual class="+resource.getClass());
		}
	}

	
	public Object getParent(Object resource) {
		if(resource==null)
		{
			throw new IllegalArgumentException(
					"Arguement resource must not be null");
		}
		else if(resource instanceof File)
		{
			return ((File)resource).getParentFile();
		}
		else
		{
			throw new IllegalArgumentException(
					"Can only handle File instances: actual class="+resource.getClass());
		}
	}
}
