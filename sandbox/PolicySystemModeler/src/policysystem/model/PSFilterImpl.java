/**
 * 
 */
package policysystem.model;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author congo
 *
 */
public class PSFilterImpl implements PSFilter 
{
	Resource filter;
	Logger logger;
	
	
	public PSFilterImpl(Resource filter) {
		super();
		this.filter = filter;
		logger=Logger.getLogger(PSFilterImpl.class);
	}

	/* (non-Javadoc)
	 * @see policysystem.model.PSFilter#getHasCondition()
	 */
	public Vector getHasCondition() 
	{
		if(filter==null)
		{
			logger.warn("filter is null returning null condition");
			return null;
		}
		
		return PolicySystemRDFModel.getMultipleProperty(
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
			logger.warn("Filter is null cannot add condition:"+condition);
			return;
		}
		PolicySystemRDFModel.addMultipleStringProperty(
				filter,
				PolicySystemRDFModel.PROP_HAS_CONDITION,
				condition);
		
	}

	/* (non-Javadoc)
	 * @see policysystem.model.PSFilter#getIsprotectedBy()
	 */
	public Vector getIsprotectedBy() 
	{
		if(filter==null)
		{
			logger.warn("filter is null returning empty vector");
			return new Vector();
		}
		
		return 
			PolicySystemRDFModel.getMultipleProperty(
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
		PolicySystemRDFModel.addMultipleProperty(
				filter,
				PolicySystemRDFModel.PROP_IS_PROTECTED_BY,
				(Resource)policy.getModelObject());
		
	}
	
	/* (non-Javadoc)
	 * @see policysystem.model.PSFilter#getLabel()
	 */
	public String getLabel() 
	{
		if(filter==null)
		{
			logger.warn("Filter is null cannot returned label is null");
			return null;
		}
		return PolicySystemRDFModel.getStringProperty(
				filter,
				RDFS.label);
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
	 * @see policysystem.model.ModelObjectWrapper#getModelObject()
	 */
	public Object getModelObject() {
		return filter;
	}
	
	public String toString()
	{
		return "filter:"+getLabel();
	}

}
