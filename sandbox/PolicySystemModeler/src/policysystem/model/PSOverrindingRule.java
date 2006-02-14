package policysystem.model;



public interface PSOverrindingRule extends ModelObjectWrapper {
	public String getHasName();
	public void setHasName(String name);
	
	public PSPolicy getHasOverridden();
	public void setHasOverriden(PSPolicy policy);
	
	public PSPolicy getHasOverridder();
	public void setHasOverrider(PSPolicy policy);
	
	public boolean getIsInheritable();
	public void setIsInheritable(boolean isInheritable);
	
	public Object getModelObject();
	
}
