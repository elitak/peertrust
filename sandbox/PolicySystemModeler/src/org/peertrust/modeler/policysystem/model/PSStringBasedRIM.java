/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker;

public class PSStringBasedRIM implements PSResourceIdentityMaker 
{
	public String makeIdentity(Object resource) 
	{
		return (String)resource;
	}

	public String makeLabel(Object resource) 
	{
		return (String)resource;
	}
}