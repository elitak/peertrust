/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelStatement;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;


import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * The default implementation for the PSResource interface.
 * It wrapps a Jana resource that represents a policy system resource
 *  
 * @author Patrice Congo
 */
class PSResourceImpl implements PSResource
{
	
	/**
	 * Model resource representing the PSResource
	 */
	private Resource resource;
	
	/**
	 * Logger for the PSRsourceImpl class
	 */
	private static final Logger logger= 
				Logger.getLogger(PSResourceImpl.class);
	
	/**
	 * The role play by this resource
	 */
	private String role;
	
	private PSPolicySystem psModel;//PolicySystemRDFModel psModel;
	
	public PSResourceImpl(Resource resource, PSPolicySystem psModel)
	{
		this.resource=resource;
		this.psModel=psModel;//PolicySystemRDFModel.getInstance();
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

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#getHasMapping()
	 */
	public String getHasMapping() 
	{
		//TODO check hasMapping
//		return PolicySystemRDFModel.getStringProperty(
//					resource,PolicySystemRDFModel.PROP_HAS_MAPPING);
		return PolicySystemRDFModel.getStringProperty(
				resource,PolicySystemRDFModel.PROP_HAS_IDENTITY);
	}

	public void setHasMapping(String mapping) 
	{
		if(mapping==null)
		{
			return;
		}
		//TODO check has mapping
//		PolicySystemRDFModel.setStringProperty(
//				resource,PolicySystemRDFModel.PROP_HAS_MAPPING,mapping);
		psModel.alterModelObjectProperty(
				this,Vocabulary.PS_MODEL_PROP_NAME_HAS_IDENTITY,mapping);
//		PolicySystemRDFModel.setStringProperty(
//				resource,PolicySystemRDFModel.PROP_HAS_IDENTITY,mapping);
	}

	public Vector getIsOverrindingRule() 
	{
		return psModel.getModelObjectProperties(
				this,Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE);
		
//		return psModel.getMultipleProperty(
//						this,//resource,
//						PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES);
	}

	public void addIsOverrindingRule(PSOverridingRule rule) 
	{
//		resource.addProperty(
//					PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES,
//					(Resource)rule.getModelObject());
		PSModelStatement stm= 
			new PSModelStatementImpl(
					this,
					Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE,
					rule);
		psModel.addStatement(stm);
//		psModel.addMultipleProperty(
//					resource,
//					PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES,
//					(Resource)rule.getModelObject());
	}

	public PSResource getParent() 
	{
		Vector supers=
			psModel.getModelObjectProperties(
						this,Vocabulary.PS_MODEL_PROP_NAME_HAS_SUPER);
		if(supers==null)
		{
			return null;
		}
		else if(supers.size()==1)
		{
			return (PSResource)supers.elementAt(0);
		}
		else
		{
			logger.error("Illegal super count:"+supers);
			return null;
		}
//		return
//			psModel.getMultipleProperty(
//							this,//resource,
//							PolicySystemRDFModel.PROP_HAS_SUPER);
	}

	public void setParent(PSModelObject parent) 
	{
		PSModelStatement stm=
			new PSModelStatementImpl(this,Vocabulary.PS_MODEL_PROP_NAME_HAS_SUPER,parent);
		psModel.addStatement(stm);
//		psModel.addMultipleProperty(
//				resource,
//				PolicySystemRDFModel.PROP_HAS_SUPER,
//				(Resource)parent.getModelObject());
	}

	public void addIsProtectedBy(PSPolicy policy) 
	{
		PSModelStatement stm= 
			new PSModelStatementImpl(
					this, Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY,policy);
		psModel.addStatement(stm);
//		psModel.addMultipleProperty(
//				resource,
//				PolicySystemRDFModel.PROP_IS_PROTECTED_BY,
//				(Resource)policy.getModelObject());
		
	}

	public List getIsProtectedBy() 
	{
		return psModel.getModelObjectProperties(
					this,Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY);
//		return 
//			psModel.getMultipleProperty(
//						this,//resource,
//						PolicySystemRDFModel.PROP_IS_PROTECTED_BY);
	}

	public List getHasFilter()
	{
		return psModel.getModelObjectProperties(
					this,Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER);
//		return psModel.getMultipleProperty(
//					this,//resource,
//					PolicySystemRDFModel.PROP_HAS_FILTER);
	}
	
	public Object getModelObject() 
	{
		return resource;
	}
	
	public String toString() 
	{
		return " "+getHasMapping(); 
		//return "   "+getLabel()+"  ";
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
		PSModelStatement stm= 
			new PSModelStatementImpl(
					this,
					Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER,
					filter);
		psModel.addStatement(stm);
		
//		resource.addProperty(
//				PolicySystemRDFModel.PROP_HAS_FILTER,
//				(Resource)filter.getModelObject());
		
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

	public List getChildren() 
	{
		return psModel.getDirectChildren(this);
		
	}

	public boolean canHaveChild() {
		return PolicySystemRDFModel.getBooleanProperty(
					resource,
					PolicySystemRDFModel.PROP_CAN_HAVE_CHILD).booleanValue();
	}

	public void setCanHaveChild(boolean canHaveChild) {
		PolicySystemRDFModel.setBooleanProperty(
						resource,
						PolicySystemRDFModel.PROP_CAN_HAVE_CHILD,
						canHaveChild);
		
	}
}