/**
 * 
 */
package policysystem.model;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

class PSHierarchyRelationshipImpl implements PSHierarchyRelationship
{

	private Statement stm;
	
	public PSHierarchyRelationshipImpl(Statement stm)
	{
		this.stm=stm;
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
		return PolicySystemRDFModel.createModelObjectWrapper(res);
	}

	public void setSource(ModelObjectWrapper source) {
		
	}

	public ModelObjectWrapper getTarget() {
		if(stm==null)
		{
			return null;
		}
		Resource res= (Resource)stm.getObject();
		return PolicySystemRDFModel.createModelObjectWrapper(res);
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
					PolicySystemRDFModel.createModelObjectWrapper((Resource)stm.getSubject()),
					PolicySystemRDFModel.createModelObjectWrapper((Resource)stm.getObject())};
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
	
	
}