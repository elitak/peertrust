/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;


import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

class PSResourceImpl implements PSResource
{
	private Resource resource;
	private static final Logger logger= Logger.getLogger(PSResourceImpl.class);
	private String role;
	
	public PSResourceImpl(Resource resource)
	{
		this.resource=resource;
	}
	
	public PSModelLabel getLabel() 
	{
		if(resource==null)
		{
			logger.warn(
				"wrapped resources is null; returning null as label");
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
			logger.warn("param name is null skipping setting");
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
	}

	public String getHasMapping() 
	{
		return PolicySystemRDFModel.getStringProperty(
					resource,PolicySystemRDFModel.PROP_HAS_MAPPING);
	}

	public void setHasMapping(String mapping) {
		PolicySystemRDFModel.setStringProperty(
				resource,PolicySystemRDFModel.PROP_HAS_MAPPING,mapping);
	}

	public Vector getIsOverrindingRule() 
	{
		return PolicySystemRDFModel.getInstance().getMultipleProperty(
							this,//resource,
							PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES);
	}

	public void addIsOverrindingRule(PSOverridingRule rule) 
	{
		resource.addProperty(
					PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES,
					(Resource)rule.getModelObject());
		PolicySystemRDFModel.getInstance().addMultipleProperty(
					resource,
					PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES,
					(Resource)rule.getModelObject());
	}

	public Vector getHasSuper() {
		return
			PolicySystemRDFModel.getInstance().getMultipleProperty(
							this,//resource,
							PolicySystemRDFModel.PROP_HAS_SUPER);
	}

	public void addHasSuper(PSModelObject parent) {
		PolicySystemRDFModel.getInstance().addMultipleProperty(
					resource,
					PolicySystemRDFModel.PROP_HAS_SUPER,
					(Resource)parent.getModelObject());
	}

	public void addIsProtectedBy(PSPolicy policy) {
		PolicySystemRDFModel.getInstance().addMultipleProperty(
				resource,
				PolicySystemRDFModel.PROP_IS_PROTECTED_BY,
				(Resource)policy.getModelObject());
		
	}

	public Vector getIsProtectedBy() {
		return 
			PolicySystemRDFModel.getInstance().getMultipleProperty(
							this,//resource,
							PolicySystemRDFModel.PROP_IS_PROTECTED_BY);
	}

	public Vector getHasFilter()
	{
		return PolicySystemRDFModel.getInstance().getMultipleProperty(
					this,//resource,
					PolicySystemRDFModel.PROP_HAS_FILTER);
	}
	public Object getModelObject() 
	{
		return resource;
	}
	
	public String toString() 
	{
		return "   "+getLabel()+"  ";
	}

	public String getRole() 
	{
		return role;
	}

	public void setRole(String role) 
	{
		this.role=role;
	}

	public void addHasFilter(PSFilter filter) 
	{
		resource.addProperty(
				PolicySystemRDFModel.PROP_HAS_FILTER,
				(Resource)filter.getModelObject());
		
	}
}