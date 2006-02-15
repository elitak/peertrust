package policysystem.model;

import java.util.Vector;

public interface PSFilter extends ModelObjectWrapper {

	public Vector getHasCondition();
	public void addHasCondition(String condition);
	
	public Vector getIsprotectedBy();
	public void addIsProtectedBy(PSPolicy policy) ;
	public String getLabel();
}
