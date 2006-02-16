/**
 * 
 */
package policysystem.model;


import org.apache.log4j.Logger;

import policysystem.model.abtract.ModelObjectWrapper;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

class PSHierarchyRelationshipImpl implements PSHierarchyRelationship
{

	private Statement stm;
	private Logger logger;
	
	public PSHierarchyRelationshipImpl(Statement stm)
	{
		this.stm=stm;
		logger=Logger.getLogger(PSHierarchyRelationship.class);
	}
	
	public boolean isDirected() {
		return true;
	}

	public ModelObjectWrapper getSource() {
		if(stm==null)
		{
			return null;
		}
		Resource res= stm.getSubject();
		return PolicySystemRDFModel.getInstance().createModelObjectWrapper(res,null);//TODO change null
	}

	public void setSource(ModelObjectWrapper source) {
		
	}

	public ModelObjectWrapper getTarget() {
		if(stm==null)
		{
			return null;
		}
		Resource res= (Resource)stm.getObject();
		return PolicySystemRDFModel.getInstance().createModelObjectWrapper(res,null);//TODO change that
	}

	public void setTarget(ModelObjectWrapper target) {
		// TODO Auto-generated method stub
		
	}

	public ModelObjectWrapper[] getHierarchyNodes() 
	{
		if(stm==null)
		{
			return null;
		}
		
		return new ModelObjectWrapper[]{
					PolicySystemRDFModel.getInstance().createModelObjectWrapper(
							(Resource)stm.getSubject(),
							null),
					PolicySystemRDFModel.getInstance().createModelObjectWrapper(
							(Resource)stm.getObject(),
							null)//TODO change these nulls
							};
	}

	public Object getModelObject() 
	{
		return stm;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return " ";
	}

	public String getLabel() {
		if(stm==null)
		{
			logger.warn("wrapped stm is null");
			return null;
		}
		return stm.getPredicate().getLocalName();
	}

	public void setLabel(String label) {
		if(label==null)
		{
			logger.warn("label is null cannot set!");
			return;
		}
		if(stm==null)
		{
			logger.warn("wrapped stm is null cannot set label: "+label);
			return;
		}
		stm.changeObject(label);
	}
	
	
}