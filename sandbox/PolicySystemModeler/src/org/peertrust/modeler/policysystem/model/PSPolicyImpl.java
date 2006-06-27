/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.List;
import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem;


import com.hp.hpl.jena.rdf.model.Resource;

/**
 * An implementation of PSPolicy
 * @author Patrice Congo
 *
 */
class PSPolicyImpl implements PSPolicy
{
	
	/**
	 * The jena rdf resource representing a policy
	 */
	private Resource resource;
	
	/**
	 * The logger for the PSPolicyImpl class
	 */
	private static final Logger logger=Logger.getLogger(PSPolicyImpl.class);
	
	/**
	 *Holds annobject which is being protected by this policy 
	 */
	private PSModelObject guarded;
	
	/**
	 * The role played by this policy
	 */
	private String role;
	
	/**
	 * The policy System model
	 */
	private PSPolicySystem psModel;//=PolicySystemRDFModel.getInstance();
	
//	private PSPolicyImpl(Resource resource)
//	{
////		this.resource=resources;
////		logger=Logger.getLogger(PSPolicyImpl.class);
//		this(resource,null);
//	}
	
	/**
	 * Creates a PSPolicyImpl wrapping the provided resource and 
	 * protecting the provided model object
	 * @param resource -- the policy rdf resource 
	 * @param guarded -- the guarded model object
	 */
	public PSPolicyImpl(
				PSPolicySystem psModel,
				Resource resource, 
				PSModelObject guarded)
	{
		this.resource=resource;
		this.guarded=guarded;
		this.psModel=psModel;
	}
	

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#getLabel()
	 */
	public PSModelLabel getLabel() 
	{
		if(resource==null)
		{
			logger.warn("wrapper resource is null returning null has label");
			return null;
		}
		
//		String labelValue=
//			PolicySystemRDFModel.getStringProperty(
//												resource,
//												RDFS.label);
//		return new PSModelLabelImpl(this,labelValue);
		List props=
			psModel.getModelObjectProperties(
							this,Vocabulary.PS_MODEL_PROP_NAME_HAS_NAME);
		if(props==null)
		{
			return null;
		}
		else
		{
			if(props.size()==1)
			{
				String labelValue= (String) props.get(0);
				return new PSModelLabelImpl(this,labelValue);
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#setLabel(java.lang.String)
	 */
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
		psModel.alterModelObjectProperty(
						this,
						Vocabulary.PS_MODEL_PROP_NAME_HAS_NAME,
						label);
//		PolicySystemRDFModel.setStringProperty(
//						resource,
//						RDFS.label,//PolicySystemRDFModel.PROP_HAS_NAME,
//						label);
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
		PSModelLabel label=getLabel();
		if(label!=null)
		{
			return getLabel().getValue();
		}
		else
		{
			return "polnull";
		}
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