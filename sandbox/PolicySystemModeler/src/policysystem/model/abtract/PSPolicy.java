package policysystem.model.abtract;


public interface PSPolicy extends ModelObjectWrapper {
//	public String getHasName();
//	public void setHasName(String name);
	
	public String getHasValue();
	public void setHasValue(String name);
	
	public Object getModelObject();
	public ModelObjectWrapper getGuarded();
	public void setGuarded(ModelObjectWrapper guarded);
	
}
