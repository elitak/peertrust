/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.List;
import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelStatement;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
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
	
	/**
	 * the model that contains the wrapped resource
	 */
	private PSPolicySystem psModel;//PolicySystemRDFModel psModel;
	
	/**
	 * Create a PSResource that wrapps the given resource from the
	 * given model
	 * @param resource -- the original resource 
	 * @param psModel -- the model containing the resouce
	 */
	public PSResourceImpl(Resource resource, PSPolicySystem psModel)
	{
		this.resource=resource;
		this.psModel=psModel;//PolicySystemRDFModel.getInstance();
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#getLabel()
	 */
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

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#setLabel(java.lang.String)
	 */
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

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#setHasMapping(java.lang.String)
	 */
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

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#getIsOverrindingRule()
	 */
	public List getIsOverrindingRule() 
	{
		return psModel.getModelObjectProperties(
				this,Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE);
		
//		return psModel.getMultipleProperty(
//						this,//resource,
//						PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES);
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#addIsOverrindingRule(org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule)
	 */
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

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#getParent()
	 */
	public PSResource getParent() 
	{
		List supers=
			psModel.getModelObjectProperties(
						this,Vocabulary.PS_MODEL_PROP_NAME_HAS_SUPER);
		if(supers==null)
		{
			return null;
		}
		else if(supers.size()==1)
		{
			return (PSResource)supers.get(0);
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

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#setParent(org.peertrust.modeler.policysystem.model.abtract.PSModelObject)
	 */
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

//	public void addIsProtectedBy(PSPolicy policy) 
//	{
//		PSModelStatement stm= 
//			new PSModelStatementImpl(
//					this, Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY,policy);
//		psModel.addStatement(stm);
////		psModel.addMultipleProperty(
////				resource,
////				PolicySystemRDFModel.PROP_IS_PROTECTED_BY,
////				(Resource)policy.getModelObject());
//		
//	}

//	public List getIsProtectedBy() 
//	{
//		return getHasFilter();
////		return psModel.getModelObjectProperties(
////					this,Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY);
//	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#getHasFilter()
	 */
	public List getHasFilter()
	{
		return psModel.getModelObjectProperties(
					this,Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER);
//		return psModel.getMultipleProperty(
//					this,//resource,
//					PolicySystemRDFModel.PROP_HAS_FILTER);
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#getModelObject()
	 */
	public Object getModelObject() 
	{
		return resource;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return " "+getHasMapping(); 
		//return "   "+getLabel()+"  ";
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#getRole()
	 */
	public String getRole() 
	{
		return role;
	}

	/*
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#setRole(java.lang.String)
	 */
	public void setRole(String role) 
	{
		this.role=role;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#addHasFilter(org.peertrust.modeler.policysystem.model.abtract.PSFilter)
	 */
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

	
//	public void removePolicy(PSPolicy policyToDel) 
//	{
//		PSModelStatement stm= 
//			new PSModelStatementImpl(
//						this,
//						Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY,
//						policyToDel); 
//		psModel.removeStatement(stm);
//	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#removeFilter(org.peertrust.modeler.policysystem.model.abtract.PSFilter)
	 */
	public void removeFilter(PSFilter filterToRemove) 
	{
		PSModelStatement stm= 
			new PSModelStatementImpl(
						this,
						Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER,
						filterToRemove); 
		psModel.removeStatement(stm);
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#removeOverriddingRule(org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule)
	 */
	public void removeOverriddingRule(PSOverridingRule ruleToDel) 
	{
		PSModelStatement stm= 
			new PSModelStatementImpl(
						this,
						Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE,
						ruleToDel); 
		psModel.removeStatement(stm);
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#getChildren()
	 */
	public List getChildren() 
	{
		return psModel.getDirectChildren(this);
		
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#canHaveChild()
	 */
	public boolean canHaveChild() {
		return PolicySystemRDFModel.getBooleanProperty(
					resource,
					PolicySystemRDFModel.PROP_CAN_HAVE_CHILD).booleanValue();
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSResource#setCanHaveChild(boolean)
	 */
	public void setCanHaveChild(boolean canHaveChild) {
		PolicySystemRDFModel.setBooleanProperty(
						resource,
						PolicySystemRDFModel.PROP_CAN_HAVE_CHILD,
						canHaveChild);
		
	}
}