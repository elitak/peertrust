/**
 * 
 */
package policysystem.model;

import org.apache.log4j.Logger;

import policysystem.model.abtract.ModelObjectWrapper;
import policysystem.model.abtract.PSPolicy;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

class PSPolicyImpl implements PSPolicy
{
	private Resource resource;
	Logger logger;
	ModelObjectWrapper guarded;
	
	private PSPolicyImpl(Resource resource)
	{
//		this.resource=resources;
//		logger=Logger.getLogger(PSPolicyImpl.class);
		this(resource,null);
	}
	
	public PSPolicyImpl(Resource resources, ModelObjectWrapper guarded)
	{
		this.resource=resources;
		logger=Logger.getLogger(PSPolicyImpl.class);
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

	public String getLabel() 
	{
		if(resource==null)
		{
			logger.warn("wrapper resource is null returning null has label");
			return null;
		}
		
		return PolicySystemRDFModel.getStringProperty(
						resource,
						RDFS.label);
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
						PolicySystemRDFModel.PROP_HAS_NAME,
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
		return getLabel();
	}


	public Object getModelObject() 
	{
		return resource;
	}


	public ModelObjectWrapper getGuarded() 
	{
		return guarded;
	}

	public void setGuarded(ModelObjectWrapper guarded) 
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
	
	
}