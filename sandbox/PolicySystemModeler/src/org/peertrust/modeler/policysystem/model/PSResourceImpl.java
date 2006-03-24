/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.ModelObjectWrapper;
import org.peertrust.modeler.policysystem.model.abtract.PSOverrindingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;


import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

class PSResourceImpl implements PSResource
{
	Resource resource;
	Logger logger;
	
	public PSResourceImpl(Resource resource)
	{
		this.resource=resource;
		logger= Logger.getLogger(PSResourceImpl.class);
	}
	
	public String getLabel() 
	{
		if(resource==null)
		{
			logger.warn(
				"wrapped resources is null; returning null as label");
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

	public Vector getIsOverrindingRule() {
		return PolicySystemRDFModel.getInstance().getMultipleProperty(
							this,//resource,
							PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES);
	}

	public void addIsOverrindingRule(PSOverrindingRule rule) {
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

	public void addHasSuper(ModelObjectWrapper parent) {
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
	public Object getModelObject() {
		return resource;
	}
	
	public String toString() 
	{
		return "   "+getLabel()+"  ";
	}
}