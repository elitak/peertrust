package policysystem.model;

public interface PSHierarchyRelationship extends ModelObjectWrapper 
{
	public boolean isDirected();
	
	public ModelObjectWrapper getSource();
	public void setSource(ModelObjectWrapper source);
	
	public ModelObjectWrapper getTarget();
	public void setTarget(ModelObjectWrapper target);
	
	public ModelObjectWrapper[] getHierarchyNodes();
}
