/**
 * 
 */
package policysystem.model;

import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Resource;

class PSResourceImpl implements PSResource
{
	Resource resource;
	public PSResourceImpl(Resource resource)
	{
		this.resource=resource;
	}
	
	public String getHasName() 
	{
		return PolicySystemRDFModel.getStringProperty(resource,PolicySystemRDFModel.PROP_HAS_NAME);
	}

	public void setHasName(String name) 
	{
		PolicySystemRDFModel.setStringProperty(resource,PolicySystemRDFModel.PROP_HAS_NAME,name);
	}

	public String getHasMapping() 
	{
		return PolicySystemRDFModel.getStringProperty(resource,PolicySystemRDFModel.PROP_HAS_MAPPING);
	}

	public void setHasMapping(String mapping) {
		PolicySystemRDFModel.setStringProperty(resource,PolicySystemRDFModel.PROP_HAS_MAPPING,mapping);
	}

	public Vector getIsOverrindingRule() {
		return PolicySystemRDFModel.getMultipleProperty(
									resource,
									PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES);
	}

	public void addIsOverrindingRule(PSOverrindingRule rule) {
		resource.addProperty(
					PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES,
					(Resource)rule.getModelObject());
		PolicySystemRDFModel.addMultipleProperty(
					resource,
					PolicySystemRDFModel.PROP_HAS_OVERRIDING_RULES,
					(Resource)rule.getModelObject());
	}

	public Vector getHasSuper() {
		return
			PolicySystemRDFModel.getMultipleProperty(resource,PolicySystemRDFModel.PROP_HAS_SUPER);
	}

	public void addHasSuper(ModelObjectWrapper parent) {
		PolicySystemRDFModel.addMultipleProperty(
					resource,
					PolicySystemRDFModel.PROP_HAS_SUPER,
					(Resource)parent.getModelObject());
	}

	public void addIsProtectedBy(PSPolicy policy) {
		PolicySystemRDFModel.addMultipleProperty(
				resource,
				PolicySystemRDFModel.PROP_IS_PROTECTED_BY,
				(Resource)policy.getModelObject());
		
	}

	public Vector getIsProtectedBy() {
		return 
			PolicySystemRDFModel.getMultipleProperty(
							resource,
							PolicySystemRDFModel.PROP_IS_PROTECTED_BY);
	}

	public Vector getHasFilter()
	{
		return PolicySystemRDFModel.getMultipleProperty(
					resource,
					PolicySystemRDFModel.PROP_HAS_FILTER);
	}
	public Object getModelObject() {
		return resource;
	}
	
	public String toString() 
	{
		System.out.println("---------------------------------------------SSSS");
		return "   "+getHasName()+"  ";
	}
}