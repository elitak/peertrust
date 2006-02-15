package policysystem.model;

import java.util.Vector;


public interface PSResource extends ModelObjectWrapper {
	
	public String getHasMapping();
	public void setHasMapping(String name);
	
	
	public Vector getIsOverrindingRule();
	public void addIsOverrindingRule(PSOverrindingRule rule);
	
	public Vector getHasSuper();
	public void addHasSuper(ModelObjectWrapper res);
	
	public Vector getIsProtectedBy();
	public void addIsProtectedBy(PSPolicy policy);
	
	public Vector getHasFilter();
}
