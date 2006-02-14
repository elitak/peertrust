/**
 * 
 */
package policysystem.model;

import com.hp.hpl.jena.rdf.model.Resource;

class PSPolicyImpl implements PSPolicy
{
	private Resource resource;
	
	public PSPolicyImpl(Resource resources)
	{
		this.resource=resources;
	}
	
	
	public String getHasName() 
	{
		return PolicySystemRDFModel.getStringProperty(
						resource,
						PolicySystemRDFModel.PROP_HAS_NAME);
	}

	public void setHasName(String name) 
	{
		if(name==null)
		{
			return;
		}
		
		PolicySystemRDFModel.setStringProperty(
						resource,
						PolicySystemRDFModel.PROP_HAS_NAME,
						name);
		return;
	}

	public String getHasValue() {
		return PolicySystemRDFModel.getStringProperty(
								resource,
								PolicySystemRDFModel.PROP_HAS_VALUE);
	}

	public void setHasValue(String value) {
		if(value==null)
		{
			return;
		}
		PolicySystemRDFModel.setStringProperty(
							resource,
							PolicySystemRDFModel.PROP_HAS_VALUE,
							value);
	}


	public String toString() 
	{
		System.out.println("---------------------------------------------");
		return getHasName();
	}


	public Object getModelObject() {
		return resource;
	}			
}