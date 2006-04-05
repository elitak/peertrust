/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;


import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

class PSPolicyImpl implements PSPolicy
{
	private Resource resource;
	private static final Logger logger=Logger.getLogger(PSPolicyImpl.class);
	private PSModelObject guarded;
	private String role;
	
	private PSPolicyImpl(Resource resource)
	{
//		this.resource=resources;
//		logger=Logger.getLogger(PSPolicyImpl.class);
		this(resource,null);
	}
	
	public PSPolicyImpl(Resource resources, PSModelObject guarded)
	{
		this.resource=resources;
		this.guarded=guarded;
		
	}
	
//	public String getHasName() 
//	{
//		if(resource==null)
//		{
//			logger.warn("resource is null return null as label");
//			return null;
//		}
//		return PolicySystemRDFModel.getStringProperty(
//						resource,
//						RDFS.label);
//	}
//
//	public void setHasName(String name) 
//	{
//		if(name==null)
//		{
//			logger.warn("param name is null skipping setting");
//			return;
//		}
//		if(resource==null)
//		{
//			logger.warn("resource is null skipping setting of name:"+name);
//		}
//		PolicySystemRDFModel.setStringProperty(
//						resource,
//						PolicySystemRDFModel.PROP_HAS_NAME,
//						name);
//		return;
//	}

	public PSModelLabel getLabel() 
	{
		if(resource==null)
		{
			logger.warn("wrapper resource is null returning null has label");
			return null;
		}
		
		String labelValue=
			PolicySystemRDFModel.getStringProperty(
												resource,
												RDFS.label);
		return new PSModelLabelImpl(this,labelValue);
	}

	public void setLabel(String label) 
	{
		if(label==null)
		{	
			logger.warn("label is null setting skipped");
			return;
		}
		if(resource==null)
		{
			logger.warn(
				"wrapped resources is null; skipping setting of label:"+label);
			return;
		}
		
		PolicySystemRDFModel.setStringProperty(
						resource,
						RDFS.label,//PolicySystemRDFModel.PROP_HAS_NAME,
						label);
		return;
	}
	
	public String getHasValue() 
	{
		return PolicySystemRDFModel.getStringProperty(
								resource,
								PolicySystemRDFModel.PROP_HAS_VALUE);
	}

	public void setHasValue(String value) 
	{
		if(value==null)
		{
			logger.warn("value is null; skipping setting");
			return;
		}
		if(resource==null)
		{
			logger.warn(
				"wrapped resources is null; skipping setting of value:"+value);
			return;
		}
		PolicySystemRDFModel.setStringProperty(
							resource,
							PolicySystemRDFModel.PROP_HAS_VALUE,
							value);
	}


	public String toString() 
	{
		return getLabel().getValue();
	}


	public Object getModelObject() 
	{
		return resource;
	}


	public PSModelObject getGuarded() 
	{
		return guarded;
	}

	public void setGuarded(PSModelObject guarded) 
	{
		this.guarded=guarded;		
	}

	public boolean equals(Object obj) 
	{
		if(obj==null)
		{
			return false;
		}
		else if(obj instanceof Resource)
		{
			return resource.equals(obj);
		}
		else if(obj instanceof PSPolicy)
		{
			return resource.equals(((PSPolicy)obj).getModelObject());
		}
		else
		{
			return super.equals(obj);
		}
	}

	public String getRole() 
	{
		return role;
	}	
	
	public void setRole(String role)
	{
		this.role=role;
	}
	
}