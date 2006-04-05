/**
 * 
 */
package org.peertrust.modeler.policysystem.model;


import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSModelLabel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;


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

	public PSModelObject getSource() {
		if(stm==null)
		{
			return null;
		}
		Resource res= stm.getSubject();
		return PolicySystemRDFModel.getInstance().createModelObjectWrapper(res,null);//TODO change null
	}

	public void setSource(PSModelObject source) {
		
	}

	public PSModelObject getTarget() {
		if(stm==null)
		{
			return null;
		}
		Resource res= (Resource)stm.getObject();
		return PolicySystemRDFModel.getInstance().createModelObjectWrapper(res,null);//TODO change that
	}

	public void setTarget(PSModelObject target) {
		// TODO Auto-generated method stub
		
	}

	public PSModelObject[] getHierarchyNodes() 
	{
		if(stm==null)
		{
			return null;
		}
		
		return new PSModelObject[]{
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

	public PSModelLabel getLabel() {
		if(stm==null)
		{
			logger.warn("wrapped stm is null");
			return null;
		}
		return new PSModelLabelImpl(
						this,
						stm.getPredicate().getLocalName());
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

	public String getRole() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setRole(String role) {
		// TODO Auto-generated method stub
		
	}
	
	
}