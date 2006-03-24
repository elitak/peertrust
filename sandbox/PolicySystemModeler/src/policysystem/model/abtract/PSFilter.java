package policysystem.model.abtract;

import java.util.Vector;


public interface PSFilter extends ModelObjectWrapper {

	public Vector getHasCondition();
	public void addHasCondition(String condition);
	public boolean containsCondition(String condition);
	public void removeCondition(String condition);
	public void removeAllConditions();
	
	public Vector getIsprotectedBy();
	public void addIsProtectedBy(PSPolicy policy) ;
	public String getLabel();
}
