package org.peertrust.modeler.policysystem.model.abtract;


public interface PSPolicy extends PSModelObject 
{
	public String getHasValue();
	public void setHasValue(String name);
	
	public Object getModelObject();
	public PSModelObject getGuarded();
	public void setGuarded(PSModelObject guarded);
	
}
