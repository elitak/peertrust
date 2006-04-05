package org.peertrust.modeler.policysystem.model.abtract;

public interface PSModelObject {
	public static final String ROLE_RESOURCE_POLICY="ROLE_RESOURCE_POLICY";
	public static final String ROLE_ORULE_OVERRIDDER="ROLE_ORULE_OVERRIDDER";
	public static final String ROLE_ORULE_OVERRIDDEN="ROLE_ORULE_OVERRIDDEN";
	
	public abstract Object getModelObject();
	public PSModelLabel getLabel();
	public void setLabel(String label);
	
	public String getRole();
	public void setRole(String role);
}