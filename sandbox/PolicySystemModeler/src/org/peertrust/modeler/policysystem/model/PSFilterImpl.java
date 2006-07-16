/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.dnd.FileTransfer;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelStatement;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * An implementation of PSFilter
 * @author Patrice Congo
 *
 */
public class PSFilterImpl implements PSFilter 
{
	/** the jena rdf resource representing the ps filter*/
	private Resource filter;
	
	/**
	 * The logger for the PSFilterImpl class
	 */
	private final static Logger logger=
				Logger.getLogger(PSFilterImpl.class);
	
	
//	static private PolicySystemRDFModel psModel=
//						PolicySystemRDFModel.getInstance();
	
	/**
	 * The policy system model 
	 */
	private PSPolicySystem psModel;//=PolicySystemRDFModel.getInstance();
	
	/**
	 * Creates a PSFilterImpl which represents the the provided resource
	 * @param filter
	 */
	public PSFilterImpl(PSPolicySystem psModel,Resource filter) 
	{
		super();
		this.filter = filter;
		this.psModel=psModel;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.PSFilter#getCondition()
	 */
	public String getCondition() 
	{
		if(filter==null)
		{
			logger.warn("filter is null returning null condition");
			return null;
		}
		
//		return PolicySystemRDFModel.getInstance().getMultipleProperty(
//										this,
//										PolicySystemRDFModel.PROP_HAS_CONDITION);
		
		List conds=
			psModel.getModelObjectProperties(
				this,
				Vocabulary.PS_MODEL_PROP_NAME_HAS_CONDITION //PolicySystemRDFModel.PROP_HAS_CONDITION
				);
		if(conds==null)
		{
			return "*";
		}
		else if(conds.size()>0)
		{
			return (String)conds.get(0);
		}
		else
		{
			return "*";
		}
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSFilter#setHasCondition(java.lang.String)
	 */
	public void setHasCondition(String condition) 
	{
		if(condition==null)
		{
			logger.warn("param condition is null, add skipped");
			return;
		}
		if(filter==null)
		{
			logger.warn(
					"Filter is null cannot add condition:"+
					condition);
			return;
		}
		logger.info("adding condition:"+condition);
		PSModelStatement stm=
			new PSModelStatementImpl(
					this,
					Vocabulary.PS_MODEL_PROP_NAME_HAS_CONDITION,
					condition);
		psModel.addStatement(stm);
//		PolicySystemRDFModel.getInstance().addMultipleStringProperty(
//				filter,
//				PolicySystemRDFModel.PROP_HAS_CONDITION,
//				condition);
		
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.PSFilter#getIsProtectedBy()
	 */
	public PSPolicy getIsProtectedBy() 
	{
		if(filter==null)
		{
			logger.debug("filter is null returning empty vector");
			return null;//new Vector();
		}
		//a filter is now supposed to habe at most one protecting policy 
		List filterPolicies=
			psModel.getModelObjectProperties(
				this,
				Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY);
		if(filterPolicies==null)
		{
			return null;
		}
		else
		{
			int size=filterPolicies.size();
			if(size==1)
			{
				return (PSPolicy)filterPolicies.get(0);
			}
			else if(size==0)
			{
				return null;
			}
			else// if(filterPolicies.size()>1)
			{
					logger.error(
							"\nFilter  must not contain more than one protecting policy policy"+
							"\n\tsize="+size+
							"\n\tfilterPolicies="+filterPolicies);
					return (PSPolicy)filterPolicies.get(0);
			}
				
			
		}

		
//		return 
//			PolicySystemRDFModel.getInstance().getMultipleProperty(
//						this,//filter,
//						PolicySystemRDFModel.PROP_IS_PROTECTED_BY);
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSFilter#setIsProtectedBy(org.peertrust.modeler.policysystem.model.abtract.PSPolicy)
	 */
	public void setIsProtectedBy(PSPolicy policy) 
	{
		if(filter==null)
		{
			logger.warn("Filter is null cannot add policy:"+policy);
			return;
		}
//		PolicySystemRDFModel.getInstance().addMultipleProperty(
//				filter,
//				PolicySystemRDFModel.PROP_IS_PROTECTED_BY,
//				(Resource)policy.getModelObject());
		PSModelStatement stm=
			new PSModelStatementImpl(
					this,
					Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY,
					policy);
		psModel.addStatement(stm);
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.PSFilter#getLabel()
	 */
	public PSModelLabel getLabel() 
	{
		if(filter==null)
		{
			logger.warn("Filter is null cannot returned label is null");
			return null;
		}
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
//					PolicySystemRDFModel.getStringProperty(
//													filter,
//													RDFS.label);
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
	public void setLabel(String label) {
		if(label==null)
		{
			logger.warn("label is null cannot set!");
			return;
		}
		if(filter==null)
		{
			logger.warn("wrapped filter is null cannot set label: "+label);
			return;
		}
		
//		PSModelStatement stm=
//			new PSModelStatementImpl(
//					this,
//					Vocabulary.PS_MODEL_PROP_NAME_HAS_NAME,
//					label);
//		//TODO use psModel
//		PolicySystemRDFModel.setStringProperty(filter,RDFS.label,label);
		psModel.alterModelObjectProperty(this,Vocabulary.PS_MODEL_PROP_NAME_HAS_NAME,label);
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.ModelObjectWrapper#getModelObject()
	 */
	public Object getModelObject() {
		return filter;
	}
	
	
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSFilter#containsCondition(java.lang.String)
	 */
	public boolean containsCondition(String condition) 
	{
		if(condition==null)
		{
			logger.warn("condition is null");
			return false;
		}
		
		String conds=getCondition();
		if(conds==null)
		{
			return false;
		}
		else
		{
			return conds.equals(condition);
		}
//		return PolicySystemRDFModel.getInstance().getRdfModel().contains(
//										filter,
//										PolicySystemRDFModel.PROP_HAS_CONDITION,
//										condition);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		PSModelLabel label=getLabel();
		if(label==null)
		{
			return "ErroFilterLabelNull";
		}
		else
		{
			String value=label.getValue();
			if(value==null)
			{
				return "ErrorlabelValueNull";
			}
			else
			{
				return value;
			}
		}
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSFilter#removeCondition(java.lang.String)
	 */
	public void removeCondition(String condition) 
	{
		if(condition==null)
		{
			return;
		}
		
		PolicySystemRDFModel psModel=
			PolicySystemRDFModel.getInstance();
		
		psModel.removeStringProperty(
				filter,
				PolicySystemRDFModel.PROP_HAS_CONDITION,
				condition);
		return ;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSFilter#removeAllConditions()
	 */
	public void removeAllConditions() 
	{
		String oldCond=
			getCondition();
		removeCondition(oldCond);		
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#getRole()
	 */
	public String getRole() 
	{
		return null;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelObject#setRole(java.lang.String)
	 */
	public void setRole(String role) 
	{
		
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSFilter#removeIsProtectedBy(org.peertrust.modeler.policysystem.model.abtract.PSPolicy)
	 */
	public void removeIsProtectedBy(PSPolicy policy) {
		if(policy==null)
		{
			return;
		}
		
		PSModelStatement psStm= new PSModelStatementImpl(
							this,
							Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY,
							policy);
		psModel.removeStatement(psStm);
		
	}
}
