/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelStatement;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;


import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

class PSResourceImpl implements PSResource
{
	
	private Resource resource;
	
	private static final Logger logger= 
				Logger.getLogger(PSResourceImpl.class);
	
	private String role;
	
	PolicySystemRDFModel psModel;
	
	public PSResourceImpl(Resource resource)
	{
		this.resource=resource;
		this.psModel=PolicySystemRDFModel.getInstance();
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
		return psModel.getMultipleProperty(
						this,//resource,
						PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES);
	}

	public void addIsOverrindingRule(PSOverridingRule rule) 
	{
		resource.addProperty(
					PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES,
					(Resource)rule.getModelObject());
		psModel.addMultipleProperty(
					resource,
					PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES,
					(Resource)rule.getModelObject());
	}

	public Vector getHasSuper() {
		return
			psModel.getMultipleProperty(
							this,//resource,
							PolicySystemRDFModel.PROP_HAS_SUPER);
	}

	public void addHasSuper(PSModelObject parent) 
	{
		psModel.addMultipleProperty(
				resource,
				PolicySystemRDFModel.PROP_HAS_SUPER,
				(Resource)parent.getModelObject());
	}

	public void addIsProtectedBy(PSPolicy policy) 
	{
		psModel.addMultipleProperty(
				resource,
				PolicySystemRDFModel.PROP_IS_PROTECTED_BY,
				(Resource)policy.getModelObject());
		
	}

	public Vector getIsProtectedBy() {
		return 
			psModel.getMultipleProperty(
						this,//resource,
						PolicySystemRDFModel.PROP_IS_PROTECTED_BY);
	}

	public Vector getHasFilter()
	{
		return psModel.getMultipleProperty(
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

	public void removePolicy(PSPolicy policyToDel) 
	{
		PSModelStatement stm= 
			new PSModelStatementImpl(
						this,
						Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY,
						policyToDel); 
		psModel.removeStatement(stm);
	}

	public void removeFilter(PSFilter filterToRemove) 
	{
		PSModelStatement stm= 
			new PSModelStatementImpl(
						this,
						Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER,
						filterToRemove); 
		psModel.removeStatement(stm);
	}

	public void removeOverriddingRule(PSOverridingRule ruleToDel) 
	{
		PSModelStatement stm= 
			new PSModelStatementImpl(
						this,
						Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE,
						ruleToDel); 
		psModel.removeStatement(stm);
	}
}