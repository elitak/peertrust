package org.peertrust.modeler.policysystem.model;


import java.util.Collection;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;


/**
 * A Default implementation of the PSOverridingRule which is part of
 * a {@link org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem}. 
 * 
 * @author Patrice Congo
 *
 */
public class PSOverriddingRuleImpl implements PSOverridingRule 
{
	/**
	 * The overridding rule resource
	 */
	private Resource overriddingRule;
	
	/**
	 * A resource where this overriding rule is applied
	 */
	private PSResource overriddingPlace; 
	
	/**
	 * The overridden poliy
	 */
	private PSPolicy overridden;
	
	
	/**
	 * The overridding policy
	 */
	private PSPolicy overridder;
	
	/**
	 * The logger for the PSOverriddingRuleImpl class
	 */
	private static final Logger logger= 
		Logger.getLogger(PSOverriddingRuleImpl.class);
	
	/**
	 * The policy system
	 */
	private PSPolicySystem psModel;
	
	/**
	 * Create an overridding rule
	 * @param psModel -- the policy system containing the rule
	 * @param overridenRule -- the original model object representing the rule
	 * @param psResource -- a resouce where the rule is applied
	 */
	public PSOverriddingRuleImpl(
						PSPolicySystem psModel,
						Resource overridenRule,
						PSResource psResource)
	{
		this.overriddingRule=overridenRule;
		this.overriddingPlace=psResource;
		this.psModel=psModel;
		initPolicies();
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule#getHasOverridden()
	 */
	public PSPolicy getHasOverridden() 
	{
		if(overridden==null)
		{
			initPolicies();
		}
		return overridden;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule#setHasOverriden(org.peertrust.modeler.policysystem.model.abtract.PSPolicy)
	 */
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
		psModel.alterModelObjectProperty(
				this,
				Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDDEN,
				psPolicy);
//		PolicySystemRDFModel.getInstance().setResourceProperty(
//								overriddingRule,
//								PolicySystemRDFModel.PROP_HAS_OVERRIDDEN,
//								policy);
		this.overridden=psPolicy;
	}

	public PSPolicy getHasOverridder() 
	{
		if(overridder==null)
		{
			initPolicies();
		}
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
//		PolicySystemRDFModel.getInstance().setResourceProperty(
//								overriddingRule,
//								PolicySystemRDFModel.PROP_HAS_OVERRIDDER,
//								policy);
		psModel.alterModelObjectProperty(
					this,
					Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDDER,
					psPolicy);
		this.overridder=psPolicy;
	}

	public void performOverridding(Collection psPolicies)
	{
		if(psPolicies.contains(overridden))
		{
			psPolicies.remove(overridden);
			psPolicies.add(overridder);
		}
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

	public PSModelLabel getLabel() {
		String labelValue=
			PolicySystemRDFModel.getStringProperty(
						overriddingRule,
						RDFS.label);
		return new PSModelLabelImpl(this,labelValue);
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
		if(overridden!=null)
		{
			overridden.setRole(PSModelObject.ROLE_ORULE_OVERRIDDEN);
		}
		overridder=
			(PSPolicy)PolicySystemRDFModel.getResourceProperty(
				overriddingRule,
				PolicySystemRDFModel.PROP_HAS_OVERRIDDER);
		if(overridder!=null)
		{
			overridder.setRole(PSModelObject.ROLE_ORULE_OVERRIDDER);
		}
		
	}

	public String toString() 
	{
		return getLabel().getValue();
	}

	public String getRole() 
	{
		return null;
	}

	public void setRole(String role) 
	{
		
	}
	
	
}
