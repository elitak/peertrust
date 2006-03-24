package org.peertrust.modeler.policysystem.model;


import java.util.Collection;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSOverrindingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;


public class PSOverriddingRuleImpl implements PSOverrindingRule {
	private Resource overriddingRule;
	private PSResource overriddingPlace; 
	private PSPolicy overridden;
	private PSPolicy overridder;
	private Logger logger;
	
	public PSOverriddingRuleImpl(
						Resource overridenRule,
						PSResource psResource)
	{
		this.overriddingRule=overridenRule;
		overriddingPlace=psResource;
		initPolicies();
	}
	
	public PSPolicy getHasOverridden() 
	{
		return overridden;
	}

	public void setHasOverriden(PSPolicy psPolicy) 
	{
		if(overriddingRule==null)
		{
			logger.warn("Wrapped overriding rule is null; skipping set");
		}
		if(psPolicy==null)
		{
			logger.warn("Param psPolicy must not be null");
			return;
		}
		Resource policy=
			(Resource)psPolicy.getModelObject();
		if(policy==null)
		{
			logger.warn("PSPolicy wrapped policy is null; skipping set");
			return;
		}
		PolicySystemRDFModel.setResourceProperty(
								overriddingRule,
								PolicySystemRDFModel.PROP_HAS_OVERRIDDEN,
								policy);
		this.overridden=psPolicy;
	}

	public PSPolicy getHasOverridder() {
		return overridder;
	}

	public void setHasOverrider(PSPolicy psPolicy) 
	{
		if(overriddingRule==null)
		{
			logger.warn("Wrapped overriding rule is null; skipping set");
		}
		if(psPolicy==null)
		{
			logger.warn("Param psPolicy must not be null");
			return;
		}
		Resource policy=
			(Resource)psPolicy.getModelObject();
		if(policy==null)
		{
			logger.warn("PSPolicy wrapped policy is null; skipping set");
			return;
		}
		PolicySystemRDFModel.setResourceProperty(
								overriddingRule,
								PolicySystemRDFModel.PROP_HAS_OVERRIDDER,
								policy);
		this.overridder=psPolicy;
	}

	public void performOverridding(Collection psPolicies)
	{
		psPolicies.remove(overridden);
		psPolicies.add(overridder);
	}
//	public boolean getIsInheritable() 
//	{
//		return false;
//	}
//
//	public void setIsInheritable(boolean isInheritable) 
//	{
//		
//	}

	public Object getModelObject() 
	{
		return overriddingRule;
	}

	public String getLabel() {
		return PolicySystemRDFModel.getStringProperty(
						overriddingRule,
						RDFS.label);
	}

	public void setLabel(String label) {
		if(label==null)
		{
			logger.warn("Param label mus not be null");
			return;
		}
		PolicySystemRDFModel.setStringProperty(
										overriddingRule,
										RDFS.label,
										label);
	}

	private void initPolicies()
	{
		if(overriddingRule==null)
		{
			logger.warn("wrapped overridding objec is null; skipp policies init");
			return;
		}
		
		overridden=
			(PSPolicy)PolicySystemRDFModel.getResourceProperty(
				overriddingRule,
				PolicySystemRDFModel.PROP_HAS_OVERRIDDEN);
		overridder=
			(PSPolicy)PolicySystemRDFModel.getResourceProperty(
				overriddingRule,
				PolicySystemRDFModel.PROP_HAS_OVERRIDDER);
		
	}
}
