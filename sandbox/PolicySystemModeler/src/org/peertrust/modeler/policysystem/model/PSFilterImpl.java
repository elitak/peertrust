/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;


import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author congo
 *
 */
public class PSFilterImpl implements PSFilter 
{
	private Resource filter;
	private static Logger logger;
	
	
	public PSFilterImpl(Resource filter) {
		super();
		this.filter = filter;
		logger=Logger.getLogger(PSFilterImpl.class);
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.PSFilter#getHasCondition()
	 */
	public Vector getHasCondition() 
	{
		if(filter==null)
		{
			logger.warn("filter is null returning null condition");
			return null;
		}
		
		return PolicySystemRDFModel.getInstance().getMultipleProperty(
										this,
										PolicySystemRDFModel.PROP_HAS_CONDITION);
	}
	
	public void addHasCondition(String condition) 
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
		PolicySystemRDFModel.getInstance().addMultipleStringProperty(
				filter,
				PolicySystemRDFModel.PROP_HAS_CONDITION,
				condition);
		
	}

	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.PSFilter#getIsprotectedBy()
	 */
	public Vector getIsprotectedBy() 
	{
		if(filter==null)
		{
			logger.warn("filter is null returning empty vector");
			return new Vector();
		}
		
		return 
			PolicySystemRDFModel.getInstance().getMultipleProperty(
						this,//filter,
						PolicySystemRDFModel.PROP_IS_PROTECTED_BY);
	}

	public void addIsProtectedBy(PSPolicy policy) 
	{
		if(filter==null)
		{
			logger.warn("Filter is null cannot add policy:"+policy);
			return;
		}
		PolicySystemRDFModel.getInstance().addMultipleProperty(
				filter,
				PolicySystemRDFModel.PROP_IS_PROTECTED_BY,
				(Resource)policy.getModelObject());
		
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
		String labelValue= 
			PolicySystemRDFModel.getStringProperty(
											filter,
											RDFS.label);
		return new PSModelLabelImpl(this,labelValue);
	}
	
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
		
		PolicySystemRDFModel.setStringProperty(filter,RDFS.label,label);
	}
	
	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.ModelObjectWrapper#getModelObject()
	 */
	public Object getModelObject() {
		return filter;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSFilter#containsCondition(java.lang.String)
	 */
	public boolean containsCondition(String condition) {
		if(condition==null)
		{
			logger.warn("condition is null");
			return false;
		}
		return PolicySystemRDFModel.getInstance().getRdfModel().contains(
										filter,
										PolicySystemRDFModel.PROP_HAS_CONDITION,
										condition);
	}

	public String toString()
	{
		return "filter:"+getLabel();
	}

	public void removeCondition(String condition) {
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

	public void removeAllConditions() 
	{
		Vector oldCond=
			getHasCondition();
		for(Iterator it=oldCond.iterator();it.hasNext();)
		{
			removeCondition((String)it.next());
		}
	}

	public String getRole() 
	{
		return null;
	}

	public void setRole(String role) 
	{
		
	}
}
